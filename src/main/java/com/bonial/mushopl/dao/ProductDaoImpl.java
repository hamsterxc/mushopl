package com.bonial.mushopl.dao;

import com.bonial.mushopl.model.Product;
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

public class ProductDaoImpl implements ProductDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    private final SessionFactory sessionFactory;

    public ProductDaoImpl(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Product> getAll(final long userId) {
        try(final Session session = sessionFactory.openSession()) {
            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<Product> criteria = criteriaBuilder.createQuery(Product.class);
            final Root<Product> criteriaRoot = criteria.from(Product.class);

            criteria
                    .where(criteriaBuilder.equal(criteriaRoot.get("userId"), userId))
                    .orderBy(criteriaBuilder.asc(criteriaRoot.get("id")));

            final Query<Product> query = session.createQuery(criteria);
            final List<Product> products = query.list();
            logger.debug("Fetched {} products for user id {}", products.size(), userId);
            return products;
        }
    }

    @Override
    public void persist(final Product product) {
        try(final Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(product);
            transaction.commit();
        }
        logger.debug("Persisted {}", product);
    }

}
