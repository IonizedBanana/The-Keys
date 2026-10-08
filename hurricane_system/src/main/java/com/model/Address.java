package com.model;

/**
 * A street address within a {@link Location}.
 *
 * <p>Extends Location because an address is a place, plus the street line
 * that pins it down: the UML gives Address a single {@code address} field
 * but a constructor taking state, city and address, so state and city are
 * inherited. The JSON matches, carrying "state", "city" and "address" in one
 * object.</p>
 *
 * <p>Users and shelters have addresses, because help has to be sent
 * somewhere specific. Hurricanes, requests and alerts work in plain
 * Locations, because a storm covers a city rather than a street.</p>
 *
 * <p><strong>Comparing an Address against a Location:</strong> an Address is
 * never {@code equals} to a plain Location, even one naming the same city --
 * they carry different information, and treating them as interchangeable
 * would make two addresses on the same street compare equal. To ask whether
 * a user is in a hurricane's path, compare the Location:</p>
 *
 * <pre>
 * hurricane.getImpactArea().contains(user.getAddress().getLocation())
 * </pre>
 *
 * <p>Passing the Address itself to that call always returns false.</p>
 *
 * @author SynthwaveFox
 */
public class Address extends Location {

    /** The street line, such as "401 Old ln". Never null. */
    private final String address;

    /**
     * Creates an address.
     *
     * <p>Whitespace is trimmed and null becomes an empty string, matching
     * {@link Location} so a partially filled JSON record loads rather than
     * crashing the read.</p>
     *
     * @param state   the state, such as "Florida"; null is treated as empty
     * @param city    the city, such as "Jacksonville"; null is treated as empty
     * @param address the street line, such as "401 Old ln"; null is treated as empty
     */
    public Address(String state, String city, String address) {
        super(state, city);
        this.address = (address == null) ? "" : address.trim();
    }

    /**
     * Returns the street line.
     *
     * @return the street line, never null, possibly empty
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns just the city and state, dropping the street.
     *
     * <p>This is how an address is compared against the Locations held by
     * hurricanes, requests and alerts. Those collections hold plain
     * Locations, and an Address never equals one, so comparing directly
     * would silently find nothing.</p>
     *
     * @return a Location for this address's city and state
     */
    public Location getLocation() {
        return new Location(getState(), getCity());
    }

    /**
     * Compares by state, city and street, ignoring case.
     *
     * <p>Defers to {@link Location#equals(Object)} for the null check, the
     * class check and the city and state comparison, then adds the street.
     * Because Location compares by exact class, an Address is equal only to
     * another Address.</p>
     *
     * @param o the object to compare with
     * @return true if {@code o} is an Address naming the same street address
     */
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Address other = (Address) o;
        return address.equalsIgnoreCase(other.address);
    }

    /**
     * Returns the address as "Street, City, State".
     *
     * <p>Returns the values as stored, so the output reflects however the
     * data was capitalised.</p>
     *
     * @return the formatted address
     */
    @Override
    public String toString() {
        return address + ", " + super.toString();
    }
}
