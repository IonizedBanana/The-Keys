package com.controllers;

import java.io.IOException;

import com.hurricane.App;
import com.hurricane.Const;
import com.model.HurricaneApplication;
import com.model.location.Address;
import com.model.users.Admin;
import com.model.users.Dispatcher;
import com.model.users.User;
import com.model.users.Victim;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controls the Account Settings screen: the user's profile, their details and password.
 */
public class AccountController {

    /** Injected from the fx:include with fx:id="nav" in account.fxml. */
    @FXML
    private NavController navController;

    @FXML
    private Label avatarLabel;

    @FXML
    private Label profileName;

    @FXML
    private Label profileUsername;

    @FXML
    private Label typeTag;

    @FXML
    private Label profileEmail;

    @FXML
    private Label profileHome;

    @FXML
    private VBox profilePhoneBox;

    @FXML
    private Label profilePhone;

    @FXML
    private HBox phoneRow;

    @FXML
    private TextField phoneField;

    @FXML
    private VBox aboutSection;

    @FXML
    private TextField ageField;

    @FXML
    private ComboBox<String> sexBox;

    @FXML
    private TextArea notesArea;

    /** Sex choices, matched by position to the chars Victim stores (' ' means not given). */
    private static final String[] SEX_LABELS = { "Prefer not to say", "Male", "Female", "Other" };

    private static final char[] SEX_CODES = { ' ', 'm', 'f', 'o' };

    /** Oldest age accepted; ages are read back from the JSON as a byte. */
    private static final int MAX_AGE = 120;

    /** What Victim stores when there's no description. */
    private static final String NO_DESCRIPTION = "No description";

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
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label savedLabel;

    @FXML
    private void initialize() {
        navController.setActive(NavController.Page.ACCOUNT);
        // only reachable from the nav bar after login, but don't crash if nobody is logged in
        if (user() == null) {
            return;
        }
        boolean hasPhone = hasPhone(user());
        show(profilePhoneBox, hasPhone);
        show(phoneRow, hasPhone);
        show(aboutSection, user() instanceof Victim);
        sexBox.getItems().setAll(SEX_LABELS);
        showProfile();
        fillForm();
    }

    private static User user() {
        return HurricaneApplication.getInstance().getCurrentUser();
    }

    /** Fills the profile card on the left from the saved account. */
    private void showProfile() {
        User user = user();
        avatarLabel.setText(NavController.initials(user));
        profileName.setText(user.getFirstName() + " " + user.getLastName());
        profileUsername.setText("@" + user.getUsername());
        typeTag.setText(user.getType() == null ? "USER" : user.getType().ASCII.toUpperCase());
        profileEmail.setText(user.getEmailAddress());
        Address home = user.getAddress();
        profileHome.setText(home == null ? "Not set" : home.getCity() + ", " + home.getState() + " " + home.getZip());
        String phone = getPhone(user);
        profilePhone.setText(phone == null || phone.isBlank() ? "Not set" : phone);
    }

