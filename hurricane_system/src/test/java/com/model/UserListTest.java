package com.model;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.BeforeClass;

import com.model.location.*;
import com.model.users.*;
import com.model.users.Victim.Person;


import java.util.HashMap;

import org.junit.Test;

/*
UserList
-testFindUser (users with accounts should be able to be found in the list) Passed
-testUsernameNotTaken (usernames not associated with an account should not be labeled taken) Passed
-testAddValidUser (valid users should have no problems being added) Passed
-testAddUserPasswordTooShort (passwords must be at least 4 characters) --Failed #66
-testAddDuplicateUsername (usernames cannot be shared between accounts) --Failed #67
-testAddUserNoAtInEmail (emails must contain an @) --Failed #68

User
-testIsMatchSame (User should match is username and password are the same) Passed
-testIsMatchDifferentUsername (User should not match if username is different) Passed
-testIsMatchDifferentPassword (User should not match if password is different, even if username matches) Passed
-testRecieveAlertRelevent (User should be able recieve an alert that involves their location) Passed
-testRecieveAlertIrrelevent (User should not recieve an alert if it does not involve their location) Passed

-testDonateResource (User should be able to donate resources to a shelter)
-testDonateNullResource (User shouldn't be able to donate null)
-testDonateResourceNullShelter (User shouldn't be able to donate resources to null)




*/
public class UserListTest {
    public static final Address SC = new Address("1 Vendue Range", "Charleston", "SC", "9401");
    // Random McDonald's in Ohio: 1801 W State St, Fremont, OH 43420. To be used as a location not relevent to people in SC when sending alerts
    public static final Address NOTSC = new Address("1801 W State St", "Fremont", "OH", "43420");

    @BeforeClass
	public static void setup() {
        UserList.getInstance().clear();
        // Add an admin
        UserList.getInstance().addUser(new Admin("Kirk", "McDirk", "kmcdirk", "mcdirk@thepecans.org", "findthegoodineveryday", SC, "8035551234"));
        UserList.getInstance().getUser("kmcdirk").setReceivedAlerts(new ArrayList<Alert>());

        // Add a victim (with a pet)
        UserList.getInstance().addUser(new Victim("Trey", "Climber", "treeclimber", "tclimber@mailmail.org", "iliketrees", new Address("Columbia", "SC")));
        Victim victimInstance = new Victim();
        Person p = victimInstance.new Person("Peter Chimney", (byte)1, true, "A perfectly normal hamster with no special attributes. He seems to have a habit of getting lost.");
        ((Victim)(UserList.getInstance().getUser("treeclimber"))).addPartyMember(p);

        // Add a dispatcher
        UserList.getInstance().addUser(new Dispatcher("Marcus", "Banker", "mbanker", "mbanker@korycrayinc.org", "ilovefoodandmoney", new Address("Columbia", "SC"), "8885551234"));

        // Add a shelter worker

        // Add a volunteer
        
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
    public void testAddUserPasswordTooShort() {
        UserList.getInstance().addUser(new Admin("Jarrod", "Burham", "jimmyjar", "jimmyjar@thepecans.org", "jam", new Address("Columbia", "SC"), "8035551235"));
        UserList.getInstance().save();
        if(UserList.getInstance().usernameTaken("jimmyjar")) {
            System.out.println(UserList.getInstance().getUser("jimmyjar").getPassword());
        }
        assertFalse(UserList.getInstance().usernameTaken("jimmyjar"));
    }


    @Test
    // User gets created with null username
    public void testAddDuplicateUsername() {
        UserList.getInstance().addUser(new Admin("Kory", "Cray", "kmcdirk", "mcdirk@thepecans.org", "findthegoodineveryday", new Address("Columbia", "SC"), "8035551234"));
        UserList.getInstance().save();
        boolean found = false;
        for (String s : UserList.getInstance().getUserList().keySet()) {
            if (UserList.getInstance().getUser(s).getLastName().equals("Cray")) found = true;
        }
        assertFalse(found);
    }

    @Test
    // User gets created, no checks for email
    public void testAddUserNoAtInEmail() {
        UserList.getInstance().addUser(new Admin("Kory", "Cray", "kcray", "crayfish.org", "funnymoney", new Address("Columbia", "SC"), "8885551111"));
        assertFalse(UserList.getInstance().usernameTaken("kcray"));
    }

    @Test
    // User gets created, no checks for blank username
    public void testAddUserBlankUsername() {
        UserList.getInstance().addUser(new Admin("Kory", "Cray", "", "cray@crayfish.org", "funnymoney", new Address("Columbia", "SC"), "8885551111"));
        UserList.getInstance().save();
        //System.out.print(UserList.getInstance().getUser("").getEmailAddress());
        assertFalse(UserList.getInstance().usernameTaken(""));
    }

    //User tests
    @Test
    public void testIsMatchSame() {
        String username = "kmcdirk";
        String password = "findthegoodineveryday";
        User user = UserList.getInstance().getUser(username);
        assertTrue(user.isMatch(username, password));
    }

    @Test
    // "kmcdrk" is not a username already taken, so "user" is null and the test crashes unless I check for that
    public void testIsMatchDifferentUsername() {
        String username = "kmcdrk";
        String password = "findthegoodineveryday";
        User user = UserList.getInstance().getUser(username);
        // "user" is null, so there is no match
        assertFalse(user != null && !user.isMatch(username, password));
    }

    @Test
    public void testIsMatchDifferentPassword() {
        String username = "kmcdirk";
        String password = "whycantistealhisaccount";
        User user = UserList.getInstance().getUser(username);
        assertFalse(user.isMatch(username, password));
    }

    @Test
    // crashes saying "MAP_API_KEY environment variable has not been set." It has and works when running the actual program, but not when testing on my end
    // fixed by adding settings.json with the path to .env
    public void testRecieveAlertRelevent() {
        User user = UserList.getInstance().getUser("kmcdirk");
        ArrayList<Address> temp = new ArrayList<Address>();
        temp.add(user.getAddress());
        Alert alert = new Alert(temp, "Look, everyone, It's me! I'm being sent across the airwaves!", "Issued by ME!", Date.from(Instant.now()));
        user.receiveAlert(alert);
        assertTrue(user.getReceivedAlerts().size() > 0);
    }
    
    @Test
    public void testRecieveAlertIrrelevent() {
        User user = UserList.getInstance().getUser("kmcdirk");
        ArrayList<Address> temp = new ArrayList<Address>();
        temp.add(NOTSC);
        Alert alert = new Alert(temp, "Look, everyone, It's me! I'm being sent across the airwaves!", "Issued by ME!", Date.from(Instant.now()));
        user.receiveAlert(alert);
        assertFalse(user.getReceivedAlerts().size() > 0);
    } 

    // 
}
