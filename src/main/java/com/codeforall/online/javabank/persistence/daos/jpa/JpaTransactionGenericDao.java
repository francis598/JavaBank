package com.codeforall.online.javabank.persistence.daos.jpa;

import com.codeforall.online.javabank.model.transaction.Transaction;
import com.codeforall.online.javabank.persistence.daos.TransactionDao;
import org.springframework.stereotype.Repository;

/**
 * A JPA {@link TransactionDao} implementation
 */
@Repository
public class JpaTransactionGenericDao extends JpaGenericDao<Transaction> implements TransactionDao {

    /**
     * @see JpaGenericDao#JpaGenericDao(Class)
     */
    public JpaTransactionGenericDao() {
        super(Transaction.class);
    }
}
