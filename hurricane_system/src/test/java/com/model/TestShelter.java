package com.model;

/*
 * Resources are the same resource when their type, description and unit match
 * (Resource.equals). updateResource takes a delta (change in quantity), not a new quantity.
 *
 * +--------------------------------------------------+------------------------------------------+
 * | Test                                             | Reasoning                                |
 * +--------------------------------------------------+------------------------------------------+
 * | Add a resource                                   | the resource ends up in the shelter      |
 * | Add the same resource object twice               | adding must not duplicate it             |
 * | Add an equal (type/description/unit) resource    | an equal resource is the same resource,  |
 * |                                                  | so it must not duplicate either          |
 * | Add a null resource                              | nothing should be added                  |
 * | Add a resource with a negative quantity          | invalid amounts should not be added      |
 * | Add a resource with quantity 0                   | an empty resource should not be added    |
 * | Receive a resource the shelter doesn't have      | it should be added                       |
 * | Receive a resource the shelter already has       | the shelter keeps a single entry         |
 * | Receive adds the delivered quantity              | the quantities add together              |
 * | Update a resource by a positive delta            | the quantity goes up by the delta        |
 * | Update changes the matching resource             | the matching resource gets the delta     |
 * | Update leaves other resources alone              | non-matching resources are unchanged     |
 * | Update by a negative delta that stays >= 0       | using up supplies is valid               |
 * | Update by a delta that uses up everything        | reaching exactly 0 is valid              |
 * | Update by a delta that makes quantity negative   | invalid, rejected with an exception      |
 * | A rejected update leaves the quantity unchanged  | invalid updates must not alter the data  |
 * | Update by a delta of 0                           | nothing changes                          |
 * | Update a null resource                           | list and quantities are left alone (2)   |
 * | Update a resource the shelter doesn't have       | list and quantities are left alone (2)   |
 * +--------------------------------------------------+------------------------------------------+
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.junit.Test;

public class TestShelter {
  // ---- adding a resource ----
  @Test
  public void testAddingResourceAddsItToShelter() {
    Shelter shelter = newEmptyShelter();
    Resource resource = resource("drinking water", 3);

    shelter.addResource(resource);

    assertTrue(shelter.getResources().contains(resource));
  }

  @Test
  public void testAddingExistingResourceDoesNotDuplicate() {
    Shelter shelter = newEmptyShelter();
    Resource resource = resource("drinking water", 3);
    shelter.addResource(resource);

    shelter.addResource(resource);

    assertEquals(1, shelter.getResources().size());
  }

  @Test
  public void testAddingEqualButDistinctResourceDoesNotDuplicate() {
    Shelter shelter = newEmptyShelter();
    shelter.addResource(resource("drinking water", 3));

    shelter.addResource(resource("drinking water", 3));

    assertEquals(1, shelter.getResources().size());
  }

  @Test
  public void testAddingNullResourceDoesNotAddIt() {
    Shelter shelter = newEmptyShelter();

    shelter.addResource(null);

    assertEquals(0, shelter.getResources().size());
  }

  @Test
  public void testAddingNegativeQuantityResourceDoesNotAddIt() {
    Shelter shelter = newEmptyShelter();

    shelter.addResource(resource("drinking water", -1));

    assertEquals(0, shelter.getResources().size());
  }

  @Test
  public void testAddingZeroQuantityResourceDoesNotAddIt() {
    Shelter shelter = newEmptyShelter();

    shelter.addResource(resource("drinking water", 0));

    assertEquals(0, shelter.getResources().size());
  }

  // ---- receiving a resource ----
  @Test
  public void testReceivingNewResourceAddsIt() {
    Shelter shelter = newEmptyShelter();
    Resource received = resource("drinking water", 3);

    shelter.recieveResource(received);

    assertTrue(shelter.getResources().contains(received));
  }

  @Test
  public void testReceivingExistingResourceDoesNotDuplicate() {
    Shelter shelter = newEmptyShelter();
    shelter.addResource(resource("drinking water", 3));

    shelter.recieveResource(resource("drinking water", 2));

    assertEquals(1, shelter.getResources().size());
  }

  @Test
  public void testReceivingExistingResourceAddsDeliveredQuantity() {
    Shelter shelter = newEmptyShelter();
    shelter.addResource(resource("drinking water", 3));

    shelter.recieveResource(resource("drinking water", 2));

    assertEquals(5, shelter.getResources().get(0).getQuantity());
  }

  // ---- updating a resource (by delta) ----
  @Test
  public void testUpdatingResourceAppliesPositiveDelta() {
    Shelter shelter = newEmptyShelter();
    Resource target = resource("drinking water", 3);
    shelter.addResource(target);

    shelter.updateResource(target, 2);

    assertEquals(5, target.getQuantity());
  }

  @Test
  public void testUpdatingResourceChangesTheMatchingResource() {
    Shelter shelter = newEmptyShelter();
    Resource target = resource("drinking water", 3);
    shelter.addResource(target);
    shelter.addResource(resource("food", 7));

    shelter.updateResource(target, 2);

    assertEquals(5, target.getQuantity());
  }

  @Test
  public void testUpdatingResourceLeavesOtherResourcesAlone() {
    Shelter shelter = newEmptyShelter();
    Resource target = resource("drinking water", 3);
    Resource other = resource("food", 7);
    shelter.addResource(target);
    shelter.addResource(other);

    shelter.updateResource(target, 2);

    assertEquals(7, other.getQuantity());
  }

  @Test
  public void testUpdatingResourceThroughEqualResourceUpdatesStoredOne() {
    Shelter shelter = newEmptyShelter();
    Resource stored = resource("drinking water", 3);
    shelter.addResource(stored);

    shelter.updateResource(resource("drinking water", 1), 2);

    assertEquals(5, stored.getQuantity());
  }

  @Test
  public void testUpdatingResourceWithNegativeDeltaThatStaysNonNegative() {
    Shelter shelter = newEmptyShelter();
    Resource resource = resource("drinking water", 3);
    shelter.addResource(resource);

    shelter.updateResource(resource, -1);

    assertEquals(2, resource.getQuantity());
  }

  @Test
  public void testUpdatingResourceCanUseUpAllOfIt() {
    Shelter shelter = newEmptyShelter();
    Resource resource = resource("drinking water", 3);
    shelter.addResource(resource);

    shelter.updateResource(resource, -3);

    assertEquals(0, resource.getQuantity());
  }

  @Test
  public void testUpdatingResourceRejectsDeltaThatMakesQuantityNegative() {
    Shelter shelter = newEmptyShelter();
    Resource resource = resource("drinking water", 3);
    shelter.addResource(resource);

    assertThrows(IllegalArgumentException.class, () -> shelter.updateResource(resource, -4));
  }

  @Test
  public void testRejectedUpdateLeavesQuantityUnchanged() {
    Shelter shelter = newEmptyShelter();
    Resource resource = resource("drinking water", 3);
    shelter.addResource(resource);

    try {
      shelter.updateResource(resource, -4);
    } catch (IllegalArgumentException expected) {
      // rejection is checked in the test above
    }

    assertEquals(3, resource.getQuantity());
  }

  @Test
  public void testUpdatingResourceWithZeroDeltaChangesNothing() {
    Shelter shelter = newEmptyShelter();
    Resource resource = resource("drinking water", 3);
    shelter.addResource(resource);

    shelter.updateResource(resource, 0);

    assertEquals(3, resource.getQuantity());
  }

  @Test
  public void testUpdatingNullResourceLeavesResourceListAlone() {
    Shelter shelter = newEmptyShelter();
    shelter.addResource(resource("drinking water", 3));

    shelter.updateResource(null, 5);

    assertEquals(1, shelter.getResources().size());
  }

  @Test
  public void testUpdatingNullResourceLeavesQuantitiesAlone() {
    Shelter shelter = newEmptyShelter();
    Resource resource = resource("drinking water", 3);
    shelter.addResource(resource);

    shelter.updateResource(null, 5);

    assertEquals(3, resource.getQuantity());
  }

  @Test
  public void testUpdatingResourceNotInShelterLeavesResourceListAlone() {
    Shelter shelter = newEmptyShelter();
    shelter.addResource(resource("drinking water", 3));

    shelter.updateResource(resource("food", 7), 2);

    assertEquals(1, shelter.getResources().size());
  }

  @Test
  public void testUpdatingResourceNotInShelterLeavesQuantitiesAlone() {
    Shelter shelter = newEmptyShelter();
    Resource stored = resource("drinking water", 3);
    shelter.addResource(stored);

    shelter.updateResource(resource("food", 7), 2);

    assertEquals(3, stored.getQuantity());
  }

  private Resource resource(String description, int quantity) {
    return new Resource(ResourceType.WATER, description, quantity, Unit.GALLONS);
  }

  private Shelter newEmptyShelter() {
    return new Shelter(UUID.randomUUID(), "Test Shelter", null, 10, 0, 0,
        new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>());
  }
}
