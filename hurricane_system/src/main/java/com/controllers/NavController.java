package com.controllers;

import com.hurricane.App;
import com.hurricane.MapController;
import java.io.IOException;
import java.util.ArrayList;

import com.model.Hurricane;
import com.model.HurricaneApplication;
import com.model.HurricaneList;
import com.model.HurricaneStatus;
import com.model.Shelter;
import com.model.location.Address;
import com.model.location.MapTools;
import com.model.users.User;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controls the top navigation bar shown on every screen after login.
 */
public class NavController {

    /** The screens the nav bar can point at. */
    public enum Page { HOME, SHELTERS, REQUESTS, NEW_REQUEST, ACCOUNT }

    @FXML
    private Button homeLink;

    @FXML
    private Button sheltersLink;

    @FXML
    private Button requestsLink;

    @FXML
    private Button newRequestButton;

    @FXML
    private Button accountLink;

    @FXML
    private Label stormTag;

    @FXML
    private Label initialsLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private void initialize() {
        showUser();

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
        setActive(accountLink, page == Page.ACCOUNT);

        // no point offering "New Request" while already making one
        boolean onNewRequest = page == Page.NEW_REQUEST;
        newRequestButton.setVisible(!onNewRequest);
        newRequestButton.setManaged(!onNewRequest);
    }

    /**
     * Shows the logged in user's initials and name. Called again after the
     * user edits their name in Account Settings.
     */
    public void showUser() {
        User user = HurricaneApplication.getInstance().getCurrentUser();
        if (user != null) {
            initialsLabel.setText(initials(user));
            nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        }
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
    private void goRequests() throws IOException {
        App.setRoot("myrequests");
    }

    /**
     * Opens the shelter map and routes the user from home to the closest shelter.
     * Loads map.fxml here (not through App.setRoot) so we can reach its MapController.
     */
    @FXML
    private void goShelters() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("map.fxml"));
        Parent root = loader.load();
        MapController map = loader.getController();
        // map.fxml includes nav.fxml with fx:id="nav", so its controller is in the namespace
        NavController nav = (NavController) loader.getNamespace().get("navController");
        if (nav != null) {
            nav.setActive(Page.SHELTERS);
        }
        homeLink.getScene().setRoot(root);

        User user = HurricaneApplication.getInstance().getCurrentUser();
        ArrayList<Shelter> shelters = HurricaneApplication.getInstance().getShelters();
        ArrayList<Address> addresses = new ArrayList<Address>();
        for (Shelter shelter : shelters) {
            if (shelter.getAddress() != null) {
                addresses.add(shelter.getAddress());
            }
        }
        if (addresses.isEmpty()) {
            return;
        }
        Address[] shelterAddresses = addresses.toArray(new Address[0]);
        Address home = user == null ? null : user.getAddress();
        if (home == null) {
            map.showAddressesOnMap(shelterAddresses);
            return;
        }

        // finding the closest shelter calls the map api once per shelter, so keep it off the UI thread
        Task<Address> findClosest = new Task<Address>() {
            @Override
            protected Address call() throws Exception {
                return MapTools.getInstance().findClosestAddress(home, shelterAddresses);
            }
        };
        findClosest.setOnSucceeded(event -> {
            if (findClosest.getValue() != null) {
                map.showRouteOnMap(home, findClosest.getValue());
            } else {
                map.showAddressesOnMap(shelterAddresses);
            }
        });
        findClosest.setOnFailed(event -> {
            findClosest.getException().printStackTrace();
            map.showAddressesOnMap(shelterAddresses);
        });
        Thread thread = new Thread(findClosest);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void goNewRequest() throws IOException {
        App.setRoot("newrequest");
    }

    @FXML
    private void goAccount() throws IOException {
        App.setRoot("account");
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

    /** A user's initials, e.g. "GC" for Gary Carter. */
    static String initials(User user) {
        return initial(user.getFirstName()) + initial(user.getLastName());
    }

    private static String initial(String name) {
        return name == null || name.isEmpty() ? "" : name.substring(0, 1).toUpperCase();
    }
}
