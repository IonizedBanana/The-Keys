package com.model;
import java.util.Scanner;
public class UserManager {
    private static Scanner scanner = new Scanner(System.in);
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
                System.out.println("Enter new state:");
                String state = scanner.next();
                System.out.println("Enter new city:");
                String city = scanner.next();
                System.out.println("Enter new address:");
                String address = scanner.next();
                Address newAddress = new Address(state, city, address);
                changeAddress(user, newAddress);
                break;
        }
        return user;
    }
    public static User changeUsername(User user, String username) {
       
        user.setUsername(username);
        return user;
    }
    public static User changePassword(User user, String password) {
        
        user.setPassword(password);
        return user;
    }
    public static User changeEmailAddress(User user, String emailAddress) {
       
        user.setEmailAddress(emailAddress);
        return user;
    }
    public static User changeFirstName(User user, String firstName) {
       
        user.setFirstName(firstName);
        return user;
    }
     public static User changeLastName(User user, String lastName) {
        user.setLastName(lastName);
        return user;
    }
    public static User changeAddress(User user, Address address) {
      
        user.setAddress(address);
        return user;
    }
}
