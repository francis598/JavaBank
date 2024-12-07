package com.codeforall.online.javabank.model.transaction;

import com.codeforall.online.javabank.model.AbstractModel;
import com.codeforall.online.javabank.model.Recipient;
import com.codeforall.online.javabank.model.account.Account;

import jakarta.persistence.*;

/**
 * A class which represents the financial transactions of an account
 */
@Entity
@Table(name = "transactions")
public class Transaction extends AbstractModel {

    private Double amount;

    @ManyToOne
    private Account account;

    @ManyToOne(optional = true)
    private Purchase purchase;

    @ManyToOne(optional = true)
    private Recipient recipient;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    /**
     * Get the amount
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Get the account to which the transaction is associated with
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Get the purchase
     * @return the purchase
     */
    public Purchase getPurchase() {
        return purchase;
    }

    /**
     * Get the recipient
     * @return the recipient
     */
    public Recipient getRecipient() {
        return recipient;
    }

    /**
     * Get the transaction type
     * @return the transaction type
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * Set the amount to the given amount
     * @param amount to give
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Set the account to the given account
     * @param account to give
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Set the purchase to the given purchase
     * @param purchase to give
     */
    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    /**
     * Set the recipient to the given recipient
     * @param recipient to give
     */
    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    /**
     * Set the transactionType to the given transactionType
     * @param transactionType to give
     */
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
