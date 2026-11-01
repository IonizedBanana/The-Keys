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
    public void findHurricane() {
        Hurricane theHurricane = HurricaneList.getInstance().getHurricane("Scarlett");
        assertTrue(theHurricane.getName().equals("Scarlett"));
    }
}
