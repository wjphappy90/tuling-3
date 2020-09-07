package com.tuling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by smlz on 2019/7/12.
 */
@Controller
public class TulingController {

    @RequestMapping("/hello")
    public String hello() {
        System.out.println("hello");
        return "addUsers";
    }
}
