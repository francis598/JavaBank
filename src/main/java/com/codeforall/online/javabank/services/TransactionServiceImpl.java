package com.codeforall.online.javabank.services;

import com.codeforall.online.javabank.exceptions.AccountNotFoundException;
import com.codeforall.online.javabank.exceptions.TransactionInvalidException;
import com.codeforall.online.javabank.model.Recipient;
import com.codeforall.online.javabank.model.account.Account;
import com.codeforall.online.javabank.model.transaction.Transaction;
import com.codeforall.online.javabank.model.transaction.TransactionType;
import com.codeforall.online.javabank.persistence.daos.TransactionDao;
import com.codeforall.online.javabank.persistence.managers.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An {@link TransactionService} implementation
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    private AccountService accountService;
    private TransactionDao transactionDao;
    private TransactionManager transactionManager;

    /**
     * @see TransactionService#getAccountStatement(int)
     */
    @Override
    public List<Transaction> getAccountStatement(int accountId) throws AccountNotFoundException {
        Account account = accountService.get(accountId);

        return account.getTransactions().stream()
                .sorted(Comparator.comparingInt(Transaction::getId).reversed())
                .collect(Collectors.toList());
    }

    /**
     * @see TransactionService#registerSimpleTransaction(Account, double, TransactionType)
     */
    @Override
    public void registerSimpleTransaction(Account account, double amount, TransactionType transactionType) throws TransactionInvalidException {

        if(!transactionManager.isTransactionActive()) {
            throw new TransactionInvalidException();
        }

        transactionDao.saveOrUpdate(buildTransaction(account, amount, transactionType));
    }

    /**
     * @see TransactionService#registerTransfer(Account, double, Recipient)
     */
    public void registerTransfer(Account account, double amount, Recipient recipient) throws TransactionInvalidException {

        if(!transactionManager.isTransactionActive()) {
            throw new TransactionInvalidException();
        }

        Transaction transaction = buildTransaction(account, amount, TransactionType.TRANSFER);

        transaction.setRecipient(recipient);
        transactionDao.saveOrUpdate(transaction);
    }

    /**
     * Build a transaction to use for all operations
     * @param account the account in which the transaction was done
     * @param amount the amount used in the operation
     * @param transactionType the type of transaction
     * @return the transaction
     */
    private Transaction buildTransaction(Account account, double amount, TransactionType transactionType) {
        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);

        return transaction;
    }

    /**
     * Set the account service
     * @param accountService the account service to set
     */
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Set the transaction data access object
     * @param transactionDao the transactionDAO to set
     */
    @Autowired
    public void setTransactionDao(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    /**
     * Set the transaction manager
     * @param transactionManager to set
     */
    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
