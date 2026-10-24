package com.model.users;

import com.model.*;
import com.model.location.*;
import java.util.UUID;
import java.util.ArrayList;
/**
 * 
 * ShelterWorker is a class that represents a shelter worker in the hurricane response system. It extends the User class and has a reference to a Shelter object.
 * @author LoganH627
 */
public class ShelterWorker extends User {
private Shelter shelter;
/**
 * ShelterWorker is a constructor that takes in parameters for the shelter worker's ID, first name, last name, username, password, email address, address, received alerts, and shelter. It calls the superclass constructor to initialize the user fields and sets the shelter field to the provided shelter. It also sets the user type to SHELTER_WORKER.
 * @param id the unique identifier for the shelter worker
 * @param firstName the first name of the shelter worker
 * @param lastName the last name of the shelter worker
 * @param username the username of the shelter worker
 * @param password the password of the shelter worker
 * @param emailAddress the email address of the shelter worker
 * @param address the address of the shelter worker
 * @param recievedAlerts the list of alerts received by the shelter worker
 * @param shelter the shelter associated with the shelter worker
 */
    public ShelterWorker(UUID id, String firstName, String lastName, String username, String password, String emailAddress, Address address, ArrayList<Alert> recievedAlerts, Shelter shelter) {
        super(id,firstName, lastName, username, emailAddress, password, address, recievedAlerts);
        this.shelter = shelter;
        this.setType(UserType.SHELTER_WORKER);
    }
    /**
     * ShelterWorker is a constructor that takes in parameters for the shelter worker's first name, last name, username, password, email address, address, and shelter. It calls the superclass constructor to initialize the user fields and sets the shelter field to the provided shelter. It also sets the user type to SHELTER_WORKER.
     * @param firstName the first name of the shelter worker
     * @param lastName the last name of the shelter worker
     * @param username the username of the shelter worker
     * @param password the password of the shelter worker
     * @param emailAddress the email address of the shelter worker
     * @param address the address of the shelter worker
     * @param shelter the shelter associated with the shelter worker
     */
    public ShelterWorker(String firstName, String lastName, String username, String password, String emailAddress, Address address, Shelter shelter) {
        super(firstName, lastName, username, emailAddress, password, address);
        this.shelter = shelter;
        this.setType(UserType.SHELTER_WORKER);
    }
    /**
     * ShelterWorker is a constructor that takes in a User object and initializes the ShelterWorker object with the user's information. It also sets the shelter field to null and sets the user type to SHELTER_WORKER.
     * @param user the User object containing the shelter worker's information
     */
    public ShelterWorker(User user) {
        super(user.firstName, user.lastName, user.username, user.emailAddress, user.password, user.address);
        this.shelter = null;
        this.setType(UserType.SHELTER_WORKER);
    }
    /**
     * Updates the resources available in the shelter.
     * @param resource the resource to be updated
     * @param quantity the new quantity of the resource
     */
    public void updateResources(Resource resource, int quantity) {
        // Update the resources available in the shelter
        shelter.updateResource(resource, quantity);
    }
    /**
     * Transfers resources from the current shelter to another shelter.
     * @param resource the resource to be transferred
     * @param shelter the shelter to which the resource will be transferred
     */
    public void transferResources(Resource resource, Shelter shelter) {
        // Transfer resources from the current shelter to another shelter
        this.shelter.updateResource(resource, -(resource.getQuantity()));
        shelter.recieveResource(resource);
    }
    /**
     * Returns the shelter associated with the shelter worker.
     * @return the shelter associated with the shelter worker
     */
    public Shelter getShelter() {
        return this.shelter;
    }
    /**
     * Sets the shelter associated with the shelter worker.
     * @param shelter the new shelter to be associated with the shelter worker
     */
    public void setShelter(Shelter shelter) {
        if(shelter != null) {
            this.shelter = shelter;
        }
        else {
            System.out.println("Invalid shelter.");
        }
    }

    /**
     * Marks a request as done.
     * @param request The request to be updated.
     */
    public void finishRequest(Request request) {
        //marks a request as done
        request.updateStatus(ResponseStatus.COMPLETE);
    }
    /**
     * Returns a string representation of the shelter worker.
     * @return a string representation of the shelter worker
     */
    public String toString() {
        return (this.firstName + " " + this.lastName + " " + this.username);
    }
}
