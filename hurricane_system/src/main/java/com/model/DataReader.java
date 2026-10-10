package com.model;

import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataReader extends DataConstants {
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
        UUID id = UUID.fromString((String) shelterObject.get(SHELTER_UUID));
        String name = (String) shelterObject.get(SHELTER_NAME);
        Address address = getAddress((JSONObject) shelterObject.get(SHELTER_ADDRESS));
        int totalCapacity = ((Long) shelterObject.get(SHELTER_TOTAL_CAPACITY)).intValue();
        int usedCapacity = ((Long) shelterObject.get(SHELTER_USED_CAPACITY)).intValue();
        int expectedArrivals = ((Long) shelterObject.get(SHELTER_EXPECTED_ARRIVALS)).intValue();
        ArrayList<Resource> resources = getResourceList((JSONArray) shelterObject.get(SHELTER_RESOURCES));
        Shelter shelter = new Shelter(id, name, address, totalCapacity, usedCapacity, expectedArrivals, resources);
        shelters.put(shelter.getName(), shelter);
      }
      reader.close();
      return shelters;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  private static Address getAddress(JSONObject addressObject) {
    String state = (String) addressObject.get(LOCATION_STATE);
    String city = (String) addressObject.get(LOCATION_CITY);
    String address_string = (String) addressObject.get(ADDRESS_ADDRESS);
    Address address = new Address(state, city, address_string);
    return address;
  }

  private static ArrayList<Resource> getResourceList(JSONArray arr) {
    ArrayList<Resource> resources = new ArrayList<Resource>();
    for (int i = 0; i < arr.size(); i++) {
      JSONObject resourceObject = (JSONObject) arr.get(i);
      Resource resource = getResource(resourceObject);
      resources.add(resource);
    }
    return resources;
  }

  private static Resource getResource(JSONObject resourceObject) {
    ResourceType type = getResourceType((String) resourceObject.get(RESOURCE_TYPE));
    String description = (String) resourceObject.get(REQUEST_DESCRIPTION);
    int quantity = ((Long) resourceObject.get(RESOURCE_QUANTITY)).intValue();
    Unit unit = getUnit((String) resourceObject.get(RESOURCE_UNIT));
    Resource resource = new Resource(type, description, quantity, unit);
    return resource;
  }

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

  public static HashMap<String, User> getUsers() {
    HashMap<String, User> users = new HashMap<String, User>();
    File userFile = new File(USER_FILE_PATH);
    try {
      FileReader reader = new FileReader(userFile);
      JSONArray userJSONArray = (JSONArray) new JSONParser().parse(reader);

      for (int i = 0; i < userJSONArray.size(); i++) {
        JSONObject userObject = (JSONObject) userJSONArray.get(i);
      }
      reader.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return users;
  }

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

  private static ArrayList<Alert> getRecievedAlerts(JSONArray arr) {
    ArrayList<Alert> alerts = new ArrayList<Alert>();
    for (int i = 0; i < arr.size(); i++) {
      JSONObject o = (JSONObject) arr.get(i);
      Alert alert = getAlert(o);
      alerts.add(alert);
    }
    return alerts;
  }

  private static Alert getAlert(JSONObject alert) {
    ArrayList<Location> affectedLocations = getLoactionList((JSONArray) alert.get(ALERT_AFFECTED_LOCATIONS));
    String description = (String) alert.get(ALERT_DESCRIPTION);
    String issuedBy = (String) alert.get(ALERT_ISSUED_BY);
    Date timestamp = (Date) alert.get(ALERT_TIMESTAMP);
    return new Alert(affectedLocations, description, issuedBy, timestamp);
  }

  public static HashMap<UUID, Request> getRequests() {
    HashMap<UUID, Request> requests = new HashMap<UUID, Request>();
    File requestFile = new File(REQUEST_FILE_PATH);
    try {
      FileReader reader = new FileReader(requestFile);
      JSONArray requestJSONArray = (JSONArray) new JSONParser().parse(reader);

      for (int i = 0; i < requestJSONArray.size(); i++) {
        JSONObject requestObject = (JSONObject) requestJSONArray.get(i);
        UUID id = (UUID) requestObject.get(REQUEST_UUID);
        User requestee = getUser((JSONObject) requestObject.get(REQUEST_REQUESTEE));
        byte severity = ((Long) requestObject.get(REQUEST_SEVERITY)).byteValue();
        Location location = getLocation((JSONObject) requestObject.get(REQUEST_LOCATION));
        ResponseStatus status = getResponseStatus((String) requestObject.get(REQUEST_STATUS));
        ResponseType responderType = getResponseType((String) requestObject.get(REQUEST_RESPONSE_TYPE));
        String description = (String) requestObject.get(REQUEST_DESCRIPTION);

        Request request = new Request(id, requestee, severity, location, status, responderType, description);
        requests.put(request.getId(), request);
      }
      reader.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return requests;
  }

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
      }
    }

  }

  private static ArrayList<Credential> getCredentialList(JSONArray arr) {
    ArrayList<Credential> credentials = new ArrayList<Credential>();
    for (int i = 0; i < arr.size(); i++) {
      JSONObject credentialObject = (JSONObject) arr.get(i);
      Credential credential = getCredential(credentialObject);
      credentials.add(credential);
    }
  }

  private static Credential getCredential(JSONObject o) {
    String type = (String)o.get(CREDENTIAL_TYPE);
    String issuer = (String)o.get(CREDENTIAL_ISSUER);
    Date date = null;
    try {
      date = DateFormat.getDateInstance().parse((String)o.get(CREDENTIAL_EXPIRATION_DATE));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    Date expirationData = date;
    boolean verified = (Boolean)o.get(CREDENTIAL_VERIFIED);
    return new Credential(type, issuer, expirationDate)
  }

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

  public static HashMap<String, Hurricane> getHurricanes() {
    HashMap<String, Hurricane> hurricanes = new HashMap<>();
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
    }
    return hurricanes;
  }

  private static Location getLocation(JSONObject object) {
    String city = (String) object.get(LOCATION_CITY);
    String state = (String) object.get(LOCATION_STATE);
    return new Location(state, city);
  }

  private static ArrayList<Location> getLoactionList(JSONArray arr) {
    ArrayList<Location> locations = new ArrayList<Location>();
    for (Object o : arr) {
      JSONObject locationObject = (JSONObject) o;
      Location location = getLocation(locationObject);
      locations.add(location);
    }
    return locations;
  }

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
  }
}
