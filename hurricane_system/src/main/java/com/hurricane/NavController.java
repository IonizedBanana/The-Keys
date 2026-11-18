package com.hurricane;

import java.io.IOException;

import com.model.Hurricane;
import com.model.HurricaneApplication;
import com.model.HurricaneList;
import com.model.HurricaneStatus;
import com.model.users.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controls the top navigation bar shown on every screen after login.
 */
public class NavController {

    /** The screens the nav bar can point at. */
    public enum Page { HOME, SHELTERS, REQUESTS, NEW_REQUEST }

    @FXML
    private Button homeLink;

    @FXML
    private Button sheltersLink;

    @FXML
    private Button requestsLink;

    @FXML
    private Button newRequestButton;

    @FXML
    private Label stormTag;

    @FXML
    private Label initialsLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private void initialize() {
        User user = HurricaneApplication.getInstance().getCurrentUser();
        if (user != null) {
            initialsLabel.setText(initial(user.getFirstName()) + initial(user.getLastName()));
            nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        }

        Hurricane storm = activeStorm();
        if (storm == null) {
            stormTag.setVisible(false);
            stormTag.setManaged(false);
        } else {
            stormTag.setText(("Hurricane " + storm.getName() + " · Cat " + storm.getCategory()).toUpperCase());
        }
    }

    /**
     * Marks the screen the user is on. Screens that include nav.fxml with
     * fx:id="nav" get this controller injected as navController and call this
     * from their own initialize().
     *
     * @param page the current screen
     */
    public void setActive(Page page) {
        setActive(homeLink, page == Page.HOME);
        setActive(sheltersLink, page == Page.SHELTERS);
        setActive(requestsLink, page == Page.REQUESTS);

        // no point offering "New Request" while already making one
        boolean onNewRequest = page == Page.NEW_REQUEST;
        newRequestButton.setVisible(!onNewRequest);
        newRequestButton.setManaged(!onNewRequest);
    }

    private static void setActive(Button link, boolean active) {
        link.getStyleClass().remove("active");
        if (active) {
            link.getStyleClass().add("active");
        }
    }

    @FXML
    private void goHome() throws IOException {
        App.setRoot("home");
    }

    @FXML
    private void logout() throws IOException {
        HurricaneApplication.getInstance().logout();
        App.setRoot("landing");
    }

    /**
     * Finds a storm that isn't over yet.
     *
     * @return the storm, or null if there isn't one
     */
    static Hurricane activeStorm() {
        for (Hurricane hurricane : HurricaneList.getInstance().getHurricaneList().values()) {
            if (hurricane.getStatus() != HurricaneStatus.OVER) {
                return hurricane;
            }
        }
        return null;
    }

    private static String initial(String name) {
        return name == null || name.isEmpty() ? "" : name.substring(0, 1).toUpperCase();
    }
}
