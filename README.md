# Multi User Shopping List

This is a simple shopping list system that allows users to manage their shopping list. Each user has one personal shopping list where they can add entries to after signing up to the system.

### Running application

Execute `./gradlew clean build appStart`.  
Logs will go to stdout, the application will run on `http://localhost:8080/myshopl/`.

### Using the application

Start page is login page where one can either log in under an existing user or create a new one.  
If a valid session exists, one will be redirected to their products list. Session is stored in cookies.

Products list shows a list of user's products with a possibility to add new products.  
It is also possible to log out, being redirected back to the login page.  
If one logs out, their other sessions will not be destroyed.

It is not possible to view foreign user's shopping list, one will either be redirected to their own list or to the login page if no valid user session exists.

### Test data

Two test users will be added upon web-server start: `admin:admin` and `root:root`.

### Technical details

* The application uses an embedded web-server Gretty, build system is Gradle.
* Parts of the application are initialized using Spring beans.
* In-memory database HyperSQL is used with Hibernate mappings.
* HTML pages are constructed using JAXB and XSLT.
* Test frameworks JUnit 4 and EasyMock are used for unit-testing.
