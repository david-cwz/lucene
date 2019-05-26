package graduation.cwz.service;

import graduation.cwz.entity.*;
import graduation.cwz.model.OnlineSearchResultData;
import graduation.cwz.model.SearchResultData;
import graduation.cwz.model.UrlData;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SearchService {
    List<SearchHistory> getRecordList(Map<String, Object> map);
    List<SearchHistory> getAllRecordList();
    List<SearchHistory> getAllRecordListByName(String userName);
    int addRecord(String keyWord, String userName, String date, String searchTarget);
    void delRecord(int deleteId);
    void shiftPreEmbeddedStatus(int id);
    int countRecordByName(String userName);
    void updateHaveNewResultStatus(int id, String haveNewResult);

    void createIndex(List<Message> messageList, String indexPath) throws IOException;
    void createOnlineIndex(String urlList, String indexPath) throws IOException;
    List<SearchResultData> search(String keyWord, int recordId, String indexPath) throws ParseException, InvalidTokenOffsetsException, IOException;
    List<OnlineSearchResultData> searchOnline(String keyWord, int recordId, String indexPath) throws ParseException, InvalidTokenOffsetsException, IOException;

    List<SearchResult> getSearchResultList(Map<String, Object> map);
    List<SearchResult> getSearchResultListByRecordId(int recordId);
    void addSearchResult(SearchResultData searchResultData);
    List<SearchResultData> getResultDataList(List<SearchResult> searchResultList);

    List<OnlineSearchResult> getOnlineSearchResultList(Map<String, Object> map);
    List<OnlineSearchResult> getOnlineSearchResultListByRecordId(int recordId);
    SearchHistory getRecordById(int recordId);
    void addOnlineSearchResult(OnlineSearchResultData onlineSearchResultData);

    List<Url> getUrlList(Map<String, Object> map);
    void addUrl(UrlData urlData);
    void modifyUrl(UrlData urlData);
    void delUrl(int urlId);
}
