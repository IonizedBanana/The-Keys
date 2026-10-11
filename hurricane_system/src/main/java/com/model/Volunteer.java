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
        this.credentials = new ArrayList<Credential>();
        this.fieldsOfExpertise = new ArrayList<ResponseType>();
        this.identityVerified = false;
        this.availible = false;
    }

    public Volunteer(User user) {
        //Turn another user type into a volunteer.
        super(user.firstName, user.lastName, user.username, user.emailAddress, user.password, user.address);
        this.credentials = new ArrayList<Credential>();
        this.fieldsOfExpertise = new ArrayList<ResponseType>();
        this.identityVerified = false;
        this.availible = false;

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
        //attempt to get verified by an admin (should only be called by an admin)
        this.identityVerified = true;
    }

    public void submitCredential(Credential credential) {
        //submit a credential to be added to credentials
        this.credentials.add(credential);
    }

    public void offerToHelp() {
        //offer to help. Might be what sets availibility to true
        this.setAvailible(availible);
    }

    public void updateRequest(Request request, ResponseStatus status) {
        //updates a request
        request.updateStatus(status);
    }

    public void finishRequest(Request request) {
        //marks a request as done
        request.updateStatus(ResponseStatus.COMPLETED);
    }
}
