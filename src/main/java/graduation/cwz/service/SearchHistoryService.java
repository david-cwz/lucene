package graduation.cwz.service;

import graduation.cwz.entity.SearchHistory;

import java.util.List;

public interface SearchHistoryService {
    List<SearchHistory> getRecordList();
    List<SearchHistory> getRecordListByUser(String username);
    void addRecord(String record, String username);
    void delRecord(int deleteId);
}
