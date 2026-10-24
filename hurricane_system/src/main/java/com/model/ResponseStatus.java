package com.model;
/**
 * 
 * ResponseStatus is an enum that represents the status of a response to a request.
 * @author LoganH627
 */
public enum ResponseStatus {
     IN_PROGRESS("In Progress"),
     WAITING("Waiting"),
     MORE_INFO_NEEDED("More Info Needed"),
     COMPLETE("Complete"),
     ASSIGNED("Assigned");

     public final String ASCII;
     /**
      * ResponseStatus is a constructor that takes in a String ASCII and assigns it to the ASCII field.
      * @param ASCII the ASCII representation of the response status
      */
     private ResponseStatus(String ASCII) {
         this.ASCII = ASCII;
     }
}
