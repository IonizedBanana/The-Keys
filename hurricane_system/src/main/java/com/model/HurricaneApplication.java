package com.model;

import com.model.location.*;
import com.model.users.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * The single entry point the UI uses to reach the model.
 *
 * <p>Controllers and the console driver call this class instead of touching
 * the lists, DataReader, DataWriter, MapTools, or the user classes directly,
 * so the UI never needs to know how anything is stored or looked up. Methods
 * return results instead of printing, and the UI decides what to show.</p>
 *
 * <p>Distances and directions come from the map api, so the MAP_API_KEY
 * environment variable has to be set (see .env).</p>
 *
 * TODO the rest of the UML methods that the scenarios don't use (newHurricane, newAlert, editUser, etc.)
 *
 * @author SynthwaveFox
 */
public class HurricaneApplication {

    /** How close a request has to be, in miles, to count as nearby. */
    private static final int NEARBY_MILES = 5;

    /** How close a storm has to be, in miles, to count as affecting someone (same as Alert). */
    private static final int AFFECTED_MILES = 25;

    /** The one shared instance. */
    private static HurricaneApplication app;

    /** Whoever is logged in, or null. */
    private User currentUser;

    /** Miles from the current user to each request, so the map api isn't asked twice. */
    private HashMap<UUID, Double> requestDistances;

    /** Private so the only instance comes from {@link #getInstance()}. */
    private HurricaneApplication() {
        // load everything up front so a bad file shows up at startup, not mid-scenario
        UserList.getInstance();
        ShelterList.getInstance();
        RequestList.getInstance();
        HurricaneList.getInstance();
        requestDistances = new HashMap<UUID, Double>();
    }

    /**
     * Returns the shared instance, creating it the first time.
     *
     * @return the application
     */
    public static HurricaneApplication getInstance() {
        if (app == null) {
            app = new HurricaneApplication();
        }
        return app;
    }

    // ---------------------------------------------------------------- accounts

