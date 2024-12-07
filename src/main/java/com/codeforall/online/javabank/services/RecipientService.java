package com.codeforall.online.javabank.services;

import com.codeforall.online.javabank.exceptions.CustomerNotFoundException;
import com.codeforall.online.javabank.exceptions.RecipientNotFoundException;
import com.codeforall.online.javabank.model.Recipient;
import java.util.List;


/**
 * Common interface for recipient services, provides methods to manage recipients
 */
public interface RecipientService {

    /**
     * Get customer's recipients list
     * @param cid the id of the customer
     * @return a set of recipients
     * @throws CustomerNotFoundException if the customer is not found
     */
    List<Recipient> getCustomerRecipients(int cid) throws CustomerNotFoundException;

    /**
     * Get a recipient by its id
     * @param rid the id of the recipient
     * @return the recipient
     */
    Recipient getRecipient(int rid) throws RecipientNotFoundException;
}
