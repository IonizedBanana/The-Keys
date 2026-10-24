package com.model;
/**
 * Resource is a class that represents a resource with a type, description, quantity, and unit.
 * @author LoganH627
 */
public class Resource {
    private ResourceType type;
    private String description;
    private int quantity;
    private Unit unit;
/**
 * Resource is a constructor that takes in a ResourceType, String description, int quantity, and Unit unit and assigns them to the corresponding fields.
 * @param type the type of the resource
 * @param description the description of the resource
 * @param quantity the quantity of the resource
 * @param unit the unit of measurement for the resource
 */
    public Resource(ResourceType type, String description, int quantity, Unit unit) {
        this.type = type;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
    }
    /**
     * Updates the quantity of the resource to a new value if it is greater than 0, otherwise throws an IllegalArgumentException.
     * @param newQuantity the new quantity to set for the resource
     * @throws IllegalArgumentException if the new quantity is not greater than 0
     */
    public void updateQuantity(int newQuantity) {
        if(newQuantity > 0) {
            this.quantity = newQuantity;
        } else {
            throw new IllegalArgumentException("Quantity must be greater than 0");  
        }
    }
    /**
     * Returns the type of the resource.
     * @return the type of the resource
     */
    public ResourceType getType() {
        return type;
    }
    /**
     * Returns the description of the resource.
     * @return the description of the resource
     */
    public String getDescription() {
        return description;
    }
    /**
     * Returns the quantity of the resource.
     * @return the quantity of the resource
     */
    public int getQuantity() {
        return quantity;
    }
    /**
     * Returns the unit of measurement for the resource.
     * @return the unit of measurement for the resource
     */
    public Unit getUnit() {
        return unit;
    }
    /**
     * Returns a string representation of the resource.
     * @return a string representation of the resource
     */
    public String toString() {
    return (quantity + " " +this.unit.ASCII + " of " + this.type.ASCII);
  }
}
