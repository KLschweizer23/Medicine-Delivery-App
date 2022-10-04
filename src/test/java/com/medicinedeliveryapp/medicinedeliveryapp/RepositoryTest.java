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
import com.medicinedeliveryapp.medicinedeliveryapp.objects.Driver;
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
    private UserRepo userRepo;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testBuyer(){
        
        Buyer buyer = new Buyer();
        User user = new User(1L, "Ken", "Bill", "Male", "Bill@gmail.com", "password", "Tagum City", "driver", 1L);
        
        buyer.setBuyer_id(1L);
        buyer.setUser(user);
        
        Buyer savedBuyer = buyerRepo.save(buyer);

        Buyer existBuyer = testEntityManager.find(Buyer.class, savedBuyer.getBuyer_id());

        assertThat(existBuyer.getBuyer_id()).isEqualTo(buyer.getBuyer_id());

    }

}
