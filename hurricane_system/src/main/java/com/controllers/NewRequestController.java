package com.controllers;

import com.hurricane.Const;
import com.hurricane.App;
import java.io.IOException;

import com.model.HurricaneApplication;
import com.model.Request;
import com.model.RequestList;
import com.model.ResponseType;
import com.model.location.Address;
import com.model.users.User;
import com.model.users.UserType;
import com.model.users.Victim;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * Controls the new request screen, where a victim asks for help.
 */
public class NewRequestController {

    /** Urgency choices; the position in the list plus one is the request's severity (1 to 5). */
    private static final String[] URGENCY = {
        "1 - Can wait", "2 - Low", "3 - Medium", "4 - High", "5 - Urgent"
    };

    /** Injected from the fx:include with fx:id="nav" in newrequest.fxml. */
    @FXML
    private NavController navController;

    @FXML
    private ComboBox<ResponseType> typeBox;

    @FXML
    private ComboBox<String> urgencyBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField streetField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField stateField;

    @FXML
    private TextField zipField;

    @FXML
    private TextArea infoArea;

    @FXML
    private Button minusButton;

    @FXML
    private Label partySizeLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Button submitButton;

    @FXML
    private VBox formPane;

    @FXML
    private VBox successPane;

    @FXML
    private Label summaryType;

    @FXML
    private Label summaryAddress;

    @FXML
    private Label summaryDetails;

    private int partySize = 1;

    @FXML
    private void initialize() {
        navController.setActive(NavController.Page.NEW_REQUEST);

        typeBox.getItems().setAll(ResponseType.values());
        typeBox.setConverter(new StringConverter<ResponseType>() {
            @Override
            public String toString(ResponseType type) {
                return type == null ? "" : type.ASCII;
            }

            @Override
            public ResponseType fromString(String text) {
                return null;
            }
        });
        urgencyBox.getItems().setAll(URGENCY);

        HurricaneApplication app = HurricaneApplication.getInstance();
        User user = app.getCurrentUser();

        // start from the user's home; they can change it if they need help somewhere else
        if (user != null && user.getAddress() != null) {
            Address home = user.getAddress();
            streetField.setText(home.getStreet());
            cityField.setText(home.getCity());
            stateField.setText(home.getState());
            zipField.setText(home.getZip());
        }
        if (user instanceof Victim) {
            partySize = Math.max(1, ((Victim) user).getPartySize());
        }
        showPartySize();

        if (!app.isType(UserType.VICTIM)) {
            errorLabel.setText("Only people asking for help can make requests.");
            submitButton.setDisable(true);
        }
    }

    @FXML
    private void decreaseParty() {
        partySize = Math.max(1, partySize - 1);
        showPartySize();
    }

    @FXML
    private void increaseParty() {
        partySize++;
        showPartySize();
    }

    private void showPartySize() {
        partySizeLabel.setText(String.valueOf(partySize));
        minusButton.setDisable(partySize <= 1);
    }

    @FXML
    private void submit() throws IOException {
        clearErrors();

        ResponseType type = typeBox.getValue();
        int urgency = urgencyBox.getSelectionModel().getSelectedIndex();
        String description = descriptionArea.getText();
        String street = streetField.getText();
        String city = cityField.getText();
        String state = stateField.getText();
        String zip = zipField.getText();

        if (type == null) {
            typeBox.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Choose the type of help you need.");
            return;
        }
        if (urgency < 0) {
            urgencyBox.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Choose how urgent this is.");
            return;
        }
        if (Const.anyBlank(description)) {
            descriptionArea.setStyle(Const.ERROR_STYLE);
            errorLabel.setText("Describe what you need.");
            return;
        }
        if (Const.anyBlank(street, city, state, zip)) {
            errorLabel.setText("Fill in the full address where help is needed.");
            return;
        }

        HurricaneApplication app = HurricaneApplication.getInstance();
        // party size is saved with the description so responders see it with the request
        String fullDescription = description.trim() + "\nParty size: " + partySize;
        Request request = app.newRequest((byte) (urgency + 1), type, fullDescription, infoArea.getText());

        // newRequest uses the home address, so switch it if they asked for help somewhere else
        boolean saved = request != null;
        Address address = new Address(street.trim(), city.trim(), state.trim(), zip.trim());
        if (saved && !sameAddress(address, request.getAddress())) {
            request.setAddress(address);
            saved = app.saveData();
        }

        // only confirm once the request is actually in requests.json
        if (saved && RequestList.getInstance().isSaved(request)) {
            showSuccess(request, URGENCY[urgency]);
        } else {
            // newRequest may already have added it in memory, so a retry here could save it twice
            submitButton.setDisable(true);
            errorLabel.setText("We couldn't confirm your request was saved. Go back to Home and try again. "
                    + "If this is life threatening, call 911.");
        }
    }

    private void showSuccess(Request request, String urgency) {
        summaryType.setText(request.getResponderType().ASCII + " request  ·  " + urgency.substring(4));
        summaryAddress.setText(request.getAddress().toString());
        summaryDetails.setText("Party of " + partySize + "  ·  Status: " + request.getStatus().ASCII);
        formPane.setVisible(false);
        formPane.setManaged(false);
        successPane.setVisible(true);
        successPane.setManaged(true);
    }

    @FXML
    private void goRequests() throws IOException {
        App.setRoot("myrequests");
    }

    @FXML
    private void goHome() throws IOException {
        App.setRoot("home");
    }

    private void clearErrors() {
        errorLabel.setText("");
        typeBox.setStyle(Const.NORMAL_STYLE);
        urgencyBox.setStyle(Const.NORMAL_STYLE);
        descriptionArea.setStyle(Const.NORMAL_STYLE);
    }

    private static boolean sameAddress(Address a, Address b) {
        return b != null
                && a.getStreet().equalsIgnoreCase(String.valueOf(b.getStreet()))
                && a.getCity().equalsIgnoreCase(String.valueOf(b.getCity()))
                && a.getState().equalsIgnoreCase(String.valueOf(b.getState()))
                && a.getZip().equalsIgnoreCase(String.valueOf(b.getZip()));
    }
}
