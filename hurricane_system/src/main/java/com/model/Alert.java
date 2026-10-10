package com.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Alert
 * @author Jason
 */
public class Alert {
    private ArrayList<Location> affectedLocations;
    private String description;
    private String issuedBy;
    private Date timestamp;

    public Alert(ArrayList<Location> affectedLocations, String description, String issuedBy, Date timestamp) {
        setAffectedLocations(affectedLocations);
        setDescription(description);
        setIssuedBy(issuedBy);
        setTimestamp(timestamp);
    }

    public ArrayList<Location> getAffectedLocations() {
        return this.affectedLocations;
    }

    public void setAffectedLocations (ArrayList<Location> affectedLocations) {
        if(affectedLocations == null)
            return;
        this.affectedLocations = affectedLocations;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if(description == null)
            return;
        this.description = description;
    }

    public String getIssuedBy() {
        return this.issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRelevantTo(User user) {
        return (this.getAffectedLocations().contains(user.address.getLocation()));
    }
}
