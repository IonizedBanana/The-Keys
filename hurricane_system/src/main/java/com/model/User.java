package com.model;

import java.util.UUID;

import java.util.ArrayList;


/**
 * User
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
    protected ArrayList<Alert> recievedAlerts;
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
     */
    public User(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address) {
        this.id = id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setUsername(username);
        this.setEmailAddress(emailAddress);
        this.setPassword(password);
        this.setAddress(address);
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

    public ArrayList<Alert> getRecievedAlerts() {
      return recievedAlerts;
    }

    public UUID getId() {
      return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public UserType getType() {
        return this.type;
    }

    public void setType(UserType type) {
        this.type = type;
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
     * Adds an alert to the user's list of recieved alerts
     * @param alert The alert to be recieved.
     */
    public void receiveAlert(Alert alert) {
        if(alert.isRelevantTo(this)) {
            recievedAlerts.add(alert);
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
