package com.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import com.model.location.Address;
import com.model.users.Dispatcher;
import com.model.users.ShelterWorker;
import com.model.users.UserType;
import com.model.users.Victim;
import com.model.users.Volunteer;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * +-----------------------------------------------------------------+-------------------------------------------------------------+
 * | Test                                                            | Reasoning                                                   |
 * +-----------------------------------------------------------------+-------------------------------------------------------------+
 * | getInstanceReturnsSameInstanceEveryTime                         | the facade is a singleton, so there is only one app         |
 * | loginWithCorrectCredentialsReturnsTrue                          | a saved user can log in with their username and password    |
 * | loginWithCorrectCredentialsSetsCurrentUser                      | logging in makes that user the current user                 |
 * | loginWithWrongPasswordReturnsFalse                              | the password has to match                                   |
 * | loginWithUnknownUsernameReturnsFalse                            | only saved users can log in                                 |
 * | loginWithBlankUsernameReturnsFalse                              | a username is required to log in                            |
 * | loginWithNullPasswordReturnsFalse                               | a password is required to log in                            |
 * | loginIgnoresUsernameCapitalization                              | usernames match regardless of case (like User.isMatch)      |
 * | loginFailureLeavesNobodyLoggedIn                                | a failed login doesn't log anyone in                        |
 * | logoutClearsCurrentUser                                         | logging out leaves nobody logged in                         |
 * | isTypeMatchesLoggedInUsersType                                  | isType is true for the logged in user's type                |
 * | isTypeIsFalseForADifferentType                                  | isType is false for any other type                          |
 * | isTypeIsFalseWhenNobodyLoggedIn                                 | nobody logged in means no type                              |
 * | createAccountWithAllFieldsReturnsTrue                           | an account with every field filled in gets created          |
 * | createAccountLogsInTheNewUser                                   | a new user is logged in right away                          |
 * | createAccountMakesAVictim                                       | new accounts are victims                                    |
 * | createAccountSavesUserSoTheyCanLogInAgain                       | new accounts are saved and can log in after a restart       |
 * | createAccountWithTakenUsernameReturnsFalse                      | each username has to be unique                              |
 * | createAccountWithBlankUsernameReturnsFalse                      | a username is required                                      |
 * | createAccountWithNullFirstNameReturnsFalse                      | a first name is required                                    |
 * | createAccountWithBlankPasswordReturnsFalse                      | a password is required                                      |
 * | createAccountWithNullAddressReturnsFalse                        | an address is required                                      |
 * | createAccountWithoutAddressParameterReturnsFalse                | an account can't be saved without an address                |
 * | createAccountWithEmailMissingAtSignReturnsFalse                 | emails need an @ sign                                       |
 * | createAccountWithEmailMissingDomainReturnsFalse                 | emails need a domain name                                   |
 * | addInfoAsVictimReturnsTrue                                      | victims can add their details                               |
 * | addInfoAsVictimSetsPhoneNumber                                  | added details are stored on the victim                      |
 * | addInfoAsVolunteerReturnsFalse                                  | only victims have victim details                            |
 * | addInfoWhenNobodyLoggedInReturnsFalse                           | someone has to be logged in to add details                  |
 * | addPartyMemberAsVictimReturnsTrue                               | victims can add people to their party                       |
 * | addPartyMemberPersonIncreasesPartySize                          | people count toward party size                              |
 * | addPartyMemberPetDoesNotIncreasePartySize                       | pets don't count toward party size                          |
 * | addPartyMemberPetIsStillAddedToParty                            | pets are still part of the party                            |
 * | addPartyMemberWithBlankNameReturnsFalse                         | party members need a name                                   |
 * | addPartyMemberWithNegativeAgeReturnsFalse                       | age can't be negative                                       |
 * | addPartyMemberWithAgeTooLargeForAByteReturnsFalse               | age has to fit in a byte (max 127)                          |
 * | addPartyMemberAsVolunteerReturnsFalse                           | only victims have a party                                   |
 * | newRequestAsVictimReturnsRequest                                | victims can make relief requests                            |
 * | newRequestStartsWaiting                                         | new requests start out waiting                              |
 * | newRequestIsForTheLoggedInVictim                                | a request belongs to the victim who made it                 |
 * | newRequestUsesVictimsHomeAddress                                | requests are picked up at the victim's home                 |
 * | newRequestCanBeLookedUpById                                     | new requests are added to the request list                  |
 * | newRequestIsAddedToVictimsCreatedRequests                       | victims keep track of requests they made                    |
 * | newRequestWithBlankMedicalNoteStoresNull                        | a blank medical note means no medical need                  |
 * | newRequestKeepsMedicalNote                                      | a medical note is kept on the request                       |
 * | newRequestWithSeverityZeroReturnsNull                           | severity has to be at least 1                               |
 * | newRequestWithSeveritySixReturnsNull                            | severity can be at most 5                                   |
 * | newRequestWithNullTypeReturnsNull                               | a request needs a type of help                              |
 * | newRequestWithBlankDescriptionReturnsNull                       | a request needs a description                               |
 * | newRequestAsVolunteerReturnsNull                                | only victims make requests                                  |
 * | newRequestForVictimWithoutAddressReturnsNull                    | a request needs a pickup address                            |
 * | newRequestWhenNobodyLoggedInReturnsNull                         | someone has to be logged in to make a request               |
 * | getRequestWithSavedIdFindsIt                                    | saved requests can be found by id                           |
 * | getRequestWithUnknownIdReturnsNull                              | an unknown id finds nothing                                 |
 * | getOpenRequestsReturnsEveryWaitingRequest                       | every request that isn't complete is open                   |
 * | getOpenRequestsSortsMostSevereFirst                             | the most severe requests come first                         |
 * | getOpenRequestsLeavesOutCompleteRequests                        | complete requests aren't open                               |
 * | getOpenRequestsPutsRecommendedRequestFirst                      | recommended requests come before more severe ones           |
 * | isRecommendedIsFalseOutsideVolunteersSpecialty                  | only requests in the volunteer's specialty are recommended  |
 * | isRecommendedIsFalseForFarAwayRequest                           | only requests within 5 miles are recommended                |
 * | isRecommendedIsFalseForVictim                                   | recommendations are only for volunteers                     |
 * | isRecommendedIsFalseForShelterWorker                            | recommendations are only for volunteers                     |
 * | isRecommendedIsFalseForNullRequest                              | a missing request can't be recommended                      |
 * | isRecommendedIsTrueForNearbyRequestInSpecialty                  | nearby requests in the specialty are recommended            |
 * | getRecommendationReasonNamesTheSpecialty                        | the reason says which specialty matched                     |
 * | getRecommendationReasonIsNullWhenNotRecommended                 | no reason when it isn't recommended                         |
 * | getDistanceToNearbyRequestIsUnderFiveMiles                      | distance to a nearby request is worked out                  |
 * | getDistanceToWhenNobodyLoggedInIsNegativeOne                    | no distance without a logged in user                        |
 * | getDistanceToNullRequestIsNegativeOne                           | no distance to a missing request                            |
 * | getRequesterReturnsVictimWhoMadeRequest                         | a request knows who made it                                 |
 * | getRequesterOfUnknownUserReturnsNull                            | a requester who isn't saved can't be found                  |
 * | getRequesterOfNullRequestReturnsNull                            | a missing request has no requester                          |
 * | getPartyPutsRequesterFirst                                      | the requester is listed first in the party                  |
 * | getPartyIncludesRequesterAndMembers                             | the party is the requester plus everyone with them          |
 * | getPartyOfUnknownUserIsEmpty                                    | no party if the requester can't be found                    |
 * | getPartyOfNullRequestIsEmpty                                    | a missing request has no party                              |
 * | assignRequestAsVolunteerReturnsTrue                             | volunteers can take a request                               |
 * | assignRequestMarksRequestAssigned                               | a taken request is marked assigned                          |
 * | assignRequestMakesVolunteerUnavailable                          | a volunteer with a request isn't available                  |
 * | getAssignedVolunteerFindsVolunteerWhoTookRequest                | others can see who took a request                           |
 * | getAssignedVolunteerOfUnassignedRequestIsNull                   | nobody has a request that wasn't taken                      |
 * | assignRequestAlreadyTakenReturnsFalse                           | two volunteers can't take the same request                  |
 * | assignRequestThatIsCompleteReturnsFalse                         | complete requests can't be taken                            |
 * | assignRequestAsVictimReturnsFalse                               | only volunteers take requests                               |
 * | assignNullRequestReturnsFalse                                   | a missing request can't be taken                            |
 * | finishRequestAsShelterWorkerReturnsTrue                         | shelter workers can finish requests                         |
 * | finishRequestAsShelterWorkerMarksRequestComplete                | a finished request is marked complete                       |
 * | finishRequestAsDispatcherReturnsTrue                            | dispatchers can finish requests                             |
 * | finishRequestFreesTheAssignedVolunteer                          | finishing a request unassigns the volunteer                 |
 * | finishRequestMakesAssignedVolunteerAvailableAgain               | the volunteer is available again                            |
 * | finishRequestAsVolunteerReturnsFalse                            | volunteers can't finish requests                            |
 * | finishRequestAsVolunteerLeavesRequestOpen                       | a volunteer's attempt doesn't change the request            |
 * | finishRequestAsVictimReturnsFalse                               | victims can't finish requests                               |
 * | finishRequestWhenNobodyLoggedInReturnsFalse                     | someone has to be logged in to finish a request             |
 * | finishNullRequestReturnsFalse                                   | a missing request can't be finished                         |
 * | exportTripInfoWhenNobodyLoggedInReturnsFalse                    | someone has to be logged in to export a trip                |
 * | exportTripInfoAsVolunteerWritesFile                             | a responder can export trip info                            |
 * | exportTripInfoWithNullRequestReturnsFalse                       | a missing request can't be exported                         |
 * | getDirectionsWithNullStartReturnsNull                           | directions need a start                                     |
 * | getDirectionsWithNullEndReturnsNull                             | directions need an end                                      |
 * | getDirectionsBetweenRealAddressesHasSteps                       | real addresses get turn by turn steps                       |
 * | getSheltersReturnsEverySavedShelter                             | every saved shelter is listed                               |
 * | getShelterByNameFindsIt                                         | shelters can be found by name                               |
 * | getShelterWithUnknownNameReturnsNull                            | an unknown name finds nothing                               |
 * | getShelterNeedsIsEmptyWithoutPetsOrMedicalNote                  | no pets or medical needs means no special needs             |
 * | getShelterNeedsIncludesPetFriendlyWhenPartyHasPet               | a party with a pet needs a pet friendly shelter             |
 * | getShelterNeedsListsPetFriendlyOnceForTwoPets                   | each need is listed only once                               |
 * | getShelterNeedsIncludesMedicalStaffWhenMedicalNote              | a medical note needs a shelter with medical staff           |
 * | getShelterNeedsOfNullRequestIsEmpty                             | a missing request has no needs                              |
 * | findSheltersWithNoNeedsReturnsSheltersWithRoom                  | any shelter with room fits a party with no needs            |
 * | findSheltersSkipsSheltersWithoutRoomForWholeParty               | a shelter needs room for the whole party                    |
 * | findSheltersWithPetOnlyReturnsPetFriendlyShelters               | parties with pets only get pet friendly shelters            |
 * | findSheltersWithMedicalNoteOnlyReturnsSheltersWithMedicalStaff  | medical needs only get shelters with medical staff          |
 * | findSheltersWithPetAndMedicalNoteReturnsSheltersWithBoth        | shelters have to meet every need                            |
 * | findSheltersForRequestWithoutAddressIsEmpty                     | no shelters without a pickup address                        |
 * | findSheltersForNullRequestIsEmpty                               | no shelters for a missing request                           |
 * | findClosestShelterPicksShelterAtPickupAddress                   | the closest fitting shelter is picked                       |
 * | findClosestShelterForNullRequestIsNull                          | no closest shelter for a missing request                    |
 * | getMyShelterAsShelterWorkerReturnsTheirShelter                  | shelter workers can get the shelter they run                |
 * | getMyShelterReturnsTheCopyThatGetsSaved                         | changes go to the shelter that gets saved                   |
 * | getMyShelterForWorkerWithoutShelterReturnsNull                  | a worker without a shelter has none                         |
 * | getMyShelterAsVolunteerReturnsNull                              | only shelter workers run a shelter                          |
 * | checkInAsShelterWorkerReturnsTrue                               | shelter workers can check a party in                        |
 * | checkInAddsPeopleToUsedCapacity                                 | people checked in fill up the shelter                       |
 * | checkInDoesNotCountPetsTowardUsedCapacity                       | pets don't take up shelter space                            |
 * | checkInRegistersPetsSeparately                                  | pets are recorded separately                                |
 * | checkInRegistersRequesterAsOccupant                             | the requester is recorded as an occupant                    |
 * | checkInWithoutRoomForWholePartyReturnsFalse                     | a full shelter can't take the party                         |
 * | checkInAsVolunteerReturnsFalse                                  | only shelter workers check parties in                       |
 * | checkInNullRequestReturnsFalse                                  | a missing request can't be checked in                       |
 * | getHurricaneByNameFindsIt                                       | hurricanes can be found by name                             |
 * | getHurricaneWithUnknownNameReturnsNull                          | an unknown name finds nothing                               |
 * | isAffectedWhenNobodyLoggedInIsFalse                             | nobody logged in can't be affected                          |
 * | isAffectedForUserWithoutAddressIsFalse                          | a user without an address can't be placed                   |
 * | isAffectedIsTrueForUserNearActiveStorm                          | users within 25 miles of an active storm are affected       |
 * | isAffectedIsFalseWhenStormIsOver                                | storms that are over don't affect anyone                    |
 * | saveDataReturnsTrue                                             | all data can be saved                                       |
 * | saveDataKeepsChangesAfterReload                                 | saved changes are still there after a restart               |
 * +-----------------------------------------------------------------+-------------------------------------------------------------+
 */
