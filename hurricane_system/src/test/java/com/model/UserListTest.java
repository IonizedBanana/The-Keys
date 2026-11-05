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

/* I did not use AI, but I'm making this table anyway, just to be safe.
-Test------------------------------------Reasoning--------------------------------------
UserList
-testFindUser                           |(users with accounts should be able to be found in the list) Passed
-testUsernameNotTaken                   |(usernames not associated with an account should not be labeled taken) Passed
-testAddValidUser                       |(valid users should have no problems being added) Passed
-testAddUserPasswordTooShort            |(passwords must be at least 4 characters) --Failed #66
-testAddDuplicateUsername               |(usernames cannot be shared between accounts) --Failed #67
-testAddUserNoAtInEmail                 |(emails must contain an @) --Failed #68

User
-testIsMatchSame                        |(User should match is username and password are the same) Passed
-testIsMatchDifferentUsername           |(User should not match if username is different) Passed
-testIsMatchDifferentPassword           |(User should not match if password is different, even if username matches) Passed
-testRecieveAlertRelevent               |(User should be able recieve an alert that involves their location) Passed
-testRecieveAlertIrrelevent             |(User should not recieve an alert if it does not involve their location) Passed

Victim
-testGetFullParty                       |(Should return all party members, including the user) Passed
-testUpdatePartySizeWhenAddingMember    |(Party size should increase when adding a party member) Passed
-testUpdatePartySizeWhenAddingPet       |(Party size shouldn't increase when adding a pet) Passed
-testAddPartyMemberValid                |(Party members should have no problems being added if they are valid) Passed
-testConvertToVictim                    |(Other users can become victims) Failed #97

Admin
-testSendValidAlert                     |(Admins can send alerts) Passed
-testSendBlankIssuedByAlert             |(Admins shouldn't be able to send Alerts with no "issuedBy") --Failed #69
-testVerifyCredential                   |(Admins should be able to verify credentials) Passed
-TestNoPhoneNumberAdmin                 |(Admin phone num can't be blank) Failed #96
-TestShortPhoneNumAdmin                 |(Admin phone num can't be less than 10 characters) Failed #96


Dispatcher
-testAssignToRequest                    |(Dispatchers should be able to assign availible volunteers to a request, which should update the volunteer's assigned request and availibility) --Failed #75
-testRequestMoreInfo                    |(Dispatchers should send a notifications to Victims to ask for more information) Passed
-testAssignToUnavailible                |(Unavailible volunteer shouldn't be assigned to requests) Passed
-TestNoPhoneNumberDispatcher            |(Dispatcher phone num can't be blank) Failed #96
-TestShortPhoneNumAdmin                 |(Dispatcher phone num can't be less than 10 characters) Failed #96
-testConvertToDispatcher                |(Other users can become dispatchers) Failed #97

Volunteer
-testSubmitNullCredential               |(Credential can't be null) --Failed #83
-testSubmitNullStringsCredential        |(Attributes of Credential can't be null) --Failed #85
-testSubmitBlankStringsCredential       |(Attributes of Credential can't be blank) --Failed #87
-testUpdateRequestValid                 |(Volunteers can update the status of requests, except for complete) Passed
-testUpdateRequestInvalid               |(Volunteers cannot update the status of requests to complete) Passed
-testConvertToVolunteer                 |(Other users can become volunteers) Failed #97

-----------------------------------------------------------------------------------------------------
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
        UserList.getInstance().getUser("treeclimber").setReceivedAlerts(new ArrayList<Alert>());

        Victim victimInstance = new Victim();
        Person p = victimInstance.new Person("Peter Chimney", (byte)1, true, "A perfectly normal hamster with no special attributes. He seems to have a habit of getting lost.");
        ((Victim)(UserList.getInstance().getUser("treeclimber"))).addPartyMember(p);
        p = victimInstance.new Person("Nick Name", (byte)31, false, "A friend.");
        ((Victim)(UserList.getInstance().getUser("treeclimber"))).addPartyMember(p);

        ((Victim)(UserList.getInstance().getUser("treeclimber"))).createRequest((byte)1, SC, "Help me I've fallen and I can't get up.");


        // Add a dispatcher
        UserList.getInstance().addUser(new Dispatcher("Marcus", "Banker", "mbanker", "mbanker@korycrayinc.org", "ilovefoodandmoney", new Address("Columbia", "SC"), "8885551234"));
        UserList.getInstance().getUser("mbanker").setReceivedAlerts(new ArrayList<Alert>());


        // Add a shelter worker
        Shelter shelter = ShelterList.getInstance().getShelter("Bond Court");
        UserList.getInstance().addUser(new ShelterWorker("Juan", "Nicolas", "lamburger", "password123", "lamburger@lamburgerandfert.org", SC, shelter));

        // Add a volunteer
        UserList.getInstance().addUser(new Volunteer("Marco", "Martinez", "fert", "fert@lamburgerandfert.org", "L154187!@#4168923037$%&698", SC));
        UserList.getInstance().getUser("fert").setReceivedAlerts(new ArrayList<Alert>());
        ((Volunteer) UserList.getInstance().getUser("fert")).submitCredential(new Credential("Driver's license", "SCDMV", Date.from(Instant.now()), false));
        ((Volunteer) UserList.getInstance().getUser("fert")).setAvailible(true);

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

    //Victim tests
    @Test
    public void testGetFullParty() {
        Victim victim = (Victim) UserList.getInstance().getUser("treeclimber");
        assertTrue(victim.getPartySize() == 2);
    }

    @Test
    public void testUpdatePartySizeWhenAddingMember() {
        Victim victimInstance = new Victim();
        Victim victim = ((Victim)(UserList.getInstance().getUser("treeclimber")));
        Person p = victimInstance.new Person("Place Hold", (byte)30, false, "Another friend.");
        victim.addPartyMember(p);
        assertTrue(victim.getPartySize() == 3);
    }

    @Test
    public void testUpdatePartySizeWhenAddingPet() {
        Victim victimInstance = new Victim();
        Victim victim = ((Victim)(UserList.getInstance().getUser("treeclimber")));
        Person p = victimInstance.new Person("Rabbo", (byte)2, true, "A pet dog.");
        victim.addPartyMember(p);
        assertTrue(victim.getPartySize() == 2);
    }

    @Test
    public void testConvertToVictim() {
        Admin admin = ((Admin)(UserList.getInstance().getUser("kmcdirk"))); 
        Victim victim = new Victim(admin);
        UserList.getInstance().addUser(victim);
        UserList.getInstance().save();
        assertTrue(UserList.getInstance().getUser("kmcdirk").getType() == UserType.VICTIM);
    }

    // Admin tests
    @Test
    public void testSendValidAlert() {
        Admin admin = ((Admin)(UserList.getInstance().getUser("kmcdirk"))); 
        ArrayList<Address> temp = new ArrayList<Address>();
        temp.add(admin.getAddress());
        Alert alert = new Alert(temp, "Hi everyone! This is an alert!", "Issued by ME!", Date.from(Instant.now()));
        admin.sendAlert(alert);
        assertTrue(admin.getReceivedAlerts().size() == 1);
    }

    @Test
    public void testSendBlankIssuedByAlert() {
        Admin admin = ((Admin)(UserList.getInstance().getUser("kmcdirk"))); 
        ArrayList<Address> temp = new ArrayList<Address>();
        temp.add(admin.getAddress());
        Alert alert = new Alert(temp, "Hi everyone! This is an alert!", "", Date.from(Instant.now()));
        admin.sendAlert(alert);
        assertFalse(admin.getReceivedAlerts().size() == 1);
    }

    @Test
    public void testVerifyCredential() {
        Admin admin = ((Admin)(UserList.getInstance().getUser("kmcdirk"))); 
        Volunteer volunteer = ((Volunteer)(UserList.getInstance().getUser("fert"))); 
        Credential credential = volunteer.getCredentials().getFirst();
        admin.verifyCredential(credential);
        assertTrue(volunteer.getCredentials().getFirst().isVerified());
    }

    @Test 
    public void TestNoPhoneNumber() {
        UserList.getInstance().addUser(new Admin("Jarrod", "Burham", "jimmyjar", "jimmyjar@thepecans.org", "jammin", new Address("Columbia", "SC"), ""));
        UserList.getInstance().save();
        assertFalse(UserList.getInstance().usernameTaken("jimmyjar"));
    }

    @Test 
    public void TestShortPhoneNumberAdmin() {
        UserList.getInstance().addUser(new Admin("Jarrod", "Burham", "jimmyjar", "jimmyjar@thepecans.org", "jammin", new Address("Columbia", "SC"), "123"));
        UserList.getInstance().save();
        assertFalse(UserList.getInstance().usernameTaken("jimmyjar"));
    }

    // Dispatcher tests
    @Test
    public void testAssignToRequest() {
        Dispatcher dispatcher = ((Dispatcher)(UserList.getInstance().getUser("mbanker")));
        Victim victim = ((Victim)(UserList.getInstance().getUser("treeclimber")));
        Request request = victim.getCreatedRequests().getFirst();
        Volunteer volunteer = ((Volunteer)(UserList.getInstance().getUser("fert")));
        dispatcher.respondToRequest(request, volunteer);
        assertTrue(volunteer.getAssignedRequest() == request);
    }

    @Test
    public void testAssignToUnavailible() {
        Dispatcher dispatcher = ((Dispatcher)(UserList.getInstance().getUser("mbanker")));
        Victim victim = ((Victim)(UserList.getInstance().getUser("treeclimber")));
        Request request = victim.getCreatedRequests().getFirst();
        Volunteer volunteer = ((Volunteer)(UserList.getInstance().getUser("fert")));
        volunteer.setAvailible(false);
        dispatcher.respondToRequest(request, volunteer);
        assertFalse(volunteer.getAssignedRequest() == request);
    }

    @Test
    public void testRequestMoreInfo() {
        Dispatcher dispatcher = ((Dispatcher)(UserList.getInstance().getUser("mbanker")));
        Victim victim = ((Victim)(UserList.getInstance().getUser("treeclimber")));
        Request request = victim.getCreatedRequests().getFirst();
        System.out.println(victim.getReceivedAlerts().size() > 0);
        dispatcher.requestMoreInfo(request, "Tell me exactly what kind of help you need");
        System.out.println(victim.getReceivedAlerts().size() > 0);
        assertTrue(victim.getReceivedAlerts().size() > 0);
    }

    @Test
    public void testConvertToDispatcher() {
        Admin admin = ((Admin)(UserList.getInstance().getUser("kmcdirk"))); 
        Dispatcher dispatcher = new Dispatcher(admin, "8035551234");
        UserList.getInstance().addUser(dispatcher);
        UserList.getInstance().save();
        assertTrue(UserList.getInstance().getUser("kmcdirk").getType() == UserType.DISPATCHER);
    }

    @Test 
    public void TestNoPhoneNumberDispatcher() {
        UserList.getInstance().addUser(new Dispatcher("Jarrod", "Burham", "jimmyjar", "jimmyjar@thepecans.org", "jammin", new Address("Columbia", "SC"), ""));
        UserList.getInstance().save();
        assertFalse(UserList.getInstance().usernameTaken("jimmyjar"));
    }

    @Test 
    public void TestShortPhoneNumberDispatcher() {
        UserList.getInstance().addUser(new Dispatcher("Jarrod", "Burham", "jimmyjar", "jimmyjar@thepecans.org", "jammin", new Address("Columbia", "SC"), "123"));
        UserList.getInstance().save();
        assertFalse(UserList.getInstance().usernameTaken("jimmyjar"));
    }

    // Volunteer tests
    @Test 
    public void testSubmitValidCredential() {
        Volunteer volunteer = ((Volunteer) UserList.getInstance().getUser("fert"));
        volunteer.submitCredential(new Credential("Super driver's license 2.0", "Super SCDMV 2.0", Date.from(Instant.now()), false));
        assertTrue(volunteer.getCredentials().size() == 2);
    }

    @Test 
    public void testSubmitNullCredential() {
        Volunteer volunteer = ((Volunteer) UserList.getInstance().getUser("fert"));
        volunteer.submitCredential(null);
        assertFalse(volunteer.getCredentials().size() == 2);
    }

    @Test 
    public void testSubmitNullStringsCredential() {
        Volunteer volunteer = ((Volunteer) UserList.getInstance().getUser("fert"));
        volunteer.submitCredential(new Credential(null, null, Date.from(Instant.now()), false));
        assertFalse(volunteer.getCredentials().size() == 2);
    }

    @Test
    public void testSubmitBlankStringsCredential() {
        Volunteer volunteer = ((Volunteer) UserList.getInstance().getUser("fert"));
        volunteer.submitCredential(new Credential("", "", Date.from(Instant.now()), false));
        assertFalse(volunteer.getCredentials().size() == 2);
    }

    @Test 
    public void testUpdateRequestValid() {
        Volunteer volunteer = ((Volunteer) UserList.getInstance().getUser("fert"));
        Victim victim = ((Victim)(UserList.getInstance().getUser("treeclimber")));
        Request request = victim.getCreatedRequests().getFirst();
        volunteer.updateRequest(request, ResponseStatus.IN_PROGRESS);
        assertTrue(request.getStatus() == ResponseStatus.IN_PROGRESS);
    }

    @Test 
    public void testUpdateRequestInvalid() {
        Volunteer volunteer = ((Volunteer) UserList.getInstance().getUser("fert"));
        Victim victim = ((Victim)(UserList.getInstance().getUser("treeclimber")));
        Request request = victim.getCreatedRequests().getFirst();
        volunteer.updateRequest(request, ResponseStatus.COMPLETE);
        assertFalse(request.getStatus() == ResponseStatus.COMPLETE);
    }

    @Test
    public void testConvertToVolunteer() {
        Admin admin = ((Admin)(UserList.getInstance().getUser("kmcdirk"))); 
        Volunteer volunteer = new Volunteer(admin);
        UserList.getInstance().addUser(volunteer);
        UserList.getInstance().save();
        assertTrue(UserList.getInstance().getUser("kmcdirk").getType() == UserType.VOLUNTEER);
    }
}
