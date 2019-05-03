package graduation.cwz.service.impl;

import graduation.cwz.controller.MessageController;
import graduation.cwz.dao.SearchHistoryDao;
import graduation.cwz.entity.Message;
import graduation.cwz.entity.SearchHistory;
import graduation.cwz.entity.User;
import graduation.cwz.model.RecordData;
import graduation.cwz.model.SearchResultData;
import graduation.cwz.service.MessageService;
import graduation.cwz.service.SearchHistoryService;
import graduation.cwz.service.UserService;
import graduation.cwz.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {
    @Autowired
    SearchHistoryDao searchHistoryDao;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    public static final List<Message> newMessageList = new LinkedList<>();  //新消息队列

    @PostConstruct
    public void runThread() {
        Thread preEmbeddedThread = new Thread(() -> {
            while (true) {
                synchronized (newMessageList) {
                    try {
                        while (newMessageList.size() == 0) {
                            newMessageList.wait();
                        }
                        messageService.createIndex(newMessageList, MessageController.INDEX_PATH2); //创建索引
                        List<SearchHistory> list = searchHistoryDao.getPreEmbeddedRecords();
                        for (SearchHistory record : list) {
                            List<SearchResultData> resultList = messageService.search(record.getRecord(), MessageController.INDEX_PATH2); //搜索
                            if (resultList != null && resultList.size() > 0) {
                                searchHistoryDao.updateHaveNewResultStatus(record.getId(), "have new result!!!");
                                EmailUtil.sendEmail(record.getUser().getEmail(), record.getUser().getUserName(), record.getRecord());
                            }
                        }
                        newMessageList.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        preEmbeddedThread.start();
    }

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
    public List<SearchHistory> getAllRecordListByName(String userName) {
        try {
            return searchHistoryDao.getAllRecordListByName(userName);
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
    public void shiftPreEmbeddedStatus(int id) {
        try {
            List<SearchHistory> list = getAllRecordList();
            for (SearchHistory record : list) {
                if (record.getId() == id) {
                    if (record.isPreEmbedded()) { //预埋单true转换为false时
                        searchHistoryDao.updateHaveNewResultStatus(id, "");
                    } else{ //预埋单false转换为true时
                        searchHistoryDao.updateHaveNewResultStatus(id, "no new results");
                    }
                    searchHistoryDao.updatePreEmbeddedStatus(id, !record.isPreEmbedded());
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public int countRecordByName(String userName) {
        try {
            return getAllRecordListByName(userName).size();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updateHaveNewResultStatus(int id, String haveNewResult) {
        try {
            List<SearchHistory> list = getAllRecordList();
            for (SearchHistory record : list) {
                if (record.getId() == id) {
                    searchHistoryDao.updateHaveNewResultStatus(id, haveNewResult);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
