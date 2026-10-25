package com.model;

import com.model.location.Address;
import com.model.users.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Console driver that runs the Hurricane Scarlett scenarios against
 * {@link HurricaneApplication}, so the model can be tested without the JavaFX screens.
 *
 * <p>Everything goes through the facade. Run it on its own: it has its own
 * {@code main} and does not start the GUI. Run it from hurricane_system/
 * (the JSON paths are relative to it) with MAP_API_KEY set.</p>
 *
 * <p>Run once with no arguments to play the scenarios, then again with
 * {@code verify} to show the changes were saved and survive a restart. Each
 * scenario can also be run on its own by commenting out the others in
 * {@link #run()}.</p>
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

    /**
     * Runs the scenarios in order.
     *
     * <p>To run one at a time, comment out the others. Each one picks up what
     * the earlier runs saved, so run them in order and only reset the JSON
     * ({@code git checkout json/}) before scenario 1.</p>
     */
    public void run() {
        createReliefRequest();  // scenario 1
        matchRosa();            // scenario 2
        findShelterAndAssign(); // scenario 3
        exportTrip();           // scenario 4
        rolePermissions();      // scenario 5
        checkInFamily();        // scenario 6
        // verify();            // after a restart: shows everything above was saved
    }

    /** Sophie's father makes an account and asks for help getting the family to a shelter. */
    public void createReliefRequest() {
        heading("Create the relief request");

        Address home = new Address("48 Tradd St", "Charleston", "SC", "29401");
        if (!app.createAccount("Gary", "Carter", CARTER_USERNAME, "gary.carter@email.com",
                CARTER_PASSWORD, home)) {
            System.out.println("Sorry, Gary Carter's account couldn't be created. "
                    + "If it already exists, reset the JSON (git checkout json/) and run again.");
            return;
        }
        System.out.println("Gary Carter created an account and is logged in.");

        app.addInfo(41, 'm', "(843) 555-0142", "Father");
        if (!app.addPartyMember("Megan Carter", 39, false, "Mother")
                || !app.addPartyMember("Sophie Carter", 9, false, "Daughter. Asthma is getting worse, needs medical assistance")
                || !app.addPartyMember("Buddy", 4, true, "Dog")) {
            System.out.println("Sorry, the Carters' party couldn't be saved.");
        } else {
            System.out.println("Added Megan, Sophie, and Buddy the dog to Gary's party.");
        }

        carterRequest = app.newRequest((byte) 4, ResponseType.TRAVEL,
                "Need transportation to a shelter with medical staff for 2 adults, 1 child, and our dog Buddy. "
                + "Power is out and floodwater is rising, so we can't use our car. We won't leave Buddy behind. "
                + "Contact: gary.carter@email.com, (843) 555-0142.",
                "Sophie (9) has asthma that is getting worse and needs medical assistance.");
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

        if (!loginAs(ROSA_USERNAME, ROSA_PASSWORD, "Rosa Delgado")) {
            return;
        }

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

        if (!findCarterRequest()) {
            return;
        }
        System.out.println();
        System.out.println("Rosa opens the Carters' request:");
        System.out.println(app.getRequest(carterRequest.getId()));
        Victim requester = (Victim) app.getRequester(carterRequest);
        System.out.println("Contact: " + requester.getEmailAddress() + ", " + requester.getPhoneNumber());
        System.out.println("Who needs a ride:");
        for (Victim.Person person : app.getParty(carterRequest)) {
            System.out.println("- " + person.getName() + (person.isPet() ? " (pet)" : "")
                    + ", age " + person.getAge() + ". " + person.getDescription());
        }
    }

    /** Rosa finds the closest shelter that fits the Carters and takes the request. */
    public void findShelterAndAssign() {
        heading("Find a shelter and assign the request");

        if (!findCarterRequest() || !loginAs(ROSA_USERNAME, ROSA_PASSWORD, "Rosa Delgado")) {
            return;
        }

        System.out.println("Searching " + app.getShelters().size() + " shelters for room for "
                + app.getParty(carterRequest).size() + " (people and pets) and: "
                + accomodationText(app.getShelterNeeds(carterRequest)));
        ArrayList<Shelter> matches = app.findShelters(carterRequest);
        if (matches.isEmpty()) {
            System.out.println("Sorry, no shelter fits the Carters.");
            return;
        }
        System.out.println(matches.size() + " fit the family:");
        for (Shelter shelter : matches) {
            System.out.println("- " + shelter.getName() + ", " + shelter.getAddress()
                    + " (" + shelter.availableSpace() + " spaces open; "
                    + accomodationText(shelter.getAccomodations()) + ")");
        }

        chosenShelter = chooseShelter(matches);
        System.out.println("Closest by driving: " + chosenShelter.getName() + ". Rosa picks it.");

        if (!app.assignRequest(carterRequest)) {
            System.out.println("Sorry, Rosa couldn't take the request.");
            return;
        }
        System.out.println("Request status is now: " + carterRequest.getStatus().ASCII);
        Volunteer handler = app.getAssignedVolunteer(carterRequest);
        System.out.println("Other volunteers see it is handled by: "
                + handler.getFirstName() + " " + handler.getLastName());
    }

    /** Rosa gets directions for both legs and saves a printable trip sheet. */
    public void exportTrip() {
        heading("Export the trip information");

        if (!findCarterRequest() || !loginAs(ROSA_USERNAME, ROSA_PASSWORD, "Rosa Delgado")) {
            return;
        }
        if (chosenShelter == null) {
            chosenShelter = chooseShelter(app.findShelters(carterRequest));
            if (chosenShelter == null) {
                System.out.println("Sorry, no shelter fits the Carters.");
                return;
            }
            System.out.println("Rosa is taking the Carters to " + chosenShelter.getName() + ".");
        }
        Address rosa = app.getCurrentUser().getAddress();
        Address pickup = carterRequest.getAddress();
        printDirections("From Rosa to the Carters", app.getDirections(rosa, pickup));
        printDirections("From the Carters to " + chosenShelter.getName(),
                app.getDirections(pickup, chosenShelter.getAddress()));

        if (!app.exportTripInfo(carterRequest, chosenShelter, TRIP_FILE)) {
            System.out.println("Sorry, the trip information couldn't be exported.");
            return;
        }
        System.out.println("Trip information saved to " + TRIP_FILE + ".");
    }

    /** Rosa tries to complete the request and is told she can't. */
    public void rolePermissions() {
        heading("Demonstrate role permissions");

        if (!findCarterRequest() || !loginAs(ROSA_USERNAME, ROSA_PASSWORD, "Rosa Delgado")) {
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

        boolean haveRequest = findCarterRequest();
        if (!loginAs(FELIX_USERNAME, FELIX_PASSWORD, "Felix")) {
            return;
        }

        Shelter shelter = app.getMyShelter();
        if (shelter == null) {
            System.out.println("Felix isn't assigned to a shelter.");
            app.logout();
            return;
        }
        printShelter(shelter);

        if (haveRequest) {
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

    /** After a restart, shows that the account, party, request, and check-in were saved. */
    public void verify() {
        heading("Show that the changes are saved");

        if (!app.login(CARTER_USERNAME, CARTER_PASSWORD)) {
            System.out.println("Gary Carter's account was not saved.");
            return;
        }
        System.out.println("Gary Carter's account was saved, with his party:");
        for (Victim.Person person : ((Victim) app.getCurrentUser()).getParty()) {
            System.out.println("- " + person.getName() + (person.isPet() ? " (pet)" : ""));
        }
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

    /**
     * Finds the Carters' request, either from this run or from what an earlier
     * run saved, so each scenario can also be run on its own.
     */
    private boolean findCarterRequest() {
        if (carterRequest == null) {
            for (Request request : app.getOpenRequests()) {
                if (CARTER_USERNAME.equals(request.getRequestee())) {
                    carterRequest = request;
                }
            }
        }
        if (carterRequest == null) {
            System.out.println("There's no open request from the Carters. Run scenario 1 first.");
            return false;
        }
        return true;
    }

    /**
     * Logs a user in unless they already are, logging out whoever was.
     */
    private boolean loginAs(String username, String password, String name) {
        User current = app.getCurrentUser();
        if (current != null && current.getUsername().equals(username)) {
            return true;
        }
        if (current != null) {
            app.logout();
        }
        if (!app.login(username, password)) {
            System.out.println("Sorry, " + name + " couldn't log in.");
            return false;
        }
        System.out.println(name + " is now logged in.");
        return true;
    }

    /**
     * Picks the shelter that is the shortest drive away, or the nearest one if
     * driving times aren't available.
     */
    private Shelter chooseShelter(ArrayList<Shelter> matches) {
        Shelter closest = app.findClosestShelter(carterRequest);
        if (closest == null && !matches.isEmpty()) {
            System.out.println("Couldn't get driving times, so Rosa picks the nearest one instead.");
            return matches.get(0);
        }
        return closest;
    }

    private void printDirections(String title, String[] directions) {
        System.out.println(title + ":");
        if (directions == null) {
            System.out.println("  Directions aren't available right now.");
            return;
        }
        for (int i = 0; i < directions.length; i++) {
            System.out.println("  " + (i + 1) + ". " + directions[i]);
        }
    }

    private void printShelter(Shelter shelter) {
        System.out.println(shelter.getName() + ", " + shelter.getAddress());
        System.out.println("  Total capacity:    " + shelter.getTotalCapacity());
        System.out.println("  Current occupancy: " + shelter.getUsedCapacity());
        System.out.println("  Available spaces:  " + shelter.availableSpace());
        System.out.println("  Offers:            " + accomodationText(shelter.getAccomodations()));
        System.out.println("  Checked in:        " + namesText(shelter.getRegisteredPeople().keySet()));
        System.out.println("  Pets:              " + namesText(shelter.getRegisteredPets().keySet()));
    }

    private String accomodationText(ArrayList<Accomodation> accomodations) {
        if (accomodations == null || accomodations.isEmpty()) {
            return "nothing extra";
        }
        ArrayList<String> names = new ArrayList<String>();
        for (Accomodation accomodation : accomodations) {
            names.add(accomodation.accomodation.toLowerCase());
        }
        return String.join(", ", names);
    }

    private String namesText(Collection<String> names) {
        return names.isEmpty() ? "none" : String.join(", ", names);
    }

    private String summarize(Request request) {
        return request.getResponderType().ASCII + " request from " + request.getRequestee()
                + " at " + request.getAddress() + ", severity " + request.getSeverity()
                + ", " + request.getStatus().ASCII;
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
