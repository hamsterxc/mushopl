package com.bonial.mushopl.servlet;

import com.bonial.mushopl.SpringApplicationContextListener;
import com.bonial.mushopl.dao.UserDao;
import com.bonial.mushopl.model.User;
import com.bonial.mushopl.model.UserSession;
import com.bonial.mushopl.util.HttpUtils;
import com.bonial.mushopl.util.password.PasswordManager;
import com.bonial.mushopl.util.session.SessionManager;
import com.bonial.mushopl.util.xml.XmlManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    private static final String GET_PARAM_LOGOUT = "logout";
    private static final String POST_PARAM_NAME = "name";
    private static final String POST_PARAM_PASSWORD = "password";
    private static final String POST_PARAM_LOGIN = "login";
    private static final String POST_PARAM_SIGNUP = "signup";

    private UserDao userDao;
    private PasswordManager passwordManager;
    private SessionManager sessionManager;
    private XmlManager xmlManager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.info("Initializing servlet");

        super.init(config);

        final ApplicationContext applicationContext = (ApplicationContext) config.getServletContext()
                .getAttribute(SpringApplicationContextListener.ATTRIBUTE_APPLICATION_CONTEXT);
        userDao = applicationContext.getBean("userDao", UserDao.class);
        passwordManager = applicationContext.getBean("passwordManager", PasswordManager.class);
        sessionManager = applicationContext.getBean("sessionManager", SessionManager.class);
        xmlManager = applicationContext.getBean("xmlManager", XmlManager.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("GET {}", req.getPathInfo());

        final String sessionKey = sessionManager.extractSessionKey(req.getCookies());
        final User user = sessionManager.obtainUser(sessionKey);
        if(user != null) {
            if(req.getParameter(GET_PARAM_LOGOUT) != null) {
                sessionManager.deleteSession(sessionKey);
                logger.debug("Removed session {} for user {}, redirecting to login page", sessionKey, user.getName());
                HttpUtils.redirect(resp, req.getContextPath() + "/");
            } else {
                logger.debug("Found session for user {}, redirecting to products list", user.getName());
                redirectToProducts(resp, req.getContextPath(), user);
            }
            return;
        }

        logger.debug("Showing login page");
        output(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("POST {}", req.getPathInfo());

        final String name = req.getParameter(POST_PARAM_NAME);
        final String password = req.getParameter(POST_PARAM_PASSWORD);
        if(req.getParameter(POST_PARAM_LOGIN) != null) {
            final User user = userDao.get(name);
            if((user != null) && passwordManager.verify(password, user.getPassword())) {
                startSession(resp, user);
                logger.debug("Login successful, started session for user {}, redirecting to products list", user.getName());
                redirectToProducts(resp, req.getContextPath(), user);
            } else if((name == null) || (name.length() == 0)) {
                logger.debug("User name empty");
                output(resp, "User name is empty", name);
            } else {
                logger.debug("Password incorrect");
                output(resp, "Incorrect password", name);
            }
        } else if(req.getParameter(POST_PARAM_SIGNUP) != null) {
            User user = userDao.get(name);
            if(user != null) {
                logger.debug("Name taken");
                output(resp, "Name already taken", name);
            } else {
                user = new User();
                user.setName(name);
                user.setPassword(passwordManager.generate(password));
                userDao.persist(user);

                startSession(resp, user);

                logger.debug("User created, started session for user {}, redirecting to products list", user.getName());
                redirectToProducts(resp, req.getContextPath(), user);
            }
        } else {
            logger.debug("Showing login page");
            output(resp);
        }
    }

    private void redirectToProducts(final HttpServletResponse response, final String urlBase, final User user) {
        HttpUtils.redirect(response, urlBase + "/" + user.getName() + "/");
    }

    private void output(final HttpServletResponse response) throws IOException {
        output(response, new LoginServletView());
    }

    private void output(final HttpServletResponse response, final String error, final String name) throws IOException {
        final LoginServletView view = new LoginServletView();
        view.setError(error);
        view.setName(name);
        output(response, view);
    }

    private void output(final HttpServletResponse response, final LoginServletView view) throws IOException {
        response.getWriter().println(xmlManager.transform(LoginServletView.class, view, "xsl/login.xsl"));
    }

    private void startSession(final HttpServletResponse response, final User user) {
        final UserSession userSession = sessionManager.createSession(user.getId());
        response.addCookie(sessionManager.createCookie(userSession));
    }

}
