package com.model;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.BeforeClass;

import com.model.location.Address;
import com.model.users.*;
import com.model.users.Victim.Person;

import java.util.HashMap;

import org.junit.Test;
public class UserListTest {
    @BeforeClass
	public static void setup() {
        UserList.getInstance().clear();
        // Add an admin
        UserList.getInstance().addUser(new Admin("Kirk", "McDirk", "kmcdirk", "mcdirk@thepecans.org", "findthegoodineveryday", new Address("Columbia", "SC"), "8035551234"));

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
    public void testAddUserInvalidPassword() {
        UserList.getInstance().addUser(new Admin("Jarrod", "Burham", "jimmyjar", "jimmyjar@thepecans.org", "jam", new Address("Columbia", "SC"), "8035551235"));
        UserList.getInstance().save();
        if(UserList.getInstance().usernameTaken("jimmyjar")) {
            System.out.println(UserList.getInstance().getUser("jimmyjar").getPassword());
        }
        assertFalse(UserList.getInstance().usernameTaken("jimmyjar"));
    }


    @Test
    public void testAddDuplicateUsername() {
        UserList.getInstance().addUser(new Admin("Kory", "Cray", "kmcdirk", "mcdirk@thepecans.org", "findthegoodineveryday", new Address("Columbia", "SC"), "8035551234"));
        UserList.getInstance().save();
        assertFalse(UserList.getInstance().getUser("kmcdirk").getFirstName().equals("Kory"));
    }

    @Test
    public void testAddUserNoAtInEmail() {
        UserList.getInstance().addUser(new Admin("Kory", "Cray", "kcray", "crayfish.org", "funnymoney", new Address("Columbia", "SC"), "8885551111"));
        assertFalse(UserList.getInstance().usernameTaken("kcray"));
    }

    @Test
    public void testAddUserBlankUsername() {
        UserList.getInstance().addUser(new Admin("Kory", "Cray", "", "cray@crayfish.org", "funnymoney", new Address("Columbia", "SC"), "8885551111"));
        UserList.getInstance().save();
        //System.out.print(UserList.getInstance().getUser("").getEmailAddress());
        assertFalse(UserList.getInstance().usernameTaken(""));
    }

    //User tests
    public void testIsMatchSame() {

    }
    public void testIsMatchDifferentUsername() {

    }
    public void testIsMatchDifferentPassword() {

    }
}
