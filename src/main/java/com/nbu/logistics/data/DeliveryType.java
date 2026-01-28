
package com.nbu.logistics.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "delivery_types")
public class DeliveryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "type_name", nullable = false, unique = true) 
    private String typeName;
    
    @Column(name = "requires_office") 
    private Boolean requiresOffice;

    public DeliveryType() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Boolean getRequiresOffice() {
        return requiresOffice;
    }

    public void setRequiresOffice(Boolean requiresOffice) {
        this.requiresOffice = requiresOffice;
    }

    
    

   
     
     
     
}
