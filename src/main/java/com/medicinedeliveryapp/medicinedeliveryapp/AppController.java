package com.medicinedeliveryapp.medicinedeliveryapp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.User;

@RestController
public class AppController {
    
    @GetMapping("")
    public ModelAndView homePage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index.html");

        return mav;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(Model model){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login.html");

        model.addAttribute("user", new User());

        return mav;
    }

    @GetMapping("/register")
    public ModelAndView registerPage(Model model){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("register.html");

        model.addAttribute("user", new User());

        return mav;
    }

    @PostMapping("/process-user")
    public ModelAndView processUserRegistration(User user, Model model){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("register_user_role.html");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        model.addAttribute("user", user);

        return mav;
    }

}
