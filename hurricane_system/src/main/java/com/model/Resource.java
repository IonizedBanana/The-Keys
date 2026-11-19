package com.model;
/**
 * 
 * Resource
 * @author LoganH627
 */
public class Resource {
    private ResourceType type;
    private String description;
    private double quantity;
    private Unit unit;

    public Resource(ResourceType type, String description, double quantity, Unit unit) {
        this.type = type;
        this.description = description;
        if (quantity > 0) {
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.unit = unit;
    }
    public void updateQuantity(double newQuantity) {
        if(newQuantity > 0) {
            this.quantity = newQuantity;
        } else {
            throw new IllegalArgumentException("Quantity must be greater than 0");  
        }
    }
    public ResourceType getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }
    public double getQuantity() {
        return quantity;
    }
    public Unit getUnit() {
        return unit;
    }
    public boolean equals(Resource other) {
        if (other == null) {
            return false;
        } 
        return this.type == other.type &&
               this.unit == other.unit;
    }
    public String toString() {
    return (quantity + " " +this.unit.ASCII + " of " + this.type.ASCII);
  }
}
