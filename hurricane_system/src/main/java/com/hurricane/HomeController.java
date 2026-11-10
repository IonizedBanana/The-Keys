package com.hurricane;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

import com.model.Hurricane;
import com.model.HurricaneApplication;
import com.model.HurricaneStatus;
import com.model.Shelter;
import com.model.users.User;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Controls the home screen: greeting, storm alert, current storm and shelters.
 *
 * TODO show different home screens for volunteers, dispatchers and shelter workers
 */
public class HomeController {

    /** How many shelters to list on the home screen. */
    private static final int SHELTERS_SHOWN = 5;

    /** A shelter at least this full (used / total) is shown as filling up. */
    private static final double FILLING_UP = 0.8;

    @FXML
    private Label greetingLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private VBox alertBanner;

    @FXML
    private Label alertTitle;

    @FXML
    private Label alertBody;

    @FXML
    private Label stormStatusTag;

    @FXML
    private Label stormNameLabel;

    @FXML
    private HBox stormStats;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label stormLocationLabel;

    @FXML
    private VBox shelterList;

    @FXML
    private void initialize() {
        HurricaneApplication app = HurricaneApplication.getInstance();
        User user = app.getCurrentUser();

        greetingLabel.setText(greeting() + (user == null ? "" : ", " + user.getFirstName()));
        if (user != null && user.getAddress() != null) {
            locationLabel.setText("Here's the latest for " + user.getAddress().getCity() + ", " + user.getAddress().getState() + ".");
        } else {
            locationLabel.setText("Here's the latest storm and shelter information.");
        }

        Hurricane storm = NavController.activeStorm();
        showAlert(storm, app.isAffected());
        showStorm(storm);
        showShelters(app.getShelters());
    }

    private void showAlert(Hurricane storm, boolean affected) {
        if (storm != null && affected) {
            alertBanner.getStyleClass().add("alert-warning");
            alertTitle.setText("Hurricane " + storm.getName() + " may reach your area");
            alertBody.setText("Category " + storm.getCategory() + " storm near " + storm.getCurrentAddress()
                    + ". Follow local evacuation orders and request help if you need it.");
        } else {
            alertBanner.getStyleClass().add("alert-info");
            alertTitle.setText("No storms near you");
            alertBody.setText("We'll warn you here if a storm is expected within 25 miles of your home.");
        }
    }

    private void showStorm(Hurricane storm) {
        if (storm == null) {
            stormNameLabel.setText("No active storms");
            stormStatusTag.setVisible(false);
            stormStats.setVisible(false);
            stormStats.setManaged(false);
            return;
        }
        stormNameLabel.setText("Hurricane " + storm.getName());
        categoryLabel.setText(String.valueOf(storm.getCategory()));
        statusLabel.setText(storm.getStatus() == HurricaneStatus.INCOMING ? "Incoming" : "Landfall");
        stormLocationLabel.setText(storm.getCurrentAddress() == null ? "Unknown" : storm.getCurrentAddress().getCity());
        stormStatusTag.setText(storm.getStatus() == HurricaneStatus.INCOMING ? "INCOMING" : "IN PROGRESS");
        stormStatusTag.getStyleClass().add("tag-warning");
    }

    private void showShelters(ArrayList<Shelter> shelters) {
        shelters.sort(Comparator.comparingInt(Shelter::availableSpace).reversed());
        if (shelters.isEmpty()) {
            Label none = new Label("No shelters are open yet.");
            none.getStyleClass().add("text-secondary");
            shelterList.getChildren().add(none);
            return;
        }
        for (Shelter shelter : shelters.subList(0, Math.min(SHELTERS_SHOWN, shelters.size()))) {
            shelterList.getChildren().add(shelterRow(shelter));
        }
    }

    private HBox shelterRow(Shelter shelter) {
        Label name = new Label(shelter.getName());
        name.getStyleClass().add("h3");
        String city = shelter.getAddress() == null ? "" : shelter.getAddress().getCity() + "  ·  ";
        Label details = new Label(city + Math.max(0, shelter.availableSpace()) + " spaces open");
        details.getStyleClass().add("caption");
        VBox text = new VBox(2, name, details);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(12, text, spacer, capacityTag(shelter));
        row.getStyleClass().add("divided-row");
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Label capacityTag(Shelter shelter) {
        Label tag = new Label();
        tag.getStyleClass().add("tag");
        if (shelter.availableSpace() <= 0) {
            tag.setText("FULL");
            tag.getStyleClass().add("tag-danger");
        } else if (shelter.getTotalCapacity() > 0
                && (double) shelter.getUsedCapacity() / shelter.getTotalCapacity() >= FILLING_UP) {
            tag.setText("FILLING UP");
            tag.getStyleClass().add("tag-warning");
        } else {
            tag.setText("OPEN");
            tag.getStyleClass().add("tag-success");
        }
        return tag;
    }

    private static String greeting() {
        int hour = LocalTime.now().getHour();
        if (hour < 12) {
            return "Good morning";
        }
        return hour < 18 ? "Good afternoon" : "Good evening";
    }
}
