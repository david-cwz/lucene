package graduation.cwz.controller;

import graduation.cwz.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/config")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    @RequestMapping(value="/currentUser",method= RequestMethod.GET)
    @ResponseBody
    public String getMCurrentUser(){
        String currentUsername = "";
        try {
            currentUsername = configurationService.getCurrentUser();
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return currentUsername;
    }

}
