package graduation.cwz.service.impl;

import graduation.cwz.dao.SearchHistoryDao;
import graduation.cwz.entity.SearchHistory;
import graduation.cwz.service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SearchHistoryServiceImpl implements SearchHistoryService {
    @Autowired
    SearchHistoryDao searchHistoryDao;

    @Override
    public List<SearchHistory> getRecordList() {
        return null;
    }

    @Override
    public void addRecord(String record, String username) {

    }

    @Override
    public void delRecord(int id) {

    }
}
