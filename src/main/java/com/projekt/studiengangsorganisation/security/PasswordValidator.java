package com.projekt.studiengangsorganisation.security;

import java.util.regex.Pattern;

/**
 * Klasse zur Validierung von Passwörtern.
 * 
 * @author Finn Plassmeier
 */
public class PasswordValidator {
    public static boolean validate(String password) {
        // Mindestlänge 8 Zeichen
        if (password.length() < 8) {
            return false;
        }

        // Enthält mindestens einen Großbuchstaben
        if (!containsUppercase(password)) {
            return false;
        }

        // Enthält mindestens einen Kleinbuchstaben
        if (!containsLowercase(password)) {
            return false;
        }

        // Enthält mindestens eine Zahl
        if (!containsDigit(password)) {
            return false;
        }

        // Enthält mindestens ein Sonderzeichen
        if (!containsSpecialCharacter(password)) {
            return false;
        }

        return true;
    }

    private static boolean containsUppercase(String password) {
        return !password.equals(password.toLowerCase());
    }

    private static boolean containsLowercase(String password) {
        return !password.equals(password.toUpperCase());
    }

    private static boolean containsDigit(String password) {
        return password.matches(".*\\d.*");
    }

    private static boolean containsSpecialCharacter(String password) {
        Pattern specialCharPattern = Pattern.compile("[!@#$%&-_]");
        return specialCharPattern.matcher(password).find();
    }
}
