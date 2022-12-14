package com.medicinedeliveryapp.medicinedeliveryapp;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.medicinedeliveryapp.medicinedeliveryapp.details.AbstractDetails;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Buyer;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Cart;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.CartProduct;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Consultation;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Doctor;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Driver;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Medicine;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Order;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.OrderList;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Pharmacist;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Product;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Transaction;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.User;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.BuyerRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.CartProductRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.CartRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.ConsultationRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.DoctorRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.DriverRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.MedicineRepo;
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

    @Autowired
    private ConsultationRepo consultationRepo;

    @Autowired
    private MedicineRepo medicineRepo;

    private double shippingFeeValue = 50.0;
    private double seniorDiscount = 25.0;

    private final String permanentAdminCode = "abc123";

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

    @GetMapping("")
    public ModelAndView homePage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index.html");

        mav.addObject("notif_count", getDriverNotifCount());

        return mav;
    }

    @GetMapping("/about")
    public ModelAndView aboutUs(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("about.html");

        mav.addObject("notif_count", getDriverNotifCount());

        return mav;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(@RequestParam( value = "nc" , required = false) String noCredentials){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login.html");

        mav.addObject("user", new User());
        if(noCredentials != null){
            mav.addObject("nc", noCredentials.equals("1"));
        }

        mav.addObject("notif_count", getDriverNotifCount());

        return mav;
    }

    @PostMapping("/login-fail")
    public RedirectView loginFail(){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/login?nc=1");

        return rv;
    }

    @GetMapping("/register")
    public ModelAndView registerPage(@RequestParam( value = "ue", required = false) String userExists){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("register.html");

        mav.addObject("user", new User());
        mav.addObject("buyer", new Buyer());
        mav.addObject("doctor", new Doctor());
        mav.addObject("pharmacist", new Pharmacist());
        mav.addObject("driver", new Driver());
        if(userExists != null){
            mav.addObject("ue", userExists.equals("1"));
        }

        mav.addObject("notif_count", getDriverNotifCount());
        
        return mav;
    }

    @PostMapping("/process-user")
    public RedirectView processUserRegistration(User user, Buyer buyer, Doctor doctor, Pharmacist pharmacist, Driver driver, @RequestParam("admin_code") String adminCode){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/login");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String finalAdminCode = "";

        if(userRepo.findByEmail(user.getEmail()) != null){
            rv.setUrl("/register?ue=1");
            return rv;
        }

        if(user.getRole().equals("buyer")){

            LocalDate bday = LocalDate.parse(user.getBirthday());
            int age = Period.between(bday, LocalDate.now()).getYears();
            if(age >= 60){
                buyer.setDiscount(seniorDiscount);
            }
            buyer.setUser(user);
            buyer.setCart(new Cart());
            buyerRepo.save(buyer);

        }else if(user.getRole().equals("doctor")){

            doctor.setUser(user);

            doctor.setLicense_id(doctor.getLicense_id().split(",")[0]);
            finalAdminCode = adminCode.split(",")[0];
            if(finalAdminCode.equals(permanentAdminCode)){
                doctorRepo.save(doctor);
            }else{
                rv.setUrl("/register");
            }
            
        }else if(user.getRole().equals("pharmacist")){

            pharmacist.setUser(user);

            pharmacist.setLicense_id(pharmacist.getLicense_id().split(",")[1]);
            finalAdminCode = adminCode.split(",")[1];
            if(finalAdminCode.equals(permanentAdminCode)){
                pharmacistRepo.save(pharmacist);
            }else{
                rv.setUrl("/register");
            }
            
        }else if(user.getRole().equals("driver")){

            driver.setUser(user);

            driver.setLicense_id(driver.getLicense_id().split(",")[2]);
            finalAdminCode = adminCode.split(",")[2];
            if(finalAdminCode.equals(permanentAdminCode)){
                driverRepo.save(driver);
            }else{
                rv.setUrl("/register");
            }
            
        }

        return rv;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboardPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("dashboard.html");

        List<Consultation> consultations = consultationRepo.findAllByStatusAndDeliveryStatus("consulting", "Not ready for delivery");
        List<Consultation> pendingConsultations = consultationRepo.findAllByStatusAndDeliveryStatus("pending", "Not ready for delivery");
        List<Consultation> confirmedConsultations = consultationRepo.findAllByStatusAndDeliveryStatus("confirmed", "Ready for delivery");
        List<Consultation> deliveredConsultations = consultationRepo.findAllByStatusAndDeliveryStatus("confirmed", "Delivered");

        mav.addObject("notif_count", getDriverNotifCount());
        mav.addObject("consultations", consultations);
        mav.addObject("pendingConsultations", pendingConsultations);
        mav.addObject("confirmedConsultations", confirmedConsultations);
        mav.addObject("deliveredConsultations", deliveredConsultations);

        return mav;
    }

    @GetMapping("/store")
    public ModelAndView storePage(@RequestParam(value = "keyword", required = false) String keyword){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("store.html");

        List<Product> products = productRepo.findAll();
        
        if(keyword != null && !keyword.equals("")){
            products = productRepo.findAllByBrandNameContainingOrGenericNameContaining(keyword, keyword);
        }

        mav.addObject("notif_count", getDriverNotifCount());
        mav.addObject("products", products);
        mav.addObject("count", products.size());
        mav.addObject("keyword", keyword);

        return mav;
    }

    @GetMapping("/product-search")
    public List<Product> getProducts(@RequestParam( value = "keyword", required = false ) String keyword){
        List<Product> products = productRepo.findAllByBrandNameContainingOrGenericNameContaining(keyword, keyword);
        return products;
    }

    @GetMapping("/new-product")
    public String newProduct(@RequestParam( value = "b", required = false ) String brand, @RequestParam( value = "g", required = false ) String generic, @RequestParam( value = "d", required = false ) String dosage, @RequestParam( value = "f", required = false ) String form, @RequestParam( value = "desc", required = false ) String description, @RequestParam( value = "p", required = false ) double price, @RequestParam( value = "s", required = false ) int stock){

        Product product = new Product();
        product.setBrandName(brand);
        product.setGenericName(generic);
        product.setDosageStrength(dosage);
        product.setForm(form);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setUrl("none.jpg");

        Product savedProduct = productRepo.save(product);

        if(savedProduct.getBrandName().equals(brand)){
            return "true";
        }

        return "false";
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

    @PostMapping("/edit-product")
    public RedirectView editProduct(Product product, @RequestParam( value = "product", required = true ) long id){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("product/" + id);

        productRepo.save(product);

        return rv;
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
        rv.setContextRelative(false);
        rv.setUrl("/cart");

        Buyer currentBuyer = getBuyer(getCurrentUser());

        if(id == 0){
            Cart cart = currentBuyer.getCart();
            cartRepo.deleteById(cart.getId());
            List<CartProduct> cartProducts = cart.getCartProducts();
            List<Long> ids = new ArrayList<>();
            for(int i = 0; i < cartProducts.size(); i++){
                ids.add(cartProducts.get(i).getId());
            }
            while(cartProducts.size() != 0){
                cartProducts.remove(0);
            }
            cart.setCartProducts(cartProducts);
            currentBuyer.setCart(cart);

            for(Long idCopy : ids){
                cartProductRepo.deleteById(idCopy);
            }
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

        double discount = 0;

        if(currentBuyer.getDiscount() != 0){
            discount = totalPrice * (currentBuyer.getDiscount() / 100.0);
        }

        mav.addObject("notif_count", getDriverNotifCount());
        mav.addObject("buyer", currentBuyer);
        mav.addObject("count", currentBuyer.getCart().getCartProducts().size());
        mav.addObject("total", totalPrice);
        mav.addObject("shippingTotal", shippingFeeValue);
        mav.addObject("discount", discount);
        mav.addObject("discountValue", seniorDiscount);
        mav.addObject("cart", false);
        mav.addObject("transaction", new Transaction());

        return mav;
    }

    @GetMapping("/my-order")
    public ModelAndView orderPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("order.html");

        List<Transaction> deliveries = transactionRepo.findAllByDeliveryStatusAndUser("To deliver and pay", getCurrentUser());
        List<Transaction> received = transactionRepo.findAllByDeliveryStatusAndUser("Delivered", getCurrentUser());
        List<Transaction> cancelled = transactionRepo.findAllByDeliveryStatusAndUser("Cancelled", getCurrentUser());


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

        double discount = 0;

        if(transaction.getDiscount() != 0){
            discount = total * (transaction.getDiscount() / 100.0);
        }

        mav.addObject("notif_count", getDriverNotifCount());
        mav.addObject("transaction", transaction);
        mav.addObject("discount", discount);
        mav.addObject("total", total);

        return mav;
    }

    @PostMapping("/place-order")
    public RedirectView orderProcess(Transaction transaction){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/thank-you?data=true");

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
        transaction.setOrderList(orderList);
        transaction.setDateTransaction(dateTime);
        transaction.setDeliveryStatus("To deliver and pay");
        transaction.setShippingTotal(shippingFeeValue);
        transaction.setDiscount(getBuyer(user).getDiscount());
        
        transactionRepo.save(transaction);

        Cart currentCart = getBuyer(user).getCart();
        cartRepo.deleteById(currentCart.getId());
        List<CartProduct> cartProducts2 = currentCart.getCartProducts();
        List<Long> ids = new ArrayList<>();
        for(int i = 0; i < cartProducts2.size(); i++){
            ids.add(cartProducts2.get(i).getId());
        }
        while(cartProducts2.size() != 0){
            cartProducts2.remove(0);
        }

        currentCart.setCartProducts(cartProducts2);
        for(Long id : ids){
            cartProductRepo.deleteById(id);
        }

        return rv;
    }

    @GetMapping("/thank-you")
    public ModelAndView thankYou(@RequestParam( value = "data", required = false ) String data){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("thankyou.html");
        
        mav.addObject("data", data);
        mav.addObject("notif_count", getDriverNotifCount());

        return mav;
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

    @GetMapping("/delivered-order")
    public RedirectView orderDelivered(@RequestParam( value = "transaction", required = true ) long id){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/deliveries");

        Transaction transaction = transactionRepo.findById(id).get();
        transaction.setDeliveryStatus("Delivered");

        transactionRepo.save(transaction);

        return rv;
    }

    @GetMapping("/deliveries")
    public ModelAndView deliveryPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("deliveries.html");

        List<Transaction> deliveries = transactionRepo.findAllByDeliveryStatus("To deliver and pay");
        List<Consultation> confirmedConsultations = consultationRepo.findAllByStatusAndDeliveryStatus("confirmed", "Ready for delivery");

        mav.addObject("notif_count", getDriverNotifCount());
        mav.addObject("deliveries", deliveries);
        mav.addObject("confirmedConsultations", confirmedConsultations);

        return mav;
    }

    @GetMapping("/dashboard/consultation")
    public ModelAndView consultationPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("consult.html");
        
        mav.addObject("notif_count", getDriverNotifCount());
        mav.addObject("consult", new Consultation());

        return mav;
    }

    @PostMapping("/process-consult")
    public RedirectView processConsultation(Consultation consultation){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);

        consultation.setStatus("consulting");
        consultation.setDeliveryStatus("Not ready for delivery");
        consultation.setShippingFee(shippingFeeValue);
        Consultation savedConsult = consultationRepo.save(consultation);

        rv.setUrl("dashboard/consultation/" + savedConsult.getId());
        return rv;
    }

    @GetMapping("/dashboard/consultation/{id}")
    public ModelAndView consultPage(@PathVariable("id") long id){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("consultation.html");

        Consultation consultation = consultationRepo.findById(id).get();

        boolean ready = true;
        for(Medicine med : consultation.getMedicines()){
            if(!med.getStatus().equals("Confirmed")){
                ready = false;
                break;
            }
        }

        mav.addObject("notif_count", getDriverNotifCount());
        mav.addObject("consultation", consultation);
        mav.addObject("medicine", new Medicine());
        mav.addObject("total", consultation.getTotal());
        mav.addObject("new_consultation", new Consultation());
        mav.addObject("ready", ready && consultation.getTotal() > 0 && consultation.getShippingFee() > 0);

        return mav;
    }

    @PostMapping("/update-total")
    public RedirectView updateSubtotal(Consultation consultation, @RequestParam( value = "id", required = true) long id){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard/consultation/" + id);

        Consultation currentConsultation = consultationRepo.findById(id).get();

        currentConsultation.setTotal(consultation.getTotal());

        consultationRepo.save(currentConsultation);

        return rv;
    }

    @PostMapping("/process-medicine")
    public RedirectView processMedicine(@RequestParam( value = "consultation", required = true ) long id, Medicine medicine){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard/consultation/" + id);

        Consultation consultation = consultationRepo.findById(id).get();
        List<Medicine> medicines = consultation.getMedicines();
        
        if(medicines == null){
            medicines = new ArrayList<>();
        }

        medicine.setStatus("Pending");
        Medicine savedMedicine = medicineRepo.save(medicine);

        medicines.add(savedMedicine);

        consultation.setMedicines(medicines);

        consultationRepo.save(consultation);

        return rv;
    }

    @GetMapping("/remove-medicine")
    public boolean removeMedicine(@RequestParam( value = "medicine", required = true ) long id, @RequestParam( value = "consultation", required = true ) long cid){

        Consultation currentConsultation = consultationRepo.findById(cid).get();
        Medicine selectedMedicine = medicineRepo.findById(id).get();
        currentConsultation.getMedicines().remove(selectedMedicine);

        medicineRepo.delete(selectedMedicine);
        consultationRepo.save(currentConsultation);

        return true;
    }

    @GetMapping("/confirm-consultation")
    public RedirectView confirmConsultation(@RequestParam( value = "consultation", required = true ) long id){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");

        Consultation consultation = consultationRepo.findById(id).get();
        consultation.setStatus("pending");

        consultationRepo.save(consultation);

        return rv;
    }

    @GetMapping("/delete-consultation")
    public RedirectView deleteConsultation(@RequestParam( value = "consultation", required = true ) long id){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");

        Consultation consultation = consultationRepo.findById(id).get();

        consultationRepo.delete(consultation);

        return rv;
    }

    @GetMapping("/proceed-consultation")
    public RedirectView proceedConsultation(@RequestParam( value = "consultation", required = true ) long id){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");

        Consultation consultation = consultationRepo.findById(id).get();
        consultation.setStatus("confirmed");
        consultation.setDeliveryStatus("Ready for delivery");

        consultationRepo.save(consultation);

        return rv;
    }

    @GetMapping("/consultation-delivered")
    public RedirectView consultationDelivered(@RequestParam( value = "consultation", required = true ) long id){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");

        Consultation consultation = consultationRepo.findById(id).get();
        consultation.setStatus("confirmed");
        consultation.setDeliveryStatus("Delivered");

        consultationRepo.save(consultation);

        return rv;
    }

    @GetMapping("/medicine-status")
    public RedirectView confirmedicine(@RequestParam( value = "id", required = true ) long id, @RequestParam( value = "status", required = true ) int status){
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/dashboard");
        
        Medicine medicine = medicineRepo.findById(id).get();
        String med_status = "";
        if(status == 1){
            med_status = "Confirmed";
        }else{
            med_status = "N/A";
        }

        medicine.setStatus(med_status);

        medicineRepo.save(medicine);

        return rv;
    }

    @GetMapping("/my-account")
    public ModelAndView myAccountPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("my-account.html");

        mav.addObject("notif_count", getDriverNotifCount());
        if(getCurrentUser().getRole().equals("driver")){
            mav.addObject("license_id", driverRepo.findByUser(getCurrentUser()).getLicense_id());
        }else if(getCurrentUser().getRole().equals("doctor")){
            mav.addObject("license_id", doctorRepo.findByUser(getCurrentUser()).getLicense_id());
        }else if(getCurrentUser().getRole().equals("pharmacist")){
            mav.addObject("license_id", pharmacistRepo.findByUser(getCurrentUser()).getLicense_id());
        }

        return mav;
    }

    @GetMapping("/check-password")
    public boolean passwordChecker(@RequestParam( value = "pass", required = true) String pass){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(pass, getCurrentUser().getPassword());
    }

    @GetMapping("/change-password")
    public boolean passwordChanger(@RequestParam( value = "pass", required = true) String pass){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(pass);

        User currentUser = getCurrentUser();
        currentUser.setPassword(encodedPassword);

        User savedUser = userRepo.save(currentUser);

        return encoder.matches(pass, savedUser.getPassword());
    }

    @PostMapping("/upload-image")
    public RedirectView uploadImage(@RequestParam( value = "id", required = true) long id, @RequestParam("image") MultipartFile file) throws IOException{
        RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/product/" + id);

        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

        Product product = productRepo.findById(id).get();

        if(!product.getUrl().equals("none.jpg")){
            Files.delete(Paths.get(UPLOAD_DIRECTORY, product.getUrl()));
        }

        product.setUrl(file.getOriginalFilename());
        productRepo.save(product);
        
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
                count += deliveries.size();
                List<Consultation> confirmedConsultations = consultationRepo.findAllByStatusAndDeliveryStatus("confirmed", "Ready for delivery");
                count += confirmedConsultations.size();
            }
        }

        return count;
    }

}
