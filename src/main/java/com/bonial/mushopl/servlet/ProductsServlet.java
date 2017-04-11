package com.bonial.mushopl.servlet;

import com.bonial.mushopl.SpringApplicationContextListener;
import com.bonial.mushopl.dao.ProductDao;
import com.bonial.mushopl.model.Product;
import com.bonial.mushopl.model.User;
import com.bonial.mushopl.util.HttpUtils;
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
import java.util.stream.Collectors;

public class ProductsServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProductsServlet.class);

    private static final String POST_PARAM_ADD = "add";
    private static final String POST_PARAM_NAME = "name";

    private ProductDao productDao;
    private SessionManager sessionManager;
    private XmlManager xmlManager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.info("Initializing servlet");

        super.init(config);

        final ApplicationContext applicationContext = (ApplicationContext) config.getServletContext()
                .getAttribute(SpringApplicationContextListener.ATTRIBUTE_APPLICATION_CONTEXT);
        productDao = applicationContext.getBean("productDao", ProductDao.class);
        sessionManager = applicationContext.getBean("sessionManager", SessionManager.class);
        xmlManager = applicationContext.getBean("xmlManager", XmlManager.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("GET {}", req.getPathInfo());

        final User user = verifyUser(req, resp);
        if(user != null) {
            logger.debug("Showing products, user {}", user.getName());

            output(resp, req.getContextPath(), user);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("POST {}", req.getPathInfo());

        final User user = verifyUser(req, resp);
        if(user != null) {
            if(req.getParameter(POST_PARAM_ADD) != null) {
                String productName = req.getParameter(POST_PARAM_NAME);
                if(productName == null) {
                    productName = "";
                }

                final Product product = new Product();
                product.setUserId(user.getId());
                product.setName(productName);
                productDao.persist(product);

                logger.debug("Added product {} for user {}", productName, user.getName());
            }

            output(resp, req.getContextPath(), user);
        }
    }

    private User verifyUser(final HttpServletRequest request, final HttpServletResponse response) {
        final String path = request.getPathInfo();
        final String name = HttpUtils.extractPathFirstPart(path);

        final String sessionKey = sessionManager.extractSessionKey(request.getCookies());
        final User user = sessionManager.obtainUser(sessionKey);

        if(user == null) {
            logger.debug("Foreign products list requested (requested {}), redirecting to login", name);
            HttpUtils.redirect(response, request.getContextPath() + "/");
            return null;
        }

        final String userName = user.getName();
        if(!userName.equals(name)) {
            logger.debug("Foreign products list requested (user {}, requested {}), redirecting to own products list", userName, name);
            HttpUtils.redirect(response, request.getContextPath() + "/" + userName + "/");
            return null;
        }

        return user;
    }

    private void output(final HttpServletResponse response, final String urlBase, final User user) throws IOException {
        final ProductsServletView view = new ProductsServletView();
        view.setUrlBase(urlBase);
        view.setName(user.getName());
        view.setProducts(productDao.getAll(user.getId())
                .stream()
                .map(Product::getName)
                .collect(Collectors.toList())
        );
        response.getWriter().println(xmlManager.transform(ProductsServletView.class, view, "xsl/products.xsl"));
    }

}
