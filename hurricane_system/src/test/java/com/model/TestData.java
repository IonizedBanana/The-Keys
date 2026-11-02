package com.model;

/*
 * +--------------------------------------------------+------------------------------------------+
 * | Test                                             | Reasoning                                |
 * +--------------------------------------------------+------------------------------------------+
 * | Use the testing file path for test data          | make sure our data tests wont effect     |
 * |                                                  | production data (all four file types)    |
 * | Testing out paths differ from the input paths    | writing must never overwrite the fixture |
 * | Read Shelters                                    | make sure we properly read in Shelters   |
 * | Read Users                                       | make sure we properly read in Users      |
 * | Read Requests                                    | make sure we properly read in Requests   |
 * | Read Hurricane                                   | make sure we properly read in Hurricanes |
 * | Read collection sizes                            | every record in the file is read in      |
 * | Saving returns true                              | a valid save reports success             |
 * | Reject null data when saving                     | if we try to save null data, nothing     |
 * |                                                  | should happen (returns false)            |
 * | Save empty collections                           | empty collections write an empty list    |
 * | Empty lists stay empty when written              | empty lists should stay empty            |
 * | Written data contains all read data              | every value we read is present in the    |
 * |                                                  | file we write (all records, all types)   |
 * | Read data contains all written data              | every value we write is present in the   |
 * |                                                  | data we read (all records, all types);   |
 * |                                                  | null/empty extras are ignored            |
 * +--------------------------------------------------+------------------------------------------+
 */

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;

import com.model.users.*;

public class TestData {
  @Before
  public void createOutputDirectory() {
    new File("./src/test/resources/data/out").mkdirs();
  }

  @Test
  public void testUserTestingFilePath() {
    assertEquals(DataConstants.USER_FILE_PATH_TESTING,
        DataConstants.getFilePath(DataConstants.USER_FILE_PATH, DataConstants.USER_FILE_PATH_TESTING));
  }

  @Test
  public void testShelterTestingFilePath() {
    assertEquals(DataConstants.SHELTER_FILE_PATH_TESTING,
        DataConstants.getFilePath(DataConstants.SHELTER_FILE_PATH, DataConstants.SHELTER_FILE_PATH_TESTING));
  }

  @Test
  public void testRequestTestingFilePath() {
    assertEquals(DataConstants.REQUEST_FILE_PATH_TESTING,
        DataConstants.getFilePath(DataConstants.REQUEST_FILE_PATH, DataConstants.REQUEST_FILE_PATH_TESTING));
  }

  @Test
  public void testHurricaneTestingFilePath() {
    assertEquals(DataConstants.HURRICANE_FILE_PATH_TESTING,
        DataConstants.getFilePath(DataConstants.HURRICANE_FILE_PATH, DataConstants.HURRICANE_FILE_PATH_TESTING));
  }

  @Test
  public void testReadShelterId() {
    assertEquals(UUID.fromString("11111111-0000-0000-0000-000000000001"),
        DataReader.getShelters().get("Military Magnet Academy").getId());
  }

  @Test
  public void testReadShelterName() {
    assertEquals("Military Magnet Academy", DataReader.getShelters().get("Military Magnet Academy").getName());
  }

  @Test
  public void testReadShelterAddressStreet() {
    assertEquals("2950 Carner Avenue",
        DataReader.getShelters().get("Military Magnet Academy").getAddress().getStreet());
  }

  @Test
  public void testReadShelterAddressCity() {
    assertEquals("North Charleston", DataReader.getShelters().get("Military Magnet Academy").getAddress().getCity());
  }

  @Test
  public void testReadShelterAddressState() {
    assertEquals("SC", DataReader.getShelters().get("Military Magnet Academy").getAddress().getState());
  }

  @Test
  public void testReadShelterAddressZip() {
    assertEquals("29405", DataReader.getShelters().get("Military Magnet Academy").getAddress().getZip());
  }

  @Test
  public void testReadShelterTotalCapacity() {
    assertEquals(250, DataReader.getShelters().get("Military Magnet Academy").getTotalCapacity());
  }

  @Test
  public void testReadShelterUsedCapacity() {
    assertEquals(249, DataReader.getShelters().get("Military Magnet Academy").getUsedCapacity());
  }

