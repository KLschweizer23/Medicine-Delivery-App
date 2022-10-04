package com.medicinedeliveryapp.medicinedeliveryapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Buyer;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Doctor;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Driver;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Pharmacist;
import com.medicinedeliveryapp.medicinedeliveryapp.objects.User;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.BuyerRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.DoctorRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.DriverRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.PharmacistRepo;
import com.medicinedeliveryapp.medicinedeliveryapp.repositories.UserRepo;

@DataJpaTest
@AutoConfigureTestDatabase( replace = Replace.NONE )
@Rollback(false)
public class RepositoryTest {
    
    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private BuyerRepo buyerRepo;

    @Autowired
    private PharmacistRepo pharmacistRepo;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testBuyer(){
        
        Buyer buyer = new Buyer();
        User user = new User();
        user.setFirstName("Buyer Ken");
        user.setLastName("Bill");
        user.setSex("Male");
        user.setEmail("buyer@gmail.com");
        user.setAddress("Tagum City");
        user.setPassword("password");
        user.setRole("buyer");
        
        buyer.setBuyer_id(1L);
        buyer.setUser(user);
        
        Buyer savedBuyer = buyerRepo.save(buyer);

        Buyer existBuyer = testEntityManager.find(Buyer.class, savedBuyer.getBuyer_id());

        assertThat(existBuyer.getBuyer_id()).isEqualTo(buyer.getBuyer_id());

    }

    @Test
    public void testDriver(){
        
        Driver driver = new Driver();
        User user = new User();
        user.setFirstName("Driver Ken");
        user.setLastName("Bill");
        user.setSex("Male");
        user.setEmail("driver@gmail.com");
        user.setAddress("Tagum City");
        user.setPassword("password");
        user.setRole("buyer");

        driver.setDriver_id(1L);
        driver.setUser(user);
        
        Driver savedDriver = driverRepo.save(driver);

        Driver existDriver = testEntityManager.find(Driver.class, savedDriver.getDriver_id());

        assertThat(existDriver.getDriver_id()).isEqualTo(driver.getDriver_id());

    }

    @Test
    public void testDoctor(){
        
        Doctor doctor = new Doctor();

        User user = new User();
        user.setFirstName("Doctor Ken");
        user.setLastName("Bill");
        user.setSex("Male");
        user.setEmail("doctor@gmail.com");
        user.setAddress("Tagum City");
        user.setPassword("password");
        user.setRole("buyer");
        
        doctor.setDoctor_id(1L);
        doctor.setLicense_id("D-ABC123");
        doctor.setUser(user);
        
        Doctor savedDoctor = doctorRepo.save(doctor);

        Doctor existDoctor = testEntityManager.find(Doctor.class, savedDoctor.getDoctor_id());

        assertThat(existDoctor.getDoctor_id()).isEqualTo(doctor.getDoctor_id());

    }

    @Test
    public void testPharmacist(){
        
        Pharmacist pharmacist = new Pharmacist();
        User user = new User();
        user.setFirstName("Pharmacist Ken");
        user.setLastName("Bill");
        user.setSex("Male");
        user.setEmail("pharmacist@gmail.com");
        user.setAddress("Tagum City");
        user.setPassword("password");
        user.setRole("buyer");
        
        pharmacist.setPharmacist_id(1L);
        pharmacist.setLicense_id("P-ABC123");
        pharmacist.setUser(user);
        
        Pharmacist savedPharmacist = pharmacistRepo.save(pharmacist);

        Pharmacist existPharmacist = testEntityManager.find(Pharmacist.class, savedPharmacist.getPharmacist_id());

        assertThat(existPharmacist.getPharmacist_id()).isEqualTo(pharmacist.getPharmacist_id());

    }

}
