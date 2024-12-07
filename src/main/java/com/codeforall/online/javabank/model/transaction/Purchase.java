package com.codeforall.online.javabank.model.transaction;

import com.codeforall.online.javabank.model.AbstractModel;
import com.codeforall.online.javabank.model.Establishment;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * A class which represents a purchase
 */
@Entity
@Table(name = "purchases")
public class Purchase extends AbstractModel {

    @ManyToOne
    private Establishment establishment;

    @OneToMany(
            cascade = {CascadeType.ALL},
            orphanRemoval = true,
            mappedBy = "purchase",
            fetch = FetchType.EAGER
    )
    private Set<Transaction> transactions = new HashSet<>();


    /**
     * Add transaction
     * @param transaction the transaction to be added
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setPurchase(this);
    }

    /**
     * Remove transaction
     * @param transaction the transaction to be removed
     */
    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setPurchase(null);
    }

    /**
     * Get the establishment
     * @return the establishment
     */
    public Establishment getEstablishment() {
        return establishment;
    }

    /**
     * Get the set of transactions
     * @return the set of transactions
     */
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Set the establishment to the given establishment
     * @param establishment to set
     */
    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    /**
     * Set the set of transactions
     * @param transactions to set
     */
    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
}
