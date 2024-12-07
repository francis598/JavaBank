package com.codeforall.online.javabank.model.account;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * A checking account with no restrictions
 * @see Account
 * @see AccountType#CHECKING
 */
@Entity
@DiscriminatorValue("CHECKING")
public class CheckingAccount extends Account {

    /**
     * @see Account#getAccountType()
     */
    @Override
    public AccountType getAccountType() {
        return AccountType.CHECKING;
    }
}
