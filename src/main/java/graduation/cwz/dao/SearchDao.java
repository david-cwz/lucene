package graduation.cwz.dao;

import graduation.cwz.entity.*;

import java.util.List;
import java.util.Map;

public interface SearchDao {
    List<SearchHistory> getRecordList(Map<String, Object> map);
    List<SearchHistory> getAllRecordList();
    List<SearchHistory> getAllRecordListByName(String userName);
    List<SearchHistory> getPreEmbeddedRecords();
    void addRecord(String record, User user, String date, String searchTarget);
    void delRecord(int deleteId);
    void updatePreEmbeddedStatus(int id, boolean isisPreEmbedded);
    void updateHaveNewResultStatus(int id, String haveNewResult);

    List<SearchResult> getSearchResultList(Map<String, Object> map);
    void addSearchResult(SearchHistory record, Message message, String intro, String content);

    List<OnlineSearchResult> getOnlineSearchResultList(Map<String, Object> map);
    void addOnlineSearchResult(SearchHistory record, String content, String url);

    List<Url> getUrlList(Map<String, Object> map);
    void addUrl(String name, String url);
    void modifyUrl(int urlId, String name, String url);
    void delUrl(int urlId);
}
