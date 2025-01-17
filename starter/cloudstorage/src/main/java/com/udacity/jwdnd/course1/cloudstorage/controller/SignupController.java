package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signup() {
        return "/signup";
    }

    @PostMapping
    public String signupUser(@ModelAttribute Users users, ModelMap model, RedirectAttributes redirectAttributes) {
        String signUpError = null;

        if (userService.isUserAvailable(users.getUsername())) {
            signUpError = "Username already exist";
        }
        if (signUpError == null) {
            int rowsAdded = userService.createUser(users);
            if (rowsAdded < 0) {
                signUpError = "Signup error occurred please try again";
            }
        }
        if (signUpError == null) {
            redirectAttributes.addFlashAttribute("signUpSuccess", true);
            return "redirect:/login";
        } else {
            model.addAttribute("signUpError", signUpError);
            return "/signup";
        }

    }
}
