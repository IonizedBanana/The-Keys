package com.model.users;


/**
 * A user's type.
 * @author Jason
 */
public enum UserType {
    VICTIM("Victim"),
    VOLUNTEER("Volunteer"), ADMIN("Admin"), DISPATCHER("Dispatcher"), SHELTER_WORKER("Shelter Worker");
    public final String ASCII;
     private UserType(String ASCII) {
         this.ASCII = ASCII;
     }
}
