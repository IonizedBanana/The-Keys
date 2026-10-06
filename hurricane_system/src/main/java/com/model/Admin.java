package com.model;

import java.util.ArrayList;

public class Admin extends User{
    private String phoneNumber;
    
    public Admin(String firstName,  String lastName, String username, String emailAddress, String password, Address address, String phoneNumber) {
        //TODO
    }

    public Admin(User user, String phoneNumber) {
        //TODO
    }

    //getters and setters

    public String getPhoneNumber() {
        //TODO
    }

    public void setPhoneNumbers() {
        //TODO
    }

    //other methods

    public void addHurricane(Hurricane hurricane) {
        //TODO
    }

    public void removeHurricane(Hurricane hurricane) {
        //TODO
    }

    public void updateHurricaneLocation(Hurricane hurricane, Location location) {
        //TODO
    }

    public void updateHurricanePath(Hurricane hurricane, ArrayList<Location> path) {
        //TODO
    }

    public void sendAlert(Alert alert) {
        //TODO
    }

    public void verifyCredential(Credential credential) {
        //TODO
    }
}
