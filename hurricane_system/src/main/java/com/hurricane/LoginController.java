package com.hurricane;

import java.io.IOException;

import com.model.HurricaneApplication;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controls the login screen.
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            errorLabel.setText("Enter your username and password.");
            return;
        }

        if (HurricaneApplication.getInstance().login(username, password)) {
            App.setRoot("home");
        } else {
            errorLabel.setText("Incorrect username or password.");
            passwordField.clear();
        }
    }

    @FXML
    private void switchToSignUp() throws IOException {
        App.setRoot("signup");
    }
}
