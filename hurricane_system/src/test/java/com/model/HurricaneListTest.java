package com.model;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.BeforeClass;

import com.model.location.*;
import com.model.users.*;
import com.model.users.Victim.Person;

import java.util.HashMap;

import org.junit.Test;

/* Like with UserListTest, I didn't use AI, I'm just listing this to make sure I don't lose points for NOT doing it.
-Test------------------------------------Reasoning--------------------------------------
-testFindHurricane              |(Hurricanes should be able to be found) Passed
-testAddHurricaneValid          |(Valid hurricanes should have no problems being added) Passed
-testAddNullHurricane           |(null should not be added as a hurricane) --Runtime Error #94
-testAddNoNameHurricane         |(Hurricanes should be required to have a name) --Failed #71
-testUpdateAddress              |(Updating the hurricane's current area should add it to the impact area, remove it from the predicted path, and set the current address to the new address) Passed
-testUpdateNewAddress           |(The program should have no issues when the hurricane's new address is not already in the predicted path) Passed
-testAddValidArea               |(Valid areas should have no problems being added to the impactArea) Passed
-testAddNullToArea              |(null should not be allowed as the address in impactArea) Passed
-testAddNullArea                |(addresses with null as attributes should not be allowed in impactArea) Failed #76
-testAddDuplicateArea           |(areas should not repeat in impactArea) --Failed #77
-testSeverityValid              |(Category 1-5 hurricanes should have no issues being created) Passed
-testSeverityOutOfRange         |(Categories outside of 5 should not be allowed) Failed #89
-testSeverityNegative           |(Negative Categories should not be allowed) Failed #89
-testEndHurricane               |(Ending a hurricane should mark it as over and clear its path) Passed
-testWillReach                  |(If a location is in the path, willReach should return true) --Failed #95
--------------------------------------------------------------------------------------
*/

public class HurricaneListTest {
    @BeforeClass
	public static void setup() {
        HurricaneList.getInstance().clear();
        Address a = new Address("Charleston", "SC");
        ArrayList<Address> path = new ArrayList<Address>();
        path.add(new Address("Summerville", "SC"));

        ArrayList<Address> area = new ArrayList<Address>();
        path.add(a);

        //Hurricane h = new Hurricane("Scarlett", ((byte) 4), a, path, area);
        Hurricane h = new Hurricane("Scarlett", ((byte) 4), a);

        HurricaneList.getInstance().addHurricane(h);

        HurricaneList.getInstance().save();

    }

    @Test
    public void testFindHurricane() {
        Hurricane theHurricane = HurricaneList.getInstance().getHurricane("Scarlett");
        assertTrue(theHurricane.getName().equals("Scarlett"));
    }

    @Test
    public void testAddValidHurricane() {
        Hurricane h = new Hurricane("McDirk", ((byte) 4), new Address("Charleston", "SC"));
        HurricaneList.getInstance().addHurricane(h);
        assertTrue(HurricaneList.getInstance().getHurricaneList().containsKey("McDirk"));
    }

    @Test
    public void testAddNoNameHurricane() {
        Hurricane h = new Hurricane("", ((byte) 4), new Address("Charleston", "SC"));
        HurricaneList.getInstance().addHurricane(h);
        assertFalse(HurricaneList.getInstance().getHurricaneList().size() == 1);
    }

    @Test
    // Causes runtime error #94
    public void testAddNullHurricane() {
        Hurricane h = null;
        HurricaneList.getInstance().addHurricane(h);
        assertFalse(HurricaneList.getInstance().getHurricaneList().size() == 1);
    }

    @Test
    public void testAddValidArea() {
        Hurricane theHurricane = HurricaneList.getInstance().getHurricane("Scarlett");
        Address area = new Address("Charleston", "SC");
        theHurricane.addImpactArea(area);
        assertTrue(theHurricane.getImpactArea().contains(area));

    }
    @Test
    public void testAddNulltoArea() {
        Hurricane theHurricane = HurricaneList.getInstance().getHurricane("Scarlett");
        Address area = null;
        theHurricane.addImpactArea(area);
        assertFalse(theHurricane.getImpactArea().contains(area));
    }

