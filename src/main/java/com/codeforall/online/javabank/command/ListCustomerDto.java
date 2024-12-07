package com.codeforall.online.javabank.command;

/**
 * A concrete implementation of a Customer Data Transfer Object to show on index page
 */
public class ListCustomerDto extends BaseCustomerDto {

    private String photoURL;
    private double totalBalance;
    private int numOfAccounts;

    /**
     * Get the customer's photo url
     * @return the photo url
     */
    public String getPhotoURL() {
        return photoURL;
    }

    /**
     * Get the customer's total balance
     * @return the total balance
     */
    public double getTotalBalance() {
        return totalBalance;
    }

    /**
     * Get the number of active accounts of the customer
     * @return the number of accounts of the customer
     */
    public int getNumOfAccounts() {
        return numOfAccounts;
    }

    /**
     * Set the customer's photo url
     * @param photoURL to set
     */
    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    /**
     * Set the customer's total balance
     * @param totalBalance to set
     */
    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    /**
     * Set the customer's number of accounts
     * @param numOfAccounts to set
     */
    public void setNumOfAccounts(int numOfAccounts) {
        this.numOfAccounts = numOfAccounts;
    }
}
