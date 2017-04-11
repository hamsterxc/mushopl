package com.bonial.mushopl.util.password;

public interface PasswordManager {

    /**
     * Verifies if user input matches this password
     * @param input user input
     * @param password password
     * @return true, if input and password match, false otherwise
     */
    boolean verify(String input, String password);

    /**
     * Generates password based on user input
     * @param input user input
     * @return generated password
     */
    String generate(String input);

}
