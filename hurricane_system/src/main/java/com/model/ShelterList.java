package com.model;

import java.util.HashMap;

public class ShelterList {
  private static ShelterList shelterList;
  private HashMap<String, Shelter> shelters;

  private ShelterList() {
    shelters = DataReader.getShelters();
  }

  public static ShelterList getInstance() {
    if (shelterList == null) {
      shelterList = new ShelterList();
      return shelterList;
    }
    return shelterList;
  }

  public Shelter getShelter(String name) {
    return shelters.get(name);
  }

  public void addShelter(Shelter shelter) {
    shelters.put(shelter.getName(), shelter);
  }

  public boolean save() {
    return DataWriter.saveShelters(shelters);
  }
}
