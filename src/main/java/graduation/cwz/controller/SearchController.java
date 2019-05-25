package graduation.cwz.controller;

import graduation.cwz.entity.OnlineSearchResult;
import graduation.cwz.entity.SearchHistory;
import graduation.cwz.entity.SearchResult;
import graduation.cwz.entity.Url;
import graduation.cwz.model.PageBean;
import graduation.cwz.model.SearchResultData;
import graduation.cwz.model.UrlData;
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

    @RequestMapping("/resultList")
    public String getSearchResultList(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows,
                                @RequestParam(value = "recordId") int recordId, HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        map.put("recordId", recordId);
        List<SearchResult> searchResultList = searchService.getSearchResultList(map);
        List<SearchResultData> resultDataList = searchService.getResultDataList(searchResultList);
        int total = searchService.getSearchResultListByRecordId(recordId).size();
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(resultDataList);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/onlineResultList")
    public String getOnlineSearchResultList(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows,
                                @RequestParam(value = "recordId") int recordId, HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        map.put("recordId", recordId);
        List<OnlineSearchResult> onlineSearchResultList = searchService.getOnlineSearchResultList(map);
        int total = searchService.getOnlineSearchResultListByRecordId(recordId).size();
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(onlineSearchResultList);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/urlList")
    public String getUrlList(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows,
                                HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        List<Url> urlList = searchService.getUrlList(map);
        int total = urlList.size();
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(urlList);
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
    public String search(@RequestParam(value = "keyWord") String keyWord, @RequestParam(value = "userName") String userName,
                         @RequestParam(value = "searchTarget") String searchTarget, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            int recordId = searchService.addRecord(keyWord, userName, DateUtil.getCurrentDateStr(), searchTarget);

            SearchHistory record = searchService.getRecordById(recordId);
            HttpSession session = request.getSession();
            session.setAttribute("record", record);

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

    /**
     * 添加网站
     */
    @RequestMapping("/addUrl")
    public String addUrl(UrlData urlData, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            searchService.addUrl(urlData);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 修改网站
     */
    @RequestMapping("/modifyUrl")
    public String modifyUrl(UrlData urlData, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            searchService.modifyUrl(urlData);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 删除网站
     */
    @RequestMapping("/deleteUrl")
    public String deleteUrl(@RequestParam(value = "idList") String idList, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            String[] idListStr = idList.split(",");
            for (int i = 0; i < idListStr.length; i++) {
                searchService.delUrl(Integer.parseInt(idListStr[i]));
            }
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/setRecord")
    public String setRecordId(@RequestParam(value = "recordId") int recordId, HttpServletRequest request) throws Exception {
        SearchHistory record = searchService.getRecordById(recordId);
        HttpSession session = request.getSession();
        session.setAttribute("record", record);
        return null;
    }

}
