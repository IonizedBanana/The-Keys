package com.hurricane;

public class Const {

    private Const() {} // prevents creating instances

    public static final String ERROR_STYLE = "-fx-border-color: red;";

    public static final String NORMAL_STYLE = "";

    public static final int MIN_PASSWORD_LENGTH = 4;

    public static boolean isValidEmail(String email) {
    if (email.contains(" ")) return false;

    int at = email.indexOf('@');
    // exactly one @, and something before it
    if (at <= 0 || at != email.lastIndexOf('@')) return false;

    String domain = email.substring(at + 1);
    int dot = domain.indexOf('.');
    // the domain needs a dot with text on both sides
    if (dot <= 0 || dot == domain.length() - 1 || dot != domain.lastIndexOf('.')) return false;

    return true;
}

}
