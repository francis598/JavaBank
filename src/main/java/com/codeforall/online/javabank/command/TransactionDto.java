package com.codeforall.online.javabank.command;

import com.codeforall.online.javabank.model.transaction.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;


/**
 * A class which represent the data transfer object related to {@link Transaction}.
 */
public class TransactionDto {

    @NotNull(message = "The id of the account is mandatory")
    private Integer accountId;

    @NumberFormat
    @NotBlank(message = "Initial amount is mandatory")
    @NotNull(message = "Amount is mandatory")
    private String amount;


    /**
     * Get the id of the transaction DTO
     * @return the account DTO id
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * Get the amount of the transaction DTO
     * @return the transaction DTO amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Set the id of the transaction DTO
     * @param accountId to set
     */
    public void setId(Integer accountId) {
        this.accountId = accountId;
    }


    /**
     * Set the amount of the transaction DTO
     * @param amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }
}
