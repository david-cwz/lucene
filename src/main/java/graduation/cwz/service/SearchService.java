package graduation.cwz.service;

import graduation.cwz.entity.*;
import graduation.cwz.model.OnlineSearchResultData;
import graduation.cwz.model.RecordData;
import graduation.cwz.model.SearchResultData;
import graduation.cwz.model.UrlData;

import java.util.List;
import java.util.Map;

public interface SearchService {
    List<SearchHistory> getRecordList(Map<String, Object> map);
    List<SearchHistory> getAllRecordList();
    List<SearchHistory> getAllRecordListByName(String userName);
    void addRecord(RecordData recordData);
    void delRecord(int deleteId);
    void shiftPreEmbeddedStatus(int id);
    int countRecordByName(String userName);
    void updateHaveNewResultStatus(int id, String haveNewResult);

    void createIndex(List<Message> messageList, String indexPath);
    void createOnlineIndex(String url, String indexPath);
    List<SearchResultData> search(String keyWord, String indexPath);
    List<SearchResultData> searchOnline(String keyWord, String indexPath);

    List<SearchResult> getSearchResultList(Map<String, Object> map);
    void addSearchResult(SearchResultData searchResultData);

    List<OnlineSearchResult> getOnlineSearchResultList(Map<String, Object> map);
    void addOnlineSearchResult(OnlineSearchResultData onlineSearchResultData);

    List<Url> getUrlList(Map<String, Object> map);
    void addUrl(UrlData urlData);
    void modifyUrl(int urlId, UrlData urlData);
    void delUrl(int urlId);
}
