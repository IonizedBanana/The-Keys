package com.model;

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
