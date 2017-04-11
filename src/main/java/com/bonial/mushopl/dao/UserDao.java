package com.bonial.mushopl.dao;

import com.bonial.mushopl.model.User;

/**
 * Users DAO class
 * Supported operations:
 * - get user by name or id
 * - add new user or update existing one
 */
public interface UserDao {

    /**
     * Returns a user by their id or null if not found
     * @param id user id
     * @return user or null if user not found
     */
    User get(long id);

    /**
     * Returns a user by their name or null if not found
     * @param name user name
     * @return user or null if user not found
     */
    User get(String name);

    /**
     * Adds or updates a user
     * @param user user to persist
     */
    void persist(User user);

}
