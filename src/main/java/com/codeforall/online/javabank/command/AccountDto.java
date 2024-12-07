package com.codeforall.online.javabank.command;

import com.codeforall.online.javabank.model.account.AccountType;
import com.codeforall.online.javabank.model.account.Account;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

/**
 * A class which represent the data transfer object related to {@link Account}.
 */
public class AccountDto {

    private Integer id;

    @NotNull(message = "account type is mandatory")
    private AccountType accountType;

    @NumberFormat
    @Min(value = 100L, message = "Accounts should have at least 100€ to be created.")
    @NotNull(message = "Initial amount is mandatory")
    private double balance;

    /**
     * Get the account id
     * @return the account id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get the account type
     * @return the account type
     */
    public AccountType getAccountType() {
        return accountType;
    }

    /**
     * Get the account's balance
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Set the id
     * @param id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Set the account type
     * @param accountType to set
     */
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    /**
     * Set the account's balance
     * @param balance to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