  @Test
  public void testReadShelterExpectedArrivals() {
    assertEquals(0, DataReader.getShelters().get("Military Magnet Academy").getExpectedArrivals());
  }

  @Test
  public void testReadShelterResourceType() {
    assertEquals(ResourceType.WATER,
        DataReader.getShelters().get("Military Magnet Academy").getResources().get(0).getType());
  }

  @Test
  public void testReadShelterResourceDescription() {
    assertEquals("jugs of water",
        DataReader.getShelters().get("Military Magnet Academy").getResources().get(0).getDescription());
  }

  @Test
  public void testReadShelterResourceQuantity() {
    assertEquals(40, DataReader.getShelters().get("Military Magnet Academy").getResources().get(0).getQuantity());
  }

  @Test
  public void testReadShelterResourceUnit() {
    assertEquals(Unit.GALLONS, DataReader.getShelters().get("Military Magnet Academy").getResources().get(0).getUnit());
  }

  @Test
  public void testReadShelterAccomodations() {
    assertEquals(2, DataReader.getShelters().get("Military Magnet Academy").getAccomodations().size());
  }

  @Test
  public void testReadShelterFirstAccomodation() {
    assertEquals(Accomodation.PET_FRIENDLY,
        DataReader.getShelters().get("Military Magnet Academy").getAccomodations().get(0));
  }

  @Test
  public void testReadShelterRegisteredPeople() {
    assertEquals(0, DataReader.getShelters().get("Military Magnet Academy").getRegisteredPeople().size());
  }

  @Test
  public void testReadShelterRegisteredPets() {
    assertEquals(0, DataReader.getShelters().get("Military Magnet Academy").getRegisteredPets().size());
  }

  @Test
  public void testReadUserId() {
    assertEquals(UUID.fromString("22222222-0000-0000-0000-000000000001"),
        DataReader.getUsers().get("rdelgado").getId());
  }

  @Test
  public void testReadUserType() {
    assertEquals(UserType.VOLUNTEER, DataReader.getUsers().get("rdelgado").getType());
  }

  @Test
  public void testReadUserFirstName() {
    assertEquals("Rosa", DataReader.getUsers().get("rdelgado").getFirstName());
  }

  @Test
  public void testReadUserLastName() {
    assertEquals("Delgado", DataReader.getUsers().get("rdelgado").getLastName());
  }

  @Test
  public void testReadUserUsername() {
    assertEquals("rdelgado", DataReader.getUsers().get("rdelgado").getUsername());
  }

  @Test
  public void testReadUserEmail() {
    assertEquals("rosa.delgado@email.com", DataReader.getUsers().get("rdelgado").getEmailAddress());
  }

  @Test
  public void testReadUserPassword() {
    assertEquals("delgado123", DataReader.getUsers().get("rdelgado").getPassword());
  }

  @Test
  public void testReadUserAddressStreet() {
    assertEquals("1 Vendue Range", DataReader.getUsers().get("rdelgado").getAddress().getStreet());
  }

  @Test
  public void testReadUserAddressCity() {
    assertEquals("Charleston", DataReader.getUsers().get("rdelgado").getAddress().getCity());
  }

  @Test
  public void testReadUserAddressState() {
    assertEquals("SC", DataReader.getUsers().get("rdelgado").getAddress().getState());
  }

  @Test
  public void testReadUserAddressZip() {
    assertEquals("29401", DataReader.getUsers().get("rdelgado").getAddress().getZip());
  }

  @Test
  public void testReadUserReceivedAlerts() {
    assertEquals(0, DataReader.getUsers().get("rdelgado").getReceivedAlerts().size());
  }

  @Test
  public void testReadVolunteerCredentials() {
    assertEquals(0, ((Volunteer) DataReader.getUsers().get("rdelgado")).getCredentials().size());
  }

  @Test
  public void testReadVolunteerFieldsOfExpertise() {
    assertEquals(ResponseType.TRAVEL,
        ((Volunteer) DataReader.getUsers().get("rdelgado")).getFieldsOfExpertise().get(0));
  }

