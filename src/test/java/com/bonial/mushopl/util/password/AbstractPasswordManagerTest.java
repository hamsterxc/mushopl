package com.bonial.mushopl.util.password;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

abstract class AbstractPasswordManagerTest {

    private static final String PASSWORD = "password";
    private static final String PASSWORD_ANOTHER = "password1";

    private PasswordManager testSubject;

    @Before
    public void beforeTest() {
        testSubject = obtainTestSubject();
    }

    protected abstract PasswordManager obtainTestSubject();

    @Test
    public void testVerifyCorrect() {
        final String generated = testSubject.generate(PASSWORD);
        Assert.assertTrue(testSubject.verify(PASSWORD, generated));
    }

    @Test
    public void testVerifyIncorrect() {
        final String generated = testSubject.generate(PASSWORD);
        Assert.assertFalse(testSubject.verify(PASSWORD_ANOTHER, generated));
    }

    @Test
    public void testGenerationDifferent() {
        final String generated = testSubject.generate(PASSWORD);
        final String generatedAnother = testSubject.generate(PASSWORD_ANOTHER);
        Assert.assertNotEquals(generated, generatedAnother);
    }

    @Test
    public void testGenerationStable() {
        final String generated = testSubject.generate(PASSWORD);
        final String generatedSecond = testSubject.generate(PASSWORD);
        Assert.assertEquals(generated, generatedSecond);
    }

}
