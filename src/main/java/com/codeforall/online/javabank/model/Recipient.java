package com.codeforall.online.javabank.model;

import com.codeforall.online.javabank.model.transaction.Transaction;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


/**
 * A class which represents a recipient
 */
@Entity
@Table(name = "recipients")
public class Recipient extends AbstractModel {

    private Integer accountNumber;
    private String name;
    private String description;
    @ManyToOne
    private Customer customer;
    @OneToMany(
            cascade = {CascadeType.ALL},
            orphanRemoval = true,
            mappedBy = "recipient",
            fetch = FetchType.EAGER
    )
    private Set<Transaction> transactions = new HashSet<>();


    /**
     * Add transaction
     * @param transaction the transaction to be added
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setRecipient(this);
    }

    /**
     * Remove transaction
     * @param transaction the transaction to be removed
     */
    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setRecipient(null);
    }

    /**
     * Get the account number
     * @return the account number
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * Get the recipient name
     * @return the recipient name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the customer
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Get the transactions related to that recipient
     * @return the set of transactions
     */
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Set the account number to the given number
     * @param accountNumber
     */
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Set the recipient name to the given name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the description to the given description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the customer to the given customer
     * @param customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Set the list of transaction to the given transaction list
     * @param transactions
     */
    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
}
