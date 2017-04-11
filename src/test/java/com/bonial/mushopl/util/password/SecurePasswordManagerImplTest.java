package com.bonial.mushopl.util.password;

public class SecurePasswordManagerImplTest extends AbstractPasswordManagerTest {

    @Override
    protected PasswordManager obtainTestSubject() {
        return new SecurePasswordManagerImpl();
    }

}
