package com.hurricane;

import java.io.IOException;
import java.util.Arrays;

import com.model.HurricaneApplication;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controls the sign-up screen.
 */
public class SignUpController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    // TODO add address fields (state, city, street) once Address exists; User's constructor needs one
    // TODO add a way to choose the account type, or decide that sign-up always makes a Victim

    @FXML
    private void signUp() throws IOException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        TextField[] fields = {firstNameField, lastNameField, usernameField, emailField,
                passwordField, confirmPasswordField};
        FormError.clear(errorLabel, fields);

        TextField[] emptyFields = Arrays.stream(fields)
                .filter(field -> field.getText().isBlank())
                .toArray(TextField[]::new);
        if (emptyFields.length > 0) {
            FormError.show(errorLabel, "Fill in every field.", emptyFields);
            return;
        }

        if (!password.equals(confirmPasswordField.getText())) {
            confirmPasswordField.clear();
            FormError.show(errorLabel, "Passwords do not match.", confirmPasswordField, passwordField);
            return;
        }

        if (HurricaneApplication.getInstance().createAccount(firstName, lastName, username, email, password)) {
            App.setRoot("home");
        } else {
            // TODO give a specific reason (e.g. username taken) once createAccount can report one
            FormError.show(errorLabel, "Could not create account. That username may already be taken.",
                    usernameField);
        }
    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login", App.Transition.SLIDE_RIGHT);
    }
}
