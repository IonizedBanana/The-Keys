package com.hurricane;

import java.io.IOException;

import com.model.HurricaneApplication;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import com.model.*;

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
    private TextField streetField;

    @FXML 
    private TextField cityField;

    @FXML
    private TextField stateField;

    @FXML
    private TextField zipField;

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
        String street = streetField.getText();
        String city = cityField.getText();
        String state = stateField.getText();
        String zip = zipField.getText();
        String password = passwordField.getText();

        com.model.location.Address address = new com.model.location.Address(street, city, state, zip);

        if (firstName.isBlank() || lastName.isBlank() || username.isBlank()
                || email.isBlank() || password.isBlank()) {
            errorLabel.setText("Fill in every field.");
            return;
        }

        if (!password.equals(confirmPasswordField.getText())) {
            errorLabel.setText("Passwords do not match.");
            confirmPasswordField.clear();
            return;
        }

        if (HurricaneApplication.getInstance().createAccount(firstName, lastName, username, email, password, address)) {
            App.setRoot("home");
        } else {
            // TODO give a specific reason (e.g. username taken) once createAccount can report one
            errorLabel.setText("Could not create account. That username may already be taken.");
        }
    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }
}
