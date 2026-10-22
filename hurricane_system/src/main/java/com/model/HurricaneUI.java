package com.model;

import com.model.users.*;

import java.util.ArrayList;

/**
 * Console driver that runs the Hurricane Scarlett scenarios against
 * {@link HurricaneApplication}, so the model can be tested without the JavaFX screens.
 *
 * <p>Everything goes through the facade. Run it on its own: it has its own
 * {@code main} and does not start the GUI.</p>
 *
 * <p>Run once with no arguments to play the scenarios, then again with
 * {@code verify} to show the changes were saved and survive a restart.</p>
 *
 * <p>Rosa, Felix (and his shelter), six other open requests, ten shelters, and
 * Hurricane Scarlett need to be in the JSON files before running.</p>
 */
public class HurricaneUI {

    private static final String CARTER_USERNAME = "gcarter";
    private static final String CARTER_PASSWORD = "buddy2026";
    private static final String ROSA_USERNAME = "rdelgado";
    private static final String ROSA_PASSWORD = "delgado123";
    private static final String FELIX_USERNAME = "Felix44";
    private static final String FELIX_PASSWORD = "passwordpassword";
    private static final String TRIP_FILE = "carter-trip.txt";

    private HurricaneApplication app;
    private Request carterRequest;
    private Shelter chosenShelter;

    HurricaneUI() {
        app = HurricaneApplication.getInstance();
    }

    public void run() {
        createReliefRequest();
        matchRosa();
        findShelterAndAssign();
        exportTrip();
        rolePermissions();
        checkInFamily();
    }

    /** Sophie's father makes an account and asks for help getting the family to a shelter. */
    public void createReliefRequest() {
        heading("Create the relief request");

        Address home = new Address("SC", "Charleston", "48 Tradd St");
        if (!app.createAccount("Gary", "Carter", CARTER_USERNAME, "gary.carter@email.com",
                CARTER_PASSWORD, home)) {
            System.out.println("Sorry, Gary Carter's account couldn't be created.");
            return;
        }
        System.out.println("Gary Carter created an account and is logged in.");

        app.addInfo(41, 'm', "Father. Phone: (843) 555-0142", 3);
        if (!app.addPartyMember("Megan Carter", 39, false, "Mother")
                || !app.addPartyMember("Sophie Carter", 9, false, "Daughter, asthma getting worse")
                || !app.addPartyMember("Buddy", 4, true, "Dog")) {
            System.out.println("Party members couldn't be stored yet, so they are listed in the request instead.");
        }

        carterRequest = app.newRequest((byte) 4, ResponseType.TRAVEL,
                "Need transportation to a shelter with medical staff. Power is out and floodwater is rising, "
                + "so we can't use our car. 2 adults: Gary (41) and Megan (39). 1 child: Sophie (9), whose "
                + "asthma is getting worse and needs medical assistance. 1 dog: Buddy, who is coming with us. "
                + "Contact: gary.carter@email.com, (843) 555-0142.");
        if (carterRequest == null) {
            System.out.println("Sorry, the Carters' request couldn't be submitted.");
            app.logout();
            return;
        }
        System.out.println("Request submitted:");
        System.out.println(carterRequest);

        app.logout();
        System.out.println("Gary Carter logged out.");
    }

    /** Rosa logs in and sees the Carters' request recommended at the top. */
    public void matchRosa() {
        heading("Match Rosa with the request");

        if (!app.login(ROSA_USERNAME, ROSA_PASSWORD)) {
            System.out.println("Sorry, Rosa Delgado couldn't log in.");
            return;
        }
        System.out.println("Rosa Delgado is now logged in.");

        ArrayList<Request> open = app.getOpenRequests();
        System.out.println("Open relief requests: " + open.size());
        for (int i = 0; i < open.size(); i++) {
            Request request = open.get(i);
            System.out.println((i + 1) + ". " + summarize(request));
            String reason = app.getRecommendationReason(request);
            if (reason != null) {
                System.out.println("   RECOMMENDED: " + reason);
            }
        }

        if (carterRequest == null) {
            return;
        }
        System.out.println();
        System.out.println("Rosa opens the Carters' request:");
        System.out.println(app.getRequest(carterRequest.getId()));
    }

