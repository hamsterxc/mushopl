package com.bonial.mushopl.dao;

import com.bonial.mushopl.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private final SessionFactory sessionFactory;

    public UserDaoImpl(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User get(long id) {
        try(final Session session = sessionFactory.openSession()) {
            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
            final Root<User> criteriaRoot = criteria.from(User.class);

            criteria.where(criteriaBuilder.equal(criteriaRoot.get("id"), id));

            final Query<User> query = session.createQuery(criteria);
            final List<User> users = query.getResultList();
            logger.debug("Fetched user {}", id, users);
            if(users.size() > 0) {
                return users.get(0);
            } else {
                return null;
            }
        }
    }

    /**
     * @throws RuntimeException if more than one user with this name found
     */
    @Override
    public User get(final String name) {
        try(final Session session = sessionFactory.openSession()) {
            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
            final Root<User> criteriaRoot = criteria.from(User.class);

            criteria.where(criteriaBuilder.equal(criteriaRoot.get("name"), name));

            final Query<User> query = session.createQuery(criteria);
            final List<User> users = query.getResultList();
            logger.debug("Fetched users for name {}: {}", name, users);
            switch(users.size()) {
                case 0:
                    return null;

                case 1:
                    return users.get(0);

                default:
                    throw new RuntimeException("More than one user found, name = " + name);
            }
        }
    }

    @Override
    public void persist(final User user) {
        try(final Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(user);
            transaction.commit();
        }
        logger.debug("Persisted {}", user);
    }

}
