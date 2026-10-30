package com.model;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.BeforeClass;

import com.model.location.Address;
import com.model.users.*;
import java.util.HashMap;

import org.junit.Test;
public class UserListTest {
    @BeforeClass
	public static void setup() {
        UserList.getInstance().clear();
        UserList.getInstance().addUser(new Admin("Kirk", "McDirk", "kmcdirk", "mcdirk@thepecans.org", "findthegoodineveryday", new Address("Columbia", "SC"), "8035551234"));
        UserList.getInstance().save();
	}

    @Test
    public void testFindUser() {
        User theUser = UserList.getInstance().getUser("kmcdirk");
        assertTrue(theUser.getUsername().equals("kmcdirk"));
    }

    @Test
    public void testUsernameNotTaken() {
        assertFalse(UserList.getInstance().usernameTaken("eduard"));

    }

    @Test
    public void testAddValidUser() {
        UserList.getInstance().addUser(new Admin("Jarrod", "Burham", "jimmyjar", "jimmyjar@thepecans.org", "jammin", new Address("Columbia", "SC"), "8035551235"));
        UserList.getInstance().save();
        assertTrue(UserList.getInstance().usernameTaken("jimmyjar"));
    }

    //Short password gets rejected, but user still gets created, just with a null password.
    @Test
    public void testAddUserInvalidPassword() {
        UserList.getInstance().addUser(new Admin("Jarrod", "Burham", "jimmyjar", "jimmyjar@thepecans.org", "jam", new Address("Columbia", "SC"), "8035551235"));
        UserList.getInstance().save();
        if(UserList.getInstance().usernameTaken("jimmyjar")) {
            System.out.println(UserList.getInstance().getUser("jimmyjar").getPassword());
        }
        assertFalse(UserList.getInstance().usernameTaken("jimmyjar"));
    }

    /*
    @Test
    public void testAddDuplicateUsername() {
        UserList.getInstance().addUser(new Admin("Kirk", "McDirk", "kmcdirk", "mcdirk@thepecans.org", "findthegoodineveryday", new Address("Columbia", "SC"), "8035551234"));
        UserList.getInstance().save();
        assertFalse(UserList.getInstance().usernameTaken("kmcdirk"));
    }
    */
}
