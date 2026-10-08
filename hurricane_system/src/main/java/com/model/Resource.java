package com.model;
/**
 * 
 * Resource
 * @author LoganH627
 */
public class Resource {
    private ResourceType type;
    private String description;
    private int quantity;
    private Unit unit;

    public Resource(ResourceType type, int quantity, Unit unit) {
        this.type = type;
        this.quantity = quantity;
        this.unit = unit;
    }
    public Resource(String description, int quantity, Unit unit) {
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
}
