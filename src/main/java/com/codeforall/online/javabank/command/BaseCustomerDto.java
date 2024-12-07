package com.codeforall.online.javabank.command;

import jakarta.validation.constraints.*;

/**
 * A generic customer data transfer object to be used as a base for concrete types of customer data transfer objects.
 */
public abstract class BaseCustomerDto {

    private Integer id;

    @NotNull(message = "First name is mandatory")
    @NotBlank(message = "First name is mandatory")
    @Size(min = 3, max = 64)
    private String firstName;

    @NotNull(message = "Last name is mandatory")
    @NotBlank(message = "Last name is mandatory")
    @Size(min = 3, max = 64)
    private String lastName;

    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]*$", message = "Phone number contains invalid characters")
    @Size(min = 9, max = 16)
    private String phone;


    /**
     * Get the customer's id
     * @return the customer's id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get the customer's first name
     * @return the customer's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the customer's last name
     * @return the customer's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the customer's email
     * @return the customer's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the customer's phone
     * @return the customer's phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set the customer's id
     * @param id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Set the customer's first name
     * @param firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set the customer's last name
     * @param lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set the customer's email
     * @param email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the customer's phone
     * @param phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
