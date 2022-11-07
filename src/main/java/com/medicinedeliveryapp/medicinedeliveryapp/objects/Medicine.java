package com.medicinedeliveryapp.medicinedeliveryapp.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false, length = 255 )
    private String brandName;

    @Column( nullable = false, length = 255 )
    private String genericName;
    
    @Column( nullable = false, length = 255 )
    private String dosageStrength;
    
    @Column( nullable = false, length = 255 )
    private String form;

    @Column( nullable = false, length = 255 )
    private String status;

}
