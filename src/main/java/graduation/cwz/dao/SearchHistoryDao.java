package graduation.cwz.dao;

import graduation.cwz.entity.SearchHistory;
import graduation.cwz.entity.User;

import java.util.List;
import java.util.Map;

public interface SearchHistoryDao {
    List<SearchHistory> getRecordList(Map<String, Object> map);
    void addRecord(String record, User user, String date);
    void delRecord(int deleteId);
}
