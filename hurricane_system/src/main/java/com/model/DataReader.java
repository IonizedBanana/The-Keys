package com.model;

import com.model.users.*;
import com.model.location.*;

import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * DataReader
 *
 * @author IonizedBanana (Cayden)
 */
public class DataReader extends DataConstants {
  /**
   * getShelters method
   * 
   * @return a hashmap where the String is the shelter's name
   * @see {@link DataReader#getShelter}
   * @see {@link Shelter}
   */
  public static HashMap<String, Shelter> getShelters() {
    HashMap<String, Shelter> shelters = new HashMap<String, Shelter>();
    File shelterFile = new File(SHELTER_FILE_PATH);
    System.out.println(SHELTER_FILE_PATH);
    try {
      FileReader reader = new FileReader(shelterFile);
      JSONArray shelterJSONArray = (JSONArray) new JSONParser().parse(reader);

      for (int i = 0; i < shelterJSONArray.size(); i++) {
        JSONObject shelterObject = (JSONObject) shelterJSONArray.get(i);
        Shelter shelter = getShelter(shelterObject);
        shelters.put(shelter.getName(), shelter);
      }
      reader.close();
      return shelters;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
      return null;
    }
  }

  /**
   * @param shelterObject the JSONObject of a shelter
   * @return a shelter object from the provided JSON
   * @see {@link Shelter}
   */
  private static Shelter getShelter(JSONObject shelterObject) {
    UUID id = UUID.fromString((String) shelterObject.get(SHELTER_UUID));
    String name = (String) shelterObject.get(SHELTER_NAME);
    Address address = getAddress((JSONObject) shelterObject.get(SHELTER_ADDRESS));
    int totalCapacity = ((Long) shelterObject.get(SHELTER_TOTAL_CAPACITY)).intValue();
    int usedCapacity = ((Long) shelterObject.get(SHELTER_USED_CAPACITY)).intValue();
    int expectedArrivals = ((Long) shelterObject.get(SHELTER_EXPECTED_ARRIVALS)).intValue();
    ArrayList<Resource> resources = getResourceList((JSONArray) shelterObject.get(SHELTER_RESOURCES));
    ArrayList<Accomodation> accomodations = getAccomodationList((JSONArray) shelterObject.get(SHELTER_ACCOMODATIONS));
    HashMap<String, Victim.Person> registeredPeople = getPeopleMap((JSONArray) shelterObject.get(SHELTER_REGISTERED_PEOPLE));
    HashMap<String, Victim.Person> registeredPets = getPeopleMap((JSONArray) shelterObject.get(SHELTER_REGISTERED_PETS));
    return new Shelter(id, name, address, totalCapacity, usedCapacity, expectedArrivals, resources, accomodations, registeredPeople, registeredPets);
  }

  private static HashMap<String, Victim.Person> getPeopleMap(JSONArray arr) {
    HashMap<String, Victim.Person> peopleMap = new HashMap<String, Victim.Person>();
    for (int i = 0; i < arr.size(); i++) {
      Victim.Person person = getPerson((JSONObject)arr.get(i));
      peopleMap.put(person.getName(), person);
    }
    return peopleMap;
  }

  private static Accomodation getAccomodation(String type) {
    if (type.equals("PET_FRIENDLY")) {
      return Accomodation.PET_FRIENDLY;
    } else if (type.equals("MEDICAL_STAFF")) {
      return Accomodation.MEDICAL_STAFF;
    }
    return null;
  }

  private static ArrayList<Accomodation> getAccomodationList(JSONArray arr) {
    ArrayList<Accomodation> accomodations = new ArrayList<Accomodation>();
    for (int i = 0; i < arr.size(); i++) {
      accomodations.add(getAccomodation((String)arr.get(i)));
    }
    return accomodations;
  }

  /**
   * @param addressObject the JSONObject of an Address
   * @return an Address object from the provided JSON
   * @see {@link Address}
   */
  private static Address getAddress(JSONObject addressObject) {
    String state = (String) addressObject.get(ADDRESS_STATE);
    String city = (String) addressObject.get(ADDRESS_CITY);
    String address_string = (String) addressObject.get(ADDRESS_ADDRESS);
    String zip = (String) addressObject.get(ADDRESS_ZIP);
    if (address_string == null && zip == null) {
      Address address = new Address(city, state);
      return address;
    }
    Address address = new Address(address_string, city, state, zip);
    return address;
  }

