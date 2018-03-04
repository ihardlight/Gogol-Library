package com.project.glib.controller;

import com.project.glib.model.User;
import com.project.glib.service.SecurityService;
import com.project.glib.service.UserService;
import com.project.glib.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "register";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.save(userForm);


        securityService.autologin(userForm.getLogin(), userForm.getPasswordConfirm());

        return "redirect:/login";
    }

    @RequestMapping(value = "/users")
    public ModelAndView users(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("allUsers",userService.getAllUsers());
        modelAndView.setViewName("patrons");
        return modelAndView;
    }





    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

}