    /**
     * Logs a user in.
     *
     * <p>Returns boolean instead of User (like the UML) so the login screen
     * can stay as it is. Use {@link #getCurrentUser()} for the user.</p>
     *
     * @param username the username entered
     * @param password the password entered
     * @return true if the username and password match a saved user
     */
    public boolean login(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            return false;
        }
        User user = UserList.getInstance().getUser(username);
        if (user == null || !user.isMatch(username, password)) {
            return false;
        }
        setCurrentUser(user);
        return true;
    }

    /**
     * Saves everything and logs the current user out.
     */
    public void logout() {
        saveData();
        setCurrentUser(null);
    }

    /**
     * Gets the logged in user.
     *
     * @return the current user, or null if nobody is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Reports whether the logged in user is a certain type.
     *
     * @param type the type to check for
     * @return true if someone is logged in and is that type
     */
    public boolean isType(UserType type) {
        return currentUser != null && currentUser.getType() == type;
    }

    /**
     * Creates a new account without an address.
     *
     * <p>Kept for the sign up screen, which doesn't ask for an address yet.
     * Always fails, since a user can't be saved without one.</p>
     *
     * TODO add address fields to the sign up screen and call the other createAccount
     *
     * @param firstName    the user's first name
     * @param lastName     the user's last name
     * @param username     the username to log in with
     * @param emailAddress the user's email address
     * @param password     the password to log in with
     * @return false until the sign up screen sends an address
     */
    public boolean createAccount(String firstName, String lastName, String username,
                                 String emailAddress, String password) {
        return createAccount(firstName, lastName, username, emailAddress, password, null);
    }

    /**
     * Creates a new victim account, saves it, and logs the new user in.
     *
     * <p>New accounts are victims. Volunteers, shelter workers, and the
     * other types are set up separately.</p>
     *
     * @param firstName    the user's first name
     * @param lastName     the user's last name
     * @param username     the username to log in with; must not already be taken
     * @param emailAddress the user's email address
     * @param password     the password to log in with
     * @param address      the user's home address
     * @return true if the account was created and saved
     */
    public boolean createAccount(String firstName, String lastName, String username,
                                 String emailAddress, String password, Address address) {
        if (isBlank(firstName) || isBlank(lastName) || isBlank(username)
                || isBlank(emailAddress) || isBlank(password) || address == null) {
            return false;
        }
        UserList users = UserList.getInstance();
        if (users.usernameTaken(username)) {
            return false;
        }
        Victim victim = new Victim(firstName, lastName, username, emailAddress, password, address);
        users.addUser(victim);
        if (!users.save()) {
            return false;
        }
        setCurrentUser(victim);
        return true;
    }

    /**
     * Fills in the logged in victim's details.
     *
     * @param age         the victim's age
     * @param sex         the victim's sex
     * @param phoneNumber a number responders can call
     * @param description anything responders should know about the victim
     * @return true if the current user is a victim and the details were saved
     */
    public boolean addInfo(int age, char sex, String phoneNumber, String description) {
        if (!isType(UserType.VICTIM)) {
            return false;
        }
        Victim victim = (Victim) currentUser;
        victim.setAge(age);
        victim.setSex(sex);
        victim.setPhoneNumber(phoneNumber);
        victim.setDescription(description);
        return UserList.getInstance().save();
    }

    /**
     * Adds someone without an account (a family member or a pet) to the
     * logged in victim's party. People count toward the party size; pets don't.
     *
     * @param name        the person's or pet's name
     * @param age         their age
     * @param isPet       true for a pet
     * @param description anything responders should know, like "dog" or "asthma"
     * @return true if they were added
     */
    public boolean addPartyMember(String name, int age, boolean isPet, String description) {
        if (!isType(UserType.VICTIM) || isBlank(name) || age < 0 || age > Byte.MAX_VALUE) {
            return false;
        }
        Victim victim = (Victim) currentUser;
        if (victim.getParty() == null) {
            victim.setParty(new ArrayList<Victim.Person>());
        }
        // Victim recounts the party size itself, leaving out pets
        victim.addPartyMember(victim.new Person(name, (byte) age, isPet, description));
        return UserList.getInstance().save();
    }

    // ---------------------------------------------------------------- requests

    /**
     * Creates a relief request for the logged in victim at their home address.
     *
     * <p>Builds the Request here instead of through Victim.createRequest(),
     * since that constructor leaves the id, status, and type empty.</p>
     *
     * @param severity    how urgent it is, 1 to 5
     * @param type        the kind of help needed
     * @param description what is going on
     * @return the new request, or null if it couldn't be made
     */
    public Request newRequest(byte severity, ResponseType type, String description) {
        if (!isType(UserType.VICTIM) || type == null || isBlank(description)
                || severity < 1 || severity > 5 || currentUser.getAddress() == null) {
            return null;
        }
        Victim victim = (Victim) currentUser;
        Request request = new Request(UUID.randomUUID(), victim.getUsername(), severity,
                victim.getAddress(), ResponseStatus.WAITING, type, description);
        RequestList.getInstance().addRequest(request);
        victim.getCreatedRequests().add(request);
        if (!saveData()) {
            return null;
        }
        return request;
    }

    /**
     * Gets a request by its id.
     *
     * @param id the request's id
     * @return the request, or null if there isn't one
     */
    public Request getRequest(UUID id) {
        return RequestList.getInstance().getRequest(id);
    }

    /**
     * Gets every request that isn't complete, with requests recommended for the
     * logged in user first, then the most severe.
     *
     * @return the open requests
     */
    public ArrayList<Request> getOpenRequests() {
        ArrayList<Request> open = new ArrayList<Request>();
        for (Request request : RequestList.getInstance().getRequestList().values()) {
            if (request.getStatus() != ResponseStatus.COMPLETE) {
                open.add(request);
            }
        }
        // decide each one once up front, since each check can call the map api
        HashMap<UUID, Boolean> recommended = new HashMap<UUID, Boolean>();
        for (Request request : open) {
            recommended.put(request.getId(), isRecommended(request));
        }
        open.sort((a, b) -> {
            boolean aRecommended = recommended.get(a.getId());
            if (aRecommended != recommended.get(b.getId())) {
                return aRecommended ? -1 : 1;
            }
            return b.getSeverity() - a.getSeverity();
        });
        return open;
    }

    /**
     * Reports whether a request is recommended for the logged in volunteer:
     * it is within {@value #NEARBY_MILES} miles and needs the kind of help
     * they specialize in.
     *
     * @param request the request to check
     * @return true if it is recommended
     */
    public boolean isRecommended(Request request) {
        if (!matchesSpecialty(request)) {
            return false;
        }
        double miles = getDistanceTo(request);
        return miles >= 0 && miles < NEARBY_MILES;
    }

    /**
     * Explains why a request is recommended for the logged in volunteer.
     *
     * @param request the request to explain
     * @return the reason, or null if it isn't recommended
     */
    public String getRecommendationReason(Request request) {
        if (!isRecommended(request)) {
            return null;
        }
        return String.format("It is %.1f miles from you and needs %s help, which is your specialty.",
                getDistanceTo(request), request.getResponderType().ASCII.toLowerCase());
    }

    /**
     * Gets roughly how far a request is from the logged in user, in a straight line.
     *
     * @param request the request
     * @return the distance in miles, or -1 if it couldn't be worked out
     */
    public double getDistanceTo(Request request) {
        if (currentUser == null || currentUser.getAddress() == null
                || request == null || request.getAddress() == null) {
            return -1;
        }
        Double miles = requestDistances.get(request.getId());
        if (miles == null) {
            miles = distanceBetween(currentUser.getAddress(), request.getAddress());
            requestDistances.put(request.getId(), miles);
        }
        return miles;
    }

    /**
     * Gets the user who made a request.
     *
     * @param request the request
     * @return the user, or null if they can't be found
     */
    public User getRequester(Request request) {
        return request.getRequesteeUser();
    }

    /**
     * Gets everyone a request is for: whoever made it first, then the people
     * and pets with them.
     *
     * @param request the request
     * @return the whole party, empty if the request wasn't made by a victim
     */
    public ArrayList<Victim.Person> getParty(Request request) {
        User user = request.getRequesteeUser();
        if (user instanceof Victim && ((Victim) user).getParty() != null) {
            return ((Victim) user).getFullParty();
        }
        return new ArrayList<Victim.Person>();
    }

    /**
     * Assigns a request to the logged in volunteer.
     *
     * @param request the request to take
     * @return true if it was assigned
     */
    public boolean assignRequest(Request request) {
        if (!isType(UserType.VOLUNTEER) || request == null
                || request.getStatus() == ResponseStatus.COMPLETE
                || getAssignedVolunteer(request) != null) {
            return false;
        }
        Volunteer volunteer = (Volunteer) currentUser;
        volunteer.setAssignedRequest(request);
        volunteer.setAvailible(false);
        request.updateStatus(ResponseStatus.ASSIGNED);
        return saveData();
    }

    /**
     * Finds the volunteer handling a request, so other volunteers can see it's taken.
     *
     * @param request the request
     * @return the volunteer, or null if nobody has it
     */
    public Volunteer getAssignedVolunteer(Request request) {
        for (User user : UserList.getInstance().getUserList().values()) {
            if (user instanceof Volunteer) {
                Request assigned = ((Volunteer) user).getAssignedRequest();
                if (assigned != null && assigned.getId().equals(request.getId())) {
                    return (Volunteer) user;
                }
            }
        }
        return null;
    }

    /**
     * Marks a request complete, if the logged in user's role is allowed to.
     *
     * <p>The user classes decide who may do this and explain when they can't
     * (volunteers can't). Shelter workers and dispatchers can.</p>
     *
     * @param request the request to finish
     * @return true if the request is now complete
     */
    public boolean finishRequest(Request request) {
        if (currentUser == null || request == null) {
            return false;
        }
        switch (currentUser.getType()) {
            case VOLUNTEER -> ((Volunteer) currentUser).finishRequest(request);
            case SHELTER_WORKER -> ((ShelterWorker) currentUser).finishRequest(request);
            case DISPATCHER -> ((Dispatcher) currentUser).finishRequest(request);
            default -> {
                return false;
            }
        }
        if (request.getStatus() != ResponseStatus.COMPLETE) {
            return false;
        }
        Volunteer volunteer = getAssignedVolunteer(request);
        if (volunteer != null) {
            volunteer.setAssignedRequest(null);
            volunteer.setAvailible(true);
        }
        return saveData();
    }

    /**
     * Writes a printable .txt summary of a trip for the logged in responder.
     *
     * TODO uncomment once txt is merged (its Request still uses Location, so switch it to Address first)
     *
     * @param request  the request being responded to
     * @param shelter  the shelter the party is going to
     * @param fileName the file to write
     * @return true if the file was written
     */
    public boolean exportTripInfo(Request request, Shelter shelter, String fileName) {
        if (currentUser == null || request == null) {
            return false;
        }
        // return request.exportToTxt(fileName, currentUser, shelter);
        return false;
    }

    // ---------------------------------------------------------------- maps

    /**
     * Gets turn by turn driving directions.
     *
     * @param from where the trip starts
     * @param to   where the trip ends
     * @return one direction per step, or null if the map api couldn't route it
     */
    public String[] getDirections(Address from, Address to) {
        if (from == null || to == null) {
            return null;
        }
        try {
            return MapTools.getInstance().getDrivingDirections(from, to);
        } catch (Exception e) {
            return null;
        }
    }

    // ---------------------------------------------------------------- shelters

    /**
     * Gets every shelter.
     *
     * @return the shelters
     */
    public ArrayList<Shelter> getShelters() {
        return new ArrayList<Shelter>(ShelterList.getInstance().getShelterList().values());
    }

    /**
     * Gets a shelter by name.
     *
     * @param name the shelter's name
     * @return the shelter, or null if there isn't one
     */
    public Shelter getShelter(String name) {
        return ShelterList.getInstance().getShelter(name);
    }

    /**
     * Finds the shelters with room for a request's whole party, nearest first
     * in a straight line.
     *
     * TODO filter on pets and medical staff once Shelter stores them
     *
     * @param request the request the party belongs to
     * @return the shelters that fit
     */
    public ArrayList<Shelter> findShelters(Request request) {
        ArrayList<Shelter> matches = new ArrayList<Shelter>();
        if (request == null || request.getAddress() == null) {
            return matches;
        }
        int partySize = getPartySize(request);
        HashMap<UUID, Double> miles = new HashMap<UUID, Double>();
        for (Shelter shelter : getShelters()) {
            if (shelter.availableSpace() >= partySize) {
                matches.add(shelter);
                miles.put(shelter.getId(), distanceBetween(request.getAddress(), shelter.getAddress()));
            }
        }
        // unknown distances (-1) go last
        matches.sort((a, b) -> Double.compare(sortableMiles(miles.get(a.getId())),
                sortableMiles(miles.get(b.getId()))));
        return matches;
    }

    /**
     * Finds the shelter with room for a request's whole party that is the
     * shortest drive from the pickup address.
     *
     * @param request the request the party belongs to
     * @return the closest shelter, or null if none fit or the map api failed
     */
    public Shelter findClosestShelter(Request request) {
        ArrayList<Shelter> matches = findShelters(request);
        if (matches.isEmpty()) {
            return null;
        }
        Address[] addresses = new Address[matches.size()];
        for (int i = 0; i < matches.size(); i++) {
            addresses[i] = matches.get(i).getAddress();
        }
        try {
            Address closest = MapTools.getInstance().findClosestAddress(request.getAddress(), addresses);
            for (Shelter shelter : matches) {
                if (shelter.getAddress() == closest) {
                    return shelter;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Gets the shelter the logged in shelter worker runs.
     *
     * <p>Looks the shelter up in ShelterList so changes land on the copy that
     * gets saved, not the separate copy loaded with the worker.</p>
     *
     * @return the shelter, or null if the current user isn't a shelter worker
     */
    public Shelter getMyShelter() {
        if (!isType(UserType.SHELTER_WORKER)) {
            return null;
        }
        Shelter own = ((ShelterWorker) currentUser).getShelter();
        return own == null ? null : getShelter(own.getName());
    }

    /**
     * Checks a request's party into the logged in shelter worker's shelter.
     *
     * TODO add the party to the shelter's occupant list and record pets once Shelter stores them
     *
     * @param request the request whose party arrived
     * @return true if they were checked in
     */
    public boolean checkIn(Request request) {
        Shelter shelter = getMyShelter();
        if (shelter == null || request == null) {
            return false;
        }
        int partySize = getPartySize(request);
        if (shelter.availableSpace() < partySize) {
            return false;
        }
        shelter.registerArrival(partySize);
        return saveData();
    }

    // ---------------------------------------------------------------- hurricanes

    /**
     * Gets a hurricane by name.
     *
     * @param name the hurricane's name
     * @return the hurricane, or null if there isn't one
     */
    public Hurricane getHurricane(String name) {
        return HurricaneList.getInstance().getHurricane(name);
    }

    /**
     * Reports whether a storm that isn't over is at, has hit, or will reach
     * somewhere within {@value #AFFECTED_MILES} miles of the logged in user.
     *
     * @return true if the current user is affected
     */
    public boolean isAffected() {
        if (currentUser == null || currentUser.getAddress() == null) {
            return false;
        }
        Address home = currentUser.getAddress();
        for (Hurricane hurricane : HurricaneList.getInstance().getHurricaneList().values()) {
            if (hurricane.getStatus() == HurricaneStatus.OVER) {
                continue;
            }
            ArrayList<Address> places = new ArrayList<Address>(hurricane.getImpactArea());
            places.addAll(hurricane.getPredictedPath());
            places.add(hurricane.getCurrentAddress());
            for (Address place : places) {
                double miles = distanceBetween(home, place);
                if (miles >= 0 && miles < AFFECTED_MILES) {
                    return true;
                }
            }
        }
        return false;
    }

    // ---------------------------------------------------------------- saving

    /**
     * Saves users, requests, shelters, and hurricanes.
     *
     * @return true if everything saved
     */
    public boolean saveData() {
        boolean saved = UserList.getInstance().save();
        saved = RequestList.getInstance().save() && saved;
        saved = ShelterList.getInstance().save() && saved;
        saved = HurricaneList.getInstance().save() && saved;
        return saved;
    }

    // ---------------------------------------------------------------- helpers

    /**
     * Switches the logged in user, forgetting distances worked out for the last one.
     */
    private void setCurrentUser(User user) {
        currentUser = user;
        requestDistances.clear();
    }

    /**
     * Reports whether a request needs the kind of help the logged in volunteer specializes in.
     */
    private boolean matchesSpecialty(Request request) {
        return isType(UserType.VOLUNTEER) && request != null
                && ((Volunteer) currentUser).getFieldsOfExpertise().contains(request.getResponderType());
    }

    /**
     * Gets the straight line distance between two addresses, or -1 if the map api can't place them.
     */
    private static double distanceBetween(Address from, Address to) {
        if (from == null || to == null) {
            return -1;
        }
        try {
            return from.getDistanceTo(to);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Puts unknown distances after every known one when sorting.
     */
    private static double sortableMiles(Double miles) {
        return (miles == null || miles < 0) ? Double.MAX_VALUE : miles;
    }

    /**
     * Gets how many people a request is for, from the victim who made it.
     */
    private static int getPartySize(Request request) {
        User user = request.getRequesteeUser();
        return (user instanceof Victim) ? ((Victim) user).getPartySize() : 1;
    }

    /**
     * Reports whether text is missing or only whitespace.
     *
     * @param text the text to check
     * @return true if the text is null or blank
     */
    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
