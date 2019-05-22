package graduation.cwz.controller;

import graduation.cwz.entity.SearchHistory;
import graduation.cwz.model.PageBean;
import graduation.cwz.model.SearchResultData;
import graduation.cwz.service.MessageService;
import graduation.cwz.service.SearchService;
import graduation.cwz.utils.Const;
import graduation.cwz.utils.DateUtil;
import graduation.cwz.utils.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private MessageService messageService;

    @RequestMapping("/recordList")
    public String getRecordList(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows,
                                @RequestParam(value = "userName") String userName, HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        map.put("userName", userName);
        List<SearchHistory> messageList = searchService.getRecordList(map);
        int total = searchService.countRecordByName(userName);
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(messageList);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 删除记录
     */
    @RequestMapping("/deleteRecord")
    public String delete(@RequestParam(value = "idList") String idList, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            String[] idListStr = idList.split(",");
            for (int i = 0; i < idListStr.length; i++) {
                searchService.delRecord(Integer.parseInt(idListStr[i]));
            }
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 转换预埋单状态
     */
    @RequestMapping("/shiftStatus")
    public String shiftStatus(@RequestParam(value = "id") int id, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            searchService.shiftPreEmbeddedStatus(id);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 更新是否有新结果的状态
     */
    @RequestMapping("/updateStatus")
    public String updateHaveNewResultStatus(@RequestParam(value = "id") int id, @RequestParam(value = "haveNewResult") String haveNewResult,
                                            HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            searchService.updateHaveNewResultStatus(id, haveNewResult);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 建立本系统索引
     */
    @RequestMapping("/index")
    public String index(HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            searchService.createIndex(messageService.getAllMessageList(), Const.INDEX_PATH);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 在本系统搜索
     */
    @RequestMapping("/search")
    public String search(@RequestParam(value = "keyword") String keyWord, @RequestParam(value = "userName") String userName,
                         @RequestParam(value = "searchTarget") String searchTarget, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            int recordId = searchService.addRecord(keyWord, userName, DateUtil.getCurrentDateStr(), searchTarget);
            List<SearchResultData> resultList = searchService.search(keyWord, recordId, Const.INDEX_PATH);
            int total = resultList.size();
            JSONArray jsonArray = JSONArray.fromObject(resultList);
            result.put("rows", jsonArray);
            result.put("total", total);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 在网页搜索
     */
    @RequestMapping("/searchOnline")
    public String searchOnline(String url, @RequestParam(value = "keyWord") String keyWord, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            searchService.createOnlineIndex(url, Const.ONLINE_INDEX_PATH); //建立索引

            List<SearchResultData> resultList = searchService.searchOnline(keyWord, Const.ONLINE_INDEX_PATH);
            int total = resultList.size();
            JSONArray jsonArray = JSONArray.fromObject(resultList);
            result.put("rows", jsonArray);
            result.put("total", total);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/setKeyWord")
    public String setKeyWord(@RequestParam(value = "keyWord") String keyWord, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        session.setAttribute("keyWord", keyWord);
        return null;
    }

}
