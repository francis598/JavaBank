package com.codeforall.online.javabank.persistence.daos.jpa;

import com.codeforall.online.javabank.model.Recipient;
import com.codeforall.online.javabank.persistence.daos.RecipientDao;
import org.springframework.stereotype.Repository;

/**
 * A JPA {@link RecipientDao} implementation
 */
@Repository
public class JpaRecipientGenericDao extends JpaGenericDao<Recipient> implements RecipientDao {

    /**
     * @see JpaGenericDao#JpaGenericDao(Class)
     */
    public JpaRecipientGenericDao() {
        super(Recipient.class);
    }
}
