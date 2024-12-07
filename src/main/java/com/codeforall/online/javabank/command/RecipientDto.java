package com.codeforall.online.javabank.command;

import com.codeforall.online.javabank.model.Recipient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * A class which represent the data transfer object related to {@link Recipient}.
 */
public class RecipientDto {

    private Integer id;

    @NotNull(message = "Account number is mandatory")
    private Integer accountNumber;

    @NotNull(message = "Name is mandatory")
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 64)
    private String name;

    @NotNull
    private String description;

    /**
     * Get the recipient's id
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get the recipient's account number
     * @return the account number
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * Get the recipient's name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the recipient's description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the recipient's id
     * @param id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Set the recipient's account number
     * @param accountNumber to set
     */
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Set the recipient's name
     * @param name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the recipient's description
     * @param description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

