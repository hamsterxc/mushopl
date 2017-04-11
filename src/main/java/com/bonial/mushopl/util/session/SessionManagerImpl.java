package com.bonial.mushopl.util.session;

import com.bonial.mushopl.dao.UserDao;
import com.bonial.mushopl.dao.UserSessionDao;
import com.bonial.mushopl.model.User;
import com.bonial.mushopl.model.UserSession;

import javax.servlet.http.Cookie;
import java.util.concurrent.TimeUnit;

public class SessionManagerImpl implements SessionManager {

    private static final String COOKIE_SESSION = "sid";
    private static final long SESSION_LENGTH_MILLIS = TimeUnit.HOURS.toMillis(1);

    private final UserDao userDao;
    private final UserSessionDao userSessionDao;

    public SessionManagerImpl(final UserDao userDao, final UserSessionDao userSessionDao) {
        this.userDao = userDao;
        this.userSessionDao = userSessionDao;
    }

    @Override
    public String extractSessionKey(Cookie[] cookies) {
        if(cookies != null) {
            for(final Cookie cookie : cookies) {
                if(COOKIE_SESSION.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public User obtainUser(String sessionKey) {
        if(sessionKey != null) {
            final UserSession userSession = userSessionDao.get(sessionKey);
            if(userSession != null) {
                return userDao.get(userSession.getUserId());
            }
        }
        return null;
    }

    @Override
    public UserSession createSession(long userId) {
        return userSessionDao.add(userId, SESSION_LENGTH_MILLIS);
    }

    @Override
    public void deleteSession(String key) {
        userSessionDao.remove(key);
    }

    @Override
    public Cookie createCookie(UserSession userSession) {
        final Cookie cookie = new Cookie(COOKIE_SESSION, userSession.getKey());
        cookie.setPath("/");
        cookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(SESSION_LENGTH_MILLIS));
        return cookie;
    }

}
