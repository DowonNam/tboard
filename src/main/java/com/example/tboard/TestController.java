package com.example.tboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


// 브라우저에 컨트롤러라고 알려준다
@Controller
public class TestController {
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello, world";
    }
}
