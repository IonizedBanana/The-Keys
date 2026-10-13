package com.model.users;

import com.model.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Victim
 * @author Jason
 */
public class Victim extends User {
    public int age;
    private char sex;
    private String description;
    private int partySize;
    private ArrayList<Request> createdRequests;

    //Victims don't provide all information immediately. They can add more info later.
    public Victim(String firstName, String lastName, String username, String emailAddress, String password, Address address) {
        super(firstName, lastName, username, emailAddress, password, address);
        this.setType(UserType.VICTIM);
        this.age = -1;
        this.sex = ' ';
        this.description = "No description";
        this.partySize = 1;
        this.createdRequests = new ArrayList<Request>();
    }

    public Victim(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address, ArrayList<Alert> recievedAlerts, byte age, char sex, String description, byte partySize, ArrayList<Request> createdRequests) {
        super(id, firstName, lastName, username, emailAddress, password, address, recievedAlerts);
        this.setType(UserType.VICTIM);
        this.age = age;
        this.sex = sex;
        this.description = description;
        this.partySize = partySize;
        this.createdRequests = createdRequests;
    }

    public Victim(User user) {
        super(user.firstName, user.lastName, user.username, user.emailAddress, user.password, user.address);
        this.setType(UserType.VICTIM);
        this.age = -1;
        this.sex = ' ';
        this.description = "No description";
        this.partySize = 1;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        if(age >= 0) {
            this.age = age;
        }
    }

    public char getSex() {
        return this.sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public int getPartySize() {
        return this.partySize;
    }

    public void setPartySize(int partySize) {
        if (partySize >= 1) {
            this.partySize = partySize;
        }
    }

    public String getInfo() {
        //A string of the attributes of victim (ONLY age, sex, description)
        return this.getAge() + "\n" + this.getSex() + "\n" + this.getDescription();
    }

    public void notifyShelter(Shelter shelter, int partySize) {
        shelter.notify(partySize);
    }

    // Added parameters to create a request
    public void createRequest(byte severity, Location location, String description) {
        Request newRequest = new Request(this, severity, location, description);
        this.createdRequests.add(newRequest);
    }

    /* 
    Removed in favor of making separate edit methods for each attribute of the request (That the victim can control)
    public void editRequest() {
        //TODO
    }
    */

    public void editRequestSeverity(Request request, byte severity) {
        request.setSeverity(severity);
    }

    public void editRequestLocation(Request request, Location location) {
        request.setLocation(location);
    }

    public void editRequestDescription(Request request, String description) {
        request.setDescription(description);
    }
}
