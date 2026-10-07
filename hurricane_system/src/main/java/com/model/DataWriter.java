package com.model;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataWriter extends DataConstants {
  @SuppressWarnings("unchecked")
  public static boolean saveShelters(HashMap<String, Shelter> shelters) {
    // File shelterFile = new File(SHELTER_FILE_PATH); TODO change this once saving
    // is tested fully
    File shelterFile = new File("../json/testing/shelter.json");
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
  public static boolean saveUsers(HashMap<String, User> users) { // TODO uncomment param
    // File userFile = new File(USER_FILE_PATH);
    File userFile = new File("../json/testing/user.json");
    JSONArray userJSONArray = new JSONArray();
    for (Map.Entry<String, User> set : users.entrySet() ){
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
    // File userFile = new File(REQUEST_FILE_PATH);
    File requestFile = new File("../json/testing/request.json");
    JSONArray userJSONArray = new JSONArray();
    for (Map.Entry<UUID, Request> set : requests.entrySet()){
      userJSONArray.add(getRequestJSON(set.getValue())); 
    }
    try {
      FileWriter writer = new FileWriter(requestFile);
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
  public static boolean saveHurricanes(HashMap<String, Hurricane> hurricanes) { 
    // File hurricaneFile = new File(HURRICANE_FILE_PATH);
    File hurricaneFile = new File("../json/testing/hurricane.json");
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
    shelterJSON.put(SHELTER_ADDRESS, shelter.getAddress()); // TODO add .toString() once Address is implemented
    shelterJSON.put(SHELTER_TOTAL_CAPACITY, shelter.getTotalCapacity());
    shelterJSON.put(SHELTER_USED_CAPACITY, shelter.getUsedCapacity());
    shelterJSON.put(SHELTER_EXPECTED_ARRIVALS, shelter.getExpectedArrivals());
    shelterJSON.put(SHELTER_RESOURCES, shelter.getResources());
    return shelterJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getUserJSON(User user) {
    JSONObject userJSON = new JSONObject();
    userJSON.put(USER_FIRST_NAME, user.getFirstName());
    userJSON.put(USER_LAST_NAME, user.getLastName());
    userJSON.put(USER_USERNAME, user.getUsername());
    userJSON.put(USER_EMAIL_ADDRESS, user.getEmailAddress());
    userJSON.put(USER_PASSWORD, user.getPassword());
    // userJSON.put(USER_ADDRESS, getAddressJSON(user.getAddress()));
    userJSON.put(USER_UUID, user.getId());
    userJSON.put(USER_TYPE, user.getType().toString());
    userJSON.put(USER_RECIEVED_ALERTS, (JSONArray)user.getRecievedAlerts());
    return userJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getRequestJSON(Request request) {
    JSONObject requestJSON = new JSONObject();
    requestJSON.put(REQUEST_UUID, request.getId().toString());
    requestJSON.put(REQUEST_REQUESTEE, getUserJSON(request.getRequestee()));
    requestJSON.put(REQUEST_SEVERITY, request.getSeverity());
    requestJSON.put(REQUEST_LOCATION, getLocationJSON(request.getLocation()));
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
    hurricaneJSON.put(HURRICANE_CURRENT_LOCATION, getLocationJSON(hurricane.getCurrentLocation()));
    hurricaneJSON.put(HURRICANE_PREDICTED_PATH, getLocationArray(hurricane.getPredictedPath()));
    hurricaneJSON.put(HURRICANE_IMPACT_AREA, getLocationArray(hurricane.getImpactArea()));
    hurricaneJSON.put(HURRICANE_STATUS, hurricane.getStatus().toString());
    return hurricaneJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getLocationJSON(Location location) {
    JSONObject locationJSON = new JSONObject();
    locationJSON.put(LOCATION_STATE, location.getState());
    locationJSON.put(LOCATION_CITY, location.getCity());
    return locationJSON;
  }

  @SuppressWarnings("unchecked")
  private static JSONArray getLocationArray(ArrayList<Location> locations) {
    JSONArray locationsJSON = new JSONArray();
    for (Location l : locations) {
      locationsJSON.add(getLocationJSON(l));
    }
    return locationsJSON;
  }
}
