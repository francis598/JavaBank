package com.codeforall.online.javabank.model;

import com.codeforall.online.javabank.model.transaction.Purchase;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * A class which represents an establishment
 */
@Entity
@Table(name = "establishments")
public class Establishment extends AbstractModel {

    private String name;

    @OneToMany(
            cascade = {CascadeType.ALL},
            orphanRemoval = true,
            mappedBy = "establishment",
            fetch = FetchType.LAZY
    )
    private Set<Purchase> purchases = new HashSet<>();


    /**
     * Add purchase
     * @param purchase the purchase to be added
     */
    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
        purchase.setEstablishment(this);
    }

    /**
     * Remove purchase
     * @param purchase the purchase to be removed
     */
    public void removePurchase(Purchase purchase) {
        purchases.remove(purchase);
        purchase.setEstablishment(null);
    }

    /**
     * Get the establishment name
     * @return the establishment name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the purchase
     * @return the purchase
     */
    public Set<Purchase> getPurchases() {
        return purchases;
    }

    /**
     * Set the establishment name to the given name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the purchases to the given purchases list
     * @param purchases to give
     */
    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }
}
