package graduation.cwz.controller;

import graduation.cwz.entity.Message;
import graduation.cwz.model.MessageData;
import graduation.cwz.service.MessageService;
import graduation.cwz.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @RequestMapping(value="/list",method= RequestMethod.GET)
    @ResponseBody
    public String getMessageList(){
        List<Message> list = null;
        try {
            list = messageService.getMessageList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONUtil.listToJson(list);
    }


    @RequestMapping(value="/add",method= RequestMethod.POST)
    @ResponseBody
    public String addRecord(@RequestBody MessageData messageData){
        try {
            messageService.addMessage(messageData.getIntro(), messageData.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @RequestMapping(value="/delete",method= RequestMethod.POST)
    @ResponseBody
    public String deleteRecord(@RequestBody MessageData messageData){
        try {
            messageService.delMessage(messageData.getDeleteId());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @RequestMapping(value="/search",method= RequestMethod.GET)
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
