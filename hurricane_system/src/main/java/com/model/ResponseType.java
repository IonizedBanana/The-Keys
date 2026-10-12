package com.model;

/**
 * Enum representing different types of responses.
 * @author LoganH627
 */
public enum ResponseType {
     MEDICAL("Medical"),
     RESOURCE("Resource"),
     ASSISTANCE("Assistance"),
     TRAVEL("Travel");
     
     public final String ASCII;
     private ResponseType(String ASCII) {
         this.ASCII = ASCII;
     }
}
