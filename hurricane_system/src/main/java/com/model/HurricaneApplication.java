package com.model;

/**
 * The single entry point the UI uses to reach the model.
 *
 * <p>Controllers call this class instead of touching DataReader, DataWriter,
 * or the model classes directly, so the UI never needs to know how users are
 * stored.</p>
 *
 * <p>PROTOTYPE: only login, account creation, and logout are here so far.
 * Everything that needs the User class is commented out for now, so
 * {@link #login} and {@link #createAccount} always fail until User is
 * merged in.</p>
 *
 * TODO add the rest of the UML methods (loadShelters, newHurricane, saveData, etc.)
 *
 * @author SynthwaveFox
 */
public class HurricaneApplication {

    /** The one shared instance. */
    private static HurricaneApplication app;

    // private User currentUser; // TODO uncomment once User is implemented

    /** Private so the only instance comes from {@link #getInstance()}. */
    private HurricaneApplication() {
        // TODO load users here (or through UserList) once DataReader.getUsers() returns ArrayList<User>
    }

    /**
     * Returns the shared instance, creating it the first time.
     *
     * @return the application
     */
    public static HurricaneApplication getInstance() {
        if (app == null) {
            app = new HurricaneApplication();
        }
        return app;
    }

    /**
     * Logs a user in.
     *
     * <p>TODO return User instead of boolean to match the UML, once User is implemented.</p>
     *
     * @param username the username entered
     * @param password the password entered
     * @return true if the username and password match a saved user
     */
    public boolean login(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            return false;
        }
        // TODO switch to UserList.getInstance().getUser(username, password) once UserList exists
        // ArrayList<User> users = DataReader.getUsers(); // TODO uncomment once getUsers() returns ArrayList<User>
        // for (User user : users) {
        //     if (user.isMatch(username, password)) {
        //         currentUser = user;
        //         return true;
        //     }
        // }
        return false;
    }

    /**
     * Creates a new account, saves it, and logs the new user in.
     *
     * <p>TODO the UML gives no return type here. This returns boolean so the UI
     * can tell whether it worked. Update the UML or this method so they match.</p>
     *
     * @param firstName    the user's first name
     * @param lastName     the user's last name
     * @param username     the username to log in with; must not already be taken
     * @param emailAddress the user's email address
     * @param password     the password to log in with
     * @return true if the account was created and saved
     */
    public boolean createAccount(String firstName, String lastName, String username,
                                 String emailAddress, String password) {
        if (isBlank(firstName) || isBlank(lastName) || isBlank(username)
                || isBlank(emailAddress) || isBlank(password)) {
            return false;
        }
        // TODO switch to UserList once it exists (getUser(username) to check, addUser(user) and save() to store)
        // ArrayList<User> users = DataReader.getUsers(); // TODO uncomment once getUsers() returns ArrayList<User>
        // for (User user : users) {
        //     if (user.getUsername().equalsIgnoreCase(username)) {
        //         return false; // username already taken
        //     }
        // }
        // TODO decide which user type a new account is, and how to get its Address (User's constructor needs one, but the UML createAccount doesn't take it)
        // User user = new Victim(firstName, lastName, username, emailAddress, password, address);
        // users.add(user);
        // if (!DataWriter.saveUsers(users)) { // TODO uncomment once saveUsers takes ArrayList<User>
        //     return false;
        // }
        // currentUser = user;
        // return true;
        return false;
    }

    /**
     * Logs the current user out.
     */
    public void logout() {
        // TODO save data before logging out, like the sequence diagrams (saveData())
        // currentUser = null; // TODO uncomment once User is implemented
    }

    /**
     * Reports whether text is missing or only whitespace.
     *
     * @param text the text to check
     * @return true if the text is null or blank
     */
    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
