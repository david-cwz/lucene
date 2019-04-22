package graduation.cwz.controller;

import graduation.cwz.entity.User;
import graduation.cwz.model.UserData;
import graduation.cwz.service.UserService;
import graduation.cwz.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value="/login",method= RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody UserData userData){
        String message = "";
        try {
            message = userService.login(userData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    @RequestMapping(value = "/register", method = POST)
    @ResponseBody
    public String register(@RequestBody UserData userData) {
        try {
            userService.register(userData);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @RequestMapping(value="/list",method= RequestMethod.GET)
    @ResponseBody
    public String userList(){
        List<User> list = null;
        try {
            list = userService.getUserList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONUtil.listToJson(list);
    }

    @RequestMapping(value="/delete",method= RequestMethod.POST)
    @ResponseBody
    public String deleteUser(@RequestBody UserData userData){
        try {
            userService.delUser(userData.getDeleteName());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @RequestMapping(value="/changePassword",method= RequestMethod.POST)
    @ResponseBody
    public String changePassword(@RequestBody UserData userData){
        try {
            userService.changePassword(userData.getUsername(), userData.getNewPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

}
