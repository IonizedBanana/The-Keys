package com.model.location;

public class Address {
  private String street;
  private String city;
  private String state;
  private String zip;

  public Address(String street, String city, String state, String zip) {
    this.street = street;
    this.city = city;
    this.state = state;
    this.zip = zip;
  }

  public Address(String city, String state){
    this.city = city;
    this.state = state;
    this.street = null;
    this.zip = null;
  }

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getZip() {
    return zip;
  }

  public Coordinates getCoordinates() {
    try {
      return MapTools.getInstance().getCoordinates(this);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String toString() {
    if (street == null && zip == null) {
      return String.format("%s, %s", city, state);
    }
    return String.format("%s, %s, %s %s", street, city, state, zip);
  }

  public double getDistanceTo(Address address) {
    final double DEGREES_TO_KM = 111.2;
    final double KM_TO_MILES = 0.6213712;
    Coordinates here = this.getCoordinates();
    Coordinates there = address.getCoordinates();
    double latDiff = Math.abs(here.getLatitude() - there.getLatitude());
    double longDiff = Math.abs(here.getLongitude() - there.getLongitude());
    double distanceApprox = Math.sqrt(Math.pow(latDiff, 2) + Math.pow(longDiff, 2));
    return distanceApprox*DEGREES_TO_KM*KM_TO_MILES;
  }
}
