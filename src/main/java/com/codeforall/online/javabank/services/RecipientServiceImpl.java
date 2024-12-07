package com.codeforall.online.javabank.services;

import com.codeforall.online.javabank.exceptions.CustomerNotFoundException;
import com.codeforall.online.javabank.exceptions.RecipientNotFoundException;
import com.codeforall.online.javabank.model.Customer;
import com.codeforall.online.javabank.model.Recipient;
import com.codeforall.online.javabank.persistence.daos.RecipientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * An {@link RecipientService} implementation
 */
@Service
public class RecipientServiceImpl implements RecipientService {

    private RecipientDao recipientDao;
    private CustomerService customerService;

    /**
     * @see RecipientService#getCustomerRecipients(int)
     */
    @Override
    public List<Recipient> getCustomerRecipients(int cid) throws CustomerNotFoundException {
        Customer customer = customerService.get(cid);

        return new ArrayList<>(customer.getRecipients());
    }

    /**
     * @see RecipientService#getRecipient(int)
     */
    @Override
    public Recipient getRecipient(int rid) throws RecipientNotFoundException {
        return Optional.ofNullable(recipientDao.findById(rid)).orElseThrow(RecipientNotFoundException::new);
    }

    /**
     * Sets the recipient data access object
     * @param recipientDao the recipient DAO to set
     */
    @Autowired
    public void setRecipientDao(RecipientDao recipientDao) {
        this.recipientDao = recipientDao;
    }

    /**
     * Sets the customer service
     * @param customerService to set
     */
    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
