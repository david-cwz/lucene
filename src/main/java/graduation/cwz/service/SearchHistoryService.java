package graduation.cwz.service;

import graduation.cwz.entity.SearchHistory;
import graduation.cwz.model.RecordData;

import java.util.List;
import java.util.Map;

public interface SearchHistoryService {
    List<SearchHistory> getRecordList(Map<String, Object> map);
    List<SearchHistory> getAllRecordList();
    void addRecord(RecordData recordData);
    void delRecord(int deleteId);
    void shiftStatus(int id);
}
