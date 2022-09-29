package com.medicinedeliveryapp.medicinedeliveryapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AppController {
    
    @GetMapping("")
    public ModelAndView homePage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index.html");

        return mav;
    }

}
