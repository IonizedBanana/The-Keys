package com.model;

import com.model.users.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Alert
 * An alert to be sent to users in a specific location.
 * @author Jason
 */
public class Alert {
    private ArrayList<Location> affectedLocations;
    private String description;
    private User issuedBy;
    private Date timestamp;

    /**
     * Create an alert.
     * @param affectedLocations The locations involved in the alert.
     * @param description The text in the alert.
     * @param issuedBy The user issuing the alert.
     * @param timestamp
     */
    public Alert(ArrayList<Location> affectedLocations, String description, User issuedBy, Date timestamp) {
        setAffectedLocations(affectedLocations);
        setDescription(description);
        setIssuedBy(issuedBy);
        setTimestamp(timestamp);
    }

    /**
     * Gets the locations involved in the alert.
     * @return An ArrayList of affected locations.
     */
    public ArrayList<Location> getAffectedLocations() {
        return this.affectedLocations;
    }

    /**
     * Sets the affected locations.
     * @param affectedLocations An ArrayList of affected locations.
     */
    public void setAffectedLocations (ArrayList<Location> affectedLocations) {
        if(affectedLocations == null)
            return;
        this.affectedLocations = affectedLocations;
    }

    /**
     * Gets the alert's description.
     * @return The description as a String.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the alert's description.
     * @param description The new description as a String.
     */
    public void setDescription(String description) {
        if(description == null)
            return;
        this.description = description;
    }

    
    public User getIssuedBy() {
        return this.issuedBy;
    }

    public void setIssuedBy(User issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRelevantTo(User user) {
        return (this.getAffectedLocations().contains(user.getAddress().getLocation()));
    }
}
