package com.model;

import com.model.users.*;

import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    // File shelterFile = new File("../json/testing/shelter.json");
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
    return new Shelter(id, name, address, totalCapacity, usedCapacity, expectedArrivals, resources);
  }

  /**
   * @param addressObject the JSONObject of an Address
   * @return an Address object from the provided JSON
   * @see {@link Address}
   */
  private static Address getAddress(JSONObject addressObject) {
    String state = (String) addressObject.get(LOCATION_STATE);
    String city = (String) addressObject.get(LOCATION_CITY);
    String address_string = (String) addressObject.get(ADDRESS_ADDRESS);
    Address address = new Address(state, city, address_string);
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
  private static ArrayList<Alert> getRecievedAlerts(JSONArray arr) {
    ArrayList<Alert> alerts = new ArrayList<Alert>();
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
    ArrayList<Location> affectedLocations = getLoactionList((JSONArray) alert.get(ALERT_AFFECTED_LOCATIONS));
    String description = (String) alert.get(ALERT_DESCRIPTION);
    User issuedBy = getUser((JSONObject) alert.get(ALERT_ISSUED_BY));
    Date timestamp;
    try {
      timestamp = DateFormat.getDateInstance().parse((String) alert.get(ALERT_TIMESTAMP));
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getStackTrace());
      timestamp = null;
    }
    return new Alert(affectedLocations, description, issuedBy, timestamp);
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
    UUID id = UUID.fromString((String) requestObject.get(REQUEST_UUID));
    User requestee = getUser((JSONObject) requestObject.get(REQUEST_REQUESTEE));
    byte severity = ((Long) requestObject.get(REQUEST_SEVERITY)).byteValue();
    Location location = getLocation((JSONObject) requestObject.get(REQUEST_LOCATION));
    ResponseStatus status = getResponseStatus((String) requestObject.get(REQUEST_STATUS));
    ResponseType responderType = getResponseType((String) requestObject.get(REQUEST_RESPONSE_TYPE));
    String description = (String) requestObject.get(REQUEST_DESCRIPTION);
    return new Request(id, requestee, severity, location, status, responderType, description);
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
    ArrayList<Alert> recievedAlerts = getRecievedAlerts((JSONArray) userObject.get(USER_RECIEVED_ALERTS));
    switch (type) {
      case VOLUNTEER -> {
        ArrayList<Credential> credentials = getCredentialList((JSONArray) userObject.get(VOLUNTEER_CREDENTIALS));
        ArrayList<ResponseType> fieldsOfExpertise = getResponseTypeList(
            (JSONArray) userObject.get(VOLUNTEER_FIELDS_OF_EXPERTISE));
        boolean identityVerified = (Boolean) userObject.get(VOLUNTEER_IDENTITY_VERIFIED);
        boolean available = (Boolean) userObject.get(VOLUNTEER_AVAILABLE);
        return new Volunteer(id, firstName, lastName, username, emailAddress, password, address, credentials,
            fieldsOfExpertise, identityVerified, available);
      }
      case VICTIM -> {
        byte age = ((Long) userObject.get(VICTIM_AGE)).byteValue();
        char sex = ((String) userObject.get(VICTIM_SEX)).toCharArray()[0];
        String description = (String) userObject.get(VICTIM_DESCRIPTION);
        byte partySize = ((Long) userObject.get(VICTIM_PARTY_SIZE)).byteValue();
        ArrayList<Request> createdRequests = getRequests((JSONArray) userObject.get(VICTIM_CREATED_REQUESTS));
        return new Victim(id, firstName, lastName, username, emailAddress, password, address, age, sex, description,
            partySize, createdRequests);
      }
      case ADMIN -> {
        String phoneNumber = (String) userObject.get(ADMIN_PHONE_NUMBER);
        return new Admin(firstName, lastName, username, emailAddress, password, address, phoneNumber);
      }
      case DISPATCHER -> {
        String phoneNumber = (String) userObject.get(ADMIN_PHONE_NUMBER);
        return new Dispatcher(firstName, lastName, username, emailAddress, password, address, phoneNumber);
      }
      case SHELTER_WORKER -> {
        Shelter shelter = getShelter((JSONObject) userObject.get(SHELTER_WORKER_SHELTER));
        return new ShelterWorker(firstName, lastName, username, password, emailAddress, address, shelter);
      }
    }
    return null;

  }

  /**
   * @param arr a JSONArray of Credentials
   * @return an ArrayList of Credentials
   * @see {@link Credential}
   * @see {@link DataReader#getCredential}
   */
  private static ArrayList<Credential> getCredentialList(JSONArray arr) {
    ArrayList<Credential> credentials = new ArrayList<Credential>();
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
      date = DateFormat.getDateInstance().parse((String) o.get(CREDENTIAL_EXPIRATION_DATE));
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
    for (int i = 0; i < arr.size(); i++) {
      JSONObject responseObject = (JSONObject) arr.get(i);
      ResponseType response = getResponseType(responseObject.toString());
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
        Location currentLocation = getLocation((JSONObject) hurricaneObject.get(HURRICANE_CURRENT_LOCATION));
        ArrayList<Location> predictedPath = getLoactionList((JSONArray) hurricaneObject.get(HURRICANE_PREDICTED_PATH));
        ArrayList<Location> impactArea = getLoactionList((JSONArray) hurricaneObject.get(HURRICANE_IMPACT_AREA));
        HurricaneStatus status = getHurricaneStatus((String) hurricaneObject.get(HURRICANE_STATUS));

        Hurricane hurricane = new Hurricane(id, name, category, currentLocation, predictedPath, impactArea, status);
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
   * @param object a JSONObject of a Location
   * @return a Location specified in the JSON
   * @see {@link Location}
   */
  private static Location getLocation(JSONObject object) {
    String city = (String) object.get(LOCATION_CITY);
    String state = (String) object.get(LOCATION_STATE);
    return new Location(state, city);
  }

  /**
   * @param arr a JSONArray of Locations
   * @return an ArrayList of Locations
   * @see {@link Location}
   * @see {@link DataReader#getLocation}
   */
  private static ArrayList<Location> getLoactionList(JSONArray arr) {
    ArrayList<Location> locations = new ArrayList<Location>();
    for (Object o : arr) {
      JSONObject locationObject = (JSONObject) o;
      Location location = getLocation(locationObject);
      locations.add(location);
    }
    return locations;
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

  public static void main(String[] args) {
    HashMap<String, Shelter> shelters = getShelters();
    for (Map.Entry<String, Shelter> set : shelters.entrySet()) {
      System.out.println(set.getValue());
    }
    DataWriter.saveShelters(shelters);
    HashMap<String, Hurricane> hurricanes = getHurricanes();
    for (Map.Entry<String, Hurricane> set : hurricanes.entrySet()) {
      System.out.println(set.getValue());
    }
    DataWriter.saveHurricanes(hurricanes);
    HashMap<UUID, Request> requests = getRequests();
    for (Map.Entry<UUID, Request> set : requests.entrySet()) {
      System.out.println(set.getValue());
    }
    HashMap<String, User> users = getUsers();
    for (Map.Entry<String, User> set : users.entrySet()) {
      System.out.println(set.getValue());
    }

  }
}