/**
 * Tests for the HurricaneApplication facade.
 *
 * <p>Before each test the json files in src/test/resources/data are copied over
 * the ones in ../json (where DataReader and DataWriter look), and every singleton
 * is cleared so the data is read fresh. The real files are put back after each test.</p>
 *
 * <p>Tests that need real distances are skipped unless MAP_API_KEY is set.</p>
 */
public class HurricaneApplicationTest {

    private static final Path DATA_DIR = Paths.get("../json");
    private static final Path TEST_DATA_DIR = Paths.get("src/test/resources/data");
    private static final String[] DATA_FILES = {"user.json", "shelter.json", "requests.json", "hurricane.json"};

    // from src/test/resources/data
    private static final String VOLUNTEER_USERNAME = "rdelgado";
    private static final String VOLUNTEER_PASSWORD = "delgado123";
    private static final String WORKER_USERNAME = "Felix44";
    private static final String WORKER_PASSWORD = "passwordpassword";
    private static final String WORKER_SHELTER = "Zucker Middle School";
    private static final UUID MEDICAL_REQUEST_ID = UUID.fromString("33333333-0000-0000-0000-000000000001");
    private static final UUID FAR_TRAVEL_REQUEST_ID = UUID.fromString("33333333-0000-0000-0000-000000000002");

