package com.model.users;

import com.model.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * A Dispatcher that assigns people to requests.
 * @author Jason
 */
public class Dispatcher extends User {
    private String phoneNumber;

    /**
     * Creates a dispatcher.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param username The user's username.
     * @param emailAddress The user's email.
     * @param password The user's password.
     * @param address The user's address.
     * @param phoneNumber The user's phone number.
     */
    public Dispatcher(String firstName,  String lastName, String username, String emailAddress, String password, Address address, String phoneNumber) {
        super(firstName, lastName, username, emailAddress, password, address);
        this.setType(UserType.DISPATCHER);
        this.setPhoneNumber(phoneNumber);
    }

    /**
     * Converts a user to a dispatcher.
     * @param user The user to be converted.
     * @param phoneNumber The user's phone number.
     */
    public Dispatcher(User user, String phoneNumber) {
        super(user.firstName, user.lastName, user.username, user.emailAddress, user.password, user.address);
        this.setType(UserType.DISPATCHER);
        this.setPhoneNumber(phoneNumber);
    }

    /**
     * Creates an dispatcher with all information (intended for loading from UserList).
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
    public Dispatcher(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address, ArrayList<Alert> recievedAlerts, String phoneNumber) {
        super(id, firstName, lastName, username, emailAddress, password, address, recievedAlerts);
        this.setType(UserType.DISPATCHER);
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
        this.phoneNumber = phoneNumber;
    }

    //other methods

    /**
     * Assign a volunteer to respond to a request.
     * @param request The request to be responded to.
     * @param volunteer The volunteer to be assigned.
     */
    public void respondToRequest(Request request, Volunteer volunteer) {
        //Return if the volunteer is not availible
        if (!volunteer.getIsAvailible())
            return;
        //Set the volunteer as not availible so it can't be assigned to somewhere else.
        volunteer.setAvailible(false);
        //Mark the request as in progress
        request.updateStatus(ResponseStatus.IN_PROGRESS);

        //create an arrayList with the volunteers location
        //this is needed so the User recieveAlert method doesn't filter out the alert
        ArrayList<Location> temp = new ArrayList<Location>();
        temp.add(volunteer.getAddress().getLocation());

        //send notification to volunteer as an alert
        Alert notification = new Alert(temp, "Dispatcher has assigned you to the following request \n"+request, this, Date.from(Instant.now()));
        volunteer.receiveAlert(notification);

        //Make an arrayList with the victim's location.
        temp = new ArrayList<Location>();
        temp.add(request.getRequestee().getAddress().getLocation());

        //send notification to victim as an alert
        notification = new Alert(temp, "Dispatcher has assigned"+volunteer.firstName+" "+volunteer.lastName+"to your request", this, Date.from(Instant.now()));
        request.getRequestee().receiveAlert(notification);
        
    }

    /**
     * Send an alert to a user, prompting them to edit a request.
     * @param request The request.
     * @param whatIsNeeded The description of the alert, describing what needs to be edited.
     */
    public void requestMoreInfo(Request request, String whatIsNeeded) {
        ArrayList<Location> temp = new ArrayList<Location>();
        temp.add(request.getRequestee().getAddress().getLocation());

        //send notification to victim as an alert
        Alert notification = new Alert(temp, whatIsNeeded, this, Date.from(Instant.now()));
        request.getRequestee().receiveAlert(notification);
        
    }

    /**
     * Updates a request's status.
     * @param request The request.
     * @param status The new status.
     */
    public void updateRequest(Request request, ResponseStatus status) {
        request.updateStatus(status);
    }

    /**
     * Marks a request as done.
     * @param request The request.
     */
    public void finishRequest(Request request) {
        //marks a request as done
        request.updateStatus(ResponseStatus.COMPLETE);
    }
    public String toString() {
        return (this.firstName + " " + this.lastName + " " + this.username);
    }
}
