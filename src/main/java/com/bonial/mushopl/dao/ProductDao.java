package com.bonial.mushopl.dao;

import com.bonial.mushopl.model.Product;

import java.util.List;

/**
 * Products DAO interface
 * Supported operations:
 * - get all products by user id
 * - add or update a product
 */
public interface ProductDao {

    /**
     * Gets all products possessed by a user with given id
     * @param userId user id to get products for
     * @return ordered collection of user's products
     */
    List<Product> getAll(long userId);

    /**
     * Adds or updates a product
     * @param product product to persist
     */
    void persist(Product product);

}
