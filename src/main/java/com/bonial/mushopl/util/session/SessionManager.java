package com.bonial.mushopl.util.session;

import com.bonial.mushopl.model.User;
import com.bonial.mushopl.model.UserSession;

import javax.servlet.http.Cookie;

public interface SessionManager {

    /**
     * Extracts session key from a cookie with a specific name
     * @param cookies cookies from request
     * @return session key
     */
    String extractSessionKey(Cookie[] cookies);

    /**
     * Obtains user for a session with given key
     * @param sessionKey session key
     * @return user or null if no session with given key exists
     */
    User obtainUser(String sessionKey);

    /**
     * Creates a new session for given user id
     * @param userId user id
     * @return newly created session
     */
    UserSession createSession(long userId);

    /**
     * Removes session with given key
     * @param key session key
     */
    void deleteSession(String key);

    /**
     * Creates a cookie for a given session to be sent to client
     * @param userSession user session
     * @return cookie to be sent
     */
    Cookie createCookie(UserSession userSession);

}