  @Test
  public void testReadVolunteerIdentityVerified() {
    assertTrue(((Volunteer) DataReader.getUsers().get("rdelgado")).getIdentityVerified());
  }

  @Test
  public void testReadVolunteerAvailable() {
    assertTrue(((Volunteer) DataReader.getUsers().get("rdelgado")).getIsAvailible());
  }

  @Test
  public void testReadRequestId() {
    assertEquals(UUID.fromString("33333333-0000-0000-0000-000000000001"),
        DataReader.getRequests().get(UUID.fromString("33333333-0000-0000-0000-000000000001")).getId());
  }

  @Test
  public void testReadRequestRequestee() {
    assertEquals("jsmith",
        DataReader.getRequests().get(UUID.fromString("33333333-0000-0000-0000-000000000001")).getRequestee());
  }

  @Test
  public void testReadRequestSeverity() {
    assertEquals(5,
        DataReader.getRequests().get(UUID.fromString("33333333-0000-0000-0000-000000000001")).getSeverity());
  }

  @Test
  public void testReadRequestLocationStreet() {
    assertEquals("100 Calhoun Street",
        DataReader.getRequests().get(UUID.fromString("33333333-0000-0000-0000-000000000001")).getAddress().getStreet());
  }

  @Test
  public void testReadRequestLocationCity() {
    assertEquals("Charleston",
        DataReader.getRequests().get(UUID.fromString("33333333-0000-0000-0000-000000000001")).getAddress().getCity());
  }

  @Test
  public void testReadRequestLocationState() {
    assertEquals("SC",
        DataReader.getRequests().get(UUID.fromString("33333333-0000-0000-0000-000000000001")).getAddress().getState());
  }

  @Test
  public void testReadRequestLocationZip() {
    assertEquals("29401",
        DataReader.getRequests().get(UUID.fromString("33333333-0000-0000-0000-000000000001")).getAddress().getZip());
  }

  @Test
  public void testReadRequestStatus() {
    assertEquals(ResponseStatus.WAITING,
        DataReader.getRequests().get(UUID.fromString("33333333-0000-0000-0000-000000000001")).getStatus());
  }

  @Test
  public void testReadRequestResponderType() {
    assertEquals(ResponseType.MEDICAL,
        DataReader.getRequests().get(UUID.fromString("33333333-0000-0000-0000-000000000001")).getResponderType());
  }

  @Test
  public void testReadRequestDescription() {
    assertEquals("Cut on leg needs stitches",
        DataReader.getRequests().get(UUID.fromString("33333333-0000-0000-0000-000000000001")).getDescription());
  }

  @Test
  public void testReadRequestMedicalNote() {
    assertNull(DataReader.getRequests().get(UUID.fromString("33333333-0000-0000-0000-000000000001")).getMedicalNote());
  }

  @Test
  public void testReadHurricaneId() {
    assertEquals(UUID.fromString("44444444-0000-0000-0000-000000000001"),
        DataReader.getHurricanes().get("Scarlett").getId());
  }

  @Test
  public void testReadHurricaneName() {
    assertEquals("Scarlett", DataReader.getHurricanes().get("Scarlett").getName());
  }

  @Test
  public void testReadHurricaneCategory() {
    assertEquals(4, DataReader.getHurricanes().get("Scarlett").getCategory());
  }

  @Test
  public void testReadHurricaneStatus() {
    assertEquals(HurricaneStatus.IN_PROGRESS, DataReader.getHurricanes().get("Scarlett").getStatus());
  }

  @Test
  public void testReadHurricaneCurrentAddressCity() {
    assertEquals("Charleston", DataReader.getHurricanes().get("Scarlett").getCurrentAddress().getCity());
  }

  @Test
  public void testReadHurricaneCurrentAddressState() {
    assertEquals("SC", DataReader.getHurricanes().get("Scarlett").getCurrentAddress().getState());
  }

  @Test
  public void testReadHurricanePredictedPath() {
    assertEquals(1, DataReader.getHurricanes().get("Scarlett").getPredictedPath().size());
  }

  @Test
  public void testReadHurricaneImpactArea() {
    assertEquals(1, DataReader.getHurricanes().get("Scarlett").getImpactArea().size());
  }

