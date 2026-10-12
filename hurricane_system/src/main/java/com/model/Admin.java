package com.model;

import java.util.ArrayList;

/**
 * Admin
 * @author Jason
 */
public class Admin extends User {
    private String phoneNumber;
    
    public Admin(String firstName,  String lastName, String username, String emailAddress, String password, Address address, String phoneNumber) {
        super(firstName, lastName, username, emailAddress, password, address);
        this.setPhoneNumber(phoneNumber);
    }

    public Admin(User user, String phoneNumber) {
        super(user.firstName, user.lastName, user.username, user.emailAddress, user.password, user.address);
        this.setPhoneNumber(phoneNumber);
    }

    //getters and setters

    public String getPhoneNumber() {
        //TODO
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //other methods

    public void addHurricane(Hurricane hurricane) {
        //TODO
        //Add to HurricaneList?
    }

    public void removeHurricane(Hurricane hurricane) {
        //TODO
    }

    public void updateHurricaneLocation(Hurricane hurricane, Location location) {
        hurricane.updateLocation(location);
    }

    public void updateHurricanePath(Hurricane hurricane, ArrayList<Location> path) {
        hurricane.updatePredictedPath(path);
    }

    public void sendAlert(Alert alert) {
        //TODO
        //Get all users, and run user.recieveAlert(alert);
    }

    public void verifyCredential(Credential credential) {
        credential.setVerified(true);
    }
}
