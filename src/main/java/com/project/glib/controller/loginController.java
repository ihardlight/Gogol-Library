package com.project.glib.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class loginController {

    @RequestMapping("/")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String verifyLogin(@RequestParam String username, @RequestParam String password, Model model) {
        if (username.equals("admin") && password.equals("123"))
            return "profile";
        model.addAttribute("loginError", "error");
        return "login";
    }

}