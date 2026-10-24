package com.model;
/**
 * 
 * Unit is an enum that represents the unit of measurement for a resource.
 * @author LoganH627
 */
public enum Unit {
     GALLONS("Gallons"),
     POUNDS("Pounds"),
     CANS("Cans"),
     QTY("Quantity"),
     MEALS("Meals");

     public final String ASCII;
     /**
      * Unit is a constructor that takes in a String ASCII and assigns it to the ASCII field.
      * @param ASCII the ASCII representation of the unit
      */
     private Unit(String ASCII) {
         this.ASCII = ASCII;
     }
}
