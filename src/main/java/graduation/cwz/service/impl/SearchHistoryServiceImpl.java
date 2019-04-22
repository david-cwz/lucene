package graduation.cwz.service.impl;

import graduation.cwz.dao.SearchHistoryDao;
import graduation.cwz.entity.SearchHistory;
import graduation.cwz.service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {
    @Autowired
    SearchHistoryDao searchHistoryDao;

    @Override
    public List<SearchHistory> getRecordList() {
        try {
            return searchHistoryDao.getRecordList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<SearchHistory> getRecordListByUser(String username) {
        try {
            return searchHistoryDao.getRecordListByUser(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addRecord(String record, String username) {
        try {
            searchHistoryDao.addRecord(record, username);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delRecord(int deleteId) {
        try {
            searchHistoryDao.delRecord(deleteId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
