package com.model;
import java.util.ArrayList;

/** 
 * Volunteer
 * @author Jason
 */
public class Volunteer extends User{
    private ArrayList<Credential> credentials;
    private ArrayList<ResponseType> fieldsOfExpertise;
    private boolean identityVerified;
    private boolean availible;

    public Volunteer(String firstName,  String lastName, String username, String emailAddress, String password, Address address) {
        //TODO
    }

    public Volunteer(User user) {
        //Turn another user type into a volunteer.
    }

    //getters and setters

    public ArrayList<Credential> getCredentials() {
        
    }

    public void setCredentials(ArrayList<Credential> credentials) {

    }

    public ArrayList<ResponseType> getFieldsOfExpertise() {
        
    }

    public void setCredentials(ArrayList<ResponseType> fieldsOfExpertise) {
        
    }

    public boolean getIdentityVerified() {

    }

    public void setIdentityVerified(boolean identityVerified) {

    }

    public boolean getIsAvailible() {

    }

    public void setAvailible(boolean available) {
        //set availibility (for consistancy with UML, has name setAvailibility)
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

    public finishRequest(Request request) {
        //marks a request as done
    }
}
