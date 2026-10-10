package com.model;
import java.util.ArrayList;

/** 
 * Volunteer
 * @author Jason
 */
public class Volunteer extends User {
    private ArrayList<Credential> credentials;
    private ArrayList<ResponseType> fieldsOfExpertise;
    private boolean identityVerified;
    private boolean availible;

    public Volunteer(String firstName,  String lastName, String username, String emailAddress, String password, Address address) {
        super(firstName, lastName, username, emailAddress, password, address);
    }

    public Volunteer(User user) {
        //Turn another user type into a volunteer.
    }

    //getters and setters

    public ArrayList<Credential> getCredentials() {
        return this.credentials;
    }

    public void setCredentials(ArrayList<Credential> credentials) {
        this.credentials = credentials;
    }

    public ArrayList<ResponseType> getFieldsOfExpertise() {
        return this.fieldsOfExpertise;
    }

    public void setFieldsOfExpertise(ArrayList<ResponseType> fieldsOfExpertise) {
        this.fieldsOfExpertise = fieldsOfExpertise;
    }

    public boolean getIdentityVerified() {
        return this.identityVerified;
    }

    public void setIdentityVerified(boolean identityVerified) {
        this.identityVerified = identityVerified;
    }

    public boolean getIsAvailible() {
        return this.availible;
    }

    public void setAvailible(boolean available) {
        //set availibility (for consistancy with UML, has name setAvailibility)
        this.availible = available;
    }

    //other methods

    public void verifyIdentity() {
        //attempt to get verified by an admin
    }

    public void submitCredential(Credential credential) {
        //submit a credential to be added to credentials
    }

    public void offerToHelp() {
        //offer to help. Might be what sets availibility to true
    }

    public void updateRequest(Request request) {
        //updates a request
    }

    public void finishRequest(Request request) {
        //marks a request as done
    }
}
