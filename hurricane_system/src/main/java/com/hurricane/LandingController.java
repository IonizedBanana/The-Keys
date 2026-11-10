package com.hurricane;

import java.io.IOException;


import javafx.fxml.FXML;

/**
 * Controls the first screen, which sends the user to log in or sign up.
 */
public class LandingController {

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void switchToSignUp() throws IOException {
        App.setRoot("signup");
    }
}
