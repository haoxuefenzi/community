package com.wang.my_community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//<editor-fold desc="Description">
@Controller
//</editor-fold>
public class HelloController {

    @GetMapping("/")
    public String Index(){

        return "index";
    }
}