  /**
   * @param arr a JSON array of Resources
   * @return an ArrayList of Resources
   * @see {@link DataReader#getResource}
   * @see {@link Resource}
   */
  private static ArrayList<Resource> getResourceList(JSONArray arr) {
    ArrayList<Resource> resources = new ArrayList<Resource>();
    for (int i = 0; i < arr.size(); i++) {
      JSONObject resourceObject = (JSONObject) arr.get(i);
      Resource resource = getResource(resourceObject);
      resources.add(resource);
    }
    return resources;
  }

  /**
   * @param resourceObject the JSONObject for a Resource
   * @return a Resource from the provided JSON
   * @see {@link Resource}
   */
  private static Resource getResource(JSONObject resourceObject) {
    ResourceType type = getResourceType((String) resourceObject.get(RESOURCE_TYPE));
    String description = (String) resourceObject.get(REQUEST_DESCRIPTION);
    int quantity = ((Long) resourceObject.get(RESOURCE_QUANTITY)).intValue();
    Unit unit = getUnit((String) resourceObject.get(RESOURCE_UNIT));
    Resource resource = new Resource(type, description, quantity, unit);
    return resource;
  }

  /**
   * @param unit the string version of a unit
   * @return the Unit enum variant specified
   * @implNote if the string doesnt correspond to a Unit variant, it returns null
   * @see {@link Unit}
   */
  private static Unit getUnit(String unit) {
    if (unit.equals("GALLONS")) {
      return Unit.GALLONS;
    } else if (unit.equals("POUNDS")) {
      return Unit.POUNDS;
    } else if (unit.equals("CANS")) {
      return Unit.CANS;
    } else if (unit.equals("MEALS")) {
      return Unit.MEALS;
    } else if (unit.equals("QTY")) {
      return Unit.QTY;
    }
    return null;
  }

  /**
   * @param type the string version of a ResourceType
   * @return the ResourceType specified
   * @implNote if the string doesnt correspond to a ResourceType variant, it
   *           returns null
   * @see {@link ResourceType}
   */
  private static ResourceType getResourceType(String type) {
    if (type.equals("WATER")) {
      return ResourceType.WATER;
    } else if (type.equals("FOOD")) {
      return ResourceType.FOOD;
    } else if (type.equals("MEDICAL")) {
      return ResourceType.MEDICAL;
    } else if (type.equals("BABY_NEEDS")) {
      return ResourceType.BABY_NEEDS;
    } else if (type.equals("CLOTHES")) {
      return ResourceType.CLOTHES;
    } else if (type.equals("OTHER")) {
      return ResourceType.OTHER;
    }
    return null;
  }

