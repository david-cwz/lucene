package graduation.cwz.service.impl;

import graduation.cwz.dao.MessageDao;
import graduation.cwz.entity.Message;
import graduation.cwz.service.MessageService;
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
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageDao messageDao;

    private static final String INDEX_PATH = "\\graduation\\cwz\\index";


    @Override
    public List<Message> getMessageList() {
        try {
            return messageDao.getMessageList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addMessage(String intro, String content) {
        try {
            messageDao.addMessage(intro, content);
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

    @Override
    public String search(String keyWord) {
        return null;
    }

    /**
     * 创建索引
     */
    public void creatIndex() {

        IndexWriter indexWriter = null;
        try
        {
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(INDEX_PATH));
            Analyzer analyzer = new SmartChineseAnalyzer(true);
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.deleteAll();// 清除以前的index

            List<Message> messageList = getMessageList();
            for (Message message : messageList) {
                Document document = new Document();
                document.add(new Field("id", String.valueOf(message.getId()), TextField.TYPE_STORED));
                document.add(new Field("intro", message.getIntro(), TextField.TYPE_STORED));
                document.add(new Field("content", message.getContent(), TextField.TYPE_STORED));
                indexWriter.addDocument(document);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(indexWriter != null) indexWriter.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 搜索
     */
    public void searchLucene(String keyWord)
    {
        DirectoryReader directoryReader = null;
        try
        {
            // 1、创建Directory
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(INDEX_PATH));
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
            System.out.println("共找到匹配处：" + topDocs.totalHits);
            // 6、根据TopDocs获取ScoreDoc对象
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            System.out.println("共找到匹配文档数：" + scoreDocs.length);

            QueryScorer scorer = new QueryScorer(multiFieldQuery, "content");
            SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<span style=\"backgroud:red\">", "</span>");
            Highlighter highlighter = new Highlighter(htmlFormatter, scorer);
            highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
            for (ScoreDoc scoreDoc : scoreDocs)
            {
                // 7、根据searcher和ScoreDoc对象获取具体的Document对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                String content = document.get("content");
                //TokenStream tokenStream = new SimpleAnalyzer().tokenStream("content", new StringReader(content));
                //TokenSources.getTokenStream("content", tvFields, content, analyzer, 100);
                //TokenStream tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), scoreDoc.doc, "content", document, analyzer);
                //System.out.println(highlighter.getBestFragment(tokenStream, content));
                System.out.println("-----------------------------------------");
                System.out.println("文章标题："+document.get("title"));
                System.out.println("文章地址：" + document.get("url"));
                System.out.println("文章内容：");
                System.out.println(highlighter.getBestFragment(analyzer, "content", content));
                System.out.println("");
                // 8、根据Document对象获取需要的值
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(directoryReader != null) directoryReader.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
