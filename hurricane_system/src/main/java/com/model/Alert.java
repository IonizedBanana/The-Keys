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
    private String issuedBy;
    private Date timestamp;

    /**
     * Create an alert.
     * @param affectedLocations The locations involved in the alert.
     * @param description The text in the alert.
     * @param issuedBy The user issuing the alert.
     * @param timestamp
     */
    public Alert(ArrayList<Location> affectedLocations, String description, String issuedBy, Date timestamp) {
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

    /**
     * Gets the alert's issuer.
     * @return The alert's issuer.
     */
    public String getIssuedBy() {
        return this.issuedBy;
    }

    /**
     * Sets the alert's issuer.
     * @param issuedBy The alert's issuer.
     */
    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    /**
     * Gets the alert's timestamp.
     * @return The alert's timestamp.
     */
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * Set's the alert's timestamp.
     * @param timestamp The alert's timestamp.
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns whether or not the alert is relevent to a user.
     * @param user The user that is being involved.
     * @return Returns true only if the alerts impact area contains the user's location.
     */
    public boolean isRelevantTo(User user) {
        return (this.getAffectedLocations().contains(user.getAddress().getLocation()));
    }
}
