package com.model;

public class ShelterWorker extends User {
private Shelter shelter;

    public ShelterWorker(String firstName, String lastName, String username, String password, String emailAddress, Address address, Shelter shelter) {
        super(firstName, lastName, username, password, emailAddress, address);
        this.shelter = shelter;
        this.setType(UserType.SHELTER_WORKER);
    }
    public ShelterWorker(User user) {
        super(user.firstName, user.lastName, user.username, user.password, user.emailAddress, user.address);
        this.shelter = null;
    }
    public void updateResources(Resource resource, int quantity) {
        // Update the resources available in the shelter
        shelter.updateResource(resource, quantity);
    }
    public void transferResources(Resource resource, int quantity, Shelter shelter) {
        // Transfer resources from the current shelter to another shelter
        shelter.transferResource(resource, quantity, shelter);
    }
}
