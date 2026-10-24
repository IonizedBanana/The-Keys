package com.model.users;

import com.model.*;
import com.model.location.*;
import java.util.ArrayList;
import java.util.UUID;

/** 
 * A volunteer who can offer to help respond to requests.
 * @author Jason
 */
public class Volunteer extends User {
    private ArrayList<Credential> credentials;
    private ArrayList<ResponseType> fieldsOfExpertise;
    private boolean identityVerified;
    private boolean availible;
    private Request assignedRequest;

    /**
     * Create a volunteer.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param username The user's user name.
     * @param emailAddress The user's email address.
     * @param password The user's password.
     * @param address The user's address.
     */
    public Volunteer(String firstName,  String lastName, String username, String emailAddress, String password, Address address) {
        super(firstName, lastName, username, emailAddress, password, address);
        this.setType(UserType.VOLUNTEER);
        this.credentials = new ArrayList<Credential>();
        this.fieldsOfExpertise = new ArrayList<ResponseType>();
        this.identityVerified = false;
        this.availible = false;
        this.assignedRequest = null;
    }

    /**
     * Converts a different type of user into a volunteer.
     * @param user The user to be converted into a volunteer.
     */
    public Volunteer(User user) {
        //Turn another user type into a volunteer.
        super(user.firstName, user.lastName, user.username, user.emailAddress, user.password, user.address);
        this.setType(UserType.VOLUNTEER);
        this.credentials = new ArrayList<Credential>();
        this.fieldsOfExpertise = new ArrayList<ResponseType>();
        this.identityVerified = false;
        this.availible = false;
        this.assignedRequest = null;

    }

    /**
     * Create a volunteer with all of the attributes provided as parameters (for importing from UserList).
     * @param id The user's UUID.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param username The user's username.
     * @param emailAddress The user's email address.
     * @param password The user's password.
     * @param address The user's address.
     * @param receivedAlerts The alerts that the user has recieved.
     * @param credentials The credentials that a user has.
     * @param fieldsOfExpertise The types of requests a volunteer is qualified to respond to.
     * @param identityVerified Whether or not the user's identity is verified.
     * @param availible Whether or not the user is availible to be assigned to a request.
     */
    public Volunteer(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address, ArrayList<Alert> receivedAlerts, ArrayList<Credential> credentials,
        ArrayList<ResponseType> fieldsOfExpertise, boolean identityVerified, boolean availible, Request assignedRequest) {
        super(id, firstName, lastName, username, emailAddress, password, address, receivedAlerts);
        this.setType(UserType.VOLUNTEER);
        this.credentials = credentials;
        this.fieldsOfExpertise = fieldsOfExpertise;
        this.identityVerified = identityVerified;
        this.availible = availible;
        this.assignedRequest = assignedRequest;
    }

    //getters and setters

    /**
     * Gets the user's credential's.
     * @return The user's credentials.
     */
    public ArrayList<Credential> getCredentials() {
        return this.credentials;
    }

    /**
     * Sets the user's credentials.
     * @param credentials The user's credentials.
     */
    public void setCredentials(ArrayList<Credential> credentials) {
        this.credentials = credentials;
    }

    /**
     * Gets the user's fields of expertise.
     * @return The user's fields of expertise.
     */
    public ArrayList<ResponseType> getFieldsOfExpertise() {
        return this.fieldsOfExpertise;
    }

    /**
     * Sets the user's fields of expertise.
     * @param fieldsOfExpertise The user's fields of expertise.
     */
    public void setFieldsOfExpertise(ArrayList<ResponseType> fieldsOfExpertise) {
        this.fieldsOfExpertise = fieldsOfExpertise;
    }

    /**
     * Gets whether or not the user's identity is verified
     * @return Returns true only if the user's identity is verified.
     */
    public boolean getIdentityVerified() {
        return this.identityVerified;
    }

    /**
     * Sets whether or not the user's identity is verified.
     * @param identityVerified
     */
    public void setIdentityVerified(boolean identityVerified) {
        this.identityVerified = identityVerified;
    }

    /**
     * Gets whether or not the user is able to be assigned to respond to a request.
     * @return Returns true only if the user is able to respond to a request.
     */
    public boolean getIsAvailible() {
        return this.availible;
    }

    /**
     * Set whether or not the user can be assigned to a request.
     * @param available Whether the user will be marked as availible.
     */
    public void setAvailible(boolean available) {
        //set availibility (for consistancy with UML, has name setAvailibility)
        this.availible = available;
    }

    public Request getAssignedRequest() {
        return assignedRequest;
    }

    public void setAssignedRequest(Request request) {
        this.assignedRequest = request;
    }

    //other methods

    /**
     * Marks the user as having its identity verified (should only be called by an admin).
     */
    public void verifyIdentity() {
        this.identityVerified = true;
    }

    /**
     * Adds a credential to the user's credential list (does not mean that the credential is verified).
     * @param credential The credential to be added.
     */
    public void submitCredential(Credential credential) {
        this.credentials.add(credential);
    }

    /**
     * Sets the user's availibility to true (meaning they are ready to help).
     */
    public void offerToHelp() {
        this.setAvailible(true);
    }

    /**
     * Updates the status of a request.
     * @param request The request to be updated.
     * @param status The new status.
     */
    public void updateRequest(Request request, ResponseStatus status) {
        if (status == ResponseStatus.COMPLETE) {
            System.out.println("Volunteers do not have permission to mark requests as complete.");
            return;
        }
        request.updateStatus(status);
    }

    /**
     * Marks a request as done.
     * @param request The request to be updated.
     */
    public void finishRequest(Request request) {
        System.out.println("Volunteers do not have permission to mark requests as complete.");
        return;
        //marks a request as done
        //request.updateStatus(ResponseStatus.COMPLETE);
        //this.assignedRequest = null;
    }
    public String toString() {
        return (this.firstName + " " + this.lastName + " " + this.username);
    }
}
