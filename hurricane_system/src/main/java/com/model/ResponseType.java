package com.model;

/**
 * ResponseType is an enum that represents the different types of responses that can be made to a request.
 * @author LoganH627
 */
public enum ResponseType {
     MEDICAL("Medical"),
     RESOURCE("Resource"),
     ASSISTANCE("Assistance"),
     TRAVEL("Travel");
     
     public final String ASCII;
     /**
      * ResponseType is a constructor that takes in a String ASCII and assigns it to the ASCII field.
      * @param ASCII the ASCII representation of the response type
      */
     private ResponseType(String ASCII) {
         this.ASCII = ASCII;
     }
}