  @Test
  public void testSaveNullSheltersReturnsFalse() {
    assertFalse(DataWriter.saveShelters(null));
  }

  @Test
  public void testSaveNullUsersReturnsFalse() {
    assertFalse(DataWriter.saveUsers(null));
  }

  @Test
  public void testSaveNullRequestsReturnsFalse() {
    assertFalse(DataWriter.saveRequests(null));
  }

  @Test
  public void testSaveNullHurricanesReturnsFalse() {
    assertFalse(DataWriter.saveHurricanes(null));
  }

  @Test
  public void testSaveEmptyShelters() {
    assertTrue(DataWriter.saveShelters(new HashMap<>()));
  }

  @Test
  public void testSaveEmptyUsers() {
    assertTrue(DataWriter.saveUsers(new HashMap<>()));
  }

  @Test
  public void testSaveEmptyRequests() {
    assertTrue(DataWriter.saveRequests(new HashMap<>()));
  }

  @Test
  public void testSaveEmptyHurricanes() {
    assertTrue(DataWriter.saveHurricanes(new HashMap<>()));
  }

  // ---- collection sizes ----

  @Test
  public void testReadShelterCount() {
    assertEquals(10, DataReader.getShelters().size());
  }

  @Test
  public void testReadUserCount() {
    assertEquals(2, DataReader.getUsers().size());
  }

  @Test
  public void testReadRequestCount() {
    assertEquals(6, DataReader.getRequests().size());
  }

  @Test
  public void testReadHurricaneCount() {
    assertEquals(1, DataReader.getHurricanes().size());
  }

  // ---- empty collections stay empty ----

  @Test
  public void testSaveEmptySheltersWritesEmptyList() throws Exception {
    DataWriter.saveShelters(new HashMap<>());
    assertTrue(readArray(DataConstants.SHELTER_FILE_PATH_TESTING_OUT).isEmpty());
  }

  @Test
  public void testSaveEmptyUsersWritesEmptyList() throws Exception {
    DataWriter.saveUsers(new HashMap<>());
    assertTrue(readArray(DataConstants.USER_FILE_PATH_TESTING_OUT).isEmpty());
  }

  @Test
  public void testSaveEmptyRequestsWritesEmptyList() throws Exception {
    DataWriter.saveRequests(new HashMap<>());
    assertTrue(readArray(DataConstants.REQUEST_FILE_PATH_TESTING_OUT).isEmpty());
  }

  @Test
  public void testSaveEmptyHurricanesWritesEmptyList() throws Exception {
    DataWriter.saveHurricanes(new HashMap<>());
    assertTrue(readArray(DataConstants.HURRICANE_FILE_PATH_TESTING_OUT).isEmpty());
  }

  // ---- testing out paths are separate from the inputs ----

  @Test
  public void testShelterOutPathIsSeparateFromInputPath() {
    assertNotEquals(DataConstants.SHELTER_FILE_PATH_TESTING, DataConstants.SHELTER_FILE_PATH_TESTING_OUT);
  }

  @Test
  public void testUserOutPathIsSeparateFromInputPath() {
    assertNotEquals(DataConstants.USER_FILE_PATH_TESTING, DataConstants.USER_FILE_PATH_TESTING_OUT);
  }

  @Test
  public void testRequestOutPathIsSeparateFromInputPath() {
    assertNotEquals(DataConstants.REQUEST_FILE_PATH_TESTING, DataConstants.REQUEST_FILE_PATH_TESTING_OUT);
  }

  @Test
  public void testHurricaneOutPathIsSeparateFromInputPath() {
    assertNotEquals(DataConstants.HURRICANE_FILE_PATH_TESTING, DataConstants.HURRICANE_FILE_PATH_TESTING_OUT);
  }

  // ---- saving returns true ----

  @Test
  public void testSaveSheltersReturnsTrue() {
    assertTrue(DataWriter.saveShelters(DataReader.getShelters()));
  }

  @Test
  public void testSaveUsersReturnsTrue() {
    assertTrue(DataWriter.saveUsers(DataReader.getUsers()));
  }

  @Test
  public void testSaveRequestsReturnsTrue() {
    assertTrue(DataWriter.saveRequests(DataReader.getRequests()));
  }

