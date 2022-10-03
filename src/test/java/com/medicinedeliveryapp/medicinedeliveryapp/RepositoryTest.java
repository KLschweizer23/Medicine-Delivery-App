package com.medicinedeliveryapp.medicinedeliveryapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.medicinedeliveryapp.medicinedeliveryapp.objects.Driver;
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
    private UserRepo userRepo;

    @Test
    public void test(){

    }

}
