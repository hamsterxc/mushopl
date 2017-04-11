package com.bonial.mushopl;

import com.bonial.mushopl.dao.ProductDao;
import com.bonial.mushopl.dao.UserDao;
import com.bonial.mushopl.model.Product;
import com.bonial.mushopl.model.User;
import com.bonial.mushopl.util.password.PasswordManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test data populator to run at web-server startup
 * Populates two users (admin:admin, root:root) and several items for both
 */
public class TestDataPopulator {

    private static final Logger logger = LoggerFactory.getLogger(TestDataPopulator.class);

    private final UserDao userDao;
    private final ProductDao productDao;
    private final PasswordManager passwordManager;

    public TestDataPopulator(final UserDao userDao, final ProductDao productDao, final PasswordManager passwordManager) {
        this.userDao = userDao;
        this.productDao = productDao;
        this.passwordManager = passwordManager;
    }

    public void populate() {
        logger.info("Populating test data");

        long id;

        id = populateUser("admin", "admin");
        populateProduct(id, "Admin tool");
        populateProduct(id, "Another admin tool");
        populateProduct(id, "Admin food to eat");

        id = populateUser("root", "root");
        populateProduct(id, "Branch");
        populateProduct(id, "Leaf");
        populateProduct(id, "Trunk");
        populateProduct(id, "Twig");
    }

    private long populateUser(final String name, final String password) {
        final User user = new User();
        user.setName(name);
        user.setPassword(passwordManager.generate(password));
        userDao.persist(user);
        return user.getId();
    }

    private void populateProduct(final long userId, final String name) {
        final Product product = new Product();
        product.setUserId(userId);
        product.setName(name);
        productDao.persist(product);
    }

}
