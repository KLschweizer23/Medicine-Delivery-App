package com.medicinedeliveryapp.medicinedeliveryapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Buyer;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Doctor;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Driver;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Pharmacist;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.User;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.BuyerRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.DoctorRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.DriverRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.PharmacistRepo;

@RestController
public class AppController {
    
    @Autowired
    private BuyerRepo buyerRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PharmacistRepo pharmacistRepo;

    @Autowired
    private DriverRepo driverRepo;

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
        model.addAttribute("buyer", new Buyer());
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("pharmacist", new Pharmacist());
        model.addAttribute("driver", new Driver());
        
        return mav;
    }

    @PostMapping("/process-user")
    public RedirectView processUserRegistration(User user, Buyer buyer, Doctor doctor, Pharmacist pharmacist, Driver driver){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/login");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        if(user.getRole().equals("buyer")){

            buyer.setUser(user);
            buyerRepo.save(buyer);

        }else if(user.getRole().equals("doctor")){

            doctor.setUser(user);

            doctor.setLicense_id(doctor.getLicense_id().split(",")[0]);

            doctorRepo.save(doctor);
            
        }else if(user.getRole().equals("pharmacist")){

            pharmacist.setUser(user);

            pharmacist.setLicense_id(pharmacist.getLicense_id().split(",")[1]);

            pharmacistRepo.save(pharmacist);
            
        }else if(user.getRole().equals("driver")){

            driver.setUser(user);

            driver.setLicense_id(driver.getLicense_id().split(",")[2]);

            driverRepo.save(driver);
            
        }


        return rv;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboardPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("dashboard.html");

        return mav;
    }

}
