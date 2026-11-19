package com.hurricane;

import java.util.Set;

public final class Const {

    private Const() {} // prevents creating instances

    public static final String ERROR_STYLE = "-fx-border-color: red;";

    public static final String NORMAL_STYLE = "";

    public static final int MIN_PASSWORD_LENGTH = 4;

    public static boolean anyBlank(String... values) {
        for (String v : values) {
            if (v == null || v.isBlank()) return true;
        }
        return false;
    }

    public static boolean hasUppercase(String s) {
        return !s.equals(s.toLowerCase());
    }

    public static boolean isValidEmail(String email) {
        if (email.contains(" ")) return false;

        int at = email.indexOf('@');
        // exactly one @, and something before it
        if (at <= 0 || at != email.lastIndexOf('@')) return false;

        String domain = email.substring(at + 1);
        int dot = domain.lastIndexOf('.');
        // the domain needs a dot with text on both sides
        if (dot <= 0 || dot == domain.length() - 1) return false;

        return true;
    }

    public static boolean passwordsMatch(String password, String confirm) {
        return password.equals(confirm);
    }

    public static boolean isPasswordLongEnough(String password) {
        return password.length() > MIN_PASSWORD_LENGTH;
    }

    // same range the users' setPhoneNumber accepts: 1234567890 up to 123-456-7890
    public static final int MIN_PHONE_LENGTH = 10;

    public static final int MAX_PHONE_LENGTH = 12;

    public static boolean isValidPhone(String phone) {
        if (phone.length() < MIN_PHONE_LENGTH || phone.length() > MAX_PHONE_LENGTH) return false;

        int digits = 0;
        for (char c : phone.toCharArray()) {
            if (Character.isDigit(c)) digits++;
            else if (c != '-') return false;
        }
        return digits == 10;
    }

    // address checks only look at the format; whether the place exists is up to the map api

    // the 50 states plus DC
    private static final Set<String> STATES = Set.of(
        "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS",
        "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC",
        "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");

    // a house number, a space, then a street name with at least one letter, like "48 Tradd St"
    public static boolean isValidStreet(String street) {
        street = street.trim();
        int space = street.indexOf(' ');
        if (space <= 0) return false; // needs a number and a name

        // house number: digits, optionally one letter or a dash range, like 12, 12B, 10-12
        String number = street.substring(0, space);
        if (!Character.isDigit(number.charAt(0))) return false;
        for (char c : number.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '-') return false;
        }

        // street name: letters, digits, spaces and . ' # - with at least one letter
        String name = street.substring(space + 1).trim();
        boolean hasLetter = false;
        for (char c : name.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            else if (!Character.isDigit(c) && " .'#-".indexOf(c) < 0) return false;
        }
        return hasLetter;
    }

    // letters, spaces and . ' - starting with a letter, like "North Charleston" or "St. Helena"
    public static boolean isValidCity(String city) {
        city = city.trim();
        if (city.isEmpty() || !Character.isLetter(city.charAt(0))) return false;
        for (char c : city.toCharArray()) {
            if (!Character.isLetter(c) && " .'-".indexOf(c) < 0) return false;
        }
        return true;
    }

    // a two-letter state code, any case, like "SC" or "sc"
    public static boolean isValidState(String state) {
        return STATES.contains(state.trim().toUpperCase());
    }

    // 5 digits, or 5 digits, a dash and 4 more, like 29401 or 29401-1234
    public static boolean isValidZip(String zip) {
        zip = zip.trim();
        if (zip.length() != 5 && zip.length() != 10) return false;
        for (int i = 0; i < zip.length(); i++) {
            char c = zip.charAt(i);
            if (i == 5) {
                if (c != '-') return false;
            } else if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
