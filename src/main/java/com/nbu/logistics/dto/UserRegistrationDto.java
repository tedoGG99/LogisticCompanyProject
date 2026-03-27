
package com.nbu.logistics.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;


public class UserRegistrationDto {
    @NotBlank(message = "username is required")
    @Size(min = 3, max = 50, message = "username must be between 3 and 50 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "username can contain only letters, digits, '.', '_' and '-'"
    )
    private String username;
    
    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Size(max = 255, message = "email must be max 255 characters")
    private String email;
    
    @NotBlank(message = "password is required")
    @Size(min = 1, max = 72, message = "password must be between 1 and 72 characters")
    private String password;
    
    @NotBlank(message = "firstName is required")
    @Size(min = 2, max = 100, message = "firstName must be between 2 and 100 characters")
    private String firstName;
    
    @NotBlank(message = "lastName is required")
    @Size(min = 2, max = 100, message = "lastName must be between 2 and 100 characters")
    private String lastName;
    
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(admin|client|office employee|courier)$", 
             message = "Role must be one of: admin, client, office employee, courier")
    private String roleName; // e.g.,admin, client, office employee, courier
    
    @PositiveOrZero(message = "officeId must be 0 or positive")
    private Integer officeId;   // Only if role is OFFICE_EMPLOYEE
    
    public UserRegistrationDto() {
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }
    
}
