package com.model;

import java.util.HashMap;

public class HurricaneList {
  private static HurricaneList hurricaneList;
  private HashMap<String, Hurricane> hurricanes;

  private HurricaneList() {
    hurricanes = DataReader.getHurricanes();
  }

  public static HurricaneList getInstance() {
    if (hurricaneList == null) {
      hurricaneList = new HurricaneList();
      return hurricaneList;
    }
    return hurricaneList;
  }

  public Hurricane getHurricane(String name) {
    return hurricanes.get(name);
  }

  public void addHurricane(Hurricane hurricane) {
    hurricanes.put(hurricane.getName(), hurricane);
  }

  public boolean save() {
    return DataWriter.saveHurricanes(hurricanes);
  }
}