  @Test
  public void testSaveHurricanesReturnsTrue() {
    assertTrue(DataWriter.saveHurricanes(DataReader.getHurricanes()));
  }

  // ---- empty lists stay empty when written ----

  @Test
  public void testShelterRegisteredPeopleStaysEmptyWhenWritten() throws Exception {
    DataWriter.saveShelters(DataReader.getShelters());
    JSONObject o = findByValue(readArray(DataConstants.SHELTER_FILE_PATH_TESTING_OUT), DataConstants.SHELTER_NAME,
        "Military Magnet Academy");
    assertTrue(((JSONArray) o.get(DataConstants.SHELTER_REGISTERED_PEOPLE)).isEmpty());
  }

  @Test
  public void testShelterRegisteredPetsStaysEmptyWhenWritten() throws Exception {
    DataWriter.saveShelters(DataReader.getShelters());
    JSONObject o = findByValue(readArray(DataConstants.SHELTER_FILE_PATH_TESTING_OUT), DataConstants.SHELTER_NAME,
        "Military Magnet Academy");
    assertTrue(((JSONArray) o.get(DataConstants.SHELTER_REGISTERED_PETS)).isEmpty());
  }

  @Test
  public void testUserReceivedAlertsStaysEmptyWhenWritten() throws Exception {
    DataWriter.saveUsers(DataReader.getUsers());
    JSONObject o = findByValue(readArray(DataConstants.USER_FILE_PATH_TESTING_OUT), DataConstants.USER_USERNAME,
        "rdelgado");
    assertTrue(((JSONArray) o.get(DataConstants.USER_RECEIVED_ALERTS)).isEmpty());
  }

  @Test
  public void testVolunteerCredentialsStaysEmptyWhenWritten() throws Exception {
    DataWriter.saveUsers(DataReader.getUsers());
    JSONObject o = findByValue(readArray(DataConstants.USER_FILE_PATH_TESTING_OUT), DataConstants.USER_USERNAME,
        "rdelgado");
    assertTrue(((JSONArray) o.get(DataConstants.VOLUNTEER_CREDENTIALS)).isEmpty());
  }

  // ---- written data contains all read data ----

  @Test
  public void testWrittenSheltersContainAllReadData() throws Exception {
    DataWriter.saveShelters(DataReader.getShelters());
    assertEquals("", findProblems(readArray(DataConstants.SHELTER_FILE_PATH_TESTING),
        readArray(DataConstants.SHELTER_FILE_PATH_TESTING_OUT), false));
  }

  @Test
  public void testWrittenUsersContainAllReadData() throws Exception {
    DataWriter.saveUsers(DataReader.getUsers());
    assertEquals("", findProblems(readArray(DataConstants.USER_FILE_PATH_TESTING),
        readArray(DataConstants.USER_FILE_PATH_TESTING_OUT), false));
  }

  @Test
  public void testWrittenRequestsContainAllReadData() throws Exception {
    DataWriter.saveRequests(DataReader.getRequests());
    assertEquals("", findProblems(readArray(DataConstants.REQUEST_FILE_PATH_TESTING),
        readArray(DataConstants.REQUEST_FILE_PATH_TESTING_OUT), false));
  }

  @Test
  public void testWrittenHurricanesContainAllReadData() throws Exception {
    DataWriter.saveHurricanes(DataReader.getHurricanes());
    assertEquals("", findProblems(readArray(DataConstants.HURRICANE_FILE_PATH_TESTING),
        readArray(DataConstants.HURRICANE_FILE_PATH_TESTING_OUT), false));
  }

  // ---- read data contains all written data (null/empty extras the writer adds
  // are ignored) ----

  @Test
  public void testReadSheltersContainAllWrittenData() throws Exception {
    DataWriter.saveShelters(DataReader.getShelters());
    assertEquals("", findProblems(readArray(DataConstants.SHELTER_FILE_PATH_TESTING_OUT),
        readArray(DataConstants.SHELTER_FILE_PATH_TESTING), true));
  }

  @Test
  public void testReadUsersContainAllWrittenData() throws Exception {
    DataWriter.saveUsers(DataReader.getUsers());
    assertEquals("", findProblems(readArray(DataConstants.USER_FILE_PATH_TESTING_OUT),
        readArray(DataConstants.USER_FILE_PATH_TESTING), true));
  }

