package com.model;
/**
 * ResourceType is an enum that represents different types of resources.
 * @author LoganH627
 */
public enum ResourceType {
    WATER("Water"),
    FOOD("Food"),
    MEDICAL("Medical"),
    CLOTHES("Clothes"),
    BABY_NEEDS("Baby Needs"),
    OTHER("Other");

    public final String ASCII;
    /**
     * ResourceType is a constructor that takes in a String ASCII and assigns it to the ASCII field.
     * @param ASCII the ASCII representation of the resource type
     */
    private ResourceType(String ASCII) {
        this.ASCII = ASCII;
    }
}
