package com.model;

import com.model.users.*;
import com.model.location.*;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataWriter extends DataConstants {
  @SuppressWarnings("unchecked")
  public static boolean saveShelters(HashMap<String, Shelter> shelters) {
    File shelterFile = new File(SHELTER_FILE_PATH);
    JSONArray shelterJSONArray = new JSONArray();
    for (Map.Entry<String, Shelter> set : shelters.entrySet()) {
      shelterJSONArray.add(getShelterJSON(set.getValue()));
    }
    try {
      FileWriter writer = new FileWriter(shelterFile);
      writer.write(shelterJSONArray.toJSONString());
      writer.flush();
      writer.close();
      return true;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  public static boolean saveUsers(HashMap<String, User> users) {
    File userFile = new File(USER_FILE_PATH);
    JSONArray userJSONArray = new JSONArray();
    for (Map.Entry<String, User> set : users.entrySet()) {
      userJSONArray.add(getUserJSON(set.getValue()));
    }
    try {
      FileWriter writer = new FileWriter(userFile);
      writer.write(userJSONArray.toJSONString());
      writer.flush();
      writer.close();
      return true;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  public static boolean saveRequests(HashMap<UUID, Request> requests) {
    File requestFile = new File(REQUEST_FILE_PATH);
    JSONArray requestJSONArray = new JSONArray();
    for (Map.Entry<UUID, Request> set : requests.entrySet()) {
      requestJSONArray.add(getRequestJSON(set.getValue()));
    }
    try {
      FileWriter writer = new FileWriter(requestFile);
      writer.write(requestJSONArray.toJSONString());
      writer.flush();
      writer.close();
      return true;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  public static boolean saveHurricanes(HashMap<String, Hurricane> hurricanes) {
    File hurricaneFile = new File(HURRICANE_FILE_PATH);
    JSONArray hurricaneJSONArray = new JSONArray();
    for (Map.Entry<String, Hurricane> set : hurricanes.entrySet()) {
      hurricaneJSONArray.add(getHurricaneJSON(set.getValue()));
    }
    try {
      FileWriter writer = new FileWriter(hurricaneFile);
      writer.write(hurricaneJSONArray.toJSONString());
      writer.flush();
      writer.close();
      return true;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getShelterJSON(Shelter shelter) {
    JSONObject shelterJSON = new JSONObject();
    shelterJSON.put(SHELTER_UUID, shelter.getId().toString());
    shelterJSON.put(SHELTER_NAME, shelter.getName());
    shelterJSON.put(SHELTER_ADDRESS, getAddressJSON(shelter.getAddress()));
    shelterJSON.put(SHELTER_TOTAL_CAPACITY, shelter.getTotalCapacity());
    shelterJSON.put(SHELTER_USED_CAPACITY, shelter.getUsedCapacity());
    shelterJSON.put(SHELTER_EXPECTED_ARRIVALS, shelter.getExpectedArrivals());
    shelterJSON.put(SHELTER_RESOURCES, getResourceListJSON(shelter.getResources()));
    return shelterJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getResourceJSON(Resource resource) {
    JSONObject resourceObject = new JSONObject();
    resourceObject.put(RESOURCE_TYPE, resource.getType().toString());
    resourceObject.put(RESOURCE_QUANTITY, resource.getQuantity());
    resourceObject.put(RESOURCE_DESCRIPTION, resource.getDescription());
    resourceObject.put(RESOURCE_UNIT, resource.getUnit().toString());
    return resourceObject;
  }

  @SuppressWarnings("unchecked")
  private static JSONArray getResourceListJSON(ArrayList<Resource> resources) {
    JSONArray resourceJSONArray = new JSONArray();
    for (Resource r : resources) {
      resourceJSONArray.add(getResourceJSON(r));
    }
    return resourceJSONArray;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getAddressJSON(Address address) {
    JSONObject addressObject = new JSONObject();
    addressObject.put(ADDRESS_STATE, address.getState());
    addressObject.put(ADDRESS_CITY, address.getCity());
    addressObject.put(ADDRESS_ADDRESS, address.getStreet());
    addressObject.put(ADDRESS_ZIP, address.getStreet());
    return addressObject;
  }

  private static JSONObject getUserJSON(User user) {
    JSONObject userJSON = new JSONObject();
    UserType type = user.getType();
    switch (type) {
      case VOLUNTEER -> {
        userJSON = getVolunteerJSON((Volunteer) user);
      }
      case VICTIM -> {
        userJSON = getVictimJSON((Victim) user);
      }
      case ADMIN -> {
        userJSON = getAdminJSON((Admin) user);
      }
      case DISPATCHER -> {
        userJSON = getDispatcherJSON((Dispatcher) user);
      }
      case SHELTER_WORKER -> {
        userJSON = getShelterWorkerJSON((ShelterWorker) user);
      }
    }
    return userJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getVolunteerJSON(Volunteer user) {
    JSONObject userJSON = new JSONObject();
    userJSON.put(USER_FIRST_NAME, user.getFirstName());
    userJSON.put(USER_LAST_NAME, user.getLastName());
    userJSON.put(USER_USERNAME, user.getUsername());
    userJSON.put(USER_EMAIL_ADDRESS, user.getEmailAddress());
    userJSON.put(USER_PASSWORD, user.getPassword());
    userJSON.put(USER_ADDRESS, getAddressJSON(user.getAddress()));
    userJSON.put(USER_UUID, user.getId().toString());
    userJSON.put(USER_TYPE, user.getType().toString());
    userJSON.put(USER_RECEIVED_ALERTS, getAlertsArray(user.getReceivedAlerts()));
    userJSON.put(VOLUNTEER_CREDENTIALS, getCredentialsArray(user.getCredentials()));
    userJSON.put(VOLUNTEER_AVAILABLE, user.getIsAvailible());
    userJSON.put(VOLUNTEER_IDENTITY_VERIFIED, user.getIdentityVerified());
    return userJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getVictimJSON(Victim user) {
    JSONObject userJSON = new JSONObject();
    userJSON.put(USER_FIRST_NAME, user.getFirstName());
    userJSON.put(USER_LAST_NAME, user.getLastName());
    userJSON.put(USER_USERNAME, user.getUsername());
    userJSON.put(USER_EMAIL_ADDRESS, user.getEmailAddress());
    userJSON.put(USER_PASSWORD, user.getPassword());
    userJSON.put(USER_ADDRESS, getAddressJSON(user.getAddress()));
    userJSON.put(USER_UUID, user.getId().toString());
    userJSON.put(USER_TYPE, user.getType().toString());
    userJSON.put(USER_RECEIVED_ALERTS, getAlertsArray(user.getReceivedAlerts()));
    userJSON.put(VICTIM_AGE, user.getAge());
    userJSON.put(VICTIM_SEX, new String(user.getSex() + ""));
    userJSON.put(VICTIM_DESCRIPTION, user.getDescription());
    userJSON.put(VICTIM_PARTY_SIZE, user.getPartySize());
    userJSON.put(VICTIM_CREATED_REQUESTS, getRequestListJSON(user.getCreatedRequests()));
    userJSON.put(VICTIM_PARTY, getPartyJSON(user.getParty()));
    return userJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONArray getPartyJSON(ArrayList<Victim.Person> party) {
    JSONArray partyJSON = new JSONArray();
    for (Victim.Person vp : party) {
      partyJSON.add(getPersonJSON(vp));
    }
    return partyJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getPersonJSON(Victim.Person person) {
    JSONObject personObject = new JSONObject();
    personObject.put(PERSON_NAME, person.getName());
    personObject.put(VICTIM_AGE, person.getAge());
    personObject.put(PERSON_IS_PET, person.getIsPet());
    personObject.put(VICTIM_DESCRIPTION, person.getDescription());
    return personObject;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getAdminJSON(Admin user) {
    JSONObject userJSON = new JSONObject();
    userJSON.put(USER_FIRST_NAME, user.getFirstName());
    userJSON.put(USER_LAST_NAME, user.getLastName());
    userJSON.put(USER_USERNAME, user.getUsername());
    userJSON.put(USER_EMAIL_ADDRESS, user.getEmailAddress());
    userJSON.put(USER_PASSWORD, user.getPassword());
    userJSON.put(USER_ADDRESS, getAddressJSON(user.getAddress()));
    userJSON.put(USER_UUID, user.getId().toString());
    userJSON.put(USER_TYPE, user.getType().toString());
    userJSON.put(USER_RECEIVED_ALERTS, getAlertsArray(user.getReceivedAlerts()));
    userJSON.put(ADMIN_PHONE_NUMBER, user.getPhoneNumber());
    return userJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getDispatcherJSON(Dispatcher user) {
    JSONObject userJSON = new JSONObject();
    userJSON.put(USER_FIRST_NAME, user.getFirstName());
    userJSON.put(USER_LAST_NAME, user.getLastName());
    userJSON.put(USER_USERNAME, user.getUsername());
    userJSON.put(USER_EMAIL_ADDRESS, user.getEmailAddress());
    userJSON.put(USER_PASSWORD, user.getPassword());
    userJSON.put(USER_ADDRESS, getAddressJSON(user.getAddress()));
    userJSON.put(USER_UUID, user.getId().toString());
    userJSON.put(USER_TYPE, user.getType().toString());
    userJSON.put(USER_RECEIVED_ALERTS, getAlertsArray(user.getReceivedAlerts()));
    userJSON.put(ADMIN_PHONE_NUMBER, user.getPhoneNumber());
    return userJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getShelterWorkerJSON(ShelterWorker user) {
    JSONObject userJSON = new JSONObject();
    userJSON.put(USER_FIRST_NAME, user.getFirstName());
    userJSON.put(USER_LAST_NAME, user.getLastName());
    userJSON.put(USER_USERNAME, user.getUsername());
    userJSON.put(USER_EMAIL_ADDRESS, user.getEmailAddress());
    userJSON.put(USER_PASSWORD, user.getPassword());
    userJSON.put(USER_ADDRESS, getAddressJSON(user.getAddress()));
    userJSON.put(USER_UUID, user.getId().toString());
    userJSON.put(USER_TYPE, user.getType().toString());
    userJSON.put(USER_RECEIVED_ALERTS, getAlertsArray(user.getReceivedAlerts()));
    userJSON.put(SHELTER_WORKER_SHELTER, getShelterJSON(user.getShelter()));
    return userJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONArray getCredentialsArray(ArrayList<Credential> credentials) {
    JSONArray credentialsJSONArray = new JSONArray();
    for (Credential c : credentials) {
      credentialsJSONArray.add(getCredentialJSON(c));
    }
    return credentialsJSONArray;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getCredentialJSON(Credential credential) {
    JSONObject credentialObject = new JSONObject();
    credentialObject.put(CREDENTIAL_TYPE, credential.getType());
    String dateString = DateFormat.getDateTimeInstance().format(credential.getExpirationDate());
    credentialObject.put(CREDENTIAL_EXPIRATION_DATE, dateString);
    credentialObject.put(CREDENTIAL_ISSUER, credential.getIssuer());
    credentialObject.put(CREDENTIAL_VERIFIED, credential.isVerified());
    return credentialObject;
  }

  @SuppressWarnings("unchecked")
  private static JSONArray getAlertsArray(ArrayList<Alert> alerts) {
    JSONArray alertsJSONArray = new JSONArray();
    if (alerts == null || alerts.size() == 0) {
      return alertsJSONArray;
    }
    for (Alert a : alerts) {
      alertsJSONArray.add(getAlertJSON(a));
    }
    return alertsJSONArray;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getAlertJSON(Alert alert) {
    JSONObject alertObject = new JSONObject();
    alertObject.put(ALERT_AFFECTED_LOCATIONS, getAddressArray(alert.getAffectedAddresses()));
    alertObject.put(ALERT_DESCRIPTION, alert.getDescription());
    alertObject.put(ALERT_ISSUED_BY, alert.getIssuedBy());
    String timestampFormatted = DateFormat.getDateTimeInstance().format(alert.getTimestamp());
    alertObject.put(ALERT_TIMESTAMP, timestampFormatted);
    return alertObject;
  }

  @SuppressWarnings("unchecked")
  private static JSONArray getRequestListJSON(ArrayList<Request> requests) {
    JSONArray requestListJSON = new JSONArray();
    for (Request r : requests) {
      requestListJSON.add(getRequestJSON(r));
    }
    return requestListJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getRequestJSON(Request request) {
    JSONObject requestJSON = new JSONObject();
    requestJSON.put(REQUEST_UUID, request.getId().toString());
    requestJSON.put(REQUEST_REQUESTEE, request.getRequestee());
    requestJSON.put(REQUEST_SEVERITY, request.getSeverity());
    requestJSON.put(REQUEST_LOCATION, getAddressJSON(request.getAddress()));
    requestJSON.put(REQUEST_STATUS, request.getStatus().toString());
    requestJSON.put(REQUEST_RESPONSE_TYPE, request.getResponderType().toString());
    requestJSON.put(REQUEST_DESCRIPTION, request.getDescription());
    return requestJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getHurricaneJSON(Hurricane hurricane) {
    JSONObject hurricaneJSON = new JSONObject();
    hurricaneJSON.put(HURRICANE_UUID, hurricane.getId().toString());
    hurricaneJSON.put(HURRICANE_NAME, hurricane.getName());
    hurricaneJSON.put(HURRICANE_CATEGORY, hurricane.getCategory());
    hurricaneJSON.put(HURRICANE_CURRENT_LOCATION, getAddressJSON(hurricane.getCurrentAddress()));
    hurricaneJSON.put(HURRICANE_PREDICTED_PATH, getAddressArray(hurricane.getPredictedPath()));
    hurricaneJSON.put(HURRICANE_IMPACT_AREA, getAddressArray(hurricane.getImpactArea()));
    hurricaneJSON.put(HURRICANE_STATUS, hurricane.getStatus().toString());
    return hurricaneJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONArray getAddressArray(ArrayList<Address> addresses) {
    JSONArray addressesJSON = new JSONArray();
    for (Address l : addresses) {
      addressesJSON.add(getAddressJSON(l));
    }
    return addressesJSON;
  }
}
