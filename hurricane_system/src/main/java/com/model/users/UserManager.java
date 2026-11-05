package com.model.users;
import com.model.location.*;
/**
 * 
 * UserManager
 * @author LoganH627
 */

public class UserManager {
    public enum UserParameter {
        USERNAME,
        PASSWORD,
        EMAIL_ADDRESS,
        FIRST_NAME,
        LAST_NAME,
        ADDRESS
    }
    public static void editUser(User user, UserParameter parameter, Object value) {
        switch (parameter) {
            case USERNAME:
                changeUsername(user, (String) value);
                break;
            case PASSWORD:
                changePassword(user, (String) value);
                break;
            case EMAIL_ADDRESS:
                changeEmailAddress(user, (String) value);
                break;
            case FIRST_NAME:
                changeFirstName(user, (String) value);
                break;
            case LAST_NAME:
                changeLastName(user, (String) value);
                break;
            case ADDRESS:
                changeAddress(user, (Address) value);
                break;
            default:
                throw new IllegalArgumentException("Invalid user parameter");
        }
    }
    public static void changeUsername(User user, String username) {
        user.setUsername(username);
    }
    public static void changePassword(User user, String password) {
        user.setPassword(password);
    }
    public static void changeEmailAddress(User user, String emailAddress) {
        user.setEmailAddress(emailAddress);
    }
    public static void changeFirstName(User user, String firstName) {
       
        user.setFirstName(firstName);
    }
     public static void changeLastName(User user, String lastName) {
        user.setLastName(lastName);
    }
    public static void changeAddress(User user, Address address) {
        user.setAddress(address);
    }
}
