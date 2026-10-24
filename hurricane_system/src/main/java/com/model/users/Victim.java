package com.model.users;

import com.model.*;
import com.model.location.*;
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
    private String phoneNumber;

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
        this.party = new ArrayList<Person>();
        this.phoneNumber = "";
    }

    public Victim() {
        super();
        this.setType(UserType.VICTIM);
        this.age = -1;
        this.sex = ' ';
        this.description = "No description";
        this.partySize = 1;
        this.createdRequests = new ArrayList<Request>();
        this.party = new ArrayList<Person>();
        this.phoneNumber = "";
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
    public Victim(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address, ArrayList<Alert> recievedAlerts, byte age, char sex,
        String description, byte partySize, ArrayList<Request> createdRequests, ArrayList<Person> party, String phoneNumber) {
        super(id, firstName, lastName, username, emailAddress, password, address, recievedAlerts);
        this.setType(UserType.VICTIM);
        this.age = age;
        this.sex = sex;
        this.description = description;
        this.partySize = partySize;
        this.createdRequests = createdRequests;
        this.party = party;
        this.phoneNumber = phoneNumber;
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
        this.createdRequests = new ArrayList<Request>();
        this.party = new ArrayList<Person>();
        this.phoneNumber = "";
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
     * Gets the victim's party (not including the victim user).
     * @return The victim's party.
     */
    public ArrayList<Person> getParty() {
        return this.party;
    }

    /**
     * Gets the full party, including the victim user (does not edit original party).
     * @return The full party.
     */
    public ArrayList<Person> getFullParty() {
        ArrayList<Person> fullParty = new ArrayList<Person>();
        //make a copy of party
        for (Person p : this.party)
            fullParty.add(p);
        fullParty.addFirst(new Person(firstName + " " + lastName, ((byte) this.getAge()), false, this.description));
        return fullParty;
    }

    /**
     * Sets the victim's party.
     * @param party The victim's party.
     */
    public void setParty(ArrayList<Person> party) {
        this.party = party;
        //Gets new party size (excluding pets)
        int size = 1;
        for (int i=0; i<party.size(); i++) {
            if (!party.get(i).getIsPet()) {
                size++;
            }
        }
        this.partySize = size;
    }

    /**
     * Adds a member to the victim's party.
     * @param person The person to be added.
     */
    public void addPartyMember(Person person) {
        this.party.add(person);
        //Gets new party size (excluding pets)
        int size = 1;
        for (int i=0; i<party.size(); i++) {
            if (!party.get(i).getIsPet()) {
                size++;
            }
        }
        this.partySize = size;
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
     * Gets the phone number.
     * @return The phone number.
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Sets the phone number.
     * @param phoneNumber The phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
     * @param address The request's address.
     * @param description The request's description.
     */
    public void createRequest(byte severity, Address address, String description) {
        Request newRequest = new Request(this.username, severity, address, description);
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
     * Edits the request address.
     * @param request The request to be edited.
     * @param address The new address.
     */
    public void editRequestAddress(Request request, Address address) {
        request.setAddress(address);
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
     * @author jah73cse
     */
    public class Person {
        private String name;
        private byte age;
        private boolean isPet;
        private String description;

        /**
         * Creates a person without an account.
         * @param name The person's name.
         * @param age The person's age.
         * @param isPet Whether or not the person is a pet.
         * @param description A description of the person.
         */
        public Person(String name, byte age, boolean isPet, String description) {
            this.setName(name);
            this.setAge(age);
            this.setIsPet(isPet);
            this.setDescription(description);
        }

        /**
         * Gets the person's name.
         * @return The person's name.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Sets the person's name.
         * @param name The person's name.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets the person's age.
         * @return The person's age.
         */
        public int getAge() {
            return this.age;
        }

        /**
         * Sets the person's age.
         * @param age The person's age.
         */
        public void setAge(byte age) {
            this.age = age;
        }

        /**
         * Gets whether the person is a pet.
         * @return Whether the person is a pet.
         */
        public boolean getIsPet() {
            return this.isPet;
        }

        /**
         * Sets whether the person is a pet.
         * @param isPet Whether the person is a pet.
         */
        public void setIsPet(boolean isPet) {
            this.isPet = isPet;
        }

        /**
         * Gets the description.
         * @return The description.
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * Sets the description.
         * @param description The description.
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * Gets the person as a String.
         * @return The person as a String.
         */
        public String toString() {
            return this.getName()
                + ", age: " + this.getAge()
                + ". " + this.getDescription();
        }

    }
}
