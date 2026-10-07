package com.model;

import java.util.Locale;
import java.util.Objects;

/**
 * A place, identified by state and city.
 *
 * <p>Locations are the unit the system reasons about geographically: a
 * hurricane sits in one, moves through a list of them, and records the ones
 * it has hit; requests are reported from one; alerts are targeted at them.</p>
 *
 * <p>A Location is a value, not a thing with its own identity. Two Locations
 * naming the same place are equal and interchangeable, which is what lets
 * {@link Hurricane#willReach(Location)} and
 * {@link Hurricane#addImpactArea(Location)} work with a Location loaded from
 * JSON and one built in code. The fields are final for that reason -- a
 * Location never changes into a different place.</p>
 *
 * @author SynthwaveFox
 */
public class Location { // Address holds a Location rather than extending it; see equals()

    /** The state, never null. Stored as given, apart from trimming. */
    private final String state;

    /** The city, never null. Stored as given, apart from trimming. */
    private final String city;

    /**
     * Creates a location.
     *
     * <p>Surrounding whitespace is trimmed and null is stored as an empty
     * string, so no caller has to null-check a state or city. Nothing is
     * rejected: the JSON data files are hand-written and partially filled
     * in, and a half-known location is more useful than a crash during
     * loading.</p>
     *
     * <p>Capitalisation is left alone, because the team has not settled on
     * full state names versus abbreviations. {@link #equals(Object)} ignores
     * case so that decision can be made later without breaking anything.</p>
     *
     * @param state the state, such as "Florida"; null is treated as empty
     * @param city  the city, such as "Jacksonville"; null is treated as empty
     */
    public Location(String state, String city) {
        this.state = (state == null) ? "" : state.trim();
        this.city = (city == null) ? "" : city.trim();
    }

    /**
     * Returns the state.
     *
     * @return the state, never null, possibly empty
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the city.
     *
     * @return the city, never null, possibly empty
     */
    public String getCity() {
        return city;
    }

    /**
     * Reports whether this location names an actual place.
     *
     * <p>Useful when loading: a record missing its city or state produces a
     * Location rather than an error, and this is how a caller notices.</p>
     *
     * @return true if both state and city are non-empty
     */
    public boolean isComplete() {
        return !state.isEmpty() && !city.isEmpty();
    }

    /**
     * Compares by state and city, ignoring case.
     *
     * <p>Case is ignored because the data files are not consistent: the same
     * place appears as "jacksonville"/"florida" in one record and
     * "Houston"/"Texas" in another. Comparing exactly would make two spellings
     * of one city into two different places, and a hurricane would fail to
     * recognise a location it had already hit.</p>
     *
     * <p>Uses {@link Locale#ROOT} so the result does not depend on the
     * machine's locale.</p>
     *
     * <p>Compares by exact class rather than {@code instanceof}, so a
     * subclass is never equal to a plain Location. If Address is written as a
     * subclass, a user's Address would therefore never match a hurricane's
     * impact area, and alert filtering would silently find nobody. Give
     * Address a Location field and compare that instead.</p>
     *
     * @param o the object to compare with
     * @return true if {@code o} is a Location naming the same place
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location other = (Location) o;
        return normalized(state).equals(normalized(other.state))
            && normalized(city).equals(normalized(other.city));
    }

    /**
     * Returns a hash consistent with {@link #equals(Object)}.
     *
     * <p>Required: two Locations that are equal must return the same hash, or
     * a HashSet or HashMap keyed by Location will fail to find entries it
     * contains. Because equals() ignores case, this hashes the lowercased
     * values.</p>
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(normalized(state), normalized(city));
    }

    /**
     * Returns the location as "City, State".
     *
     * <p>Returns the values as stored, so the output reflects however the
     * data was capitalised.</p>
     *
     * @return the formatted location
     */
    @Override
    public String toString() {
        return city + ", " + state;
    }

    /**
     * Reduces a value to the form used for comparison and hashing.
     *
     * @param value the value to reduce, never null
     * @return the value lowercased in a locale-independent way
     */
    private static String normalized(String value) {
        return value.toLowerCase(Locale.ROOT);
    }
}
