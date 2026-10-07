package com.model;

public enum ResponseStatus {
     IN_PROGRESS("In Progress"),
     WAITING("Waiting"),
     MORE_INFO_NEEDED("More Info Needed"),
     COMPLETED("Completed"),
     ASSINGNED("Assigned");

     public final String ASCII;
     private ResponseStatus(String ASCII) {
         this.ASCII = ASCII;
     }
}
