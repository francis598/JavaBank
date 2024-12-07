package com.codeforall.online.javabank.services;

import com.codeforall.online.javabank.exceptions.AccountNotFoundException;
import com.codeforall.online.javabank.exceptions.CustomerNotFoundException;
import com.codeforall.online.javabank.exceptions.RecipientNotFoundException;
import com.codeforall.online.javabank.exceptions.TransactionInvalidException;
import com.codeforall.online.javabank.model.Customer;
import com.codeforall.online.javabank.model.Recipient;
import com.codeforall.online.javabank.model.account.Account;
import com.codeforall.online.javabank.model.account.SavingsAccount;
import com.codeforall.online.javabank.model.transaction.TransactionType;
import com.codeforall.online.javabank.persistence.daos.AccountDao;
import com.codeforall.online.javabank.persistence.managers.TransactionManager;
import com.codeforall.online.javabank.persistence.managers.jpa.JpaTransactionManager;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An {@link AccountService} implementation
 */
@Service
public class AccountServiceImpl implements AccountService {

    private TransactionManager transactionManager;
    private AccountDao accountDao;
    private TransactionService transactionService;
    private RecipientService recipientService;
    private CustomerService customerService;

    /**
     * @see AccountService#add(Account)
     */
    @Override
    public Account add(Account account) throws TransactionInvalidException {

        if(!transactionManager.isTransactionActive()) {
            throw new TransactionInvalidException();
        }

        if (!account.canWithdraw() &&
                account.getBalance() < SavingsAccount.MIN_BALANCE) {
            throw new TransactionInvalidException();
        }

        return Optional.ofNullable(accountDao.saveOrUpdate(account))
                .orElseThrow(TransactionInvalidException::new);
    }

    /**
     * @see AccountService#deposit(int, double)
     */
    @Override
    public void deposit(int id, double amount) throws AccountNotFoundException, TransactionInvalidException {

        try {

            transactionManager.beginWrite();

            Account account = get(id);

            if (!account.canCredit(amount)) {
                throw new TransactionInvalidException();
            }

            account.credit(amount);

            accountDao.saveOrUpdate(account);
            transactionService.registerSimpleTransaction(account, amount, TransactionType.DEPOSIT);

            transactionManager.commit();

        } catch (PersistenceException ex) {
            transactionManager.rollback();

        } finally {
            transactionManager.rollback();
        }
    }

    /**
     * @see AccountService#withdraw(int, double)
     */
    @Override
    public void withdraw(int id, double amount) throws AccountNotFoundException, TransactionInvalidException {
        try {

            transactionManager.beginWrite();

            Account account = get(id);

            if (!account.canWithdraw()) {
                throw new TransactionInvalidException();
            }

            if (!account.canDebit(amount)) {
                throw new TransactionInvalidException();
            }

            account.debit(amount);

            accountDao.saveOrUpdate(account);

            transactionService.registerSimpleTransaction(account, amount, TransactionType.WITHDRAWAL);

            transactionManager.commit();

        } catch (PersistenceException ex) {
            transactionManager.rollback();

        } finally {
            transactionManager.rollback();
        }
    }

    /**
     * @see AccountService#transfer(int, int, double, int)
     */
    @Override
    public void transfer(int srcId, int dstId, double amount, int customerId) throws AccountNotFoundException, CustomerNotFoundException, TransactionInvalidException, RecipientNotFoundException {
        try {

            transactionManager.beginWrite();

            Account srcAccount = get(srcId);
            Account dstAccount = get(dstId);

            if (!srcAccount.canDebit(amount) || !dstAccount.canCredit(amount)) {
                throw new TransactionInvalidException();
            }

            Customer customer = customerService.get(customerId);

            // make sure the source account belongs to the customer
            if (!customer.getAccounts().contains(srcAccount)) {
                throw new AccountNotFoundException();
            }

            // make sure destination account is a part of the recipient list
            verifyRecipientId(customer, dstAccount);

            doTransfer(srcAccount, dstAccount, amount);

            // register transaction on both sides
            transactionService.registerTransfer(srcAccount, amount, findTransferRecipient(customer, dstAccount.getId()));
            transactionService.registerTransfer(dstAccount, amount, null);

            transactionManager.commit();

        } catch (PersistenceException ex) {
            transactionManager.rollback();

        } finally {
            transactionManager.rollback();
        }
    }

    /**
     * @see AccountService#get(int)
     */
    @Override
    public Account get(int id) throws AccountNotFoundException {

        return Optional.ofNullable(accountDao.findById(id))
                .orElseThrow(AccountNotFoundException::new);
    }

    /**
     * Find transfer recipient
     * @param customer to which the recipient is related to
     * @param dstAccountId the id of the account of the recipient
     * @return the recipient
     * @throws RecipientNotFoundException if the recipient is not found
     */
    private Recipient findTransferRecipient(Customer customer, int dstAccountId) throws RecipientNotFoundException {
        Recipient recipient = null;

        for (Recipient r : customer.getRecipients()) {
            if(r.getAccountNumber() == dstAccountId) {
                recipient = recipientService.getRecipient(r.getId());
            }
        }
        return recipient;
    }

    /**
     * List the account numbers of all the recipients of a specified customer
     * @param customer to which the recipient is related to
     * @return
     */
    private List<Integer> listRecipientsAccountIds(Customer customer) {

        return customer.getRecipients().stream()
                .map(Recipient::getAccountNumber)
                .collect(Collectors.toList());
    }

    /**
     * Verify if the destination account id in the transfer operation belongs to a recipient or not
     * @param customer to which the recipient is related to
     * @param dstAccount the id of the account of the recipient
     * @throws AccountNotFoundException if the destination account doesn't belong to the customer or to one
     *      of its recipient's
     */
    private void verifyRecipientId(Customer customer, Account dstAccount) throws AccountNotFoundException {

        List<Integer> recipientAccountIds = listRecipientsAccountIds(customer);

        if (!customer.getAccounts().contains(dstAccount) &&
                !recipientAccountIds.contains(dstAccount.getId())) {
            throw new AccountNotFoundException();
        }
    }

    /**
     * Do the transfer and persist it to the database
     * @param srcAccount to debit
     * @param dstAccount to credit
     * @param amount to credit
     */
    private void doTransfer(Account srcAccount, Account dstAccount, Double amount) {

        srcAccount.debit(amount);
        dstAccount.credit(amount);

        accountDao.saveOrUpdate(srcAccount);
        accountDao.saveOrUpdate(dstAccount);
    }

    /**
     * Set the transaction manager
     * @param transactionManager
     */
    @Autowired
    public void setTransactionManager(JpaTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * Set the account data access object
     * @param accountDao the accountDAO to set
     */
    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * Set the transaction service
     * @param transactionService to set
     */
    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Set the recipient service
     * @param recipientService to set
     */
    @Autowired
    public void setRecipientService(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    /**
     * Set the customer service
     * @param customerService to set
     */
    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