    /** Rosa finds a shelter that fits the Carters and takes the request. */
    public void findShelterAndAssign() {
        heading("Find a shelter and assign the request");

        if (carterRequest == null) {
            System.out.println("There's no request to assign.");
            return;
        }

        System.out.println("Searching " + app.getShelters().size() + " shelters...");
        ArrayList<Shelter> matches = app.findShelters(carterRequest);
        if (matches.isEmpty()) {
            System.out.println("Sorry, no shelter has room for the Carters.");
            return;
        }
        for (Shelter shelter : matches) {
            System.out.println("- " + shelter.getName() + ", " + shelter.getAddress()
                    + " (" + shelter.availableSpace() + " spaces open)");
        }
        chosenShelter = matches.get(0);
        System.out.println("Rosa picks " + chosenShelter.getName() + ".");

        if (!app.assignRequest(carterRequest)) {
            System.out.println("Sorry, Rosa couldn't take the request.");
            return;
        }
        System.out.println("Request status is now: " + carterRequest.getStatus().ASCII);
        Volunteer handler = app.getAssignedVolunteer(carterRequest);
        System.out.println("Other volunteers see it is handled by: "
                + handler.getFirstName() + " " + handler.getLastName());
    }

    /** Rosa saves a printable trip sheet. */
    public void exportTrip() {
        heading("Export the trip information");

        if (carterRequest == null) {
            System.out.println("There's no request to export.");
            return;
        }
        if (!app.exportTripInfo(carterRequest, chosenShelter, TRIP_FILE)) {
            System.out.println("Sorry, the trip information couldn't be exported.");
            return;
        }
        System.out.println("Trip information saved to " + TRIP_FILE + ".");
    }

    /** Rosa tries to complete the request and is told she can't. */
    public void rolePermissions() {
        heading("Demonstrate role permissions");

        if (carterRequest == null) {
            return;
        }
        if (app.finishRequest(carterRequest)) {
            System.out.println("Rosa marked the request complete. That shouldn't happen.");
        } else {
            System.out.println("Rosa's volunteer role can't complete relief requests.");
        }
        System.out.println("Request status is still: " + carterRequest.getStatus().ASCII);

        app.logout();
        System.out.println("Rosa Delgado logged out.");
    }

    /** Felix checks the family in and completes the request. */
    public void checkInFamily() {
        heading("Check the family into the shelter");

        if (!app.login(FELIX_USERNAME, FELIX_PASSWORD)) {
            System.out.println("Sorry, Felix couldn't log in.");
            return;
        }
        System.out.println("Felix is now logged in.");

        Shelter shelter = app.getMyShelter();
        if (shelter == null) {
            System.out.println("Felix isn't assigned to a shelter.");
            app.logout();
            return;
        }
        printShelter(shelter);

        if (carterRequest != null) {
            if (app.checkIn(carterRequest)) {
                System.out.println("The Carter family and Buddy are checked in.");
            } else {
                System.out.println("Sorry, the Carters couldn't be checked in.");
            }
            printShelter(shelter);

            if (app.finishRequest(carterRequest)) {
                System.out.println("Felix marked the request: " + carterRequest.getStatus().ASCII);
            } else {
                System.out.println("Sorry, Felix couldn't complete the request.");
            }
        }

        app.logout();
        System.out.println("Felix logged out.");
    }

    /** After a restart, shows that the account, request, and check-in were saved. */
    public void verify() {
        heading("Show that the changes are saved");

        if (!app.login(CARTER_USERNAME, CARTER_PASSWORD)) {
            System.out.println("Gary Carter's account was not saved.");
            return;
        }
        System.out.println("Gary Carter's account was saved.");
        app.logout();

        for (Request request : app.getOpenRequests()) {
            if (CARTER_USERNAME.equals(request.getRequestee())) {
                System.out.println("The Carters' request is saved but not complete:");
                System.out.println(request);
                return;
            }
        }
        System.out.println("The Carters' request is no longer open, so it was saved as complete.");

        if (app.login(FELIX_USERNAME, FELIX_PASSWORD)) {
            printShelter(app.getMyShelter());
            app.logout();
        }
    }

    private void printShelter(Shelter shelter) {
        System.out.println(shelter.getName() + ", " + shelter.getAddress());
        System.out.println("  Total capacity:    " + shelter.getTotalCapacity());
        System.out.println("  Current occupancy: " + shelter.getUsedCapacity());
        System.out.println("  Available spaces:  " + shelter.availableSpace());
        // TODO print occupants and pets once Shelter stores them
    }

    private String summarize(Request request) {
        return request.getResponderType().ASCII + " request from " + request.getRequestee()
                + " in " + request.getLocation() + ", severity " + request.getSeverity()
                + ", " + request.getStatus().ASCII + " (" + request.getId() + ")";
    }

    private void heading(String title) {
        System.out.println();
        System.out.println("===== " + title + " =====");
    }

    public static void main(String[] args) {
        HurricaneUI hurricaneInterface = new HurricaneUI();
        if (args.length > 0 && args[0].equals("verify")) {
            hurricaneInterface.verify();
        } else {
            hurricaneInterface.run();
        }
    }
}
