package com.codeforall.online.javabank.persistence.daos.jpa;

import com.codeforall.online.javabank.model.Establishment;
import com.codeforall.online.javabank.persistence.daos.EstablishmentDao;
import org.springframework.stereotype.Repository;

/**
 * A JPA {@link EstablishmentDao} implementation
 */
@Repository
public class JpaEstablishmentGenericDao extends JpaGenericDao<Establishment> implements EstablishmentDao {

    /**
     * @see JpaGenericDao#JpaGenericDao(Class)
     */
    public JpaEstablishmentGenericDao() {
        super(Establishment.class);
    }
}
