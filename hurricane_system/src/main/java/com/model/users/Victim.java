package com.model.users;

import com.model.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A victim of hurricanes that can request for help.
 * @author Jason
 */
public class Victim extends User {
    public int age;
    private char sex;
    private String description;
    private int partySize;
    private ArrayList<Request> createdRequests;
    // New stuff
    private ArrayList<Person> party;

    /**
     * Creates a victim with only the required information (victims don't provide all information immediately; They can add more info later).
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param username The user's username.
     * @param emailAddress The user's email.
     * @param password The user's password.
     * @param address The user's address.
     */
    public Victim(String firstName, String lastName, String username, String emailAddress, String password, Address address) {
        super(firstName, lastName, username, emailAddress, password, address);
        this.setType(UserType.VICTIM);
        this.age = -1;
        this.sex = ' ';
        this.description = "No description";
        this.partySize = 1;
        this.createdRequests = new ArrayList<Request>();
    }

    /**
     * Creates a victim (this one is for loading from UserList).
     * @param id The user's UUID.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param username The user's username.
     * @param emailAddress The user's email.
     * @param password The user's password.
     * @param address The user's address.
     * @param recievedAlerts The user's alerts.
     * @param age The user's age.
     * @param sex The user's sex.
     * @param description The user's description.
     * @param partySize The user's party size.
     * @param createdRequests The user's requests.
     */
    public Victim(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address, ArrayList<Alert> recievedAlerts, byte age, char sex, String description, byte partySize, ArrayList<Request> createdRequests) {
        super(id, firstName, lastName, username, emailAddress, password, address, recievedAlerts);
        this.setType(UserType.VICTIM);
        this.age = age;
        this.sex = sex;
        this.description = description;
        this.partySize = partySize;
        this.createdRequests = createdRequests;
    }

    /**
     * Converts a different type of user to a victim.
     * @param user The user.
     */
    public Victim(User user) {
        super(user.firstName, user.lastName, user.username, user.emailAddress, user.password, user.address);
        this.setType(UserType.VICTIM);
        this.age = -1;
        this.sex = ' ';
        this.description = "No description";
        this.partySize = 1;
    }

    /**
     * Gets the user's age.
     * @return The age.
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Sets the user's age.
     * @param age The age.
     */
    public void setAge(int age) {
        if(age >= 0) {
            this.age = age;
        }
    }

    /**
     * Gets the user's sex.
     * @return
     */
    public char getSex() {
        return this.sex;
    }

    /**
     * Sets the user's sex.
     * @param sex The user's sex.
     */
    public void setSex(char sex) {
        this.sex = sex;
    }

    /**
     * Gets the user's description.
     * @return The description.
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Sets the user's description.
     * @param description The description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the user's party size.
     * @return The party size.
     */
    public int getPartySize() {
        return this.partySize;
    }

    /**
     * Sets the user's party size.
     * @param partySize The user's party size (must be at least 1).
     */
    public void setPartySize(int partySize) {
        if (partySize >= 1) {
            this.partySize = partySize;
        }
    }

    /**
     * Returns the user's info.
     * @return The user's info that is unique to victims.
     */
    public String getInfo() {
        //A string of the attributes of victim (ONLY age, sex, description)
        return this.getAge() + "\n" + this.getSex() + "\n" + this.getDescription();
    }

    /**
     * Notifies a shelter about the user going to it.
     * @param shelter The shelter to be notified.
     * @param partySize The party size.
     */
    public void notifyShelter(Shelter shelter, int partySize) {
        shelter.notify(partySize);
    }

    /**
     * Creates a new request.
     * @param severity The request's severity.
     * @param location The request's location.
     * @param description The request's description.
     */
    public void createRequest(byte severity, Location location, String description) {
        Request newRequest = new Request(this.username, severity, location, description);
        this.createdRequests.add(newRequest);
    }

    /**
     * Edits the request severity.
     * @param request The request to be edited.
     * @param severity The new severity.
     */
    public void editRequestSeverity(Request request, byte severity) {
        request.setSeverity(severity);
    }

    /**
     * Edits the request location.
     * @param request The request to be edited.
     * @param location The new location.
     */
    public void editRequestLocation(Request request, Location location) {
        request.setLocation(location);
    }

    /**
     * Edits the request description.
     * @param request The request to be edited.
     * @param description The new description.
     */
    public void editRequestDescription(Request request, String description) {
        request.setDescription(description);
    }
    
    public String toString() {
        return (this.firstName + " " + this.lastName + " " + this.username);
    }

    /**
     * Gets the created requests.
     * @return The created requests.
     */
    public ArrayList<Request> getCreatedRequests() {
        return createdRequests;
    }

    public String getPerson(int index) {
        return this.party.get(index).getName()
        + ", age: " + this.party.get(index).getAge()
        + ". " + this.party.get(index).getDescription();
    }

    /**
     * A member of a victim's party that does not have an account.
     */
    public class Person {
        private String name;
        private int age;
        private boolean isPet;
        private String description;
        public Person(String name, int age, boolean isPet, String description) {
            this.setName(name);
            this.setAge(age);
            this.setIsPet(isPet);
            this.setDescription(description);
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return this.age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean getIsPet() {
            return this.isPet;
        }

        public void setIsPet(boolean isPet) {
            this.isPet = isPet;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String toString() {
            return this.getName()
                + ", age: " + this.getAge()
                + ". " + this.getDescription();
        }

    }
}
