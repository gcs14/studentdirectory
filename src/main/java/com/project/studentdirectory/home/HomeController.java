package com.project.studentdirectory.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("home")
    public String hello(){
        return "home";
    }
}
