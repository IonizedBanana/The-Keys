package com.model.users;

import com.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * An admin that manages the app.
 * @author Jason
 */
public class Admin extends User {
    private String phoneNumber;
    
    /**
     * Creates an admin.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param username The user's username.
     * @param emailAddress The user's email.
     * @param password The user's password.
     * @param address The user's address.
     * @param phoneNumber The user's phone number.
     */
    public Admin(String firstName,  String lastName, String username, String emailAddress, String password, Address address, String phoneNumber) {
        super(firstName, lastName, username, emailAddress, password, address);
        this.setType(UserType.ADMIN);
        this.setPhoneNumber(phoneNumber);
    }

    /**
     * Converts a user to an admin.
     * @param user The user to be converted.
     * @param phoneNumber The user's phone number.
     */
    public Admin(User user, String phoneNumber) {
        super(user.firstName, user.lastName, user.username, user.emailAddress, user.password, user.address);
        this.setType(UserType.ADMIN);
        this.setPhoneNumber(phoneNumber);
    }

    /**
     * Creates an admin with all information (intended for loading from UserList).
     * @param id The user's UUID.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param username The user's username.
     * @param emailAddress The user's email.
     * @param password The user's password.
     * @param address The user's address.
     * @param recievedAlerts The user's recieved alerts.
     * @param phoneNumber The user's phone number.
     */
    public Admin(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address, ArrayList<Alert> recievedAlerts, String phoneNumber) {
        super(id, firstName, lastName, username, emailAddress, password, address, recievedAlerts);
        this.setType(UserType.ADMIN);
        this.setPhoneNumber(phoneNumber);
    }

    //getters and setters

    /**
     * Gets the user's phone number.
     * @return The phone number.
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Sets the user's phone number (cannot be blank).
     * @param phoneNumber The phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.isBlank())
            return;
        this.phoneNumber = phoneNumber;
    }

    //other methods

    /**
     * Adds a hurricane from the hurricane list
     * @param hurricane The hurricane to be added.
     */
    public void addHurricane(Hurricane hurricane) {
        //TODO Wait for HurricaneList to be added
        /*
        HashMap<String, Hurricane> list = HurricaneList.getInstance();
        list.put(hurricane.getName(), hurricane);
        */
    }

    /**
     * Removes a hurricane from the hurricane list
     * @param hurricane The hurricane to be removed.
     */
    public void removeHurricane(Hurricane hurricane) {
        //TODO Wait for HurricaneList to be added
        /*
        HashMap<String, Hurricane> list = HurricaneList.getInstance();
        list.remove(hurricane.getName());
        */

    }

    /**
     * Updates a hurricane's location.
     * @param hurricane The hurricane.
     * @param location The new location.
     */
    public void updateHurricaneLocation(Hurricane hurricane, Location location) {
        hurricane.updateLocation(location);
    }

    /**
     * Updates a hurricane's path.
     * @param hurricane The hurricane.
     * @param path The new path.
     */
    public void updateHurricanePath(Hurricane hurricane, ArrayList<Location> path) {
        hurricane.updatePredictedPath(path);
    }

    /**
     * Sends an alert to all relevent users.
     * @param alert The alert to be sent.
     */
    public void sendAlert(Alert alert) {
        //TODO
        // Wait for UserList to be added.
        /*
        HashMap<String, User> list = userList.getInstance();
        for (String key : list.keySet()) {
            list.get(key).receiveAlert(alert);
        }
        */

        
    }

    /**
     * Set a credential as verified.
     * @param credential The credential to be verified.
     */
    public void verifyCredential(Credential credential) {
        credential.setVerified(true);
    }
}
