package com.model;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.model.location.*;
import com.model.location.Address;
import com.model.users.*;

public class Main {
  public static void main(String[] args) {
    HashMap<String, Shelter> shelters = DataReader.getShelters();
    for (Map.Entry<String, Shelter> set : shelters.entrySet()) {
      System.out.println(set.getValue());
    }
    DataWriter.saveShelters(shelters);
    HashMap<String, Hurricane> hurricanes = DataReader.getHurricanes();
    for (Map.Entry<String, Hurricane> set : hurricanes.entrySet()) {
      System.out.println(set.getValue());
    }
    DataWriter.saveHurricanes(hurricanes);
    HashMap<UUID, Request> requests = DataReader.getRequests();
    for (Map.Entry<UUID, Request> set : requests.entrySet()) {
      System.out.println(set.getValue());
    }
    HashMap<String, User> users = DataReader.getUsers();
    for (Map.Entry<String, User> set : users.entrySet()) {
      System.out.println(set.getValue().toString());
    }
    
    DataWriter.saveUsers(users);
    DataWriter.saveHurricanes(hurricanes);
    DataWriter.saveRequests(requests);
    DataWriter.saveShelters(shelters);

    Address generalAddress = new Address("Charleston", "SC");
    Coordinates generalCoords = generalAddress.getCoordinates();
    System.out.println(generalCoords.getLatitude() + "\n" + generalCoords.getLongitude());
    Address shelterAddress = new Address("1069 King St", "Charleston", "SC", "29403");
    Address farAddress = new Address("360 Harbison Blvd", "Columbia", "SC", "29212");
    // System.out.println(shelterAddress.getDistanceTo(generalAddress) < 5);
    System.out.println(shelterAddress.isWithin(generalAddress, 5));
    // System.out.println(farAddress.getDistanceTo(generalAddress) < 100);
    System.out.println(farAddress.isWithin(generalAddress, 5));
    System.out.println(farAddress.getDistanceTo(shelterAddress));
  }
}
