package com.bonial.mushopl.dao;

import com.bonial.mushopl.model.UserSession;

/**
 * User sessions DAO class
 * Supported operations:
 * - get a session by key
 * - add or extend a session
 * - remove sessions by key or user id
 */
public interface UserSessionDao {

    /**
     * Gets a session by its key
     * @param key session key
     * @return session or null if session not found
     */
    UserSession get(String key);

    /**
     * Adds a session for given user id
     * @param userId session user id
     * @param duration session duration, milliseconds
     * @return newly created user session
     */
    UserSession add(long userId, long duration);

    /**
     * Removes all sessions for given user id
     * @param userId sessions user id
     */
    void remove(long userId);

    /**
     * Removes a session by given key
     * @param key session key
     */
    void remove(String key);

}
