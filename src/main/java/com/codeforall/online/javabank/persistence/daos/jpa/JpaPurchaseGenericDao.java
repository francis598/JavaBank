package com.codeforall.online.javabank.persistence.daos.jpa;

import com.codeforall.online.javabank.model.transaction.Purchase;
import com.codeforall.online.javabank.persistence.daos.PurchaseDao;
import org.springframework.stereotype.Repository;

/**
 * A JPA {@link PurchaseDao} implementation
 */
@Repository
public class JpaPurchaseGenericDao extends JpaGenericDao<Purchase> implements PurchaseDao {

    /**
     * @see JpaGenericDao#JpaGenericDao(Class)
     */
    public JpaPurchaseGenericDao() {
        super(Purchase.class);
    }
}
