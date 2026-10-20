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

    public Dispatcher(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address, ArrayList<Alert> recievedAlerts, String phoneNumber) {
        super(id, firstName, lastName, username, emailAddress, password, address, recievedAlerts);
        this.setType(UserType.DISPATCHER);
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

    public void respondToRequest(Request request, Volunteer volunteer) {
        //Return if the volunteer is not availible
        if (!volunteer.getIsAvailible())
            return;
        volunteer.setAvailible(false);

        //create an arrayList with the volunteers location
        //this is needed so the User recieveAlert method doesn't filter out the alert
        ArrayList<Location> temp = new ArrayList<Location>();
        temp.add(volunteer.getAddress().getLocation());

        //send notification to volunteer as an alert
        Alert notification = new Alert(temp, "Dispatcher has assigned you to the following request \n"+request, this, Date.from(Instant.now()));
        volunteer.receiveAlert(notification);

        temp = new ArrayList<Location>();
        temp.add(request.getRequestee().getAddress().getLocation());

        //send notification to volunteer as an alert
        notification = new Alert(temp, "Dispatcher has assigned"+volunteer.firstName+" "+volunteer.lastName+"to your request", this, Date.from(Instant.now()));
        request.getRequestee().receiveAlert(notification);
        
    }

    public void requestMoreInfo(Request request, String whatIsNeeded) {
        ArrayList<Location> temp = new ArrayList<Location>();
        temp.add(request.getRequestee().getAddress().getLocation());

        //send notification to volunteer as an alert
        Alert notification = new Alert(temp, whatIsNeeded, this, Date.from(Instant.now()));
        request.getRequestee().receiveAlert(notification);
        
    }

    public void updateRequest(Request request, ResponseStatus status) {
        //updates a request
        request.updateStatus(status);
    }

    public void finishRequest(Request request) {
        //marks a request as done
        request.updateStatus(ResponseStatus.COMPLETE);
    }
    public String toString() {
        return (this.firstName + " " + this.lastName + " " + this.username);
    }
}
