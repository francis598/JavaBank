package com.codeforall.online.javabank.model;

import com.codeforall.online.javabank.model.account.Account;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * A class which represents a customer of the bank
 */
@Entity
@Table(name = "customers")
public class Customer extends AbstractModel {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String photoURL;
    @Transient
    private double totalBalance;

    @Embedded
    private Address address;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "customers_accounts",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Account> accounts = new HashSet<>();


    @OneToMany(
            cascade = {CascadeType.ALL},
            orphanRemoval = true,
            mappedBy = "customer",
            fetch = FetchType.EAGER
    )
    private Set<Recipient> recipients = new HashSet<>();



    /**
     * Add an account to the customer accounts' list
     * @param account the account to be added
     */
    public void addAccount(Account account) {
        accounts.add(account);
        account.getCustomers().add(this);
    }

    /**
     * Remove a given account
     * @param account the account to be removed
     */
    public void removeAccount(Account account) {
        accounts.remove(account);
        account.getCustomers().remove(this);
    }


    /**
     * Add recipient
     * @param recipient the recipient to be added
     */
    public void addRecipient(Recipient recipient) {
        recipients.add(recipient);
        recipient.setCustomer(this);
    }

    /**
     * Remove a given recipient
     * @param recipient the recipient to be removed
     */
    public void removeRecipient(Recipient recipient) {
        recipients.remove(recipient);
        recipient.setCustomer(null);
    }


    /**
     * Get the customer first name
     * @return the customer first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the customer last name
     * @return the customer last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the customer email
     * @return the customer email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the customer phone
     * @return the customer phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Get the customer photo URL
     * @return the customer photo URL
     */
    public String getPhotoURL() {
        return photoURL;
    }

    /**
     * Get the customer's address
     * @return the customer's address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Get the total balance
     * @return the total balance
     */
    public double getTotalBalance() {
        return totalBalance;
    }

    /**
     * Get all customer accounts
     * @return a set of accounts
     */
    public Set<Account> getAccounts() {
        return accounts;
    }

    /**
     * List all customer recipients
     * @return a Set of recipients
     */
    public Set<Recipient> getRecipients() {
        return recipients;
    }

    /**
     * Set the customer first name to the given name
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set the customer last name to the given name
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set the customer email to the given email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the customer phone to the given phone
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Set the customer photoURL to the given URL
     * @param imageURL given
     */
    public void setPhotoURL(String imageURL) {
        this.photoURL = imageURL;
    }

    /**
     * Set the customer address to the given address
     * @param address given
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Set the totalBalance to the given balance
     * @param balance
     */
    public void setTotalBalance(double balance) {
        this.totalBalance = balance;
    }

    /**
     * Set the accounts to the given account list
     * @param accounts
     */
    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * Set the recipients to the given recipient list
     * @param recipients
     */
    public void setRecipients(Set<Recipient> recipients) {
        this.recipients = recipients;
    }
}



