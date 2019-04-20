package graduation.cwz.controller;

import graduation.cwz.entity.User;
import graduation.cwz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String getUser(HttpServletRequest request, HttpServletResponse response, Model model){
        try {
            List<User> list;
            list = userService.getUser();
            model.addAttribute("lists", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "result";
    }


}