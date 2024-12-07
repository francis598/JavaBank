package com.codeforall.online.javabank.factories;

import com.codeforall.online.javabank.model.account.Account;
import com.codeforall.online.javabank.model.account.AccountType;
import com.codeforall.online.javabank.model.account.CheckingAccount;
import com.codeforall.online.javabank.model.account.SavingsAccount;
import org.springframework.stereotype.Component;

/**
 * A factory for creating accounts of different types
 */
@Component
public class AccountFactory {

    /**
     * Create a new {@link Account}
     * @param accountType the account type
     * @return the new account
     */
    public static Account createAccount(AccountType accountType) {

        Account newAccount;
        switch (accountType) {
            case CHECKING:
                newAccount = new CheckingAccount();
                break;
            case SAVINGS:
                newAccount = new SavingsAccount();
                break;
            default:
                newAccount = null;
        }
        return newAccount;
    }
}
