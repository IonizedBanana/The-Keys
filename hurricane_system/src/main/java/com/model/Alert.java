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

    }

    public ArrayList<Location> getAffectedLocations() {

    }

    public void setAffectedLocations (ArrayList<Location> affectedLocations) {

    }

    public String getDescription() {
        //TODO
        return "";
    }

    public void setDescription(String description) {

    }

    public String getIssuedBy() {
        //TODO
        return "";
    }

    public void setIssuedBy(String issuedBy) {

    }

    public Date getTimestamp() {
        return null;
    }

    public void setTimestamp(Date timestamp) {

    }

    public boolean isRelevantTo(User user) {
        //TODO
        //return (this.affectedLocations.contains(user.address));
        return true;
    }
}
