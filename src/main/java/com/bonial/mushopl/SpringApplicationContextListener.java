package com.bonial.mushopl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Servlet context listener creating Spring application context
 */
public class SpringApplicationContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(SpringApplicationContextListener.class);

    public static final String ATTRIBUTE_APPLICATION_CONTEXT = "applicationContext";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Servlet context initializing");

        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        sce.getServletContext().setAttribute(ATTRIBUTE_APPLICATION_CONTEXT, applicationContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
