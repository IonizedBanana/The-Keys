package com.model.users;

import java.util.UUID;
import com.model.*;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A user without a specific type yet.
 * @author Jason
 */

public abstract class User {
    private UUID id;
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String emailAddress;
    protected String password;
    protected Address address;
    protected ArrayList<Alert> receivedAlerts;
    protected UserType type;

    /**
     * Creates a user.
     * @param id The UUID of the user.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param username The user's username.
     * @param emailAddress The user's email.
     * @param password The user's password.
     * @param address The user's address.
     * @param recivedAlerts The user's received alerts.
     */
    public User(UUID id, String firstName,  String lastName, String username, String emailAddress,
        String password, Address address, ArrayList<Alert> receivedAlerts) {
        this.id = id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setUsername(username);
        this.setEmailAddress(emailAddress);
        this.setPassword(password);
        this.setAddress(address);
        this.setReceivedAlerts(receivedAlerts);
    }

    /**
     * Creates a user, but also generates a random UUID for it.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param username The user's username.
     * @param emailAddress The user's email.
     * @param password The user's password.
     * @param address The user's address.
     */
    public User(String firstName, String lastName, String username, String emailAddress, String password, Address address) {
        //overloaded constructor
        this.id = UUID.randomUUID();
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setUsername(username);
        this.setEmailAddress(emailAddress);
        this.setPassword(password);
        this.setAddress(address);
    }

    //getters and setters

    /**
     * Gets the UUID.
     * @return The user's UUID.
     */
    public UUID getId() {
      return this.id;
    }

    /**
     * Gets the first name.
     * @return The user's first name.
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Sets the first name.
     * @param firstName The new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     * @return The last name.
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Sets the last name.
     * @param lastName The new first name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the username.
     * @return The new username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the username.
     * @param userName The new username.
     */
    public void setUsername(String userName) {
        //Check if the username has already been taken
        //TODO Wait for UserList to be added
        /*
        HashMap<String, User> list = UserList.getInstance();
        if(list.containsKey(userName))
            return;
        */
        this.username = userName;
    }

    /**
     * Gets the email.
     * @return The user's email.
     */
    public String getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * Sets the email (cannot be blank).
     * @param emailAddress The user's email.
     */
    public void setEmailAddress(String emailAddress) {
        if (emailAddress.isBlank())
            return;
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the password.
     * @return The user's password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets a new password only if it is at least 4 characters long.
     * @param password The new password.
     */
    public void setPassword(String password) {
        if (password.length() < 4)
            return;
        this.password = password;
    }

    /**
     * Gets the address.
     * @return The user's address.
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * Sets the user's address.
     * @param address The user's new address.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Get's the user's type.
     * @return The user's type.
     */
    public UserType getType() {
        return this.type;
    }

    /**
     * Sets the user's type.
     * @param type The new type.
     */
    public void setType(UserType type) {
        this.type = type;
    }

    /**
     * Gets the user's alerts.
     * @return The user's alerts.
     */
    public ArrayList<Alert> getReceivedAlerts() {
        return this.receivedAlerts;
    }

    /**
     * Sets the user's alerts.
     * @param receivedAlerts The new alerts.
     */
    public void setReceivedAlerts(ArrayList<Alert> receivedAlerts) {
        this.receivedAlerts = receivedAlerts;
    }

    //other methods

    /**
     * Returns whether or not the username and password provided match the user.
     * @param username The username provided as an input.
     * @param password The password provided as an input.
     * @return Whether the username and password provided match the user.
     */
    public boolean isMatch(String username, String password) {
        if (username.equalsIgnoreCase(this.getUsername()) && password.equals(this.password)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds an alert to the user's list of received alerts
     * @param alert The alert to be received.
     */
    public void receiveAlert(Alert alert) {
        if(alert.isRelevantTo(this)) {
            receivedAlerts.add(alert);
        }
    }
    
    /**
     * Donate resources to a shelter.
     * @param donation The resource to be donated.
     * @param shelter The shelter that will recieve the donation.
     */
    public void donate(Resource donation, Shelter shelter) {
        shelter.addResource(donation);
    }
}
