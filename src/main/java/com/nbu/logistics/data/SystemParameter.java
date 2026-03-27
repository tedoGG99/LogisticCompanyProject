
package com.nbu.logistics.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "system_parameters")
public class SystemParameter {

    @Id
    @Column(name = "param_key", length = 50)
    private String key; // e.g., "BASE_PRICE"

    @Column(name = "param_value", nullable = false)
    private String value; // e.g., "5.00" - We store as String to preserve decimal precision

    // Getters and Setters
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
