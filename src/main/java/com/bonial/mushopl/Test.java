package com.bonial.mushopl;

import com.bonial.mushopl.model.User;
import com.bonial.mushopl.servlet.ProductsServletView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class Test {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.reset();
        final String s = Base64.getEncoder().encodeToString(digest.digest("salt".getBytes()));
        System.out.println(s);
    }

    public static void main3(String[] args) throws JAXBException, TransformerException, IOException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(ProductsServletView.class);
        final Marshaller marshaller = jaxbContext.createMarshaller();

//        final LoginServletView loginServletView = new LoginServletView();
//        loginServletView.setError("error!");
//        loginServletView.setName("name!");

        final ProductsServletView view = new ProductsServletView();
        view.setName("Name!");
        view.setProducts(Arrays.asList("product-1", "product-2"));

        final Writer writer = new StringWriter();
        marshaller.marshal(view, writer);

        System.out.println(writer.toString());

//        final InputStream xslStream = Test.class.getClassLoader().getResourceAsStream("xsl/login.xsl");
//        final BufferedReader xslStreamReader = new BufferedReader(new InputStreamReader(xslStream));
//        String line;
//        while((line = xslStreamReader.readLine()) != null) {
//            System.out.println(line);
//        }

//        final Source sourceXsl = new StreamSource(Test.class.getClassLoader().getResourceAsStream("xsl/login.xsl"));
//        final Transformer transformer = TransformerFactory.newInstance().newTransformer(sourceXsl);
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
//
//        final Source sourceXml = new StreamSource(new StringReader(writer.toString()));
//        final Writer resultXmlWriter = new StringWriter();
//        final Result resultXml = new StreamResult(resultXmlWriter);
//        transformer.transform(sourceXml, resultXml);
//
//        System.out.println(resultXmlWriter.toString());
    }

//    private static String extractFirstPart(final String path) {
//        final int slashIndex = path.indexOf('/');
//        if(slashIndex == -1) {
//            return path;
//        } else {
//            final String afterSlash = path.substring(slashIndex + 1);
//            if(afterSlash.contains("/")) {
//                return afterSlash.substring(0, afterSlash.indexOf('/'));
//            } else {
//                return afterSlash;
//            }
//        }
//    }

    public static void main2(String[] args) {
        final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();

        final User user = new User();
        user.setName("test");
        user.setPassword("testpass");
        session.save(user);

        final User user2 = new User();
        user2.setName("test2");
        user2.setPassword("testpass2");
        session.save(user2);

        final User user3 = new User();
        user3.setName("test3");
        user3.setPassword("testpass");
        session.save(user3);

        transaction.commit();

//        System.out.println("--- saved: " + user);

//        final Query<User> query = session.createQuery("FROM User", User.class);
////        query.setParameter("name", "test");
//        final List<User> users = query.getResultList();
//        System.out.println(users.size());
//        System.out.println(users.get(0));

        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
        final Root<User> criteriaRoot = criteria.from(User.class);
//        criteria = criteria.where(criteriaBuilder.equal(criteriaRoot.get("password"), "testpass"));
        final Query<User> query = session.createQuery(criteria);
        final List<User> users = query.list();
        System.out.println(users.size());
        System.out.println(users);

//        final User userToRemove = new User();
//        user.setPassword("testpass");
//        final Transaction transaction2 = session.beginTransaction();
//        session.delete(userToRemove);
//        transaction2.commit();

        final Transaction transaction2 = session.beginTransaction();
        session
                .createQuery("delete from User where password=:password")
                .setParameter("password", "testpass")
                .executeUpdate();
        transaction2.commit();

        final CriteriaBuilder criteriaBuilder3 = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria3 = criteriaBuilder3.createQuery(User.class);
        final Root<User> criteriaRoot3 = criteria3.from(User.class);
//        criteria3 = criteria3.where(criteriaBuilder3.equal(criteriaRoot3.get("name"), "qwerty"));
        final Query<User> query3 = session.createQuery(criteria3);
        final List<User> users3 = query3.getResultList();
        System.out.println(users3.size());
        System.out.println(users3);
    }

}
