package graduation.cwz.service.impl;

import graduation.cwz.dao.SearchHistoryDao;
import graduation.cwz.entity.SearchHistory;
import graduation.cwz.entity.User;
import graduation.cwz.model.RecordData;
import graduation.cwz.service.SearchHistoryService;
import graduation.cwz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {
    @Autowired
    SearchHistoryDao searchHistoryDao;
    @Autowired
    UserService userService;

    @Override
    public List<SearchHistory> getRecordList(Map<String, Object> map) {
        try {
            return searchHistoryDao.getRecordList(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<SearchHistory> getAllRecordList() {
        try {
            return searchHistoryDao.getAllRecordList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addRecord(RecordData recordData) {
        try {
            User user = userService.getUserByName(recordData.getUserName());
            searchHistoryDao.addRecord(recordData.getRecord(), user, recordData.getDate());
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

    @Override
    public void shiftStatus(int id) {
        try {
            List<SearchHistory> list = getAllRecordList();
            for (SearchHistory record : list) {
                if (record.getId() == id) {
                    searchHistoryDao.updateStatus(id, !record.isIsisPreEmbedded());
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
