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
    this.id = id;
    this.name = name;
    this.address = address;
    this.totalCapacity = totalCapacity;
    this.usedCapacity = usedCapacity;
    this.expectedArrivals = expectedArrivals;
    this.resources = resources;
  }

  public UUID getId() {
	return id;
}

  public String getName() {
	return name;
  }

  public String getAddress() {
	return address;
  }

  public int getTotalCapacity() {
	return totalCapacity;
  }

  public int getUsedCapacity() {
	return usedCapacity;
  }

  public int getExpectedArrivals() {
	return expectedArrivals;
  }

  public String getResources() {
	return resources;
  }

  public void addResource(String resource) { // TODO change to Resource
    // TODO implement
  }

  public void updateResource(String resource, int quantity) { // TODO change to Resource
    // TODO implement
  }
  
  public int availableSpace() {
    return (totalCapacity - (usedCapacity + expectedArrivals));
  }

  public boolean isOutOfSupplies() {
    return (resources.length() < 1); // TODO change to utilize ArrayList.sizeOf()
  }

  public void notify(int partySize) {
    expectedArrivals += partySize;
  }

  public void registerArrival(int partySize) {
    usedCapacity += partySize;
  }

  public String toString() {
    return (this.id + " " + this.name + " " + this.address + " " + this.totalCapacity + " " + this.usedCapacity + " " + this.expectedArrivals + " " + this.resources);
  }
}