  /**
   * @return a HashMap of all the users, where the string is the user's username
   * @implNote uses {@link DataReader#getUser} to get the actual User object
   * @see {@link User}
   * @see {@link Volunteer}
   * @see {@link Victim}
   * @see {@link Admin}
   * @see {@link Dispatcher}
   * @see {@link ShelterWorker}
   */
  public static HashMap<String, User> getUsers() {
    HashMap<String, User> users = new HashMap<String, User>();
    File userFile = new File(USER_FILE_PATH);
    // File userFile = new File("../json/testing/user.json");
    try {
      FileReader reader = new FileReader(userFile);
      JSONArray userJSONArray = (JSONArray) new JSONParser().parse(reader);

      for (int i = 0; i < userJSONArray.size(); i++) {
        JSONObject userObject = (JSONObject) userJSONArray.get(i);
        User user = getUser(userObject);
        users.put(user.getUsername(), user);
      }
      reader.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
    return users;
  }

  /**
   * @param type the string version of a UserType
   * @return the UserType specified
   * @implNote if the string doesnt correspond to a UserType variant, it
   *           returns null
   * @see {@link UserType}
   */
  private static UserType getUserType(String type) {
    if (type.equals("VICTIM")) {
      return UserType.VICTIM;
    } else if (type.equals("VOLUNTEER")) {
      return UserType.VOLUNTEER;
    } else if (type.equals("ADMIN")) {
      return UserType.ADMIN;
    } else if (type.equals("DISPATCHER")) {
      return UserType.DISPATCHER;
    } else if (type.equals("SHELTER_WORKER")) {
      return UserType.SHELTER_WORKER;
    }
    return null;
  }

  /**
   * @param arr a JSONArray of Alerts
   * @return an ArrayList of Alerts
   * @see {@link DataReader#getAlert}
   * @see {@link Alert}
   */
  private static ArrayList<Alert> getReceivedAlerts(JSONArray arr) {
    ArrayList<Alert> alerts = new ArrayList<Alert>();
    if (arr == null || arr.size() < 1) {
      return alerts;
    }
    for (int i = 0; i < arr.size(); i++) {
      JSONObject o = (JSONObject) arr.get(i);
      Alert alert = getAlert(o);
      alerts.add(alert);
    }
    return alerts;
  }

  /**
   * @param alert a JSONObject of an alert
   * @return the Alert provided in the JSON
   * @see {@link Alert}
   */
  private static Alert getAlert(JSONObject alert) {
    ArrayList<Address> affectedAddresss = getAddressList((JSONArray) alert.get(ALERT_AFFECTED_LOCATIONS));
    String description = (String) alert.get(ALERT_DESCRIPTION);
    String issuedBy = (String) alert.get(ALERT_ISSUED_BY);
    Date timestamp;
    try {
      timestamp = DateFormat.getDateTimeInstance().parse((String) alert.get(ALERT_TIMESTAMP));
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
      timestamp = null;
    }
    return new Alert(affectedAddresss, description, issuedBy, timestamp);
  }

  /**
   * @return a hashmap of Requests
   * @see {@link Request}
   */
  public static HashMap<UUID, Request> getRequests() {
    HashMap<UUID, Request> requests = new HashMap<UUID, Request>();
    File requestFile = new File(REQUEST_FILE_PATH);
    try {
      FileReader reader = new FileReader(requestFile);
      JSONArray requestJSONArray = (JSONArray) new JSONParser().parse(reader);

      for (int i = 0; i < requestJSONArray.size(); i++) {
        JSONObject requestObject = (JSONObject) requestJSONArray.get(i);

        Request request = getRequest(requestObject);
        requests.put(request.getId(), request);
      }
      reader.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
    return requests;
  }

  private static ArrayList<Request> getRequests(JSONArray arr) {
    ArrayList<Request> requests = new ArrayList<Request>();
    for (int i = 0; i < arr.size(); i++) {
      JSONObject requestObject = (JSONObject) arr.get(i);
      Request request = getRequest(requestObject);
      requests.add(request);
    }
    return requests;
  }

  private static Request getRequest(JSONObject requestObject) {
    if (requestObject == null) {
      return null;
    }
    UUID id = UUID.fromString((String) requestObject.get(REQUEST_UUID));
    String requestee = (String) requestObject.get(REQUEST_REQUESTEE);
    byte severity = ((Long) requestObject.get(REQUEST_SEVERITY)).byteValue();
    Address address = getAddress((JSONObject) requestObject.get(REQUEST_LOCATION));
    ResponseStatus status = getResponseStatus((String) requestObject.get(REQUEST_STATUS));
    ResponseType responderType = getResponseType((String) requestObject.get(REQUEST_RESPONSE_TYPE));
    String description = (String) requestObject.get(REQUEST_DESCRIPTION);
    String medicalNote = (String) requestObject.get(REQUEST_MEDICAL_NOTE);
    return new Request(id, requestee, severity, address, status, responderType, description, medicalNote);
  }

  /**
   * @param userObject the JSON object of a User
   * @return a User object from the provided JSON
   * @see {@link User}
   */
  private static User getUser(JSONObject userObject) {
    UUID id = UUID.fromString((String) userObject.get(USER_UUID));
    String firstName = (String) userObject.get(USER_FIRST_NAME);
    String lastName = (String) userObject.get(USER_LAST_NAME);
    String username = (String) userObject.get(USER_USERNAME);
    String emailAddress = (String) userObject.get(USER_EMAIL_ADDRESS);
    String password = (String) userObject.get(USER_PASSWORD);
    Address address = getAddress((JSONObject) userObject.get(USER_ADDRESS));
    UserType type = getUserType((String) userObject.get(USER_TYPE));
    ArrayList<Alert> receivedAlerts = getReceivedAlerts((JSONArray) userObject.get(USER_RECEIVED_ALERTS));
    switch (type) {
      case VOLUNTEER -> {
        ArrayList<Credential> credentials = getCredentialList((JSONArray) userObject.get(VOLUNTEER_CREDENTIALS));
        ArrayList<ResponseType> fieldsOfExpertise = getResponseTypeList(
            (JSONArray) userObject.get(VOLUNTEER_FIELDS_OF_EXPERTISE));
        boolean identityVerified = (Boolean) userObject.get(VOLUNTEER_IDENTITY_VERIFIED);
        boolean available = (Boolean) userObject.get(VOLUNTEER_AVAILABLE);
        Request assignedRequest = getRequest((JSONObject) userObject.get(VOLUNTEER_ASSIGNED_REQUEST));
        return new Volunteer(id, firstName, lastName, username, emailAddress, password, address, receivedAlerts,
            credentials, fieldsOfExpertise, identityVerified, available, assignedRequest);
      }
      case VICTIM -> {
        byte age = ((Long) userObject.get(VICTIM_AGE)).byteValue();
        char sex = ((String) userObject.get(VICTIM_SEX)).toCharArray()[0];
        String description = (String) userObject.get(VICTIM_DESCRIPTION);
        byte partySize = ((Long) userObject.get(VICTIM_PARTY_SIZE)).byteValue();
        ArrayList<Request> createdRequests = getRequests((JSONArray) userObject.get(VICTIM_CREATED_REQUESTS));
        ArrayList<Victim.Person> party = getParty((JSONArray) userObject.get(VICTIM_PARTY));
        String phoneNumber = (String)userObject.get(ADMIN_PHONE_NUMBER);
        return new Victim(id, firstName, lastName, username, emailAddress, password, address, receivedAlerts, age, sex,
            description, partySize, createdRequests, party, phoneNumber);
      }
      case ADMIN -> {
        String phoneNumber = (String) userObject.get(ADMIN_PHONE_NUMBER);
        return new Admin(id, firstName, lastName, username, emailAddress, password, address, receivedAlerts,
            phoneNumber);
      }
      case DISPATCHER -> {
        String phoneNumber = (String) userObject.get(ADMIN_PHONE_NUMBER);
        return new Dispatcher(id, firstName, lastName, username, emailAddress, password, address, receivedAlerts,
            phoneNumber);
      }
      case SHELTER_WORKER -> {
        Shelter shelter = getShelter((JSONObject) userObject.get(SHELTER_WORKER_SHELTER));
        return new ShelterWorker(id, firstName, lastName, username, password, emailAddress, address, receivedAlerts,
            shelter);
      }
    }
    return null;

  }

  private static ArrayList<Victim.Person> getParty(JSONArray arr) {
    ArrayList<Victim.Person> party = new ArrayList<Victim.Person>();
    if (arr == null || arr.size() < 1) {
      return party;
    }
    for (int i = 0; i < arr.size(); i++) {
      JSONObject o = (JSONObject) arr.get(i);
      Victim.Person person = getPerson(o);
      party.add(person);
    }
    return party;
  }

  private static Victim.Person getPerson(JSONObject personObject) {
    String name = (String) personObject.get(PERSON_NAME);
    byte age = ((Long) personObject.get(VICTIM_AGE)).byteValue();
    boolean isPet = (Boolean) personObject.get(PERSON_IS_PET);
    String description = (String) personObject.get(VICTIM_DESCRIPTION);
    Victim victimInstance = new Victim();
    return victimInstance.new Person(name, age, isPet, description);
  }

  /**
   * @param arr a JSONArray of Credentials
   * @return an ArrayList of Credentials
   * @see {@link Credential}
   * @see {@link DataReader#getCredential}
   */
  private static ArrayList<Credential> getCredentialList(JSONArray arr) {
    ArrayList<Credential> credentials = new ArrayList<Credential>();
    if (arr == null || arr.size() < 1) {
      return credentials;
    }
    for (int i = 0; i < arr.size(); i++) {
      JSONObject credentialObject = (JSONObject) arr.get(i);
      Credential credential = getCredential(credentialObject);
      credentials.add(credential);
    }
    return credentials;
  }

  /**
   * @param o the credential JSONObject
   * @return a Credential object specified in the JSON
   * @see {@link Credential}
   */
  private static Credential getCredential(JSONObject o) {
    String type = (String) o.get(CREDENTIAL_TYPE);
    String issuer = (String) o.get(CREDENTIAL_ISSUER);
    Date date = null;
    try {
      date = DateFormat.getDateTimeInstance().parse((String) o.get(CREDENTIAL_EXPIRATION_DATE));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
    Date expirationDate = date;
    boolean verified = (Boolean) o.get(CREDENTIAL_VERIFIED);
    return new Credential(type, issuer, expirationDate, verified);
  }

  /**
   * @param status the string version of a ResponseStatus
   * @return the ResponseStatus specified
   * @implNote if the string doesnt correspond to a ResponseStatus variant, it
   *           returns null
   * @see {@link ResponseStatus}
   */
  private static ResponseStatus getResponseStatus(String status) {
    if (status.equals("IN_PROGRESS")) {
      return ResponseStatus.IN_PROGRESS;
    } else if (status.equals("WAITING")) {
      return ResponseStatus.WAITING;
    } else if (status.equals("MORE_INFO_NEEDED")) {
      return ResponseStatus.MORE_INFO_NEEDED;
    } else if (status.equals("COMPLETE")) {
      return ResponseStatus.COMPLETE;
    } else if (status.equals("ASSIGNED")) {
      return ResponseStatus.ASSIGNED;
    }
    return null;
  }

  /**
   * @param type the string version of a ResponseType
   * @return the ResponseType specified
   * @implNote if the string doesnt correspond to a ResponseType variant, it
   *           returns null
   * @see {@link ResponseType}
   */
  private static ResponseType getResponseType(String type) {
    if (type.equals("ASSISTANCE")) {
      return ResponseType.ASSISTANCE;
    } else if (type.equals("MEDICAL")) {
      return ResponseType.MEDICAL;
    } else if (type.equals("RESOURCE")) {
      return ResponseType.RESOURCE;
    } else if (type.equals("TRAVEL")) {
      return ResponseType.TRAVEL;
    }
    return null;
  }

  /**
   * @param arr a JSONArray of ResponseTypes
   * @return an ArrayList of ResponseTypes
   * @see {@link ResponseType}
   * @see {@link DataReader#getResponseType}
   */
  private static ArrayList<ResponseType> getResponseTypeList(JSONArray arr) {
    ArrayList<ResponseType> types = new ArrayList<ResponseType>();
    if (arr == null || arr.size() < 1) {
      return types;
    }
    for (int i = 0; i < arr.size(); i++) {
      String responseString = (String) arr.get(i);
      ResponseType response = getResponseType(responseString);
      types.add(response);
    }
    return types;
  }

  /**
   * @return a HashMap of the Hurricanes
   * @see {@link Hurricane}
   */
  public static HashMap<String, Hurricane> getHurricanes() {
    HashMap<String, Hurricane> hurricanes = new HashMap<String, Hurricane>();
    File hurricaneFile = new File(HURRICANE_FILE_PATH);
    try {
      FileReader reader = new FileReader(hurricaneFile);
      JSONArray hurricaneJSONArray = (JSONArray) new JSONParser().parse(reader);

      for (int i = 0; i < hurricaneJSONArray.size(); i++) {
        JSONObject hurricaneObject = (JSONObject) hurricaneJSONArray.get(i);
        UUID id = UUID.fromString((String) hurricaneObject.get(HURRICANE_UUID));
        String name = (String) hurricaneObject.get(HURRICANE_NAME);
        byte category = ((Long) hurricaneObject.get(HURRICANE_CATEGORY)).byteValue();
        Address currentAddress = getAddress((JSONObject) hurricaneObject.get(HURRICANE_CURRENT_LOCATION));
        ArrayList<Address> predictedPath = getAddressList((JSONArray) hurricaneObject.get(HURRICANE_PREDICTED_PATH));
        ArrayList<Address> impactArea = getAddressList((JSONArray) hurricaneObject.get(HURRICANE_IMPACT_AREA));
        HurricaneStatus status = getHurricaneStatus((String) hurricaneObject.get(HURRICANE_STATUS));

        Hurricane hurricane = new Hurricane(id, name, category, currentAddress, predictedPath, impactArea, status);
        hurricanes.put(hurricane.getName(), hurricane);
      }
      reader.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
    }
    return hurricanes;
  }

  /**
   * @param arr a JSONArray of Addresses
   * @return an ArrayList of Addresses
   * @see {@link Address}
   * @see {@link DataReader#getAddress}
   */
  private static ArrayList<Address> getAddressList(JSONArray arr) {
    ArrayList<Address> addresses = new ArrayList<Address>();
    for (Object o : arr) {
      JSONObject addressObject = (JSONObject) o;
      Address address = getAddress(addressObject);
      addresses.add(address);
    }
    return addresses;
  }

  /**
   * @param status the string version of a HurricaneStatus
   * @return the HurricaneStatus specified
   * @implNote if the string doesnt correspond to a HurricaneStatus variant, it
   *           returns null
   * @see {@link HurricaneStatus}
   */
  private static HurricaneStatus getHurricaneStatus(String status) {
    if (status.equals("IN_PROGRESS")) {
      return HurricaneStatus.IN_PROGRESS;
    } else if (status.equals("INCOMING")) {
      return HurricaneStatus.INCOMING;
    } else if (status.equals("OVER")) {
      return HurricaneStatus.OVER;
    }
    return null;
  }

}
