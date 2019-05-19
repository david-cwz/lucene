package graduation.cwz.dao;

import graduation.cwz.entity.SearchHistory;
import graduation.cwz.entity.User;

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
}
