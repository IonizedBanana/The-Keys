package com.model;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataReader extends DataConstants {
  public static HashMap<String, Shelter> getShelters() {
    HashMap<String, Shelter> shelters = new HashMap<String, Shelter>();
    File shelterFile = new File(SHELTER_FILE_PATH);
    System.out.println(SHELTER_FILE_PATH);
    try {
      FileReader reader = new FileReader(shelterFile);
      JSONArray shelterJSONArray = (JSONArray)new JSONParser().parse(reader);
      
      for (int i = 0; i < shelterJSONArray.size(); i++) {
        JSONObject shelterObject = (JSONObject)shelterJSONArray.get(i);
        UUID id = UUID.fromString((String)shelterObject.get(SHELTER_UUID));
        String  name = (String)shelterObject.get(SHELTER_NAME);
        // Address address = ()shelterObject.get(SHELTER_ADDRESS); // TODO change to Address when Address's are made
        String address =  new String("address");
        int totalCapacity = ((Long)shelterObject.get(SHELTER_TOTAL_CAPACITY)).intValue();
        int usedCapacity = ((Long)shelterObject.get(SHELTER_USED_CAPACITY)).intValue();
        int expectedArrivals = ((Long)shelterObject.get(SHELTER_EXPECTED_ARRIVALS)).intValue();
        // Resource resources = (String)shelterObject.get(SHELTER_RESOURCES); // TODO uncomment and use the actual resources value when resources are made
        String resources = new String("resources");
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
  public static void getUsers() { // TODO change return to HashMap<User>
    File userFile = new File(USER_FILE_PATH);
    try {
      FileReader reader = new FileReader(userFile);
      JSONArray shelterJSONArray = (JSONArray)new JSONParser().parse(reader);
      
      for (int i = 0; i < shelterJSONArray.size(); i++) {
        // TODO implement once User classes are implemented
      }
      reader.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  public static void getRequests() { // TODO change return to HashMap<Request>
    File requestFile = new File(USER_FILE_PATH);
    try {
      FileReader reader = new FileReader(requestFile);
      JSONArray shelterJSONArray = (JSONArray)new JSONParser().parse(reader);
      
      for (int i = 0; i < shelterJSONArray.size(); i++) {
        // TODO implement once Reuqest classes are implemented
      }
      reader.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static HashMap<String, Hurricane> getHurricanes() { 
    HashMap<String, Hurricane> hurricanes = new HashMap<>();
    File hurricaneFile = new File(HURRICANE_FILE_PATH);
    try {
      FileReader reader = new FileReader(hurricaneFile);
      JSONArray hurricaneJSONArray = (JSONArray)new JSONParser().parse(reader);
      
      for (int i = 0; i < hurricaneJSONArray.size(); i++) {
        JSONObject hurricaneObject = (JSONObject)hurricaneJSONArray.get(i);
        UUID id = UUID.fromString((String)hurricaneObject.get(HURRICANE_UUID));
        String name = (String)hurricaneObject.get(HURRICANE_NAME);
        byte category = ((Long)hurricaneObject.get(HURRICANE_CATEGORY)).byteValue();
        Location currentLocation = getLocation((JSONObject)hurricaneObject.get(HURRICANE_CURRENT_LOCATION));
        ArrayList<Location> predictedPath = getLoactionList((JSONArray)hurricaneObject.get(HURRICANE_PREDICTED_PATH));
        ArrayList<Location> impactArea = getLoactionList((JSONArray)hurricaneObject.get(HURRICANE_IMPACT_AREA));
        HurricaneStatus status = getHurricaneStatus((String)hurricaneObject.get(HURRICANE_STATUS));

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
    String city = (String)object.get(LOCATION_CITY);
    String state = (String)object.get(LOCATION_STATE);
    return new Location(state, city);
  }

  private static ArrayList<Location> getLoactionList(JSONArray arr) {
    ArrayList<Location> locations = new ArrayList<Location>();
    for (Object o : arr) {
      JSONObject locationObject = (JSONObject)o;
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
