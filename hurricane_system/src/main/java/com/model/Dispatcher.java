package com.model;

import java.util.UUID;

/**
 * Dispatcher
 * @author Jason
 */
public class Dispatcher extends User{
    private String phoneNumber;

    public Dispatcher(String firstName,  String lastName, String username, String emailAddress, String password, Address address, String phoneNumber) {
        super(firstName, lastName, username, emailAddress, password, address);
        this.setType(UserType.DISPATCHER);
        this.setPhoneNumber(phoneNumber);
    }

    public Dispatcher(User user, String phoneNumber) {
        super(user.firstName, user.lastName, user.username, user.emailAddress, user.password, user.address);
        this.setType(UserType.DISPATCHER);
        this.setPhoneNumber(phoneNumber);
    }

    public Dispatcher(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address, String phoneNumber) {
        super(id, firstName, lastName, username, emailAddress, password, address);
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

    public void respondToRequest(Request request) {
        //TODO
    }

    public void requestMoreInfo() {
        //TODO
    }

    public void finishRequest() {
        //TODO
    }

    public void updateRequest() {
        //TODO
    }
}
