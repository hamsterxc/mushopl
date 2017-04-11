package com.bonial.mushopl.dao;

import com.bonial.mushopl.model.UserSession;
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
import java.util.concurrent.ThreadLocalRandom;

public class UserSessionDaoImpl implements UserSessionDao {

    private static final Logger logger = LoggerFactory.getLogger(UserSessionDaoImpl.class);

    private static final char[] SESSION_KEY_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final int SESSION_KEY_LENGTH = 32;

    private final SessionFactory sessionFactory;

    public UserSessionDaoImpl(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public UserSession get(final String key) {
        try(final Session session = sessionFactory.openSession()) {
            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<UserSession> criteria = criteriaBuilder.createQuery(UserSession.class);
            final Root<UserSession> criteriaRoot = criteria.from(UserSession.class);

            criteria
                    .where(criteriaBuilder.equal(criteriaRoot.get("key"), key))
                    .orderBy(criteriaBuilder.desc(criteriaRoot.get("expire")));

            final Query<UserSession> query = session.createQuery(criteria);
            final List<UserSession> userSessions = query.list();
            logger.debug("Fetched sessions with key {}: {}", key, userSessions);
            if(userSessions.size() > 0) {
                return userSessions.get(0);
            } else {
                return null;
            }
        }
    }

    @Override
    public UserSession add(final long userId, final long duration) {
        final UserSession userSession = new UserSession();
        userSession.setUserId(userId);
        userSession.setKey(generateRandomString(SESSION_KEY_CHARS, SESSION_KEY_LENGTH));
        userSession.setExpire(System.currentTimeMillis() + duration);

        logger.debug("Persisting {}", userSession);
        try(final Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.save(userSession);
            transaction.commit();
        }
        logger.debug("Persisted {}", userSession);

        return userSession;
    }

    @Override
    public void remove(long userId) {
        try(final Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();

            session
                    .createQuery("delete from UserSession where userId=:userId")
                    .setParameter("userId", userId)
                    .executeUpdate();

            transaction.commit();
        }
        logger.debug("Removed sessions for user id {}", userId);
    }

    @Override
    public void remove(String key) {
        try(final Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();

            session
                    .createQuery("delete from UserSession where key=:key")
                    .setParameter("key", key)
                    .executeUpdate();

            transaction.commit();
        }
        logger.debug("Removed session with key {}", key);
    }

    private String generateRandomString(final char[] chars, final int length) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();

        final StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < length; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }

        return stringBuilder.toString();
    }

}
