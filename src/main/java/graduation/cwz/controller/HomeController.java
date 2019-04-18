package graduation.cwz.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import graduation.cwz.model.RegisterData;
import graduation.cwz.utils.JsonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/", "/homepage"})
public class HomeController {

    @RequestMapping(method = GET)
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/result", method = GET)
    public String result() {
        return "result";
    }

    @RequestMapping(value = "/login", method = GET)
    public String login() {
        return "result";
    }

    @RequestMapping(value = "/register", method = POST)
    @ResponseBody
    public String register(@RequestBody RegisterData registerData) {
        return JsonUtil.getJsonStr(registerData);
    }

}
