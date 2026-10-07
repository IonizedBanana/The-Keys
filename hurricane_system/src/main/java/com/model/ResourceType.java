package com.model;

public enum ResourceType {
    WATER("Water"),
    FOOD("Food"),
    MEDICAL("Medical"),
    CLOTHES("Clothes"),
    BABY_NEEDS("Baby Needs"),
    OTHER("Other");

    public final String ASCII;
    private ResourceType(String ASCII) {
        this.ASCII = ASCII;
    }
}
