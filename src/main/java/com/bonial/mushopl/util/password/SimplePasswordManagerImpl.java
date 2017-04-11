package com.bonial.mushopl.util.password;

import java.util.Objects;

public class SimplePasswordManagerImpl implements PasswordManager {

    @Override
    public boolean verify(String input, String password) {
        return Objects.equals(input, password);
    }

    @Override
    public String generate(String input) {
        return input == null ? "" : input;
    }

}
