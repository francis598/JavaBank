package com.codeforall.online.javabank.services;

import com.codeforall.online.javabank.exceptions.*;
import com.codeforall.online.javabank.model.Address;
import com.codeforall.online.javabank.model.Customer;
import com.codeforall.online.javabank.model.Recipient;
import com.codeforall.online.javabank.model.account.Account;

import java.util.List;

/**
 * Common interface for customer services, provides methods to manage customers
 */
public interface CustomerService {

    /**
     * Get the customer with the given id
     * @param customerId the customer id
     * @return the customer
     */
    Customer get(int customerId) throws CustomerNotFoundException;

    /**
     * List all customers
     * @return a list of customers
     */
    List<Customer> list() throws CustomerNotFoundException, AccountNotFoundException;

    /**
     * Get the total balance of a given customer accounts
     * @param customerId the customer id
     * @return the balance of all accounts of the given customer
     */
    double getBalance(int customerId) throws CustomerNotFoundException;

    /**
     * Add a given customer to customer list
     * @param customer the customer to add
     * @param address to add to the customer
     * @return the customer
     */
    Customer add(Customer customer, Address address);

    /**
     * Add given account to a customer and account service lists
     * @param id the customer id to associate the account with
     * @param account the account to be added
     */
    Account openAccount(Integer id, Account account) throws CustomerNotFoundException;

    /**
     * Close an account
     * @param cid the id of the customer to which the account belongs to
     * @param aid the id of the account to close
     * @throws CustomerNotFoundException if the customer is not found
     * @throws AccountNotFoundException if the account doesn't exist or does not belong to the client
     * @throws TransactionInvalidException if the account still has balance
     */
    void closeAccount(Integer cid, Integer aid) throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException;

    /**
     * Delete a customer
     * @param id of the customer to delete
     * @throws CustomerNotFoundException if the customer doesn't exist
     * @throws AssociationExistsException if the customer is still associated with other entities
     */
    void delete(Integer id) throws CustomerNotFoundException, AssociationExistsException;

    /**
     * Add a recipient
     * @param cid the id of the customer to which the recipient will be associated with
     * @param recipient the recipient to add
     * @return a recipient
     * @throws CustomerNotFoundException if the customer is not found
     * @throws AccountNotFoundException if the account number of the recipient doesn't exist on our bank
     */
    Recipient addRecipient(Integer cid, Recipient recipient) throws CustomerNotFoundException, AccountNotFoundException;

    /**
     * Remove a recipient
     * @param cid the id of the customer to which the recipient will be associated with
     * @param id of the recipient to remove
     * @throws CustomerNotFoundException if the customer is not found
     * @throws RecipientNotFoundException if the recipient is not found
     */
    void removeRecipient(Integer cid, Integer id) throws CustomerNotFoundException, RecipientNotFoundException;
}