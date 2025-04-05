package com.ZandhiDokkie.minuet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("temp")
    public String temp(Model model){
        model.addAttribute("date", "2025-03-17");
        return "temp";
    }
}
