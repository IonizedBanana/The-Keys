package com.controllers;

import com.hurricane.Const;
import java.io.IOException;

import com.hurricane.App;
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

        if (Const.anyBlank(firstName, lastName, username, email, password)) {
            errorLabel.setText("Fill in every field.");
            return;
        }

        if (Const.hasUppercase(username)) {
            usernameField.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Username cannot contain capital letters.");
            return;
        }

        if (!Const.isValidStreet(street)) {
            streetField.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Enter a valid street (e.g. 123 House St).");
            return;
        }

        if (!Const.isValidCity(city)) {
            cityField.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Enter a valid city");
            return;
        }

        if (!Const.isValidState(state)) {
            stateField.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Enter a valid state abbreviation.");
            return;
        }

        if (!Const.isValidZip(zip)) {
            zipField.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Enter a valid ZIP code.");
            return;
        }

        if (!Const.isValidEmail(email)) {
            emailField.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Enter a valid email.");
            return;
        }

        if (!Const.passwordsMatch(password, confirmPasswordField.getText())) {
            errorLabel.setText("Passwords do not match.");
            confirmPasswordField.clear();
            return;
        }

        if (!Const.isPasswordLongEnough(password)) {
            errorLabel.setText("Passwords must be greater than " + Const.MIN_PASSWORD_LENGTH + " characters.");
            passwordField.setStyle(Const.ERROR_STYLE);
            confirmPasswordField.setStyle(Const.ERROR_STYLE);
            return;
        }

        if (HurricaneApplication.getInstance().createAccount(firstName, lastName, username, email, password, address)) {
            App.setRoot("home");
        } else {
            errorLabel.setText("Could not create account. That username may already be taken.");
        }
    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }
}
