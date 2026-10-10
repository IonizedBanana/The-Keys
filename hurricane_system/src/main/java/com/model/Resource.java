package com.model;

public class Resource {
    private ResourceType type;
    private String description;
    private int quantity;
    private Unit unit;

    public Resource(ResourceType type, String description, int quantity, Unit unit) {
        this.type = type;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
    }
    public void updateQuantity(int newQuantity) {
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
    public int getQuantity() {
        return quantity;
    }
    public Unit getUnit() {
        return unit;
    }
    public String toString() {
    return (quantity + " " +this.unit.ASCII + " of " + this.type.ASCII);
  }
}