    private HashMap<String, byte[]> realData;
    private HurricaneApplication app;

    @Before
    public void setUp() throws Exception {
        realData = new HashMap<String, byte[]>();
        for (String file : DATA_FILES) {
            realData.put(file, Files.readAllBytes(DATA_DIR.resolve(file)));
            Files.copy(TEST_DATA_DIR.resolve(file), DATA_DIR.resolve(file), StandardCopyOption.REPLACE_EXISTING);
        }
        clearSingletons();
        app = HurricaneApplication.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        for (String file : DATA_FILES) {
            Files.write(DATA_DIR.resolve(file), realData.get(file));
        }
        clearSingletons();
    }

    private static void clearSingletons() throws Exception {
        clearSingleton(HurricaneApplication.class, "app");
        clearSingleton(UserList.class, "userList");
        clearSingleton(ShelterList.class, "shelterList");
        clearSingleton(RequestList.class, "requestList");
        clearSingleton(HurricaneList.class, "hurricaneList");
    }

    private static void clearSingleton(Class<?> type, String fieldName) throws Exception {
        Field field = type.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, null);
    }

    private static Address downtownCharleston() {
        return new Address("100 Calhoun Street", "Charleston", "SC", "29401");
    }

    /** Creates a victim account, which also logs them in. */
    private boolean createVictim(String username) {
        return app.createAccount("Jane", "Doe", username, "jane.doe@email.com", "janepass1", downtownCharleston());
    }

    /** Creates a victim and has them make a request, leaving them logged in. */
    private Request createVictimRequest(String username, ResponseType type, String medicalNote) {
        createVictim(username);
        return app.newRequest((byte) 3, type, "Water is coming in the house", medicalNote);
    }

    private void loginVolunteer() {
        app.login(VOLUNTEER_USERNAME, VOLUNTEER_PASSWORD);
    }

    private void loginShelterWorker() {
        app.login(WORKER_USERNAME, WORKER_PASSWORD);
    }

    // ---------------------------------------------------------------- getInstance

    @Test
    public void getInstanceReturnsSameInstanceEveryTime() {
        assertSame(app, HurricaneApplication.getInstance());
    }

    // ---------------------------------------------------------------- login / logout

    @Test
    public void loginWithCorrectCredentialsReturnsTrue() {
        assertTrue(app.login(VOLUNTEER_USERNAME, VOLUNTEER_PASSWORD));
    }

    @Test
    public void loginWithCorrectCredentialsSetsCurrentUser() {
        loginVolunteer();
        assertEquals(VOLUNTEER_USERNAME, app.getCurrentUser().getUsername());
    }

    @Test
    public void loginWithWrongPasswordReturnsFalse() {
        assertFalse(app.login(VOLUNTEER_USERNAME, "wrongpassword"));
    }

    @Test
    public void loginWithUnknownUsernameReturnsFalse() {
        assertFalse(app.login("nobodyhere", VOLUNTEER_PASSWORD));
    }

    @Test
    public void loginWithBlankUsernameReturnsFalse() {
        assertFalse(app.login("   ", VOLUNTEER_PASSWORD));
    }

    @Test
    public void loginWithNullPasswordReturnsFalse() {
        assertFalse(app.login(VOLUNTEER_USERNAME, null));
    }

    @Test
    public void loginIgnoresUsernameCapitalization() {
        // User.isMatch ignores case, so login should too
        assertTrue(app.login(VOLUNTEER_USERNAME.toUpperCase(), VOLUNTEER_PASSWORD));
    }

    @Test
    public void loginFailureLeavesNobodyLoggedIn() {
        app.login(VOLUNTEER_USERNAME, "wrongpassword");
        assertNull(app.getCurrentUser());
    }

    @Test
    public void logoutClearsCurrentUser() {
        loginVolunteer();
        app.logout();
        assertNull(app.getCurrentUser());
    }

    // ---------------------------------------------------------------- isType

    @Test
    public void isTypeMatchesLoggedInUsersType() {
        loginVolunteer();
        assertTrue(app.isType(UserType.VOLUNTEER));
    }

    @Test
    public void isTypeIsFalseForADifferentType() {
        loginVolunteer();
        assertFalse(app.isType(UserType.VICTIM));
    }

    @Test
    public void isTypeIsFalseWhenNobodyLoggedIn() {
        assertFalse(app.isType(UserType.VOLUNTEER));
    }

    // ---------------------------------------------------------------- createAccount

    @Test
    public void createAccountWithAllFieldsReturnsTrue() {
        assertTrue(createVictim("janedoe"));
    }

    @Test
    public void createAccountLogsInTheNewUser() {
        createVictim("janedoe");
        assertEquals("janedoe", app.getCurrentUser().getUsername());
    }

    @Test
    public void createAccountMakesAVictim() {
        createVictim("janedoe");
        assertTrue(app.isType(UserType.VICTIM));
    }

    @Test
    public void createAccountSavesUserSoTheyCanLogInAgain() throws Exception {
        createVictim("janedoe");
        clearSingletons();
        app = HurricaneApplication.getInstance();
        assertTrue(app.login("janedoe", "janepass1"));
    }

    @Test
    public void createAccountWithTakenUsernameReturnsFalse() {
        assertFalse(createVictim(VOLUNTEER_USERNAME));
    }

    @Test
    public void createAccountWithBlankUsernameReturnsFalse() {
        assertFalse(createVictim(""));
    }

    @Test
    public void createAccountWithNullFirstNameReturnsFalse() {
        assertFalse(app.createAccount(null, "Doe", "janedoe", "jane.doe@email.com", "janepass1",
                downtownCharleston()));
    }

    @Test
    public void createAccountWithBlankPasswordReturnsFalse() {
        assertFalse(app.createAccount("Jane", "Doe", "janedoe", "jane.doe@email.com", " ",
                downtownCharleston()));
    }

    @Test
    public void createAccountWithNullAddressReturnsFalse() {
        assertFalse(app.createAccount("Jane", "Doe", "janedoe", "jane.doe@email.com", "janepass1", null));
    }

    @Test
    public void createAccountWithoutAddressParameterReturnsFalse() {
        assertFalse(app.createAccount("Jane", "Doe", "janedoe", "jane.doe@email.com", "janepass1"));
    }

    @Test
    public void createAccountWithEmailMissingAtSignReturnsFalse() {
        assertFalse(app.createAccount("Jane", "Doe", "janedoe", "jane.doe.email.com", "janepass1",
                downtownCharleston()));
    }

    @Test
    public void createAccountWithEmailMissingDomainReturnsFalse() {
        assertFalse(app.createAccount("Jane", "Doe", "janedoe", "jane.doe@", "janepass1",
                downtownCharleston()));
    }

    // ---------------------------------------------------------------- addInfo

    @Test
    public void addInfoAsVictimReturnsTrue() {
        createVictim("janedoe");
        assertTrue(app.addInfo(34, 'F', "843-555-0100", "Uses a wheelchair"));
    }

    @Test
    public void addInfoAsVictimSetsPhoneNumber() {
        createVictim("janedoe");
        app.addInfo(34, 'F', "843-555-0100", "Uses a wheelchair");
        assertEquals("843-555-0100", ((Victim) app.getCurrentUser()).getPhoneNumber());
    }

    @Test
    public void addInfoAsVolunteerReturnsFalse() {
        loginVolunteer();
        assertFalse(app.addInfo(34, 'F', "843-555-0100", "Uses a wheelchair"));
    }

    @Test
    public void addInfoWhenNobodyLoggedInReturnsFalse() {
        assertFalse(app.addInfo(34, 'F', "843-555-0100", "Uses a wheelchair"));
    }

    // ---------------------------------------------------------------- addPartyMember

    @Test
    public void addPartyMemberAsVictimReturnsTrue() {
        createVictim("janedoe");
        assertTrue(app.addPartyMember("John Doe", 36, false, "husband"));
    }

    @Test
    public void addPartyMemberPersonIncreasesPartySize() {
        createVictim("janedoe");
        app.addPartyMember("John Doe", 36, false, "husband");
        assertEquals(2, ((Victim) app.getCurrentUser()).getPartySize());
    }

    @Test
    public void addPartyMemberPetDoesNotIncreasePartySize() {
        createVictim("janedoe");
        app.addPartyMember("Rex", 4, true, "dog");
        assertEquals(1, ((Victim) app.getCurrentUser()).getPartySize());
    }

    @Test
    public void addPartyMemberPetIsStillAddedToParty() {
        createVictim("janedoe");
        app.addPartyMember("Rex", 4, true, "dog");
        assertEquals(1, ((Victim) app.getCurrentUser()).getParty().size());
    }

    @Test
    public void addPartyMemberWithBlankNameReturnsFalse() {
        createVictim("janedoe");
        assertFalse(app.addPartyMember("", 36, false, "husband"));
    }

    @Test
    public void addPartyMemberWithNegativeAgeReturnsFalse() {
        createVictim("janedoe");
        assertFalse(app.addPartyMember("John Doe", -1, false, "husband"));
    }

    @Test
    public void addPartyMemberWithAgeTooLargeForAByteReturnsFalse() {
        createVictim("janedoe");
        assertFalse(app.addPartyMember("John Doe", 128, false, "husband"));
    }

    @Test
    public void addPartyMemberAsVolunteerReturnsFalse() {
        loginVolunteer();
        assertFalse(app.addPartyMember("John Doe", 36, false, "husband"));
    }

    // ---------------------------------------------------------------- newRequest

    @Test
    public void newRequestAsVictimReturnsRequest() {
        assertNotNull(createVictimRequest("janedoe", ResponseType.TRAVEL, null));
    }

    @Test
    public void newRequestStartsWaiting() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        assertEquals(ResponseStatus.WAITING, request.getStatus());
    }

    @Test
    public void newRequestIsForTheLoggedInVictim() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        assertEquals("janedoe", request.getRequestee());
    }

    @Test
    public void newRequestUsesVictimsHomeAddress() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        assertSame(app.getCurrentUser().getAddress(), request.getAddress());
    }

    @Test
    public void newRequestCanBeLookedUpById() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        assertSame(request, app.getRequest(request.getId()));
    }

    @Test
    public void newRequestIsAddedToVictimsCreatedRequests() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        assertTrue(((Victim) app.getCurrentUser()).getCreatedRequests().contains(request));
    }

    @Test
    public void newRequestWithBlankMedicalNoteStoresNull() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, "  ");
        assertNull(request.getMedicalNote());
    }

    @Test
    public void newRequestKeepsMedicalNote() {
        Request request = createVictimRequest("janedoe", ResponseType.MEDICAL, "insulin");
        assertEquals("insulin", request.getMedicalNote());
    }

    @Test
    public void newRequestWithSeverityZeroReturnsNull() {
        createVictim("janedoe");
        assertNull(app.newRequest((byte) 0, ResponseType.TRAVEL, "Need a ride", null));
    }

    @Test
    public void newRequestWithSeveritySixReturnsNull() {
        createVictim("janedoe");
        assertNull(app.newRequest((byte) 6, ResponseType.TRAVEL, "Need a ride", null));
    }

    @Test
    public void newRequestWithNullTypeReturnsNull() {
        createVictim("janedoe");
        assertNull(app.newRequest((byte) 3, null, "Need a ride", null));
    }

    @Test
    public void newRequestWithBlankDescriptionReturnsNull() {
        createVictim("janedoe");
        assertNull(app.newRequest((byte) 3, ResponseType.TRAVEL, "", null));
    }

    @Test
    public void newRequestAsVolunteerReturnsNull() {
        loginVolunteer();
        assertNull(app.newRequest((byte) 3, ResponseType.TRAVEL, "Need a ride", null));
    }

    @Test
    public void newRequestForVictimWithoutAddressReturnsNull() {
        UserList.getInstance().addUser(new Victim("Jane", "Doe", "janedoe", "jane.doe@email.com", "janepass1", null));
        app.login("janedoe", "janepass1");
        assertNull(app.newRequest((byte) 3, ResponseType.TRAVEL, "Need a ride", null));
    }

    @Test
    public void newRequestWhenNobodyLoggedInReturnsNull() {
        assertNull(app.newRequest((byte) 3, ResponseType.TRAVEL, "Need a ride", null));
    }

    // ---------------------------------------------------------------- getRequest

    @Test
    public void getRequestWithSavedIdFindsIt() {
        assertEquals("jsmith", app.getRequest(MEDICAL_REQUEST_ID).getRequestee());
    }

    @Test
    public void getRequestWithUnknownIdReturnsNull() {
        assertNull(app.getRequest(UUID.randomUUID()));
    }

    // ---------------------------------------------------------------- getOpenRequests

    @Test
    public void getOpenRequestsReturnsEveryWaitingRequest() {
        assertEquals(6, app.getOpenRequests().size());
    }

    @Test
    public void getOpenRequestsSortsMostSevereFirst() {
        ArrayList<Request> open = app.getOpenRequests();
        for (int i = 1; i < open.size(); i++) {
            assertTrue(open.get(i - 1).getSeverity() >= open.get(i).getSeverity());
        }
    }

    @Test
    public void getOpenRequestsLeavesOutCompleteRequests() {
        app.getRequest(MEDICAL_REQUEST_ID).updateStatus(ResponseStatus.COMPLETE);
        assertFalse(app.getOpenRequests().contains(app.getRequest(MEDICAL_REQUEST_ID)));
    }

    @Test
    public void getOpenRequestsPutsRecommendedRequestFirst() {
        assumeNotNull(System.getenv("MAP_API_KEY"));
        // least severe, but nearby and in the volunteer's specialty
        createVictim("janedoe");
        Request request = app.newRequest((byte) 1, ResponseType.TRAVEL, "Need a ride", null);
        app.logout();
        loginVolunteer();
        assertSame(request, app.getOpenRequests().get(0));
    }

    // ---------------------------------------------------------------- isRecommended / reason / distance

    @Test
    public void isRecommendedIsFalseOutsideVolunteersSpecialty() {
        loginVolunteer();
        assertFalse(app.isRecommended(app.getRequest(MEDICAL_REQUEST_ID)));
    }

    @Test
    public void isRecommendedIsFalseForFarAwayRequest() {
        // without a key every distance is unknown, so this would pass for the wrong reason
        assumeNotNull(System.getenv("MAP_API_KEY"));
        loginVolunteer();
        assertFalse(app.isRecommended(app.getRequest(FAR_TRAVEL_REQUEST_ID)));
    }

    @Test
    public void isRecommendedIsFalseForVictim() {
        createVictim("janedoe");
        assertFalse(app.isRecommended(app.getRequest(FAR_TRAVEL_REQUEST_ID)));
    }

    @Test
    public void isRecommendedIsFalseForShelterWorker() {
        loginShelterWorker();
        assertFalse(app.isRecommended(app.getRequest(FAR_TRAVEL_REQUEST_ID)));
    }

    @Test
    public void isRecommendedIsFalseForNullRequest() {
        loginVolunteer();
        assertFalse(app.isRecommended(null));
    }

    @Test
    public void isRecommendedIsTrueForNearbyRequestInSpecialty() {
        assumeNotNull(System.getenv("MAP_API_KEY"));
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.logout();
        loginVolunteer();
        assertTrue(app.isRecommended(request));
    }

    @Test
    public void getRecommendationReasonNamesTheSpecialty() {
        assumeNotNull(System.getenv("MAP_API_KEY"));
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.logout();
        loginVolunteer();
        assertTrue(app.getRecommendationReason(request).contains("travel"));
    }

    @Test
    public void getRecommendationReasonIsNullWhenNotRecommended() {
        loginVolunteer();
        assertNull(app.getRecommendationReason(app.getRequest(MEDICAL_REQUEST_ID)));
    }

    @Test
    public void getDistanceToNearbyRequestIsUnderFiveMiles() {
        assumeNotNull(System.getenv("MAP_API_KEY"));
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.logout();
        loginVolunteer();
        assertTrue(app.getDistanceTo(request) < 5);
    }

    @Test
    public void getDistanceToWhenNobodyLoggedInIsNegativeOne() {
        assertEquals(-1, app.getDistanceTo(app.getRequest(MEDICAL_REQUEST_ID)), 0);
    }

    @Test
    public void getDistanceToNullRequestIsNegativeOne() {
        loginVolunteer();
        assertEquals(-1, app.getDistanceTo(null), 0);
    }

    // ---------------------------------------------------------------- getRequester / getParty

    @Test
    public void getRequesterReturnsVictimWhoMadeRequest() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        assertSame(app.getCurrentUser(), app.getRequester(request));
    }

    @Test
    public void getRequesterOfUnknownUserReturnsNull() {
        // jsmith is in requests.json but not in user.json
        assertNull(app.getRequester(app.getRequest(MEDICAL_REQUEST_ID)));
    }

    @Test
    public void getRequesterOfNullRequestReturnsNull() {
        assertNull(app.getRequester(null));
    }

    @Test
    public void getPartyPutsRequesterFirst() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.addPartyMember("John Doe", 36, false, "husband");
        assertEquals("Jane Doe", app.getParty(request).get(0).getName());
    }

    @Test
    public void getPartyIncludesRequesterAndMembers() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.addPartyMember("John Doe", 36, false, "husband");
        app.addPartyMember("Rex", 4, true, "dog");
        assertEquals(3, app.getParty(request).size());
    }

    @Test
    public void getPartyOfUnknownUserIsEmpty() {
        assertTrue(app.getParty(app.getRequest(MEDICAL_REQUEST_ID)).isEmpty());
    }

    @Test
    public void getPartyOfNullRequestIsEmpty() {
        assertTrue(app.getParty(null).isEmpty());
    }

    // ---------------------------------------------------------------- assignRequest / getAssignedVolunteer

    @Test
    public void assignRequestAsVolunteerReturnsTrue() {
        loginVolunteer();
        assertTrue(app.assignRequest(app.getRequest(FAR_TRAVEL_REQUEST_ID)));
    }

    @Test
    public void assignRequestMarksRequestAssigned() {
        loginVolunteer();
        Request request = app.getRequest(FAR_TRAVEL_REQUEST_ID);
        app.assignRequest(request);
        assertEquals(ResponseStatus.ASSIGNED, request.getStatus());
    }

    @Test
    public void assignRequestMakesVolunteerUnavailable() {
        loginVolunteer();
        app.assignRequest(app.getRequest(FAR_TRAVEL_REQUEST_ID));
        assertFalse(((Volunteer) app.getCurrentUser()).getIsAvailible());
    }

    @Test
    public void getAssignedVolunteerFindsVolunteerWhoTookRequest() {
        loginVolunteer();
        Request request = app.getRequest(FAR_TRAVEL_REQUEST_ID);
        app.assignRequest(request);
        assertSame(app.getCurrentUser(), app.getAssignedVolunteer(request));
    }

    @Test
    public void getAssignedVolunteerOfUnassignedRequestIsNull() {
        assertNull(app.getAssignedVolunteer(app.getRequest(FAR_TRAVEL_REQUEST_ID)));
    }

    @Test
    public void assignRequestAlreadyTakenReturnsFalse() {
        Request request = app.getRequest(FAR_TRAVEL_REQUEST_ID);
        loginVolunteer();
        app.assignRequest(request);
        Volunteer other = new Volunteer("Sam", "Hill", "samhill", "sam.hill@email.com", "samspass1",
                downtownCharleston());
        UserList.getInstance().addUser(other);
        app.login("samhill", "samspass1");
        assertFalse(app.assignRequest(request));
    }

    @Test
    public void assignRequestThatIsCompleteReturnsFalse() {
        Request request = app.getRequest(FAR_TRAVEL_REQUEST_ID);
        request.updateStatus(ResponseStatus.COMPLETE);
        loginVolunteer();
        assertFalse(app.assignRequest(request));
    }

    @Test
    public void assignRequestAsVictimReturnsFalse() {
        createVictim("janedoe");
        assertFalse(app.assignRequest(app.getRequest(FAR_TRAVEL_REQUEST_ID)));
    }

    @Test
    public void assignNullRequestReturnsFalse() {
        loginVolunteer();
        assertFalse(app.assignRequest(null));
    }

    // ---------------------------------------------------------------- finishRequest

    @Test
    public void finishRequestAsShelterWorkerReturnsTrue() {
        loginShelterWorker();
        assertTrue(app.finishRequest(app.getRequest(FAR_TRAVEL_REQUEST_ID)));
    }

    @Test
    public void finishRequestAsShelterWorkerMarksRequestComplete() {
        loginShelterWorker();
        Request request = app.getRequest(FAR_TRAVEL_REQUEST_ID);
        app.finishRequest(request);
        assertEquals(ResponseStatus.COMPLETE, request.getStatus());
    }

    @Test
    public void finishRequestAsDispatcherReturnsTrue() {
        UserList.getInstance().addUser(new Dispatcher("Dana", "Ross", "dross", "dana.ross@email.com",
                "danapass1", downtownCharleston(), "843-555-0199"));
        app.login("dross", "danapass1");
        assertTrue(app.finishRequest(app.getRequest(FAR_TRAVEL_REQUEST_ID)));
    }

    @Test
    public void finishRequestFreesTheAssignedVolunteer() {
        Request request = app.getRequest(FAR_TRAVEL_REQUEST_ID);
        loginVolunteer();
        app.assignRequest(request);
        Volunteer volunteer = (Volunteer) app.getCurrentUser();
        app.logout();
        loginShelterWorker();
        app.finishRequest(request);
        assertNull(volunteer.getAssignedRequest());
    }

    @Test
    public void finishRequestMakesAssignedVolunteerAvailableAgain() {
        Request request = app.getRequest(FAR_TRAVEL_REQUEST_ID);
        loginVolunteer();
        app.assignRequest(request);
        Volunteer volunteer = (Volunteer) app.getCurrentUser();
        app.logout();
        loginShelterWorker();
        app.finishRequest(request);
        assertTrue(volunteer.getIsAvailible());
    }

    @Test
    public void finishRequestAsVolunteerReturnsFalse() {
        loginVolunteer();
        assertFalse(app.finishRequest(app.getRequest(FAR_TRAVEL_REQUEST_ID)));
    }

    @Test
    public void finishRequestAsVolunteerLeavesRequestOpen() {
        loginVolunteer();
        Request request = app.getRequest(FAR_TRAVEL_REQUEST_ID);
        app.finishRequest(request);
        assertEquals(ResponseStatus.WAITING, request.getStatus());
    }

    @Test
    public void finishRequestAsVictimReturnsFalse() {
        createVictim("janedoe");
        assertFalse(app.finishRequest(app.getRequest(FAR_TRAVEL_REQUEST_ID)));
    }

    @Test
    public void finishRequestWhenNobodyLoggedInReturnsFalse() {
        assertFalse(app.finishRequest(app.getRequest(FAR_TRAVEL_REQUEST_ID)));
    }

    @Test
    public void finishNullRequestReturnsFalse() {
        loginShelterWorker();
        assertFalse(app.finishRequest(null));
    }

    // ---------------------------------------------------------------- exportTripInfo

    @Test
    public void exportTripInfoWhenNobodyLoggedInReturnsFalse() {
        assertFalse(app.exportTripInfo(app.getRequest(FAR_TRAVEL_REQUEST_ID), app.getShelter(WORKER_SHELTER),
                "trip"));
    }

    @Test
    public void exportTripInfoAsVolunteerWritesFile() throws Exception {
        File file = File.createTempFile("trip", ".txt");
        file.deleteOnExit();
        loginVolunteer();
        assertTrue(app.exportTripInfo(app.getRequest(FAR_TRAVEL_REQUEST_ID), app.getShelter(WORKER_SHELTER),
                file.getPath()));
    }

    @Test
    public void exportTripInfoWithNullRequestReturnsFalse() {
        loginVolunteer();
        assertFalse(app.exportTripInfo(null, app.getShelter(WORKER_SHELTER), "trip"));
    }

    // ---------------------------------------------------------------- getDirections

    @Test
    public void getDirectionsWithNullStartReturnsNull() {
        assertNull(app.getDirections(null, downtownCharleston()));
    }

    @Test
    public void getDirectionsWithNullEndReturnsNull() {
        assertNull(app.getDirections(downtownCharleston(), null));
    }

    @Test
    public void getDirectionsBetweenRealAddressesHasSteps() {
        assumeNotNull(System.getenv("MAP_API_KEY"));
        String[] directions = app.getDirections(downtownCharleston(), app.getShelter(WORKER_SHELTER).getAddress());
        assertTrue(directions.length > 0);
    }

    // ---------------------------------------------------------------- shelters

    @Test
    public void getSheltersReturnsEverySavedShelter() {
        assertEquals(10, app.getShelters().size());
    }

    @Test
    public void getShelterByNameFindsIt() {
        assertEquals(220, app.getShelter(WORKER_SHELTER).getTotalCapacity());
    }

    @Test
    public void getShelterWithUnknownNameReturnsNull() {
        assertNull(app.getShelter("Not A Real Shelter"));
    }

    @Test
    public void getShelterNeedsIsEmptyWithoutPetsOrMedicalNote() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        assertTrue(app.getShelterNeeds(request).isEmpty());
    }

    @Test
    public void getShelterNeedsIncludesPetFriendlyWhenPartyHasPet() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.addPartyMember("Rex", 4, true, "dog");
        assertTrue(app.getShelterNeeds(request).contains(Accomodation.PET_FRIENDLY));
    }

    @Test
    public void getShelterNeedsListsPetFriendlyOnceForTwoPets() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.addPartyMember("Rex", 4, true, "dog");
        app.addPartyMember("Tom", 2, true, "cat");
        assertEquals(1, app.getShelterNeeds(request).size());
    }

    @Test
    public void getShelterNeedsIncludesMedicalStaffWhenMedicalNote() {
        Request request = createVictimRequest("janedoe", ResponseType.MEDICAL, "insulin");
        assertTrue(app.getShelterNeeds(request).contains(Accomodation.MEDICAL_STAFF));
    }

    @Test
    public void getShelterNeedsOfNullRequestIsEmpty() {
        assertTrue(app.getShelterNeeds(null).isEmpty());
    }

    @Test
    public void findSheltersWithNoNeedsReturnsSheltersWithRoom() {
        // every shelter has at least one open spot
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        assertEquals(10, app.findShelters(request).size());
    }

    @Test
    public void findSheltersSkipsSheltersWithoutRoomForWholeParty() {
        // Military Magnet Academy has one open spot, so a party of two doesn't fit
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.addPartyMember("John Doe", 36, false, "husband");
        assertFalse(app.findShelters(request).contains(app.getShelter("Military Magnet Academy")));
    }

    @Test
    public void findSheltersWithPetOnlyReturnsPetFriendlyShelters() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.addPartyMember("Rex", 4, true, "dog");
        assertEquals(7, app.findShelters(request).size());
    }

    @Test
    public void findSheltersWithMedicalNoteOnlyReturnsSheltersWithMedicalStaff() {
        Request request = createVictimRequest("janedoe", ResponseType.MEDICAL, "insulin");
        assertEquals(7, app.findShelters(request).size());
    }

    @Test
    public void findSheltersWithPetAndMedicalNoteReturnsSheltersWithBoth() {
        Request request = createVictimRequest("janedoe", ResponseType.MEDICAL, "insulin");
        app.addPartyMember("Rex", 4, true, "dog");
        assertEquals(5, app.findShelters(request).size());
    }

    @Test
    public void findSheltersForRequestWithoutAddressIsEmpty() {
        Request request = new Request(UUID.randomUUID(), VOLUNTEER_USERNAME, (byte) 3, null,
                ResponseStatus.WAITING, ResponseType.TRAVEL, "Need a ride", null);
        assertTrue(app.findShelters(request).isEmpty());
    }

    @Test
    public void findSheltersForNullRequestIsEmpty() {
        assertTrue(app.findShelters(null).isEmpty());
    }

    @Test
    public void findClosestShelterPicksShelterAtPickupAddress() {
        assumeNotNull(System.getenv("MAP_API_KEY"));
        app.createAccount("Jane", "Doe", "janedoe", "jane.doe@email.com", "janepass1",
                new Address("6401 Dorchester Road", "North Charleston", "SC", "29418"));
        Request request = app.newRequest((byte) 3, ResponseType.TRAVEL, "Need a ride", null);
        assertEquals(WORKER_SHELTER, app.findClosestShelter(request).getName());
    }

    @Test
    public void findClosestShelterForNullRequestIsNull() {
        assertNull(app.findClosestShelter(null));
    }

    @Test
    public void getMyShelterAsShelterWorkerReturnsTheirShelter() {
        loginShelterWorker();
        assertEquals(WORKER_SHELTER, app.getMyShelter().getName());
    }

    @Test
    public void getMyShelterReturnsTheCopyThatGetsSaved() {
        loginShelterWorker();
        assertSame(app.getShelter(WORKER_SHELTER), app.getMyShelter());
    }

    @Test
    public void getMyShelterForWorkerWithoutShelterReturnsNull() {
        UserList.getInstance().addUser(new ShelterWorker(UUID.randomUUID(), "Sam", "Hill", "samhill", "samspass1",
                "sam.hill@email.com", downtownCharleston(), new ArrayList<Alert>(), null));
        app.login("samhill", "samspass1");
        assertNull(app.getMyShelter());
    }

    @Test
    public void getMyShelterAsVolunteerReturnsNull() {
        loginVolunteer();
        assertNull(app.getMyShelter());
    }

    // ---------------------------------------------------------------- checkIn

    @Test
    public void checkInAsShelterWorkerReturnsTrue() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.logout();
        loginShelterWorker();
        assertTrue(app.checkIn(request));
    }

    @Test
    public void checkInAddsPeopleToUsedCapacity() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.addPartyMember("John Doe", 36, false, "husband");
        app.logout();
        loginShelterWorker();
        app.checkIn(request);
        assertEquals(102, app.getMyShelter().getUsedCapacity());
    }

    @Test
    public void checkInDoesNotCountPetsTowardUsedCapacity() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.addPartyMember("Rex", 4, true, "dog");
        app.logout();
        loginShelterWorker();
        app.checkIn(request);
        assertEquals(101, app.getMyShelter().getUsedCapacity());
    }

    @Test
    public void checkInRegistersPetsSeparately() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.addPartyMember("Rex", 4, true, "dog");
        app.logout();
        loginShelterWorker();
        app.checkIn(request);
        assertTrue(app.getMyShelter().getRegisteredPets().containsKey("Rex"));
    }

    @Test
    public void checkInRegistersRequesterAsOccupant() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.logout();
        loginShelterWorker();
        app.checkIn(request);
        assertTrue(app.getMyShelter().getRegisteredPeople().containsKey("Jane Doe"));
    }

    @Test
    public void checkInWithoutRoomForWholePartyReturnsFalse() {
        // Military Magnet Academy has one open spot
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.addPartyMember("John Doe", 36, false, "husband");
        app.logout();
        UserList.getInstance().addUser(new ShelterWorker(UUID.randomUUID(), "Sam", "Hill", "samhill", "samspass1",
                "sam.hill@email.com", downtownCharleston(), new ArrayList<Alert>(),
                app.getShelter("Military Magnet Academy")));
        app.login("samhill", "samspass1");
        assertFalse(app.checkIn(request));
    }

    @Test
    public void checkInAsVolunteerReturnsFalse() {
        Request request = createVictimRequest("janedoe", ResponseType.TRAVEL, null);
        app.logout();
        loginVolunteer();
        assertFalse(app.checkIn(request));
    }

    @Test
    public void checkInNullRequestReturnsFalse() {
        loginShelterWorker();
        assertFalse(app.checkIn(null));
    }

    // ---------------------------------------------------------------- hurricanes

    @Test
    public void getHurricaneByNameFindsIt() {
        assertEquals(HurricaneStatus.IN_PROGRESS, app.getHurricane("Scarlett").getStatus());
    }

    @Test
    public void getHurricaneWithUnknownNameReturnsNull() {
        assertNull(app.getHurricane("Not A Real Storm"));
    }

    @Test
    public void isAffectedWhenNobodyLoggedInIsFalse() {
        assertFalse(app.isAffected());
    }

    @Test
    public void isAffectedForUserWithoutAddressIsFalse() {
        UserList.getInstance().addUser(new Victim("Jane", "Doe", "janedoe", "jane.doe@email.com", "janepass1", null));
        app.login("janedoe", "janepass1");
        assertFalse(app.isAffected());
    }

    @Test
    public void isAffectedIsTrueForUserNearActiveStorm() {
        assumeNotNull(System.getenv("MAP_API_KEY"));
        loginVolunteer();
        assertTrue(app.isAffected());
    }

    @Test
    public void isAffectedIsFalseWhenStormIsOver() {
        // without a key every distance is unknown, so this would pass for the wrong reason
        assumeNotNull(System.getenv("MAP_API_KEY"));
        app.getHurricane("Scarlett").endHurricane();
        loginVolunteer();
        assertFalse(app.isAffected());
    }

    // ---------------------------------------------------------------- saveData

    @Test
    public void saveDataReturnsTrue() {
        assertTrue(app.saveData());
    }

    @Test
    public void saveDataKeepsChangesAfterReload() throws Exception {
        app.getRequest(MEDICAL_REQUEST_ID).updateStatus(ResponseStatus.COMPLETE);
        app.saveData();
        clearSingletons();
        app = HurricaneApplication.getInstance();
        assertEquals(ResponseStatus.COMPLETE, app.getRequest(MEDICAL_REQUEST_ID).getStatus());
    }
}
