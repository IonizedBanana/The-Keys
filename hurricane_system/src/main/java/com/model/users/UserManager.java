package com.model.users;
import java.util.Scanner;
import com.model.location.*;
/**
 * 
 * UserManager is a utility class that provides methods for editing user information.
 * It allows for changing a user's username, password, email address, first name, last name, and address.
 * It uses a Scanner to read user input from the console.
 * @author LoganH627
 */
public class UserManager {
    private static Scanner scanner = new Scanner(System.in);
    /**
     * is a method that allows a user to edit their information. It prompts the user to choose which information they want to edit and then calls the appropriate method to change that information.
     * @param user the user to edit
     * @return the edited user
     */
    public static User editUser(User user) {
        System.out.println("type 1 to change Username, type 2 to change Password, type 3 to change Email Address, type 4 to change Firstname, type 5 to change Lastname, type 6 to change Address.");
        
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Enter new username:");
                String username = scanner.next();
                changeUsername(user, username);
                break;
            case 2:
                System.out.println("Enter new password:");
                String password = scanner.next();
                changePassword(user, password);
                break;
            case 3:
                System.out.println("Enter new email address:");
                String emailAddress = scanner.next();
                changeEmailAddress(user, emailAddress);
                break;
            case 4:
                System.out.println("Enter new first name:");
                String firstName = scanner.next();
                changeFirstName(user, firstName);
                break;
            case 5:
                System.out.println("Enter new last name:");
                String lastName = scanner.next();
                changeLastName(user, lastName);
                break;
            case 6:
                System.out.println("Enter new street:");
                String street = scanner.next();
                System.out.println("Enter new city:");
                String city = scanner.next();
                System.out.println("Enter new state:");
                String state = scanner.next();
                System.out.println("Enter new zipcode:");
                String zipcode = scanner.next();
                Address newAddress = new Address(street, city, state, zipcode);
                changeAddress(user, newAddress);
                break;
        }
        return user;
    }
    /**
     * changeUsername is a method that changes the username of a user.
     * @param user the user to change the username of
     * @param username the new username
     * @return the user with the updated username
     */
    public static User changeUsername(User user, String username) {
       
        user.setUsername(username);
        return user;
    }
    /**
     * changePassword is a method that changes the password of a user.
     * It takes in a User object and a new password as parameters, and updates the user's
     * @param user the user to change the password of
     * @param password the new password
     * @return the user with the updated password
     */
    public static User changePassword(User user, String password) {
        
        user.setPassword(password);
        return user;
    }
    /**
     * changeEmailAddress is a method that changes the email address of a user.
     * @param user the user to change the email address of
     * @param emailAddress the new email address
     * @return the user with the updated email address
     */
    public static User changeEmailAddress(User user, String emailAddress) {
       
        user.setEmailAddress(emailAddress);
        return user;
    }
    /**
     * changeFirstName is a method that changes the first name of a user.
     * @param user the user to change the first name of
     * @param firstName the new first name
     * @return the user with the updated first name
     */
    public static User changeFirstName(User user, String firstName) {
       
        user.setFirstName(firstName);
        return user;
    }
    /**
     * changeLastName is a method that changes the last name of a user.
     * @param user the user to change the last name of
     * @param lastName the new last name
     * @return the user with the updated last name
     */
    public static User changeLastName(User user, String lastName) {
        user.setLastName(lastName);
        return user;
    }
    /**
     * changeAddress is a method that changes the address of a user.
     * @param user the user to change the address of
     * @param address the new address
     * @return the user with the updated address
     */
    public static User changeAddress(User user, Address address) {
      
        user.setAddress(address);
        return user;
    }
}
