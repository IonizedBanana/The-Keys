package com.model;

import java.util.UUID;

public class Shelter {
  private UUID id;
  private String name;
  private String address; // TODO change to Address
  private int totalCapacity;
  private int usedCapacity;
  private int expectedArrivals;
  private String resources; // TODO change to ArrayList<Resource>

  public Shelter(UUID id, String name, String address, int totalCapacity, int usedCapacity, int expectedArrivals,
      String resources) {
    // TODO implement
  }

  public void addResource(String resource) { // TODO change to Resource
    // TODO implement
  }

  public void updateResource(String resource, int quantity) { // TODO change to Resource
    // TODO implement
  }
  
  public int availableSpace() {
    return 0; // TODO implement
  }

  public boolean isOutOfSupplies() {
    return true; // TODO implement
  }

  public void registerArrival(int partySize) {
    // TODO implement
  }
}
