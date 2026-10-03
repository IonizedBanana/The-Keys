package com.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

/**
 * A tracked hurricane.
 *
 * <p>Admins add a hurricane when one forms, update its current location and
 * predicted path as it moves, and end it once the storm passes. The system
 * uses a hurricane's current location and predicted path to decide which
 * users are in danger and should receive an alert.</p>
 *
 * <p>A hurricane keeps three separate sets of locations:</p>
 * <ul>
 *   <li>{@code currentLocation} - where the storm is right now</li>
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
    private Location currentLocation;

    /** Locations the storm is forecast to reach, in the order it will reach them. */
    private Queue<Location> predictedPath;

    /** Locations the storm has already hit. */
    private ArrayList<Location> impactArea;

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
     * @param currentLocation where the storm is now
     * @param predictedPath   forecast locations in order, or null for none
     * @param impactArea      locations already hit, or null for none
     * @param status          the storm's lifecycle status
     */
    public Hurricane(UUID id, String name, byte category, Location currentLocation,
                     Queue<Location> predictedPath, ArrayList<Location> impactArea,
                     HurricaneStatus status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.currentLocation = currentLocation;
        this.predictedPath = (predictedPath == null) ? new LinkedList<Location>() : predictedPath;
        this.impactArea = (impactArea == null) ? new ArrayList<Location>() : impactArea;
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
     * @param currentLocation where the storm is now
     */
    public Hurricane(String name, byte category, Location currentLocation) {
        this(UUID.randomUUID(), name, category, currentLocation,
             new LinkedList<Location>(), new ArrayList<Location>(), HurricaneStatus.INCOMING);
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
     * @return the current location
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Returns the forecast track.
     *
     * <p>This is the live collection, not a copy, so changes made to the
     * returned queue affect this hurricane.</p>
     *
     * @return locations the storm is forecast to reach, in order; empty if unknown
     */
    public Queue<Location> getPredictedPath() {
        return predictedPath;
    }

    /**
     * Returns everywhere the storm has already hit.
     *
     * <p>This is the live collection, not a copy, so changes made to the
     * returned list affect this hurricane.</p>
     *
     * @return locations already affected; empty for a storm that has not landed
     */
    public ArrayList<Location> getImpactArea() {
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
     * Records a location as having been hit by this storm.
     *
     * <p>Duplicates and nulls are ignored, so the same place is never listed
     * twice. Depends on {@code Location.equals()} comparing by value rather
     * than by reference.</p>
     *
     * @param location the location to record; ignored if null or already present
     */
    public void addImpactArea(Location location) {
        if (location != null && !impactArea.contains(location)) {
            impactArea.add(location);
        }
    }

    /**
     * Moves the storm to a new location.
     *
     * <p>Arriving somewhere changes all three sets of locations, so this does
     * three things: sets the current location, records it as affected, and
     * drops it from the predicted path, since a storm is no longer forecast
     * to reach somewhere it has already arrived.</p>
     *
     * @param location the storm's new current location
     */
    public void updateLocation(Location location) {
        this.currentLocation = location;
        addImpactArea(location);
        predictedPath.remove(location);
    }

    /**
     * Replaces the forecast track, as an admin would when the forecast changes.
     *
     * @param path the new forecast locations in order; null is treated as empty
     */
    public void updatePredictedPath(Queue<Location> path) {
        this.predictedPath = (path == null) ? new LinkedList<Location>() : path;
    }

    /**
     * Reports whether the storm is forecast to hit a location.
     *
     * <p>Checks the predicted path only. Somewhere the storm is already over
     * is not somewhere it <em>will</em> reach, so alert filtering that needs
     * to cover both cases should also compare {@link #getCurrentLocation()}.</p>
     *
     * @param location the location to test
     * @return true if the location is on the predicted path
     */
    public boolean willReach(Location location) {
        return predictedPath.contains(location);
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
     * @return the name, category, current location, and status
     */
    @Override
    public String toString() {
        return name + " (Category " + category + ") - " + currentLocation + " [" + status + "]";
    }
}
