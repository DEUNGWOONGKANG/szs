package com.example.demo.restapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
	/*
    1. 화면에 helloworld가 출력된다.
     */
    @GetMapping(value = "/helloworld/string")
    @ResponseBody
    public String helloworldString() {
        return "helloworld";
    }
}
