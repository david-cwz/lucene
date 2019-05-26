package graduation.cwz.service.impl;

import graduation.cwz.dao.SearchDao;
import graduation.cwz.entity.*;
import graduation.cwz.model.OnlineSearchResultData;
import graduation.cwz.model.SearchResultData;
import graduation.cwz.model.UrlData;
import graduation.cwz.service.MessageService;
import graduation.cwz.service.SearchService;
import graduation.cwz.service.UserService;
import graduation.cwz.utils.Const;
import graduation.cwz.utils.EmailUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    SearchDao searchDao;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    public static final List<Message> newMessageList = new LinkedList<>();  //新消息队列

    @PostConstruct
    public void runThread() {
        Thread preEmbeddedThread = new Thread(() -> {
            while (true) {
                synchronized (newMessageList) {
                    try {
                        while (newMessageList.size() == 0) {
                            newMessageList.wait();
                        }
                        createIndex(newMessageList, Const.INDEX_PATH2); //创建索引
                        List<SearchHistory> list = searchDao.getPreEmbeddedRecords();
                        for (SearchHistory record : list) {
                            List<SearchResultData> resultList = search(record.getRecord(), record.getId(), Const.INDEX_PATH2); //搜索
                            if (resultList != null && resultList.size() > 0) {
                                searchDao.updateHaveNewResultStatus(record.getId(), "have new result!!!");
                                EmailUtil.sendEmail(record.getUser().getEmail(), record.getUser().getUserName(), record.getRecord());
                            }
                        }
                        newMessageList.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        preEmbeddedThread.start();
    }

    @Override
    public List<SearchHistory> getRecordList(Map<String, Object> map) {
        try {
            return searchDao.getRecordList(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<SearchHistory> getAllRecordList() {
        try {
            return searchDao.getAllRecordList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<SearchHistory> getAllRecordListByName(String userName) {
        try {
            return searchDao.getAllRecordListByName(userName);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public int addRecord(String keyWord, String userName, String date, String searchTarget) {
        try {
            User user = userService.getUserByName(userName);
            return searchDao.addRecord(keyWord, user, date, searchTarget);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delRecord(int deleteId) {
        try {
            searchDao.delSearchResultByRecordId(deleteId);
            searchDao.delOnlineSearchResultByRecordId(deleteId);
            searchDao.delRecord(deleteId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void shiftPreEmbeddedStatus(int id) {
        try {
            List<SearchHistory> list = getAllRecordList();
            for (SearchHistory record : list) {
                if (record.getId() == id) {
                    if (record.isPreEmbedded()) { //预埋单true转换为false时
                        searchDao.updateHaveNewResultStatus(id, "");
                    } else{ //预埋单false转换为true时
                        searchDao.updateHaveNewResultStatus(id, "no new results");
                    }
                    searchDao.updatePreEmbeddedStatus(id, !record.isPreEmbedded());
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public int countRecordByName(String userName) {
        try {
            return getAllRecordListByName(userName).size();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updateHaveNewResultStatus(int id, String haveNewResult) {
        try {
            List<SearchHistory> list = getAllRecordList();
            for (SearchHistory record : list) {
                if (record.getId() == id) {
                    searchDao.updateHaveNewResultStatus(id, haveNewResult);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 创建索引
     */
    @Override
    public void createIndex(List<Message> messageList, String indexPath) throws IOException {

        IndexWriter indexWriter = null;
        try {
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
            Analyzer analyzer = new SmartChineseAnalyzer(true);
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.deleteAll();// 清除以前的index

            for (Message message : messageList) {
                Document document = new Document();
                document.add(new Field("messageId", String.valueOf(message.getId()), TextField.TYPE_STORED));
                document.add(new Field("intro", message.getIntro(), TextField.TYPE_STORED));
                document.add(new Field("content", message.getContent(), TextField.TYPE_STORED));
                indexWriter.addDocument(document);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if(indexWriter != null) indexWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createOnlineIndex(String urlList, String indexPath) throws IOException {
        IndexWriter indexWriter = null;
        try {
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
            Analyzer analyzer = new SmartChineseAnalyzer(true);
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.deleteAll();// 清除以前的index

            String[] urlListStr = urlList.split(" ");
            for (String url : urlListStr) {
                String html = Jsoup.connect(url).get().html();
                String text = Jsoup.parse(html).text();
                String[] contents = text.split(" ");
                for (String content : contents) {
                    Document document = new Document();
                    document.add(new Field("content", content, TextField.TYPE_STORED));
                    document.add(new Field("url", url, TextField.TYPE_STORED));
                    indexWriter.addDocument(document);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if(indexWriter != null) indexWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 本系统搜索
     */
    @Override
    public List<SearchResultData> search(String keyWord, int recordId, String indexPath) throws ParseException, InvalidTokenOffsetsException, IOException {
        List<SearchResultData> resultList = new ArrayList<>();
        DirectoryReader directoryReader = null;
        try {
            // 1、创建Directory
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
            // 2、创建IndexReader
            directoryReader = DirectoryReader.open(directory);
            // 3、根据IndexReader创建IndexSearch
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
            // 4、创建搜索的Query
            // Analyzer analyzer = new StandardAnalyzer();
            Analyzer analyzer = new SmartChineseAnalyzer(true); // 使用中文分词

            // 简单的查询，创建Query表示搜索域为content包含keyWord的文档
            //Query query = new QueryParser("content", analyzer).parse(keyWord);

            String[] fields = {"intro", "content"};
            // MUST 表示and，MUST_NOT 表示not ，SHOULD表示or
            BooleanClause.Occur[] clauses = {BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};
            // MultiFieldQueryParser表示多个域解析， 同时可以解析含空格的字符串，如果我们搜索"上海 中国"
            Query multiFieldQuery = MultiFieldQueryParser.parse(keyWord, fields, clauses, analyzer);

            // 5、根据searcher搜索并且返回TopDocs
            TopDocs topDocs = indexSearcher.search(multiFieldQuery, 100); // 搜索前100条结果
//            System.out.println("共找到匹配处：" + topDocs.totalHits);
            // 6、根据TopDocs获取ScoreDoc对象
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
//            System.out.println("共找到匹配文档数：" + scoreDocs.length);

            QueryScorer scorer = new QueryScorer(multiFieldQuery, "content");
            SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
            Highlighter highlighter = new Highlighter(htmlFormatter, scorer);
            highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 7、根据searcher和ScoreDoc对象获取具体的Document对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                String messageId = document.get("messageId");
                String intro = document.get("intro");
                String content = document.get("content");
                String _intro = highlighter.getBestFragment(analyzer, "intro", intro);
                String _content = highlighter.getBestFragment(analyzer, "content", content);
                SearchResultData result;
                result = new SearchResultData(_intro, _content, Integer.valueOf(messageId), recordId);
                if (_intro == null || "".equals(_intro)) {
                    result.setIntro(intro);
                }
                if (_content == null || "".equals(_content)) {
                    result.setContent(content);
                }
                addSearchResult(result);
                resultList.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if(directoryReader != null) directoryReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    @Override
    public List<OnlineSearchResultData> searchOnline(String keyWord, int recordId, String indexPath) throws ParseException, InvalidTokenOffsetsException, IOException {
        DirectoryReader directoryReader = null;
        List<OnlineSearchResultData> resultList = new ArrayList<>();
        try {
            // 1、创建Directory
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
            // 2、创建IndexReader
            directoryReader = DirectoryReader.open(directory);
            // 3、根据IndexReader创建IndexSearch
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
            // 4、创建搜索的Query
            // Analyzer analyzer = new StandardAnalyzer();
            Analyzer analyzer = new SmartChineseAnalyzer(true); // 使用中文分词

            // 简单的查询，创建Query表示搜索域为content包含keyWord的文档
            //Query query = new QueryParser("content", analyzer).parse(keyWord);

            String[] fields = {"content"};
            // MUST 表示and，MUST_NOT 表示not ，SHOULD表示or
            BooleanClause.Occur[] clauses = {BooleanClause.Occur.SHOULD};
            // MultiFieldQueryParser表示多个域解析， 同时可以解析含空格的字符串，如果我们搜索"上海 中国"
            Query multiFieldQuery = MultiFieldQueryParser.parse(keyWord, fields, clauses, analyzer);

            // 5、根据searcher搜索并且返回TopDocs
            TopDocs topDocs = indexSearcher.search(multiFieldQuery, 100); // 搜索前100条结果
//            System.out.println("共找到匹配处：" + topDocs.totalHits);
            // 6、根据TopDocs获取ScoreDoc对象
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
//            System.out.println("共找到匹配文档数：" + scoreDocs.length);

            QueryScorer scorer = new QueryScorer(multiFieldQuery, "content");
            SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
            Highlighter highlighter = new Highlighter(htmlFormatter, scorer);
            highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 7、根据searcher和ScoreDoc对象获取具体的Document对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                String content = document.get("content");
                String _content = highlighter.getBestFragment(analyzer, "content", content);
                String url = document.get("url");
                OnlineSearchResultData result = new OnlineSearchResultData(_content, url, recordId);

                addOnlineSearchResult(result);
                resultList.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if(directoryReader != null) directoryReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    @Override
    public List<SearchResult> getSearchResultList(Map<String, Object> map) {
        try {
            return searchDao.getSearchResultList(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<SearchResult> getSearchResultListByRecordId(int recordId) {
        try {
            return searchDao.getSearchResultListByRecordId(recordId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public SearchHistory getRecordById(int recordId) {
        try {
            List<SearchHistory> list = getAllRecordList();
            for (SearchHistory record : list) {
                if (recordId == record.getId()) {
                    return record;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    @Override
    public void addSearchResult(SearchResultData searchResultData) {
        try {
            SearchHistory record = getRecordById(searchResultData.getRecordId());
            Message message = messageService.getMessageById(searchResultData.getMessageId());
            searchDao.addSearchResult(record, message, searchResultData.getIntro(), searchResultData.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<SearchResultData> getResultDataList(List<SearchResult> searchResultList) {
        List<SearchResultData> resultDataList = new ArrayList<>();
        try {
            for (SearchResult searchResult : searchResultList) {
                SearchResultData resultData = new SearchResultData();
                resultData.setIntro(searchResult.getIntro());
                resultData.setContent(searchResult.getContent());
                resultData.setMessageId(searchResult.getMessage().getId());
                resultData.setRecordId(searchResult.getRecord().getId());
                resultData.setUserName(searchResult.getMessage().getUser().getUserName());
                resultData.setEmail(searchResult.getMessage().getEmail());
                resultData.setDate(searchResult.getMessage().getDate());
                resultDataList.add(resultData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return resultDataList;
    }

    @Override
    public List<OnlineSearchResult> getOnlineSearchResultList(Map<String, Object> map) {
        try {
            return searchDao.getOnlineSearchResultList(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<OnlineSearchResult> getOnlineSearchResultListByRecordId(int recordId) {
        try {
            return searchDao.getOnlineSearchResultListByRecordId(recordId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addOnlineSearchResult(OnlineSearchResultData onlineSearchResultData) {
        try {
            SearchHistory record = getRecordById(onlineSearchResultData.getRecordId());
            searchDao.addOnlineSearchResult(record, onlineSearchResultData.getContent(), onlineSearchResultData.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Url> getUrlList(Map<String, Object> map) {
        try {
            return searchDao.getUrlList(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addUrl(UrlData urlData) {
        try {
            searchDao.addUrl(urlData.getName(), urlData.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void modifyUrl(UrlData urlData) {
        try {
            searchDao.modifyUrl(urlData.getId(), urlData.getName(), urlData.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delUrl(int urlId) {
        try {
            searchDao.delUrl(urlId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
