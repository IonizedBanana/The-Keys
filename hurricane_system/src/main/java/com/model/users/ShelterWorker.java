package com.model.users;

import com.model.*;
import com.model.location.*;
import java.util.UUID;
import java.util.ArrayList;
/**
 * 
 * ShelterWorker 
 * @author LoganH627
 */
public class ShelterWorker extends User {
private Shelter shelter;

    public ShelterWorker(UUID id, String firstName, String lastName, String username, String password, String emailAddress, Address address, ArrayList<Alert> recievedAlerts, Shelter shelter) {
        super(id,firstName, lastName, username, password, emailAddress, address, recievedAlerts);
        this.shelter = shelter;
        this.setType(UserType.SHELTER_WORKER);
    }
    public ShelterWorker(String firstName, String lastName, String username, String password, String emailAddress, Address address, Shelter shelter) {
        super(firstName, lastName, username, password, emailAddress, address);
        this.shelter = shelter;
        this.setType(UserType.SHELTER_WORKER);
    }
    public ShelterWorker(User user) {
        super(user.firstName, user.lastName, user.username, user.password, user.emailAddress, user.address);
        this.shelter = null;
        this.setType(UserType.SHELTER_WORKER);
    }
    public void updateResources(Resource resource, int quantity) {
        // Update the resources available in the shelter
        shelter.updateResource(resource, quantity);
    }
    public void transferResources(Resource resource, Shelter shelter) {
        // Transfer resources from the current shelter to another shelter
        this.shelter.updateResource(resource, -(resource.getQuantity()));
        shelter.recieveResource(resource);
    }
    public Shelter getShelter() {
        return this.shelter;
    }
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
    
    public String toString() {
        return (this.firstName + " " + this.lastName + " " + this.username);
    }
}
