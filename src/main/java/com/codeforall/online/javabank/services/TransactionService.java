package com.codeforall.online.javabank.services;

import com.codeforall.online.javabank.exceptions.AccountNotFoundException;
import com.codeforall.online.javabank.exceptions.TransactionInvalidException;
import com.codeforall.online.javabank.model.Recipient;
import com.codeforall.online.javabank.model.account.Account;
import com.codeforall.online.javabank.model.transaction.Transaction;
import com.codeforall.online.javabank.model.transaction.TransactionType;

import java.util.List;

/**
 * Common interface for transaction services, provides methods to manage transactions
 */
public interface TransactionService {

    /**
     * Gets all the information about the transactions made into an account
     * @param accountId the id of the account related to the transaction
     * @return the list of transactions related to that account
     * @throws AccountNotFoundException when the associated account doesn't exist
     */
    List<Transaction> getAccountStatement(int accountId) throws AccountNotFoundException;

    /**
     * Register a transaction made to an account
     * @param account the account in which the transaction was done
     * @param amount the amount used in the operation
     * @param transactionType the type of transaction
     * @throws TransactionInvalidException if this operation is done without an opened transaction
     */
    void registerSimpleTransaction(Account account, double amount, TransactionType transactionType) throws TransactionInvalidException;

    /**
     * Register a transfer
     * @param account the account in which the transaction was done
     * @param amount the amount used in the operation
     * @param recipient the recipient
     * @throws TransactionInvalidException if this operation is done without an opened transaction
     */
    void registerTransfer(Account account, double amount, Recipient recipient) throws TransactionInvalidException;

}