  @Test
  public void testReadRequestsContainAllWrittenData() throws Exception {
    DataWriter.saveRequests(DataReader.getRequests());
    assertEquals("", findProblems(readArray(DataConstants.REQUEST_FILE_PATH_TESTING_OUT),
        readArray(DataConstants.REQUEST_FILE_PATH_TESTING), true));
  }

  @Test
  public void testReadHurricanesContainAllWrittenData() throws Exception {
    DataWriter.saveHurricanes(DataReader.getHurricanes());
    assertEquals("", findProblems(readArray(DataConstants.HURRICANE_FILE_PATH_TESTING_OUT),
        readArray(DataConstants.HURRICANE_FILE_PATH_TESTING), true));
  }

  // ---- helpers ----
  // ignoreEmptyExtras: when expected is the written file, keys holding null or an
  // empty
  // list/object may be missing from actual (the writer adding those doesn't
  // change the data)
  // returns "" when every record in expected is contained in actual, else a
  // description of the problems
  // ignoreEmptyExtras: when expected is the written file, keys holding null or an
  // empty
  // list/object may be missing from actual (the writer adding those doesn't
  // change the data)
  private String findProblems(JSONArray expected, JSONArray actual, boolean ignoreEmptyExtras) {
    StringBuilder problems = new StringBuilder();
    if (expected.size() != actual.size()) {
      problems.append("record count: expected " + expected.size() + " but was " + actual.size() + "\n");
    }
    for (Object e : expected) {
      JSONObject exp = (JSONObject) e;
      JSONObject act = null;
      for (Object a : actual) {
        if (exp.get("id").equals(((JSONObject) a).get("id"))) {
          act = (JSONObject) a;
        }
      }
      if (act == null) {
        problems.append("missing record " + exp.get("id") + "\n");
      } else if (!contains(exp, act, ignoreEmptyExtras)) {
        problems.append("record " + exp.get("id") + " differs\nexpected: " + exp + "\nactual:   " + act + "\n");
      }
    }
    return problems.toString();
  }

  // strict: every key in expected (null values included) must exist in actual
  // with an equal value
  private boolean contains(Object expected, Object actual, boolean ignoreEmpty) {
    if (expected instanceof JSONObject) {
      if (!(actual instanceof JSONObject)) {
        return false;
      }
      JSONObject e = (JSONObject) expected;
      JSONObject a = (JSONObject) actual;
      for (Object key : e.keySet()) {
        if (ignoreEmpty && !a.containsKey(key) && isEmptyValue(e.get(key))) {
          continue;
        }
        if (!a.containsKey(key) || !contains(e.get(key), a.get(key), ignoreEmpty)) {
          return false;
        }
      }
      return true;
    }
    if (expected instanceof JSONArray) { // order-insensitive
      if (!(actual instanceof JSONArray) || ((JSONArray) expected).size() != ((JSONArray) actual).size()) {
        return false;
      }
      java.util.List<Object> remaining = new java.util.ArrayList<>((JSONArray) actual);
      outer: for (Object e : (JSONArray) expected) {
        for (int i = 0; i < remaining.size(); i++) {
          if (contains(e, remaining.get(i), ignoreEmpty)) {
            remaining.remove(i);
            continue outer;
          }
        }
        return false;
      }
      return true;
    }
    return java.util.Objects.equals(expected, actual);
  }

  private boolean isEmptyValue(Object v) {
    return v == null || (v instanceof JSONObject && ((JSONObject) v).isEmpty())
        || (v instanceof JSONArray && ((JSONArray) v).isEmpty());
  }

  private JSONArray readArray(String path) throws Exception {
    try (FileReader reader = new FileReader(path)) {
      return (JSONArray) new JSONParser().parse(reader);
    }
  }

  private JSONObject findByValue(JSONArray array, String key, Object value) {
    for (Object item : array) {
      JSONObject object = (JSONObject) item;
      if (value.equals(object.get(key))) {
        return object;
      }
    }
    throw new AssertionError("No JSON object found with " + key + "=" + value);
  }
}
