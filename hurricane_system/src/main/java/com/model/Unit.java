package com.model;
/**
 * 
 * Unit
 * @author LoganH627
 */
public enum Unit {
     GALLONS("Gallons"),
     POUNDS("Pounds"),
     CANS("Cans"),
     QTY("Quantity"),
     MEALS("Meals");

     public final String ASCII;
     private Unit(String ASCII) {
         this.ASCII = ASCII;
     }
}
