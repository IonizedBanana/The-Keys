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