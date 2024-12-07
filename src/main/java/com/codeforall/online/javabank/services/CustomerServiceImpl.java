package com.codeforall.online.javabank.services;

import com.codeforall.online.javabank.exceptions.*;
import com.codeforall.online.javabank.model.Address;
import com.codeforall.online.javabank.model.Customer;
import com.codeforall.online.javabank.model.Recipient;
import com.codeforall.online.javabank.model.account.Account;
import com.codeforall.online.javabank.persistence.daos.RecipientDao;
import com.codeforall.online.javabank.persistence.managers.TransactionManager;
import com.codeforall.online.javabank.persistence.daos.CustomerDao;
import com.codeforall.online.javabank.model.AbstractModel;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An {@link CustomerService} implementation
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private TransactionManager transactionManager;
    private CustomerDao customerDao;
    private RecipientDao recipientDao;
    private AccountService accountService;


    /**
     * @see CustomerService#get(int)
     */
    @Override
    public Customer get(int customerId) throws CustomerNotFoundException {

        return Optional.ofNullable(customerDao.findById(customerId)).orElseThrow(CustomerNotFoundException::new);
    }

    /**
     * @see CustomerService#list()
     */
    @Override
    public List<Customer> list() throws CustomerNotFoundException, AccountNotFoundException {

        List<Customer> customers = customerDao.findAll();

        for(Customer customer : customers) {

            // get the balance of each customer to show
            customer.setTotalBalance(getBalance(customer.getId()));
        }

        return customers;
    }

    /**
     * @see CustomerService#getBalance(int)
     */
    @Override
    public double getBalance(int customerId) throws CustomerNotFoundException {

        return get(customerId).getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    /**
     * @see CustomerService#add(Customer, Address)
     */
    @Override
    public Customer add(Customer customer, Address address) {
        Customer savedCustomer = null;

        try {
            transactionManager.beginWrite();

            if(customer.getPhotoURL() == null) {
                customer.setPhotoURL("profile-icon.png");
            }

            customer.setAddress(address);
            savedCustomer = customerDao.saveOrUpdate(customer);

            transactionManager.commit();

        } catch (PersistenceException e) {
            transactionManager.rollback();
        }

        return savedCustomer;
    }

    /**
     * @see CustomerService#openAccount(Integer, Account)
     */
    @Override
    public Account openAccount(Integer id, Account account) throws CustomerNotFoundException {
        Account newAccount = null;

        try {

            transactionManager.beginWrite();

            Customer customer = get(id);

            newAccount = accountService.add(account);

            customer.addAccount(account);
            customerDao.saveOrUpdate(customer);

            transactionManager.commit();

        } catch (PersistenceException | TransactionInvalidException e) {
            transactionManager.rollback();

        } finally {
            transactionManager.rollback();
        }

        return newAccount;
    }

    /**
     * @see CustomerService#closeAccount(Integer, Integer)
     */
    @Override
    public void closeAccount(Integer id, Integer accountId)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException {

        try {
            transactionManager.beginWrite();

            Customer customer = get(id);

            Account account = accountService.get(accountId);

            for (Customer c : account.getCustomers()) {
                if (!(c.getId() == id)) {
                    throw new AccountNotFoundException();
                }
            }

            //different from 0 in case we later decide that negative values are acceptable
            if (account.getBalance() != 0) {
                throw new TransactionInvalidException();
            }

            customer.removeAccount(account);
            customerDao.saveOrUpdate(customer);

            transactionManager.commit();

        } catch (PersistenceException e) {
            transactionManager.rollback();

        } finally {
            // if a TransactionInvalidException or AccountNotFoundException are thrown you want the session to stop
            transactionManager.rollback();
        }
    }

    /**
     * @see CustomerService#delete(Integer)
     */
    public void delete(Integer id) throws CustomerNotFoundException, AssociationExistsException {

        try {
            transactionManager.beginWrite();

            Customer customer = get(id);

            if (!customer.getAccounts().isEmpty()) {
                throw new AssociationExistsException();
            }

            customerDao.delete(id);

            transactionManager.commit();

        } catch (PersistenceException e) {
            transactionManager.rollback();

        } finally {
            // even if a CustomerNotFoundException or AssociationExistsException is thrown we should close the session
            transactionManager.rollback();
        }
    }

    /**
     * @see CustomerService#addRecipient(Integer, Recipient)
     */
    @Override
    public Recipient addRecipient(Integer cid, Recipient recipient) throws CustomerNotFoundException, AccountNotFoundException {
        Recipient addedRecipient = null;

        try {
            transactionManager.beginWrite();
            Customer customer = get(cid);

            recipient.setCustomer(customer);

            if (accountService.get(recipient.getAccountNumber()) == null ||
                    getAccountIds(customer).contains(recipient.getAccountNumber())) {
                throw new AccountNotFoundException();
            }

            if (recipient.getId() == 0) {

                customer.addRecipient(recipient);
                customerDao.saveOrUpdate(customer);

            } else {
                recipientDao.saveOrUpdate(recipient);
            }

            addedRecipient = customer.getRecipients()
                    .stream()
                    .toList()
                    .get(customer.getRecipients().size() - 1);

            transactionManager.commit();

        } catch (PersistenceException e) {
            transactionManager.rollback();

        } finally {
            transactionManager.rollback();
        }

        return addedRecipient;
    }

    /**
     * @see CustomerService#removeRecipient(Integer, Integer)
     */
    @Override
    public void removeRecipient(Integer id, Integer recipientId) throws CustomerNotFoundException, RecipientNotFoundException {

        try {
            transactionManager.beginWrite();

            Customer customer = get(id);

            Recipient recipient = Optional.ofNullable(recipientDao.findById(recipientId))
                    .orElseThrow(RecipientNotFoundException::new);

            if (!customer.getRecipients().contains(recipient)) {
                throw new RecipientNotFoundException();
            }

            customer.removeRecipient(recipient);
            customerDao.saveOrUpdate(customer);

            transactionManager.commit();

        } catch (PersistenceException e) {
            transactionManager.rollback();

        } finally {
            transactionManager.rollback();
        }
    }

    /**
     * Get the customer's accounts' ids
     * @param customer to which the accounts belong to
     * @return a set of account ids
     */
    private Set<Integer> getAccountIds(Customer customer) {
        Set<Account> accounts = customer.getAccounts();

        return accounts.stream()
                .map(AbstractModel::getId)
                .collect(Collectors.toSet());
    }


    /**
     * Set the transaction manager
     * @param transactionManager the transaction manager to set
     */
    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    /**
     * Set the customer data access object
     * @param customerDao the customer DAO to set
     */
    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * Set the recipient data access object
     * @param recipientDao the customer DAO to set
     */
    @Autowired
    public void setRecipientDao(RecipientDao recipientDao) {
        this.recipientDao = recipientDao;
    }

    /**
     * Set the account service
     * @param accountService to set
     */
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
