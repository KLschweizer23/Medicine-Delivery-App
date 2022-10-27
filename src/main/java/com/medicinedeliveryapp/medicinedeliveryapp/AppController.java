package com.medicinedeliveryapp.medicinedeliveryapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.medicinedeliveryapp.medicinedeliveryapp.details.AbstractDetails;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Buyer;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Cart;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.CartProduct;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Doctor;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Driver;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Pharmacist;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Product;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.User;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.BuyerRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.DoctorRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.DriverRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.PharmacistRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.ProductRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.UserRepo;

@RestController
public class AppController {

    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private BuyerRepo buyerRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PharmacistRepo pharmacistRepo;

    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private ProductRepo productRepo;

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
            buyer.setCart(new Cart());
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

    @GetMapping("/store")
    public ModelAndView storePage(@RequestParam(value = "keyword", required = false) String keyword){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("store.html");

        List<Product> products = productRepo.findAll();
        
        if(keyword != null && !keyword.equals("")){
            products = productRepo.findAllByGenericNameContaining(keyword);
        }

        mav.addObject("products", products);
        mav.addObject("count", products.size());
        mav.addObject("keyword", keyword);

        return mav;
    }

    @GetMapping("/product/{id}")
    public ModelAndView productPage(@PathVariable("id") long id){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("product.html");
        
        Product product = productRepo.findById(id).get();

        mav.addObject("product", product);

        return mav;
    }

    @GetMapping("/add-to-cart")
    public boolean addToCart(@RequestParam( value = "prod_id", required = true ) long prod_id, @RequestParam( value = "quantity", required = true ) int quantity){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!auth.getPrincipal().toString().equals("anonymousUser")){
            User user = getCurrentUser();

            if(!user.getRole().equals("buyer")){
                return false;
            }
            Buyer buyer = getBuyer(user);
            Cart cart = buyer.getCart();
            
            if(cart == null){
                cart = new Cart();
            }

            List<CartProduct> cartProducts = cart.getCartProducts();
            Product product = productRepo.findById(prod_id).get();
            CartProduct cartProduct = new CartProduct();
            cartProduct.setProduct(product);
            cartProduct.setQuantity(quantity);
            cartProducts.add(cartProduct);
            cart.setCartProducts(cartProducts);
            buyer.setCart(cart);

            buyerRepo.save(buyer);
        }

        return !auth.getPrincipal().toString().equals("anonymousUser");
    }

    @GetMapping("/cart")
    public ModelAndView cartPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("cart.html");

        Buyer currentBuyer = getBuyer(getCurrentUser());
        List<CartProduct> cartProducts = currentBuyer.getCart().getCartProducts();

        double totalPrice = 0;
        for(CartProduct cartProduct : cartProducts){
            Product product = cartProduct.getProduct();

            if(cartProduct.getQuantity() > product.getStock()){
                cartProduct.setQuantity(product.getStock());
            }

            if(product.getStock() != 0 && cartProduct.getQuantity() == 0){
                cartProduct.setQuantity(1);
            }

            totalPrice += product.getPrice() * cartProduct.getQuantity();
        }

        mav.addObject("buyer", currentBuyer);
        mav.addObject("count", currentBuyer.getCart().getCartProducts().size());
        mav.addObject("total", totalPrice);

        return mav;
    }

    @GetMapping("/remove-product")
    public RedirectView removeProduct(@RequestParam( value = "prod_id", required = true ) long id){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/cart");

        Buyer currentBuyer = getBuyer(getCurrentUser());

        if(id == 0){
            currentBuyer.setCart(new Cart());
        }else{
            Cart currentCart = currentBuyer.getCart();
            List<CartProduct> cartProducts = currentCart.getCartProducts();
            
            for(CartProduct cartProduct : cartProducts){
                if(cartProduct.getProduct().getId() == id){
                    cartProducts.remove(cartProduct);
                    break;
                }
            }

            currentCart.setCartProducts(cartProducts);
            currentBuyer.setCart(currentCart);
        }

        buyerRepo.save(currentBuyer);

        return rv;
    }

    @GetMapping("checkout")
    public ModelAndView checkoutPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("checkout.html");

        return mav;
    }

    private User getCurrentUser(){
        User user = new User();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AbstractDetails abstractDetails = (AbstractDetails) auth.getPrincipal();
        user = userRepo.findById(abstractDetails.getId()).get();

        return user;
    }

    private Buyer getBuyer(User user){
        Buyer buyer = new Buyer();

        buyer = buyerRepo.findByUser(user);

        return buyer;
    }

}
