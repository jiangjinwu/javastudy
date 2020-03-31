package top.homesoft.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class Index {

    @ResponseBody
    @RequestMapping("/")
    public Map index(){
        Map map = new HashMap();
        return map;
    }
}
