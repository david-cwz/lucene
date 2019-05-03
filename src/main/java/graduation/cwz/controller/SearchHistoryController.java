package graduation.cwz.controller;

import graduation.cwz.entity.SearchHistory;
import graduation.cwz.model.PageBean;
import graduation.cwz.model.RecordData;
import graduation.cwz.service.SearchHistoryService;
import graduation.cwz.utils.JSONUtil;
import graduation.cwz.utils.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/record")
public class SearchHistoryController {
    @Autowired
    private SearchHistoryService searchHistoryService;

    @RequestMapping("/list")
    public String getRecordList(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows,
                                @RequestParam(value = "userName") String userName, HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        map.put("userName", userName);
        List<SearchHistory> messageList = searchHistoryService.getRecordList(map);
        int total = searchHistoryService.countRecordByName(userName);
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(messageList);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 添加记录
     */
    @RequestMapping("/add")
    public String addRecord(RecordData recordData, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            //获取当前时间
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(now);
            recordData.setDate(date);

            searchHistoryService.addRecord(recordData);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 删除记录
     */
    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "idList") String idList, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            String[] idListStr = idList.split(",");
            for (int i = 0; i < idListStr.length; i++) {
                searchHistoryService.delRecord(Integer.parseInt(idListStr[i]));
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
            searchHistoryService.shiftPreEmbeddedStatus(id);
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
            searchHistoryService.updateHaveNewResultStatus(id, haveNewResult);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

}
