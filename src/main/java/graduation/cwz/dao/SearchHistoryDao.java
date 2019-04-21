package graduation.cwz.dao;

import graduation.cwz.entity.SearchHistory;

import java.util.List;

public interface SearchHistoryDao {
    List<SearchHistory> getRecordList();
    List<SearchHistory> getRecordListByUser(String username);
    void addRecord(String record, String username);
    void delRecord(int id);
}
