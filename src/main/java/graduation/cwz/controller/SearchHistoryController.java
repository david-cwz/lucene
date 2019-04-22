package graduation.cwz.controller;

import graduation.cwz.entity.SearchHistory;
import graduation.cwz.model.RecordData;
import graduation.cwz.service.SearchHistoryService;
import graduation.cwz.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/record")
public class SearchHistoryController {
    @Autowired
    private SearchHistoryService searchHistoryService;

    @RequestMapping(value="/list",method= RequestMethod.GET)
    @ResponseBody
    public String getRecordList(){
        List<SearchHistory> list = null;
        try {
            list = searchHistoryService.getRecordList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONUtil.listToJson(list);
    }

    @RequestMapping(value="/list/{username}",method= RequestMethod.GET)
    @ResponseBody
    public String getRecordListByUser(@PathVariable(value = "username") String username){
        List<SearchHistory> list = null;
        try {
            list = searchHistoryService.getRecordListByUser(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONUtil.listToJson(list);
    }

    @RequestMapping(value="/add",method= RequestMethod.POST)
    @ResponseBody
    public String addRecord(@RequestBody RecordData recordData){
        try {
            searchHistoryService.addRecord(recordData.getRecord(), recordData.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @RequestMapping(value="/delete",method= RequestMethod.POST)
    @ResponseBody
    public String deleteRecord(@RequestBody RecordData recordData){
        try {
            searchHistoryService.delRecord(recordData.getDeleteId());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

}
