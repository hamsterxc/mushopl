package com.bonial.mushopl.util.session;

import com.bonial.mushopl.dao.UserDao;
import com.bonial.mushopl.dao.UserSessionDao;
import com.bonial.mushopl.model.User;
import com.bonial.mushopl.model.UserSession;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;

public class SessionManagerImplTest {

    private static final String SESSION_KEY = "session_key";
    private static final long USER_ID = 777L;
    private static final String USER_NAME = "user_name";

    private UserDao userDao;
    private UserSessionDao userSessionDao;
    private SessionManager sessionManager;

    @Before
    public void beforeTest() {
        userDao = EasyMock.createStrictMock(UserDao.class);
        userSessionDao = EasyMock.createStrictMock(UserSessionDao.class);
        sessionManager = new SessionManagerImpl(userDao, userSessionDao);
    }

    @Test
    public void testExtractSessionKey() {
        final Cookie[] cookies = new Cookie[]{
                new Cookie("name", "name!"),
                new Cookie("title", "title!"),
                new Cookie("sid", SESSION_KEY),
                new Cookie("value", "value!")
        };

        EasyMock.replay(userSessionDao, userDao);

        Assert.assertEquals(SESSION_KEY, sessionManager.extractSessionKey(cookies));
        EasyMock.verify(userSessionDao, userDao);
    }

    @Test
    public void testObtainUserNullSession() {
        EasyMock.replay(userSessionDao, userDao);

        Assert.assertNull(sessionManager.obtainUser(null));
        EasyMock.verify(userSessionDao, userDao);
    }

    @Test
    public void testObtainUserNullUser() {
        final UserSession userSession = new UserSession();
        userSession.setKey(SESSION_KEY);
        userSession.setUserId(USER_ID);
        EasyMock.expect(userSessionDao.get(EasyMock.eq(SESSION_KEY))).andReturn(userSession);

        EasyMock.expect(userDao.get(EasyMock.eq(USER_ID))).andReturn(null);

        EasyMock.replay(userSessionDao, userDao);

        Assert.assertNull(sessionManager.obtainUser(SESSION_KEY));
        EasyMock.verify(userSessionDao, userDao);
    }

    @Test
    public void testObtainUser() {
        final UserSession userSession = new UserSession();
        userSession.setUserId(USER_ID);
        userSession.setKey(SESSION_KEY);
        EasyMock.expect(userSessionDao.get(EasyMock.eq(SESSION_KEY))).andReturn(userSession);

        final User user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        EasyMock.expect(userDao.get(EasyMock.eq(USER_ID))).andReturn(user);

        EasyMock.replay(userSessionDao, userDao);

        final User obtained = sessionManager.obtainUser(SESSION_KEY);
        Assert.assertEquals(USER_ID, obtained.getId());
        Assert.assertEquals(USER_NAME, obtained.getName());

        EasyMock.verify(userSessionDao, userDao);
    }

    @Test
    public void testCreateSession() {
        EasyMock.expect(userSessionDao.add(EasyMock.eq(USER_ID), EasyMock.anyLong())).andReturn(null);

        EasyMock.replay(userSessionDao, userDao);

        sessionManager.createSession(USER_ID);

        EasyMock.verify(userSessionDao, userDao);
    }

    @Test
    public void testDeleteSession() {
        userSessionDao.remove(EasyMock.eq(SESSION_KEY));
        EasyMock.expectLastCall();

        EasyMock.replay(userSessionDao, userDao);

        sessionManager.deleteSession(SESSION_KEY);

        EasyMock.verify(userSessionDao, userDao);
    }

    @Test
    public void testCreateCookie() {
        EasyMock.replay(userSessionDao, userDao);

        final UserSession userSession = new UserSession();
        userSession.setUserId(USER_ID);
        userSession.setKey(SESSION_KEY);
        final Cookie cookie = sessionManager.createCookie(userSession);

        Assert.assertEquals("sid", cookie.getName());
        Assert.assertEquals(SESSION_KEY, cookie.getValue());

        EasyMock.verify(userSessionDao, userDao);
    }

}
