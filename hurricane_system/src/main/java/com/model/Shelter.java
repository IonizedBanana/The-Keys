package com.model;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.UUID;
import com.model.location.*;
import com.model.users.*;

/**
 * <h2>Shelter</h2>
 * <p>
 * a class to represent public storm shelters and refugee camps
 * a shelter has:
 * </p>
 * <ul>
 * <li>a name</li>
 * <li>an address</li>
 * <li>total and used capacity</li>
 * <li>a list of available resources</li>
 * </ul>
 * 
 * @author IonizedBanana (Cayden)
 */
public class Shelter {
  private UUID id;
  private String name;
  private Address address;
  private int totalCapacity;
  private int usedCapacity;
  private int expectedArrivals;
  private ArrayList<Resource> resources;
  private ArrayList<Accomodation> accomodations;
  private HashMap<String, Victim.Person> registeredPeople;
  private HashMap<String, Victim.Person> registeredPets;

  public Shelter(UUID id, String name, Address address, int totalCapacity, int usedCapacity, int expectedArrivals,
      ArrayList<Resource> resources, ArrayList<Accomodation> accomodations,
      HashMap<String, Victim.Person> registeredPeople, HashMap<String, Victim.Person> registeredPets) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.totalCapacity = totalCapacity;
    this.usedCapacity = usedCapacity;
    this.expectedArrivals = expectedArrivals;
    this.resources = resources;
    this.accomodations = accomodations;
    this.registeredPeople = registeredPeople;
    this.registeredPets = registeredPets;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Address getAddress() {
    return address;
  }

  public ArrayList<Accomodation> getAccomodations() {
    return accomodations;
  }

  public HashMap<String, Victim.Person> getRegisteredPeople() {
    return registeredPeople;
  }

  public HashMap<String, Victim.Person> getRegisteredPets() {
    return registeredPets;
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

  public ArrayList<Resource> getResources() {
    return resources;
  }

  public void addResource(Resource resource) {
    resources.add(resource);
  }

  public void updateResource(Resource resource, int quantity) {
    for (Resource r : resources) {
      if (r.equals(resource)) {
        r.updateQuantity(quantity);
      }
    }
  }

  public void recieveResource(Resource resource) {
    if (resources.contains(resource)) {
      updateResource(resource, resource.getQuantity());
    } else {
      addResource(resource);
    }
  }

  public int availableSpace() {
    return (totalCapacity - (usedCapacity + expectedArrivals));
  }

  public boolean isOutOfSupplies() {
    return (resources.size() < 1);
  }

  public void notify(int partySize) {
    expectedArrivals += partySize;
  }

  public void registerArrival(ArrayList<Victim.Person> party) {
    usedCapacity += party.size();
    for (Victim.Person p : party) {
      if (p.isPet()) {
        registeredPets.put(p.getName(), p);
      } else {
        registeredPeople.put(p.getName(), p);
      }
    }
  }

  public void unregisterArrival(ArrayList<Victim.Person> party) {
    usedCapacity -= party.size();
    for (Victim.Person p : party) {
      if (p.isPet()) {
        registeredPets.remove(p.getName());
      } else {
        registeredPeople.remove(p.getName());
      }
    }
  }

  public String toString() {
    return (this.id + " " + this.name + " " + this.address + " " + this.totalCapacity + " " + this.usedCapacity + " "
        + this.expectedArrivals + " " + this.resources);
  }
}
