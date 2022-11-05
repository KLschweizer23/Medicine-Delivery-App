package com.medicinedeliveryapp.medicinedeliveryapp;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

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
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Order;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.OrderList;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Pharmacist;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Product;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Transaction;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.User;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.BuyerRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.CartProductRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.CartRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.DoctorRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.DriverRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.PharmacistRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.ProductRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.TransactionRepo;
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

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartProductRepo cartProductRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @GetMapping("")
    public ModelAndView homePage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index.html");

        mav.addObject("notif_count", getDriverNotifCount());

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

        mav.addObject("notif_count", getDriverNotifCount());

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


        mav.addObject("notif_count", getDriverNotifCount());
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


        mav.addObject("notif_count", getDriverNotifCount());
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
            cartProduct.setFixedPrice(product.getPrice());

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
        boolean hasOutOfStock = false;
        for(CartProduct cartProduct : cartProducts){
            Product product = cartProduct.getProduct();

            if(cartProduct.getQuantity() > product.getStock()){
                cartProduct.setQuantity(product.getStock());
            }

            if(product.getStock() != 0 && cartProduct.getQuantity() == 0){
                cartProduct.setQuantity(1);
            }
            
            if(cartProduct.getQuantity() == 0){
                hasOutOfStock = true;
            }

            totalPrice += product.getPrice() * cartProduct.getQuantity();
        }

        currentBuyer.getCart().setCartProducts(cartProducts);
        buyerRepo.save(currentBuyer);


        mav.addObject("notif_count", getDriverNotifCount());
        mav.addObject("buyer", currentBuyer);
        mav.addObject("count", currentBuyer.getCart().getCartProducts().size());
        mav.addObject("total", totalPrice);
        mav.addObject("cart", true);
        mav.addObject("hasOutOfStock", hasOutOfStock);

        return mav;
    }

    @GetMapping("/remove-product")
    public RedirectView removeProduct(@RequestParam( value = "prod_id", required = true ) long id){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/cart");

        Buyer currentBuyer = getBuyer(getCurrentUser());

        if(id == 0){
            Cart cart = currentBuyer.getCart();
            cart.getCartProducts().clear();
            currentBuyer.setCart(cart);
            cartProductRepo.deleteAll();
        }else{
            Cart currentCart = currentBuyer.getCart();
            List<CartProduct> cartProducts = currentCart.getCartProducts();
            
            for(CartProduct cartProduct : cartProducts){
                if(cartProduct.getProduct().getId() == id){
                    cartProducts.remove(cartProduct);
                    cartProductRepo.delete(cartProduct);
                    break;
                }
            }

            currentCart.setCartProducts(cartProducts);
            currentBuyer.setCart(currentCart);
        }

        buyerRepo.save(currentBuyer);

        return rv;
    }

    @GetMapping("/checkout")
    public ModelAndView checkoutPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("checkout.html");

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
        }
        
        currentBuyer.getCart().setCartProducts(cartProducts);
        buyerRepo.save(currentBuyer);

        
        List<CartProduct> cartProductsCopy = cartProducts;

        for(CartProduct cartProduct : cartProductsCopy){   
            Product product = cartProduct.getProduct();
            if(product.getStock() == 0){
                cartProducts.remove(cartProduct);
                continue;
            }
            totalPrice += product.getPrice() * cartProduct.getQuantity();
        }

        currentBuyer.getCart().setCartProducts(cartProductsCopy);


        mav.addObject("notif_count", getDriverNotifCount());
        mav.addObject("buyer", currentBuyer);
        mav.addObject("count", currentBuyer.getCart().getCartProducts().size());
        mav.addObject("total", totalPrice);
        mav.addObject("cart", false);
        mav.addObject("transaction", new Transaction());

        return mav;
    }

    @GetMapping("/my-order")
    public ModelAndView orderPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("order.html");

        List<Transaction> deliveries = transactionRepo.findAllByDeliveryStatus("To deliver and pay");
        List<Transaction> received = transactionRepo.findAllByDeliveryStatus("Received");
        List<Transaction> cancelled = transactionRepo.findAllByDeliveryStatus("Cancelled");


        mav.addObject("notif_count", getDriverNotifCount());
        mav.addObject("deliveries", deliveries);
        mav.addObject("received", received);
        mav.addObject("cancelled", cancelled);

        return mav;
    }

    @GetMapping("/transaction/{id}")
    public ModelAndView transactionPage(@PathVariable("id") long id){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("transaction.html");

        Transaction transaction = transactionRepo.findById(id).get();

        OrderList orderList = transaction.getOrderList();
        double total = 0;
        for(Order order : orderList.getOrders()){
            total += order.getFixedPrice() * order.getQuantity();
        }


        mav.addObject("notif_count", getDriverNotifCount());
        mav.addObject("transaction", transaction);
        mav.addObject("total", total);

        return mav;
    }

    @PostMapping("/place-order")
    public RedirectView orderProcess(Transaction transaction){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/my-order");

        User user = getCurrentUser();
        OrderList orderList = new OrderList();

        List<CartProduct> cartProducts = getBuyer(getCurrentUser()).getCart().getCartProducts();
        List<Order> orders = new ArrayList<>();

        for(CartProduct cartProduct : cartProducts){
            Order order = new Order();
            order.setProduct(cartProduct.getProduct());
            order.setQuantity(cartProduct.getQuantity());
            order.setFixedPrice(cartProduct.getFixedPrice());
            orders.add(order);

            Product product = cartProduct.getProduct();
            product.setStock(product.getStock() - order.getQuantity());
            productRepo.save(product);
        }
        orderList.setOrders(orders);

        LocalDateTime dateTime = LocalDateTime.now();

        transaction.setUser(user);
        transaction.setAddress(user.getAddress());
        transaction.setOrderList(orderList);
        transaction.setDateTransaction(dateTime);
        transaction.setDeliveryStatus("To deliver and pay");

        Cart currentCart = getBuyer(user).getCart();
        currentCart.getCartProducts().clear();
        
        transactionRepo.save(transaction);
        cartRepo.save(currentCart);
        cartProductRepo.deleteAll();

        return rv;
    }

    @GetMapping("/cancel-order")
    public RedirectView cancelOrder(@RequestParam( value = "transaction", required = true ) long id){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/my-order");

        Transaction transaction = transactionRepo.findById(id).get();
        transaction.setDeliveryStatus("Cancelled");

        OrderList orderList = transaction.getOrderList();
        for(Order order : orderList.getOrders()){
            Product product = productRepo.findById(order.getProduct().getId()).get();
            product.setStock(product.getStock() + order.getQuantity());
            productRepo.save(product);
        }

        transactionRepo.save(transaction);

        return rv;
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

    private int getDriverNotifCount(){
        int count = 0;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!auth.getPrincipal().toString().equals("anonymousUser")){
            if(getCurrentUser().getRole().equals("driver")){
                List<Transaction> deliveries = transactionRepo.findAllByDeliveryStatus("To deliver and pay");
                count = deliveries.size();
            }
        }

        return count;
    }

}
