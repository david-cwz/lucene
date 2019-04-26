package graduation.cwz.controller;

import graduation.cwz.entity.User;
import graduation.cwz.model.UserData;
import graduation.cwz.service.UserService;
import graduation.cwz.utils.JSONUtil;
import graduation.cwz.utils.MD5Util;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value="/login")
    public String login(UserData userData, HttpServletRequest request){

        String message = "";
        try {
            message = userService.login(userData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"".equals(message)) {
            request.setAttribute("errorMsg", "请认真核对账号、密码！ message：" + message);
            return "login";
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", userData.getUserName());
            return "redirect:/main.jsp";
        }

    }

    @RequestMapping(value = "/register")
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

    @RequestMapping(value="/list")
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

    @RequestMapping(value="/delete")
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

    @RequestMapping(value="/modifyPassword")
    @ResponseBody
    public String changePassword(@RequestBody UserData userData){
        try {
            userService.changePassword(userData.getUserName(), userData.getNewPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

}
