package com.model;

import java.util.ArrayList;
import java.util.UUID;
import com.model.location.*;

/**
 * A tracked hurricane.
 *
 * <p>Admins add a hurricane when one forms, update its current address and
 * predicted path as it moves, and end it once the storm passes. The system
 * uses a hurricane's current address and predicted path to decide which
 * users are in danger and should receive an alert.</p>
 *
 * <p>A hurricane keeps three separate sets of addresss:</p>
 * <ul>
 *   <li>{@code currentAddress} - where the storm is right now</li>
 *   <li>{@code predictedPath} - where it is forecast to go next, in order</li>
 *   <li>{@code impactArea} - everywhere it has already hit</li>
 * </ul>
 *
 * @author SynthwaveFox
 */
public class Hurricane {

    /** Unique identifier, kept across saves so references persist across a reload. */
    private UUID id;

    /** Name the storm is announced under, such as "Kathrine". */
    private String name;

    /** Storm category, 1 through 5. */
    private byte category;

    /** Where the storm is at the moment. */
    private Address currentAddress;

    /** Addresss the storm is forecast to reach, in the order it will reach them. */
    private ArrayList<Address> predictedPath;

    /** Addresss the storm has already hit. */
    private ArrayList<Address> impactArea;

    /** Where the storm is in its lifecycle. */
    private HurricaneStatus status;

    /**
     * Rebuilds a hurricane that was previously saved.
     *
     * <p>Used by DataLoader. Taking the id as a parameter is what lets a
     * hurricane keep the same identity across saves; anything stored by id
     * would otherwise point at a storm that no longer exists.</p>
     *
     * <p>Null collections are replaced with empty ones, so callers never have
     * to null-check the path or the impact area.</p>
     *
     * @param id              the saved identifier
     * @param name            the storm's name
     * @param category        the storm category, 1 through 5
     * @param currentAddress where the storm is now
     * @param predictedPath   forecast addresss in order, or null for none
     * @param impactArea      addresss already hit, or null for none
     * @param status          the storm's lifecycle status
     */
    public Hurricane(UUID id, String name, byte category, Address currentAddress,
                     ArrayList<Address> predictedPath, ArrayList<Address> impactArea,
                     HurricaneStatus status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.currentAddress = currentAddress;
        this.predictedPath = (predictedPath == null) ? new ArrayList<Address>() : predictedPath;
        this.impactArea = (impactArea == null) ? new ArrayList<Address>() : impactArea;
        this.status = status;
    }

    /**
     * Creates a brand new hurricane, as an admin would when a storm forms.
     *
     * <p>Generates a fresh id and starts the storm {@link HurricaneStatus#INCOMING}
     * with an empty predicted path and an empty impact area, because a storm
     * that has not arrived anywhere yet has not affected anywhere yet.</p>
     *
     * @param name            the storm's name
     * @param category        the storm category, 1 through 5
     * @param currentAddress where the storm is now
     */
    public Hurricane(String name, byte category, Address currentAddress) {
        this(UUID.randomUUID(), name, category, currentAddress,
             new ArrayList<Address>(), new ArrayList<Address>(), HurricaneStatus.INCOMING);
    }

    /**
     * Returns this hurricane's unique identifier.
     *
     * @return the identifier, stable across saves
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the storm's name.
     *
     * @return the name, such as "Kathrine"
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the storm's strength.
     *
     * @return the storm category, 1 through 5
     */
    public byte getCategory() {
        return category;
    }

    /**
     * Returns where the storm is at the moment.
     *
     * @return the current address
     */
    public Address getCurrentAddress() {
        return currentAddress;
    }

    /**
     * Returns the forecast track.
     *
     * <p>This is the live collection, not a copy, so changes made to the
     * returned queue affect this hurricane.</p>
     *
     * @return addresss the storm is forecast to reach, in order; empty if unknown
     */
    public ArrayList<Address> getPredictedPath() {
        return predictedPath;
    }

    /**
     * Returns everywhere the storm has already hit.
     *
     * <p>This is the live collection, not a copy, so changes made to the
     * returned list affect this hurricane.</p>
     *
     * @return addresss already affected; empty for a storm that has not landed
     */
    public ArrayList<Address> getImpactArea() {
        return impactArea;
    }

    /**
     * Returns where the storm is in its lifecycle.
     *
     * @return the current status
     */
    public HurricaneStatus getStatus() {
        return status;
    }

    /**
     * Records a address as having been hit by this storm.
     *
     * <p>Duplicates and nulls are ignored, so the same place is never listed
     * twice. Depends on {@code Address.equals()} comparing by value rather
     * than by reference.</p>
     *
     * @param address the address to record; ignored if null or already present
     */
    public void addImpactArea(Address address) {
        if (address != null && !impactArea.contains(address)) {
            impactArea.add(address);
        }
    }

    /**
     * Moves the storm to a new address.
     *
     * <p>Arriving somewhere changes all three sets of addresss, so this does
     * three things: sets the current address, records it as affected, and
     * drops it from the predicted path, since a storm is no longer forecast
     * to reach somewhere it has already arrived.</p>
     *
     * @param address the storm's new current address
     */
    public void updateAddress(Address address) {
        this.currentAddress = address;
        addImpactArea(address);
        predictedPath.remove(address);
    }

    /**
     * Replaces the forecast track, as an admin would when the forecast changes.
     *
     * @param path the new forecast addresss in order; null is treated as empty
     */
    public void updatePredictedPath(ArrayList<Address> path) {
        this.predictedPath = (path == null) ? new ArrayList<Address>() : path;
    }

    /**
     * Reports whether the storm is forecast to hit a address.
     *
     * <p>Checks the predicted path only. Somewhere the storm is already over
     * is not somewhere it <em>will</em> reach, so alert filtering that needs
     * to cover both cases should also compare {@link #getCurrentAddress()}.</p>
     *
     * @param address the address to test
     * @return true if the address is on the predicted path
     */
    public boolean willReach(Address address) {
        return predictedPath.contains(address);
    }

    /**
     * Marks the storm as finished.
     *
     * <p>Sets the status to {@link HurricaneStatus#OVER} and clears the
     * predicted path, since a storm that is over is not forecast to reach
     * anywhere. The impact area is kept, because where a storm hit still
     * matters after it passes.</p>
     */
    public void endHurricane() {
        this.status = HurricaneStatus.OVER;
        this.predictedPath.clear();
    }

    /**
     * Returns a one-line summary suitable for lists and logs.
     *
     * @return the name, category, current address, and status
     */
    @Override
    public String toString() {
        return name + " (Category " + category + ") - " + currentAddress + " [" + status + "]";
    }
}
