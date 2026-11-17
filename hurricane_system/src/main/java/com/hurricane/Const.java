package com.hurricane;

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
}