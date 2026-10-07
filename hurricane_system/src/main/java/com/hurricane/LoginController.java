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

        FormError.clear(errorLabel, usernameField, passwordField);

        if (username.isBlank()) {
            FormError.show(errorLabel, "Enter your username.", usernameField);
            return;
        }
        if (password.isBlank()) {
            FormError.show(errorLabel, "Enter your password.", passwordField);
            return;
        }

        if (HurricaneApplication.getInstance().login(username, password)) {
            App.setRoot("home");
        } else {
            // Mark both fields so the message doesn't reveal which one was wrong
            passwordField.clear();
            FormError.show(errorLabel, "Incorrect username or password.", passwordField, usernameField);
        }
    }

    @FXML
    private void switchToSignUp() throws IOException {
        App.setRoot("signup", App.Transition.SLIDE_LEFT);
    }
}
