package com.model;

/**
 * Console driver that runs hardcoded scenarios against {@link HurricaneApplication},
 * so the model can be tested without the JavaFX screens.
 *
 * <p>Run it on its own: it has its own {@code main} and does not start the GUI.</p>
 *
 * TODO add scenarios as HurricaneApplication gains methods (requests, shelters, hurricanes)
 */
public class HurricaneUI {

    private HurricaneApplication app;

    HurricaneUI() {
        app = HurricaneApplication.getInstance();
    }

    public void run() {
        scenario1();
        scenario2();
        scenario3();
    }

    /** An existing user logs in with the right password, then logs out. */
    public void scenario1() {
        System.out.println();

        if (!app.login("bobpubert59", "ilovemywifebarbara")) {
            System.out.println("Sorry, Bob Pubert couldn't log in.");
            return;
        }
        System.out.println("Bob Pubert is now logged in.");

        app.logout();
        System.out.println("Bob Pubert is now logged out.");
    }

    /** An existing user types the wrong password and is turned away. */
    public void scenario2() {
        System.out.println();

        if (app.login("barbpubert68", "wrongpassword")) {
            System.out.println("Barbara Pubert logged in with the wrong password. That shouldn't happen.");
            app.logout();
            return;
        }
        System.out.println("Barbara Pubert's wrong password was correctly rejected.");
    }

    /** A new user signs up, logs out, and logs back in with the new account. */
    public void scenario3() {
        System.out.println();

        if (!app.createAccount("Jane", "Doe", "janedoe", "janedoe@example.com", "hurricane1")) {
            System.out.println("Sorry, Jane Doe's account couldn't be created.");
            return;
        }
        System.out.println("Jane Doe created an account and is logged in.");

        app.logout();
        System.out.println("Jane Doe logged out.");

        if (!app.login("janedoe", "hurricane1")) {
            System.out.println("Sorry, Jane Doe couldn't log back in.");
            return;
        }
        System.out.println("Jane Doe logged back in.");
        app.logout();
    }

    public static void main(String[] args) {
        HurricaneUI hurricaneInterface = new HurricaneUI();
        hurricaneInterface.run();
    }
}
