package graduation.cwz.controller;

import graduation.cwz.entity.User;
import graduation.cwz.model.PageBean;
import graduation.cwz.model.UserData;
import graduation.cwz.service.UserService;
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
        if ("user".equals(message) || "system".equals(message)) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", userService.getUserDataByName(userData.getUserName()));
            return "redirect:/main.jsp";
        } else {
            request.setAttribute("errorMsg", "请认真核对账号、密码！ message：" + message);
            return "login";
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
            userData.setRole("user");
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
    public String modifyPassword(UserData userData, HttpServletResponse response, HttpServletRequest request)throws Exception{
        JSONObject result = new JSONObject();
        try {
            userService.modifyPassword(userData.getUserName(), userData.getPassword());
            result.put("success", true);
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", userService.getUserDataByName(userData.getUserName()));
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/modifyUserInfo")
    public String modifyUserInfo(UserData userData, @RequestParam(value = "currentUser") String currentUser,
                              HttpServletResponse response, HttpServletRequest request)throws Exception{
        JSONObject result = new JSONObject();
        try {
            userService.modifyUserInfo(userData.getUserName(), userData.getEmail(), currentUser);
            result.put("success", true);
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", userService.getUserDataByName(userData.getUserName()));
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
        List<UserData> userDataList = userService.getUserDataList(userList);
        int total = userService.countUser();
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(userDataList);
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

    /**
     * 升级用户权限
     */
    @RequestMapping("/changeToSystem")
    public String changeToSystem(@RequestParam(value = "nameList") String nameList, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        try {
            String[] nameListStr = nameList.split(",");
            for (int i = 0; i < nameListStr.length; i++) {
                userService.changeToSystem(nameListStr[i]);
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