    /** Puts the saved account back into the form, dropping anything typed. */
    private void fillForm() {
        User user = user();
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmailAddress());
        String phone = getPhone(user);
        phoneField.setText(phone == null ? "" : phone);
        if (user instanceof Victim) {
            Victim victim = (Victim) user;
            ageField.setText(victim.getAge() < 0 ? "" : String.valueOf(victim.getAge()));
            sexBox.getSelectionModel().select(sexIndex(victim.getSex()));
            String notes = victim.getDescription();
            notesArea.setText(notes == null || notes.equals(NO_DESCRIPTION) ? "" : notes);
        }
        Address home = user.getAddress();
        streetField.setText(home == null ? "" : home.getStreet());
        cityField.setText(home == null ? "" : home.getCity());
        stateField.setText(home == null ? "" : home.getState());
        zipField.setText(home == null ? "" : home.getZip());
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
        clearErrors();
    }

    @FXML
    private void cancel() {
        fillForm();
        savedLabel.setText("");
    }

    @FXML
    private void save() {
        clearErrors();
        savedLabel.setText("");
        User user = user();

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String street = streetField.getText().trim();
        String city = cityField.getText().trim();
        String state = stateField.getText().trim();
        String zip = zipField.getText().trim();
        String current = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirm = confirmPasswordField.getText();

        if (Const.anyBlank(firstName, lastName, email, street, city, state, zip)) {
            errorLabel.setText("Fill in your name, email and full home address.");
            return;
        }
        if (!Const.isValidEmail(email)) {
            emailField.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Enter a valid email.");
            return;
        }

        // setPhoneNumber quietly ignores anything it doesn't accept (including blank), so check first
        String phone = phoneField.getText().trim();
        String oldPhone = getPhone(user);
        boolean changingPhone = hasPhone(user) && !phone.equals(oldPhone == null ? "" : oldPhone);
        if (changingPhone && phone.isEmpty()) {
            phoneField.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Your phone number can be changed but not removed.");
            return;
        }
        if (changingPhone && !Const.isValidPhone(phone)) {
            phoneField.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Enter a 10-digit phone number, like 843-555-0142 or 8435550142.");
            return;
        }

        // victims only: age, sex and notes
        Victim victim = (user instanceof Victim) ? (Victim) user : null;
        int age = -1;
        if (victim != null) {
            String ageText = ageField.getText().trim();
            if (ageText.isEmpty()) {
                // setAge ignores negative numbers, so an age can't be unset once given
                if (victim.getAge() >= 0) {
                    ageField.setStyle(Const.ERROR_STYLE);
                    errorLabel.setText("Your age can be changed but not removed.");
                    return;
                }
            } else {
                try {
                    age = Integer.parseInt(ageText);
                } catch (NumberFormatException e) {
                    age = -2;
                }
                if (age < 0 || age > MAX_AGE) {
                    ageField.setStyle(Const.ERROR_STYLE);
                    errorLabel.setText("Enter your age as a number from 0 to " + MAX_AGE + ".");
                    return;
                }
            }
        }
        String notes = notesArea.getText().trim();

        // the password only changes if they typed something in one of the password fields
        boolean changingPassword = !(current.isEmpty() && newPassword.isEmpty() && confirm.isEmpty());
        if (changingPassword) {
            if (!user.isMatch(user.getUsername(), current)) {
                currentPasswordField.setStyle(Const.ERROR_STYLE);
                errorLabel.setText("Your current password is incorrect.");
                return;
            }
            if (!Const.isPasswordLongEnough(newPassword)) {
                newPasswordField.setStyle(Const.ERROR_STYLE);
                errorLabel.setText("Passwords must be greater than " + Const.MIN_PASSWORD_LENGTH + " characters.");
                return;
            }
            if (!Const.passwordsMatch(newPassword, confirm)) {
                confirmPasswordField.setStyle(Const.ERROR_STYLE);
                errorLabel.setText("New passwords do not match.");
                return;
            }
        }

        // remember the old values so a failed save doesn't leave half-changed data in memory
        String oldFirst = user.getFirstName();
        String oldLast = user.getLastName();
        String oldEmail = user.getEmailAddress();
        Address oldAddress = user.getAddress();
        String oldPassword = user.getPassword();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailAddress(email);
        user.setAddress(new Address(street, city, state, zip));
        if (changingPassword) {
            user.setPassword(newPassword);
        }
        if (changingPhone) {
            setPhone(user, phone);
        }
        int oldAge = victim == null ? -1 : victim.getAge();
        char oldSex = victim == null ? ' ' : victim.getSex();
        String oldNotes = victim == null ? null : victim.getDescription();
        if (victim != null) {
            if (age >= 0) {
                victim.setAge(age);
            }
            int sex = sexBox.getSelectionModel().getSelectedIndex();
            victim.setSex(SEX_CODES[Math.max(0, sex)]);
            victim.setDescription(notes.isEmpty() ? NO_DESCRIPTION : notes);
        }

        if (!HurricaneApplication.getInstance().saveData()) {
            if (victim != null) {
                // note: an age can't be set back to unknown (-1), since setAge ignores negatives
                victim.setAge(oldAge);
                victim.setSex(oldSex);
                victim.setDescription(oldNotes);
            }
            user.setFirstName(oldFirst);
            user.setLastName(oldLast);
            user.setEmailAddress(oldEmail);
            user.setAddress(oldAddress);
            user.setPassword(oldPassword);
            if (changingPhone) {
                // note: a victim with no phone yet keeps the new one in memory, since the setter ignores ""
                setPhone(user, oldPhone);
            }
            errorLabel.setText("Your changes couldn't be saved. Try again.");
            return;
        }

        showProfile();
        navController.showUser();
        fillForm();
        savedLabel.setText(changingPassword ? "✓  Changes and new password saved." : "✓  Changes saved.");
    }

    @FXML
    private void logout() throws IOException {
        HurricaneApplication.getInstance().logout();
        App.setRoot("landing");
    }

    // phone numbers live on Victim, Admin and Dispatcher, not on User, so each type is checked here

    private static boolean hasPhone(User user) {
        return user instanceof Victim || user instanceof Admin || user instanceof Dispatcher;
    }

    private static String getPhone(User user) {
        if (user instanceof Victim) return ((Victim) user).getPhoneNumber();
        if (user instanceof Admin) return ((Admin) user).getPhoneNumber();
        if (user instanceof Dispatcher) return ((Dispatcher) user).getPhoneNumber();
        return null;
    }

    private static void setPhone(User user, String phone) {
        if (phone == null) return;
        if (user instanceof Victim) ((Victim) user).setPhoneNumber(phone);
        else if (user instanceof Admin) ((Admin) user).setPhoneNumber(phone);
        else if (user instanceof Dispatcher) ((Dispatcher) user).setPhoneNumber(phone);
    }

    /** Finds the choice for a stored sex char; old data has both 'm' and 'F'. */
    private static int sexIndex(char sex) {
        char lower = Character.toLowerCase(sex);
        for (int i = 0; i < SEX_CODES.length; i++) {
            if (SEX_CODES[i] == lower) {
                return i;
            }
        }
        return 0;
    }

    private static void show(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    private void clearErrors() {
        errorLabel.setText("");
        emailField.setStyle(Const.NORMAL_STYLE);
        phoneField.setStyle(Const.NORMAL_STYLE);
        ageField.setStyle(Const.NORMAL_STYLE);
        currentPasswordField.setStyle(Const.NORMAL_STYLE);
        newPasswordField.setStyle(Const.NORMAL_STYLE);
        confirmPasswordField.setStyle(Const.NORMAL_STYLE);
    }
}
