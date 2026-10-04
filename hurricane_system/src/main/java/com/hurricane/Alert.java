package com.hurricane;

import java.util.ArrayList;
import java.util.Date;

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

    }

    public void setDescription(String description) {

    }

    public String getIssuedBy() {

    }

    public void setIssuedBy(String issuedBy) {

    }

    public Date getTimestamp() {

    }

    public void setTimestamp(Date timestamp) {

    }

    public boolean isRelevantTo(User user) {
        return true;
    }
}