    @Test 
    public void testAddNullArea() {
        Hurricane theHurricane = HurricaneList.getInstance().getHurricane("Scarlett");
        Address area = new Address(null, null);
        theHurricane.addImpactArea(area);
        assertFalse(theHurricane.getImpactArea().contains(area));
    }

    @Test 
    public void testAddDuplicateArea() {
        Hurricane theHurricane = HurricaneList.getInstance().getHurricane("Scarlett");
        Address area = new Address("Charleston", "SC");
        System.out.println(theHurricane.getImpactArea().size());
        theHurricane.addImpactArea(area);
        System.out.println(theHurricane.getImpactArea().size());

        // If "area" gets redefined, it gets stored, even though the attributes are equal
        // test passes if "area" is not redefined, so the !impactArea.contains(address) might be the problem 
        area = new Address("Charleston", "SC");
        System.out.println(theHurricane.getImpactArea().size());
        theHurricane.addImpactArea(area);
        System.out.println(theHurricane.getImpactArea().size());
        assertFalse(theHurricane.getImpactArea().contains(area));
    }
    @Test
    public void testUpdateAddress() {
        Hurricane theHurricane = HurricaneList.getInstance().getHurricane("Scarlett");
        Address area = new Address("Summerville", "SC");
        theHurricane.updateAddress(area);
        assertTrue(theHurricane.getCurrentAddress() == area && theHurricane.getImpactArea().contains(area) && !theHurricane.getPredictedPath().contains(area));
    }

    @Test
    public void testUpdateNewAddress() {
        Hurricane theHurricane = HurricaneList.getInstance().getHurricane("Scarlett");
        Address area = new Address("Columbia", "SC");
        theHurricane.updateAddress(area);
        assertTrue(theHurricane.getCurrentAddress() == area && theHurricane.getImpactArea().contains(area) && !theHurricane.getPredictedPath().contains(area));
    }

    @Test 
    public void testSeverityValid() {
        Hurricane h = new Hurricane("Hurricane Hurricane", ((byte) 1), new Address("Charleston", "SC"));
        HurricaneList.getInstance().addHurricane(h);
        assertTrue(HurricaneList.getInstance().getHurricaneList().get("Hurricane Hurricane").getCategory() == 1);
    }

    @Test 
    public void testSeverityOutOfRange() {
        Hurricane h = new Hurricane("Hurricane Hurricane", ((byte) 6), new Address("Charleston", "SC"));
        HurricaneList.getInstance().addHurricane(h);
        assertFalse(HurricaneList.getInstance().getHurricaneList().get("Hurricane Hurricane").getCategory() == 6);
    }

    @Test 
    public void testSeverityNegative() {
        Hurricane h = new Hurricane("Hurricane Hurricane", ((byte) -1), new Address("Charleston", "SC"));
        HurricaneList.getInstance().addHurricane(h);
        assertFalse(HurricaneList.getInstance().getHurricaneList().get("Hurricane Hurricane").getCategory() == -1);
    }

    @Test
    public void testEndHurricane() {
        Hurricane theHurricane = HurricaneList.getInstance().getHurricane("Scarlett");
        theHurricane.endHurricane();
        assertTrue(theHurricane.getStatus() == HurricaneStatus.OVER && theHurricane.getPredictedPath().size() == 0);
    }

    @Test 
    public void testWillReach() {
        Hurricane theHurricane = HurricaneList.getInstance().getHurricane("Scarlett");
        Address area = new Address("Columbia", "SC");
        ArrayList<Address> path = new ArrayList<Address>();
        path.add(area);
        theHurricane.updatePredictedPath(path);
        area = new Address("Columbia", "SC");
        assertTrue(theHurricane.willReach(area));
    }
}
