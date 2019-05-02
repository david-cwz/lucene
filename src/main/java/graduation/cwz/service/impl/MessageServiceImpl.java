package graduation.cwz.service.impl;

import graduation.cwz.dao.MessageDao;
import graduation.cwz.entity.Message;
import graduation.cwz.model.SearchResultData;
import graduation.cwz.service.MessageService;
import graduation.cwz.service.SearchHistoryService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageDao messageDao;

    @Override
    public List<Message> getMessageList(Map<String, Object> map) {
        try {
            return messageDao.getMessageList(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Message> getAllMessageList() {
        try {
            return messageDao.getAllMessageList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addMessage(String intro, String content) {
        try {
            messageDao.addMessage(intro, content);
            synchronized (SearchHistoryServiceImpl.newMessageList) {
                SearchHistoryServiceImpl.newMessageList.add(new Message(intro, content));
                SearchHistoryServiceImpl.newMessageList.notify();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delMessage(int deleteId) {
        try {
            messageDao.delMessage(deleteId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 创建索引
     */
    @Override
    public void createIndex(List<Message> messageList, String indexPath) {

        IndexWriter indexWriter = null;
        try
        {
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
            Analyzer analyzer = new SmartChineseAnalyzer(true);
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.deleteAll();// 清除以前的index

            for (Message message : messageList) {
                Document document = new Document();
                document.add(new Field("id", String.valueOf(message.getId()), TextField.TYPE_STORED));
                document.add(new Field("intro", message.getIntro(), TextField.TYPE_STORED));
                document.add(new Field("content", message.getContent(), TextField.TYPE_STORED));
                indexWriter.addDocument(document);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(indexWriter != null) indexWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 搜索
     */
    @Override
    public List<SearchResultData> search(String keyWord, String indexPath) {
        List<SearchResultData> resultList = new ArrayList<>();
        DirectoryReader directoryReader = null;
        try
        {
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
            SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("“", "”");
            Highlighter highlighter = new Highlighter(htmlFormatter, scorer);
            highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 7、根据searcher和ScoreDoc对象获取具体的Document对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                String id = document.get("id");
                String intro = document.get("intro");
                String content = document.get("content");
                String _intro = highlighter.getBestFragment(analyzer, "intro", intro);
                String _content = highlighter.getBestFragment(analyzer, "content", content);
                SearchResultData result = new SearchResultData(Integer.valueOf(id), _intro, _content);
                resultList.add(result);
                //TokenStream tokenStream = new SimpleAnalyzer().tokenStream("content", new StringReader(content));
                //TokenSources.getTokenStream("content", tvFields, content, analyzer, 100);
                //TokenStream tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), scoreDoc.doc, "content", document, analyzer);
                //System.out.println(highlighter.getBestFragment(tokenStream, content));
//                System.out.println("-----------------------------------------");
//                System.out.println("文章id：" + id);
//                System.out.println("文章intro：");
//                System.out.println(_intro);
//                System.out.println("文章content：");
//                System.out.println(_content);
//                System.out.println("");
                // 8、根据Document对象获取需要的值
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public int countMessage() {
        try {
            return getAllMessageList().size();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
