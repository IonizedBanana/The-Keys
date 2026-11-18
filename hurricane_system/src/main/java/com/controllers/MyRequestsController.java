package com.controllers;

import java.util.ArrayList;
import java.util.Comparator;

import com.model.HurricaneApplication;
import com.model.Request;
import com.model.RequestList;
import com.model.ResponseStatus;
import com.model.users.User;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Controls the My Requests screen: the logged in user's requests, split into open and completed.
 */
public class MyRequestsController {

    /** Injected from the fx:include with fx:id="nav" in myrequests.fxml. */
    @FXML
    private NavController navController;

    @FXML
    private Label subtitleLabel;

    @FXML
    private VBox openList;

    @FXML
    private VBox completedList;

    @FXML
    private void initialize() {
        navController.setActive(NavController.Page.REQUESTS);

        ArrayList<Request> open = new ArrayList<Request>();
        ArrayList<Request> completed = new ArrayList<Request>();
        for (Request request : myRequests()) {
            if (request.getStatus() == ResponseStatus.COMPLETE) {
                completed.add(request);
            } else {
                open.add(request);
            }
        }
        // most urgent open requests first
        open.sort(Comparator.comparingInt(Request::getSeverity).reversed());

        subtitleLabel.setText(open.isEmpty()
                ? "You have no open requests. Use New Request if you need help."
                : "You have " + open.size() + " open request" + (open.size() == 1 ? "" : "s")
                        + ". We'll update the status here as responders take them on.");
        fill(openList, open, "No open requests.");
        fill(completedList, completed, "Nothing completed yet.");
    }

    /**
     * Finds the logged in user's requests. Reads the shared request list rather than the
     * user's own copy, since that's where responders update the status.
     */
    private static ArrayList<Request> myRequests() {
        ArrayList<Request> mine = new ArrayList<Request>();
        User user = HurricaneApplication.getInstance().getCurrentUser();
        if (user == null) {
            return mine;
        }
        for (Request request : RequestList.getInstance().getRequestList().values()) {
            if (user.getUsername().equalsIgnoreCase(request.getRequestee())) {
                mine.add(request);
            }
        }
        return mine;
    }

    private static void fill(VBox list, ArrayList<Request> requests, String emptyText) {
        if (requests.isEmpty()) {
            Label none = new Label(emptyText);
            none.getStyleClass().add("text-secondary");
            list.getChildren().add(none);
            return;
        }
        for (Request request : requests) {
            list.getChildren().add(requestRow(request));
        }
    }

    private static HBox requestRow(Request request) {
        Label type = new Label(request.getResponderType() == null
                ? "Request" : request.getResponderType().ASCII + " Request");
        type.getStyleClass().add("h3");

        Label summary = new Label(firstLine(request.getDescription()));
        summary.getStyleClass().add("text-secondary");
        summary.setWrapText(true);

        String where = request.getAddress() == null ? "" : request.getAddress().getCity() + "  ·  ";
        Label details = new Label(where + "Urgency " + request.getSeverity() + " / 5");
        details.getStyleClass().add("caption");

        VBox text = new VBox(2, type, summary, details);
        HBox.setHgrow(text, Priority.ALWAYS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        HBox row = new HBox(12, text, spacer, statusTag(request.getStatus()));
        row.getStyleClass().add("divided-row");
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static Label statusTag(ResponseStatus status) {
        Label tag = new Label(status == null ? "UNKNOWN" : status.ASCII.toUpperCase());
        tag.getStyleClass().add("tag");
        tag.setMinWidth(Region.USE_PREF_SIZE);
        if (status == null) {
            tag.getStyleClass().add("tag-neutral");
            return tag;
        }
        switch (status) {
            case COMPLETE:
                tag.getStyleClass().add("tag-success");
                break;
            case IN_PROGRESS:
                tag.getStyleClass().add("tag-warning");
                break;
            case ASSIGNED:
                tag.getStyleClass().add("tag-info");
                break;
            case MORE_INFO_NEEDED:
                tag.getStyleClass().add("tag-danger");
                break;
            default:
                tag.getStyleClass().add("tag-neutral");
        }
        return tag;
    }

    /** The first line of a description, since the party size is added on its own line. */
    private static String firstLine(String text) {
        if (text == null || text.isBlank()) {
            return "No description";
        }
        return text.strip().split("\n", 2)[0];
    }
}
