package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResultController {

    @GetMapping("/upload")
    public String result() {


        return "result";
    }
}
