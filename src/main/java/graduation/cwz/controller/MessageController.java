package graduation.cwz.controller;

import graduation.cwz.entity.Message;
import graduation.cwz.model.MessageData;
import graduation.cwz.model.PageBean;
import graduation.cwz.service.MessageService;
import graduation.cwz.utils.JSONUtil;
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
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @RequestMapping("/list")
    public String getMessageList(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        List<Message> messageList = messageService.getMessageList(map);
        int total = messageList.size();
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(messageList);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 添加信息
     */
    @RequestMapping("/add")
    public String addMessage(MessageData messageData, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            messageService.addMessage(messageData.getIntro(), messageData.getContent());
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 删除信息
     */
    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "idList") String idList, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            String[] idListStr = idList.split(",");
            for (int i = 0; i < idListStr.length; i++) {
                messageService.delMessage(Integer.parseInt(idListStr[i]));
            }
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping(value="/search")
    @ResponseBody
    public String searchRecord(@RequestParam(value = "keyWord") String keyWord){
        String result;
        try {
            result = messageService.search(keyWord);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return result;
    }
}
