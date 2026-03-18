package com.example.teamStack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        return "redirect:teamstack";
    }

    @GetMapping("/teamstack")
    public String home() {
        return "teamstack";
    }
}