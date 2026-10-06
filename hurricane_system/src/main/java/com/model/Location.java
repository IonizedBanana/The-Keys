package com.model;

import java.util.Objects;

/**
 * A place, identified by state and city.
 *
 * <p>PROTOTYPE: a minimal version so {@link Hurricane} compiles and can be
 * tested. It follows the UML (state and city, plus the two-argument
 * constructor) and adds only what Hurricane needs to work correctly.</p>
 *
 * <p>Two locations are equal when their state and city match. Hurricane
 * relies on this for {@code contains()} and {@code remove()} on its impact
 * area and predicted path.</p>
 *
 * TODO finish and review the full implementation
 *
 * @author SynthwaveFox
 */
public class Location { // TODO Address should extend this (UML: Address(String state, String city, String address))

    /** The state, such as "SC". */
    private String state; // TODO decide on full name vs. abbreviation and enforce it

    /** The city, such as "Columbia". */
    private String city;

    /**
     * Creates a location.
     *
     * @param state the state
     * @param city  the city
     */
    public Location(String state, String city) {
        // TODO validate input (null/blank) once the expected format is decided
        this.state = state;
        this.city = city;
    }

    /**
     * Returns the state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Compares by state and city, so separately created locations for the
     * same place are equal.
     *
     * <p>TODO decide if comparison should ignore case and surrounding whitespace.
     * Data entered by users may not match exactly.</p>
     *
     * @param o the object to compare with
     * @return true if {@code o} is a Location with the same state and city
     */
    @Override
    public boolean equals(Object o) {
        // TODO when Address extends this, make sure an Address and a plain Location compare the way we want
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location other = (Location) o;
        return Objects.equals(state, other.state) && Objects.equals(city, other.city);
    }

    /**
     * Returns the location as "City, State".
     *
     * @return the formatted location
     */
    @Override
    public String toString() {
        return city + ", " + state;
    }
}
