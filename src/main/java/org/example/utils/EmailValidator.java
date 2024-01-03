package org.example.utils;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final String emailRegexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public static boolean isValidEmailAddress(String emailAddress) {
        return Pattern.compile(emailRegexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
