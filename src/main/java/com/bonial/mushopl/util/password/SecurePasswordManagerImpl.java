package com.bonial.mushopl.util.password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurePasswordManagerImpl implements PasswordManager {

    private static final String SALT = "zrIHcuDJ0kDHXrJrDjer7g"; // base64(md5("salt"))

    @Override
    public boolean verify(String input, String password) {
        return generate(input).equals(password);
    }

    @Override
    public String generate(String input) {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        digest.reset();

        final String s = input + SALT;
        final byte[] hash = digest.digest(s.getBytes());

        return Base64.getEncoder().encodeToString(hash);
    }

}
