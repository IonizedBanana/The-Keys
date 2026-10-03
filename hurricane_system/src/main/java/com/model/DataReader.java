package com.model;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataReader extends DataConstants {
  public static ArrayList<Shelter> getShelters() {
    ArrayList<Shelter> shelters = new ArrayList<Shelter>();
    File shelterFile = new File(SHELTER_FILE_PATH);
    System.out.println(SHELTER_FILE_PATH);
    try {
      FileReader reader = new FileReader(shelterFile);
      JSONArray shelterJSONArray = (JSONArray)new JSONParser().parse(reader);
      
      for (int i = 0; i < shelterJSONArray.size(); i++) {
        JSONObject shelterObject = (JSONObject)shelterJSONArray.get(i);
        UUID id = UUID.fromString((String)shelterObject.get(SHELTER_UUID));
        String  name = (String)shelterObject.get(SHELTER_NAME);
        // Address address = (String)shelterObject.get(SHELTER_ADDRESS); // TODO change to Address when Address's are made
        String address =  new String("address");
        int totalCapacity = ((Long)shelterObject.get(SHELTER_TOTAL_CAPACITY)).intValue();
        int usedCapacity = ((Long)shelterObject.get(SHELTER_USED_CAPACITY)).intValue();
        int expectedArrivals = ((Long)shelterObject.get(SHELTER_EXPECTED_ARRIVALS)).intValue();
        // Resource resources = (String)shelterObject.get(SHELTER_RESOURCES); // TODO uncomment and use the actual resources value when resources are made
        String resources = new String("resources");
        Shelter shelter = new Shelter(id, name, address, totalCapacity, usedCapacity, expectedArrivals, resources);
        shelters.add(shelter);
      }
      reader.close();
      return shelters;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }
  public static void getUsers() { // TODO change return to ArrayList<User>
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
  
  public static void getRequests() { // TODO change return to ArrayList<Request>
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

  public static void getHurricanes() { // TODO change return to ArrayList<Hurricane>
    File hurricaneFile = new File(HURRICANE_FILE_PATH);
    try {
      FileReader reader = new FileReader(hurricaneFile);
      JSONArray shelterJSONArray = (JSONArray)new JSONParser().parse(reader);
      
      for (int i = 0; i < shelterJSONArray.size(); i++) {
        // TODO implement once Hurricane class is implemented
      }
      reader.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    ArrayList<Shelter> shelters = getShelters(); 
    for (Shelter s : shelters) {
      System.out.println(s);
    }
    DataWriter.saveShelters(shelters);
  }
}
