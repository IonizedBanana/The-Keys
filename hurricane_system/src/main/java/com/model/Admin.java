package com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Admin
 * @author Jason
 */
public class Admin extends User {
    private String phoneNumber;
    
    public Admin(String firstName,  String lastName, String username, String emailAddress, String password, Address address, String phoneNumber) {
        super(firstName, lastName, username, emailAddress, password, address);
        this.setType(UserType.ADMIN);
        this.setPhoneNumber(phoneNumber);
    }

    public Admin(User user, String phoneNumber) {
        super(user.firstName, user.lastName, user.username, user.emailAddress, user.password, user.address);
        this.setType(UserType.ADMIN);
        this.setPhoneNumber(phoneNumber);
    }

    public Admin(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address, String phoneNumber) {
        super(id, firstName, lastName, username, emailAddress, password, address);
        this.setType(UserType.ADMIN);
        this.setPhoneNumber(phoneNumber);
    }

    //getters and setters

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //other methods

    public void addHurricane(Hurricane hurricane) {
        //TODO Wait for HurricaneList to be added
        /*
        HashMap<String, Hurricane> list = HurricaneList.getInstance();
        list.put(hurricane.getName(), hurricane);
        */
    }

    public void removeHurricane(Hurricane hurricane) {
        //TODO Wait for HurricaneList to be added
        /*
        HashMap<String, Hurricane> list = HurricaneList.getInstance();
        list.remove(hurricane.getName());
        */

    }

    public void updateHurricaneLocation(Hurricane hurricane, Location location) {
        hurricane.updateLocation(location);
    }

    public void updateHurricanePath(Hurricane hurricane, ArrayList<Location> path) {
        hurricane.updatePredictedPath(path);
    }

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

    public void verifyCredential(Credential credential) {
        credential.setVerified(true);
    }
}
