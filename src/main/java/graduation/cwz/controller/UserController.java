package graduation.cwz.controller;

import graduation.cwz.entity.User;
import graduation.cwz.model.PageBean;
import graduation.cwz.model.UserData;
import graduation.cwz.service.UserService;
import graduation.cwz.utils.JSONUtil;
import graduation.cwz.utils.MD5Util;
import graduation.cwz.utils.ResponseUtil;
import graduation.cwz.utils.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            session.setAttribute("currentUser", userData);
            return "redirect:/main.jsp";
        }

    }

    /**
     * 退出系统
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login.jsp";
    }

    /**
     * 注册
     */
    @RequestMapping("/register")
    public String register(UserData userData, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            userService.register(userData);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/modifyPassword")
    public String modifyPassword(UserData userData, HttpServletResponse response)throws Exception{
        JSONObject result = new JSONObject();
        try {
            userService.modifyPassword(userData.getUserName(), userData.getPassword());
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/modifyEmail")
    public String modifyEmail(UserData userData, HttpServletResponse response)throws Exception{
        JSONObject result = new JSONObject();
        try {
            userService.modifyEmail(userData.getUserName(), userData.getEmail());
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/list")
    public String userList(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        List<User> userList = userService.getUserList(map);
        int total = userList.size();
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(userList);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "nameList") String nameList, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            String[] nameListStr = nameList.split(",");
            for (int i = 0; i < nameListStr.length; i++) {
                userService.delUser(nameListStr[i]);
            }
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

}
