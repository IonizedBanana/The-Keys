import java.util.UUID;

import java.util.ArrayList;

public abstract class User {
    private UUID id;
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String emailAddress;
    protected String password;
    protected Address address;
    protected ArrayList<Alert> recievedAlerts;
    protected Type userType;

    public User(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address) {
        this.id = id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setUsername(username);
        this.setEmailAddress(emailAddress);
        this.setPassword(password);
        this.setAddress(address);
    }

    public User(String firstName, String lastName, String username, String emailAddress, String password, Address address) {
        //overloaded constructor
        this.id = UUID.randomUUID();
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setUsername(username);
        this.setEmailAddress(emailAddress);
        this.setPassword(password);
        this.setAddress(address);
    }

    //getters and setters

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    //other methods

    public boolean isMatch(String username, String password) {
        if (username.equalsIgnoreCase(this.getUsername()) && password.equals(this.password)) {
            return true;
        } else {
            return false;
        }
    }

    public void receiveAlert(Alert alert) {
        /*
        if(alert.getAffectedLocations.contains(this.address)) {
            recievedAlerts.add(alert);
        }
        */
        if(alert.isRelevantTo(this)) {
            recievedAlerts.add(alert);
        }
    }

    public void donate(Resource donation, Shelter shelter) {
        shelter.addResource(donation);
    }

    public void viewResourceReport(Shelter shelter) {
        //see what resources a shelter has
    }
}