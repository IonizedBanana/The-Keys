package com.model;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataWriter extends DataConstants {
  public static boolean saveShelters(ArrayList<Shelter> shelters) {
    // File shelterFile = new File(SHELTER_FILE_PATH); TODO change this once saving
    // is tested fully
    File shelterFile = new File("../json/testing/shelter.json");
    JSONArray shelterJSONArray = new JSONArray();
    for (Shelter s : shelters) {
      shelterJSONArray.add(getShelterJSON(s));
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

  public static boolean saveUsers(/* ArrayList<Users> users */) { // TODO uncomment param
    // File userFile = new File(USER_FILE_PATH);
    File userFile = new File("../json/testing/user.json");
    JSONArray userJSONArray = new JSONArray();
    for (char u : new String("users").toCharArray()) {
      userJSONArray.add(getUserJSON(/* u */)); // TODO change this loop to be for User u : users, and change method call
                                               // when Users are implemented
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

  public static boolean saveRequests(/* ArrayList<Request> requests */) { // TODO uncomment param
    // File userFile = new File(REQUEST_FILE_PATH);
    File requestFile = new File("../json/testing/request.json");
    JSONArray userJSONArray = new JSONArray();
    for (char r : new String("requests").toCharArray()) {
      userJSONArray.add(getUserJSON(/* r */)); // TODO change this loop to be for Request r : requests and change method
                                               // call when Requests are implemented
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

  public static boolean saveHurricanes(/* ArrayList<Hurricane> hurricanes */) { // TODO uncomment param
    // File hurricaneFile = new File(HURRICANE_FILE_PATH);
    File hurricaneFile = new File("../json/testing/hurricane.json");
    JSONArray userJSONArray = new JSONArray();
    for (char h : new String("hurricane").toCharArray()) {
      userJSONArray.add(getUserJSON(/* h */)); // TODO change this loop to be for Hurricane H : hurricanes, and change
                                               // method call when Hurricanes are implemented
    }
    try {
      FileWriter writer = new FileWriter(hurricaneFile);
      writer.write(userJSONArray.toJSONString());
      writer.flush();
      writer.close();
      return true;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  public static JSONObject getShelterJSON(Shelter shelter) {
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

  public static JSONObject getUserJSON(/* User user */) {
    JSONObject userJSON = new JSONObject();
    // TODO change parameter and implement once User classes are created
    return userJSON;
  }

  public static JSONObject getRequestJSON(/* Request request */) {
    JSONObject requestJSON = new JSONObject();
    // TODO change parameter and implement once User classes are created
    return requestJSON;
  }

  public static JSONObject getHurricaneJSON(/* Hurricane hurricane */) {
    JSONObject hurricaneJSON = new JSONObject();
    // TODO change parameter and implement once User classes are created
    return hurricaneJSON;
  }
}
