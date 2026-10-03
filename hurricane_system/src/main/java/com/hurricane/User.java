import java.util.UUID;

public abstract class User {
    private UUID id;
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String emailAddress;
    protected String password;
    protected Address address;

    public User(UUID id, String firstName,  String lastName, String username, String emailAddress, String password, Address address) {
        //TODO
    }

    public User(String firstName, String lastName, String username, String emailAddress, String password, Address address) {
        //overloaded constructor
    }

    //getters and setters

    public String getFirstName() {
        //TODO
    }

    public void setFirstName() {
        //TODO
    }

    public String getLastName() {
        //TODO
    }

    public void setLastName() {
        //TODO
    }

    public String getUsername() {
        //TODO
    }

    public void setUsername() {
        //TODO
    }

    public String getEmailAddress() {
        //TODO
    }

    public void setEmailAddress() {
        //TODO
    }

    public String getPassword() {
        //TODO
    }

    public void setPassword() {
        //TODO
    }

    public Address getAddress() {
        //TODO
    }

    public void setAddress() {
        //TODO
    }

    //other methods

    public boolean isMatch(String userName, String password) {
        //returns if the current user matches the username and password given
    }

    public void receiveAlert(Alert alert) {
        //recieve relevent alert
    }

    public void donate(Resource donation, Shelter shelter) {
        //donate to a shelter
    }

    public void viewResourceReport(Shelter shelter) {
        //see what resources a shelter has
    }
}