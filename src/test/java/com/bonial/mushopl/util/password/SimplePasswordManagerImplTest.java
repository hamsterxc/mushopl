package com.bonial.mushopl.util.password;

public class SimplePasswordManagerImplTest extends AbstractPasswordManagerTest {

    @Override
    protected PasswordManager obtainTestSubject() {
        return new SimplePasswordManagerImpl();
    }

}
