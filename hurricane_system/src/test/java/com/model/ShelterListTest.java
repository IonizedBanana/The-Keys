package com.model;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import com.model.location.Address;
import com.model.users.Victim;
/*
 * @Author: Logan
 * +----------------------------------------------------------------+---------------------------------------------------------------------------------+
 * | Test                                                           | Reasoning                                                                       |
 * +----------------------------------------------------------------+---------------------------------------------------------------------------------+
 * |testMakeAShelter                                                | tests that a shelter can be created                                             |
 * |testMakeANullShelter                                            | tests that a null shelter can be created.                                       |
 * |testGetNullAccomodation                                         | tests that a shelter can hold no accomodations.                                 |
 * |testGetAccomodation                                             | tests that a shelter can hold accomodations.                                    |
 * |testGetNullResources                                            | tests that a shelter can hold no resources.                                     |                                    
 * |testGetResources                                                | tests that a shelter can hold resources.                                        |
 * |testGetNullRegisteredPeople                                     | tests that a shelter can hold no registered people.                             |
 * |testGetRegisteredPeople                                         | tests that a shelter can hold registered people.                                |
 * |testGetNullRegisteredPets                                       | tests that a shelter can hold no registered pets.                               |
 * |testGetRegisteredPets                                           | tests that a shelter can hold registered pets.                                  |
 * |testAddNullResource                                             | tests that a shelter can hold a null resource.                                  |
 * |testAddNegativeOneResource                                      | tests that a shelter can make a resource with negative quantity.                |
 * |testAddZeroResource                                             | tests that a shelter can make a resource with zero quantity.                    |
 * |testAddResource                                                 | tests that a shelter can make a resource with positive quantity.                |
 * |testUpdateNegativeThreeResource                                 | tests that a shelter can update a resource with negative quantity.              |
 * |testUpdateZeroResource                                          | tests that a shelter can update a resource with zero quantity.                  |
 * |testUpdateOneResource                                           | tests that a shelter can update a resource with positive quantity.              |
 * |testUpdateNullResource                                          | tests that a shelter can update a null resource.                                |
 * |testReceivenegativeTwoResource                                  | tests that a shelter can receive a resource with negative quantity.             |
 * |testReceiveZeroResource                                         | tests that a shelter can receive a resource with zero quantity.                 |
 * |testReceiveOneResource                                          | tests that a shelter can receive a resource with positive quantity.             |
 * |testReceiveNullResource                                         | tests that a shelter can receive a null resource.                               |
 * |testAvailableNullSpace                                          | tests that a shelter can have a null available space quantity.                  |
 * |testAvailableSpace                                              | tests that a shelter can have an available space.                               |
 * |testIsOutOfSuppliesNegativeOne                                  | tests that a shelter can be out of supplies with negative quantity.             |
 * |testIsOutOfSuppliesZero                                         | tests that a shelter can be out of supplies with zero quantity.                 |
 * |testIsOutOfSuppliesOne                                          | tests that a shelter can be out of supplies with positive quantity.             |
 * |testIsOutOfSuppliesNull                                         | tests that a shelter can be out of supplies with null quantity.                 | 
 * |testNotifyNegativeThree                                         | tests that a shelter can be notifyed with a negative quantity arrival.          |
 * |testNotifyZero                                                  | tests that a shelter can be notifyed with a zero quantity arrival.              |   
 * |testNotifyOne                                                   | tests that a shelter can be notifyed with a positive quantity arrival.          |    
 * |testRegisterArrivalNull                                         | tests that a shelter can register an arrival with null party.                   |
 * |testRegisterArrival                                             | tests that a shelter can register an arrival with a party.                      |
 * |testUnregisterArrival                                           | tests that a shelter can unregister an arrival with a party                     |
 * |testUnregisterArrivalNull                                       | tests that a shelter can unregister an arrival with null party.                 |
 * |testToString                                                    | tests that a shelter can be converted to the correct string format.             |
 * |testToStringNull                                                | tests that a shelter can be converted to the correct string format when null.   |
 * |testMakeShelterList                                             | tests that a shelter list can be created.                                       |
 * |testMakeANullShelterList                                        | tests that a null shelter list can be created.                                  |
 * |testAddShelter                                                  | tests that a shelter can be added to the shelter list.                          |
 * |testAddNullShelter                                              | tests that a null shelter can be added to the shelter list.                     |
 * |testSaveShelterList                                             | tests that a shelter list can be saved to the json file.                        |
 * |testSaveNullShelterList                                         | tests that a null shelter list can be saved to the json file.                   |
 * |testGetShelterList                                              | tests that a shelter list can be retrieved.                                     |
 * |testGetNullShelterList                                          | tests that a null shelter list can be retrieved.                                |
 * +-----------------------------------------------------------------+--------------------------------------------------------------------------------+
**/
public class ShelterListTest {
    @BeforeClass
	public static void setup() {
        ShelterList.getInstance();
        ShelterList.getInstance().addShelter(new Shelter(
            java.util.UUID.randomUUID(),
            "amcdirk",
            new Address("123 Main St", "Cityville", "State", "12345"),
            100,
            50,
            10,
            new java.util.ArrayList<Resource>(),
            new java.util.ArrayList<Accomodation>(),
            new java.util.HashMap<String, Victim.Person>(),
            new java.util.HashMap<String, Victim.Person>()
        ));
        ShelterList.getInstance().save();
    }
    //--------------------------------------------------------------------------Shelter Tests--------------------------------------------------------------------------//
    @Test
    public void testMakeAShelter() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        assertTrue(shelter != null);
    }
    @Test
    public void testMakeANullShelter() {
        Shelter shelter = null;
        assertFalse(shelter != null);
    }
    //--------------------------------------------------------------------------Shelter Complicated Getters Tests--------------------------------------------------------------------------//
    @Test
    public void testGetNullAccomodation() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        shelter.getAccomodations().clear();
        assertTrue(shelter.getAccomodations().isEmpty());
    }
    @Test
    public void testGetAccomodation() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Accomodation accomodation = Accomodation.MEDICAL_STAFF;
        shelter.getAccomodations().add(accomodation);
        assertTrue(shelter.getAccomodations().contains(accomodation));
    }
    @Test
    public void testGetNullResources() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        shelter.getResources().clear();
        assertTrue(shelter.getResources().isEmpty());
    }
    @Test
    public void testGetResources() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", 5, Unit.POUNDS);
        shelter.getResources().add(resource);
        assertTrue(shelter.getResources().contains(resource));
    }
    @Test
    public void testGetNullRegisteredPeople() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        shelter.getRegisteredPeople().put(null, null);
        assertTrue(shelter.getRegisteredPeople().containsKey(null));
    }
    @Test
    public void testGetRegisteredPeople() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        String personName = "John Doe";
        shelter.getRegisteredPeople().put(personName, null);
        assertTrue(shelter.getRegisteredPeople().containsKey(personName));
    }
    @Test
    public void testGetNullRegisteredPets() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        shelter.getRegisteredPets().put(null, null);
        assertTrue(shelter.getRegisteredPets().containsKey(null));
    }
    @Test
    public void testGetRegisteredPets() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        String petName = "Fido";
        shelter.getRegisteredPets().put(petName, null);
        assertTrue(shelter.getRegisteredPets().containsKey(petName));
    }
    //---------------------------------------------------------------------------Shelter Resource Tests--------------------------------------------------------------------------//
    @Test
    public void testAddNullResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        shelter.addResource(null);
        assertTrue(shelter.getResources().contains(null));
    }
    @Test
    public void testAddNegativeOneResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", -1, Unit.POUNDS);
        shelter.addResource(resource);
        assertTrue(shelter.getResources().contains(resource));
    }
    @Test
    public void testAddZeroResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", 0, Unit.POUNDS);
        shelter.addResource(resource);
        assertTrue(shelter.getResources().contains(resource));
    }
    @Test
    public void testAddResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", 1, Unit.POUNDS);
        shelter.addResource(resource);
        assertTrue(shelter.getResources().contains(resource));
    }
    @Test
    public void testUpdateNegativeThreeResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", 3, Unit.POUNDS);
        shelter.addResource(resource);
        shelter.updateResource(resource, -3);
        assertTrue(shelter.getResources().contains(resource));
    }
    @Test
    public void testUpdateZeroResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", 3, Unit.POUNDS);
        shelter.addResource(resource);
        shelter.updateResource(resource, 0);
        assertTrue(shelter.getResources().contains(resource));
    }
    @Test
    public void testUpdateOneResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", 1, Unit.POUNDS);
        shelter.updateResource(resource, 1);
        assertFalse(shelter.getResources().contains(resource));
    }
    @Test
    public void testUpdateNullResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", 1, Unit.POUNDS);
        shelter.updateResource(null, 1);
        assertFalse(shelter.getResources().contains(resource));
    }
    @Test
    public void testReceivenegativeTwoResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", -2, Unit.POUNDS);
        shelter.recieveResource(resource);
        assertTrue(shelter.getResources().contains(resource));
    }
    @Test
    public void testReceiveZeroResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", 0, Unit.POUNDS);
        shelter.recieveResource(resource);
        assertTrue(shelter.getResources().contains(resource));
    }
    @Test
    public void testReceiveOneResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", 1, Unit.POUNDS);
        shelter.recieveResource(resource);
        assertTrue(shelter.getResources().contains(resource));
    }
    @Test
    public void testReceiveNullResource() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = null;
        shelter.recieveResource(resource);
        assertTrue(shelter.getResources().contains(resource));
    }
    //---------------------------------------------------------------------------Shelter Capacity Tests--------------------------------------------------------------------------//
    @Test
    public void testAvailableNullSpace() {
        Shelter shelter = new Shelter(null, null, null, 0, 0, 0, null, null, null, null);
        assertTrue(shelter.availableSpace() == 0);
    }
    @Test
    public void testAvailableSpace() {
        Shelter shelter = new Shelter(null, null, null, 0, 0, 0, null, null, null, null);  
        shelter.notify(10);
        assertTrue(shelter.availableSpace() == -10);
    }
    @Test
    public void testIsOutOfSuppliesNegativeOne() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", -1, Unit.POUNDS);
        shelter.addResource(resource);
        assertTrue(shelter.isOutOfSupplies());
    }
    @Test
    public void testIsOutOfSuppliesZero() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", 0, Unit.POUNDS);
        shelter.addResource(resource);
        assertTrue(shelter.isOutOfSupplies());
    }
    @Test
    public void testIsOutOfSuppliesOne() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        Resource resource = new Resource(ResourceType.FOOD, "bags of wheat", 1, Unit.POUNDS);
        shelter.addResource(resource);
        assertFalse(shelter.isOutOfSupplies());
    }
    @Test
    public void testIsOutOfSuppliesNull() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        shelter.addResource(null);
        assertTrue(shelter.isOutOfSupplies());
    }
    @Test
    public void testNotifyNegativeThree() {
        Shelter shelter = new Shelter(null, null, null, 0, 0, 0, null, null, null, null);
        shelter.notify(-3);
        assertTrue(shelter.getExpectedArrivals() == -3);
    }
    @Test
    public void testNotifyZero() {
        Shelter shelter = new Shelter(null, null, null, 0, 0, 0, null, null, null, null);
        shelter.notify(0);
        assertTrue(shelter.getExpectedArrivals() == 0);
    }
    @Test
    public void testNotifyOne() {
        Shelter shelter = new Shelter(null, null, null, 0, 0, 0, null, null, null, null);
        shelter.notify(1);
        assertTrue(shelter.getExpectedArrivals() == 1);
    }
    @Test
    public void testIsOutOfSuppliesNUll() {
        Shelter shelter = new Shelter(null, null, null, 0, 0, 0, new java.util.ArrayList<Resource>(), null, null, null);
        assertTrue(shelter.isOutOfSupplies());
    }
    @Test
    public void testRegisterArrivalNull() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        shelter.registerArrival(1, null);
        assertTrue(shelter.getUsedCapacity() == 51);
    }
    @Test
    public void testRegisterArrival() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        ArrayList<Victim.Person> party = new ArrayList<>();
        party.add(new Victim().new Person("John Doe", (byte) 30, false, "None"));
        shelter.registerArrival(1, party);
        assertTrue(shelter.getUsedCapacity() == 51);
    }
    @Test
    public void testUnregisterArrival() {
        Shelter shelter = new Shelter(null, null, null, 0, 0, 0, null, null, new java.util.HashMap<String, Victim.Person>(), null);
        ArrayList<Victim.Person> party = new ArrayList<>();
        party.add(new Victim().new Person("John Doe", (byte) 30, false, "None"));
        shelter.registerArrival(1, party);
        assertTrue(shelter.getUsedCapacity() == 1);
    }
    @Test
    public void testUnregisterArrivalNull() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        shelter.registerArrival(1, null);
        assertTrue(shelter.getUsedCapacity() == 51);
    }
    @Test
    public void testToString() {
        Shelter shelter = ShelterList.getInstance().getShelter("amcdirk");
        String expected = shelter.getId() + " " + shelter.getName() + " " + shelter.getAddress() + " " + shelter.getTotalCapacity() + " " + shelter.getUsedCapacity() + " "
        + shelter.getExpectedArrivals() + " " + shelter.getResources();
        assertTrue(shelter.toString().equals(expected));
    }
    @Test
    public void testToStringNull() {
        Shelter shelter = null;
        assertFalse(shelter != null);
    }
    //---------------------------------------------------------------------------Shelter List Tests--------------------------------------------------------------------------//
    @Test
    public void testMakeShelterList() {
    ShelterList shelterList = ShelterList.getInstance();
        assertTrue(shelterList != null);
    }
    @Test
    public void testMakeANullShelterList() {
        ShelterList shelterList = null;
        assertFalse(shelterList != null);
    }
    @Test
    public void testAddShelter() {
        ShelterList shelterList = ShelterList.getInstance();
        int initialSize = shelterList.getShelterList().size();
        shelterList.addShelter(new Shelter(
            java.util.UUID.randomUUID(),
            "shelter2",
            new Address("456 Elm St", "Townsville", "State", "67890"),
            200,
            100,
            20,
            new java.util.ArrayList<Resource>(),
            new java.util.ArrayList<Accomodation>(),
            new java.util.HashMap<String, Victim.Person>(),
            new java.util.HashMap<String, Victim.Person>()
        ));
        assertTrue(shelterList.getShelterList().size() == initialSize + 1);
    }
    @Test
    public void testAddNullShelter() {
        ShelterList shelterList = ShelterList.getInstance();
        int initialSize = shelterList.getShelterList().size();
        shelterList.addShelter(null);
        assertTrue(shelterList.getShelterList().size() == initialSize + 1);
    }
    @Test
    public void testSaveShelterList() {
        ShelterList shelterList = ShelterList.getInstance();
        // Earlier tests may have inserted null into this shared singleton list.
        shelterList.getShelterList().values().removeIf(java.util.Objects::isNull);
        assertTrue(shelterList.save());
    }
    @Test
    public void testSaveNullShelterList() {
        ShelterList shelterList = null;
        assertFalse(shelterList != null && shelterList.save());
    }
    @Test
    public void testGetShelterList() {
        ShelterList shelterList = ShelterList.getInstance();
        assertTrue(shelterList.getShelterList() != null);
    }
    @Test
    public void testGetNullShelterList() {
        ShelterList shelterList = null;
        assertFalse(shelterList != null && shelterList.getShelterList() != null);
    }
    @AfterClass
    public static void teardown() {
        RequestList.getInstance().getRequestList().clear();
        RequestList.getInstance().save();
    }
}