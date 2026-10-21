package com.model;
import java.util.UUID;
import com.model.users.*;

/**
 * 
 * Request
 * @author LoganH627
 */
public class Request {
    private UUID id;
    private String requestee;
    private byte severity;
    private Location location;
    private ResponseStatus status;
    private ResponseType responderType;
    private String description;

    public Request(UUID id, String requestee, byte severity, Location location, ResponseStatus status, ResponseType responderType, String description) {
        this.id = UUID.randomUUID();
        this.requestee = requestee;
        this.severity = severity;
        this.location = location;
        this.status = status;
        this.responderType = responderType;
        this.description = description;
    }
    public Request(String requestee, byte severity, Location location, String description) {
        this.requestee = requestee;
        this.severity = severity;
        this.location = location;
        this.description = description;
    }
    public void editSeverity(byte severity) {
        if(severity < 1 || severity > 5) {
            throw new IllegalArgumentException("Severity must be between 1 and 5");
        }
        else {
            this.severity = severity;
        }
    }
    public void updateStatus(ResponseStatus status) {
        this.status = status;
    }
    public UUID getId() {
      return this.id;
    }
    public String getRequestee() {
      return this.requestee;
    }
    public String getDescription() {
        return description;
    }
    public byte getSeverity() {
        return severity;
    }
    public ResponseStatus getStatus() {
        return status;
    }
    public ResponseType getResponderType() {
        return responderType;
    }
    public Location getLocation() {
        return location;
    }
    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        else {
            this.description = description;
        }
    }
    public void setLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        else {
            this.location = location;
        }
    }
    public void setSeverity(byte severity) {
        if(severity < 1 || severity > 5) {
            throw new IllegalArgumentException("Severity must be between 1 and 5");
        }
        else {
            this.severity = severity;
        }
    }
    public void setStatus(ResponseStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        else {
            this.status = status;
        }
    }
    public void setResponderType(ResponseType responderType) {
        if (responderType == null) {
            throw new IllegalArgumentException("Responder type cannot be null");
        }
        else {
            this.responderType = responderType;
        }
    }
    public void setRequestee(String requestee) {
        if (requestee == null) {
            throw new IllegalArgumentException("Requestee cannot be null");
        }
        else {
            this.requestee = requestee;
        }
    }
    public String toString() {
        return "Request ID: " + id + "\n" +
                "Requestee: " + requestee + "\n" +
                "Severity: " + severity + "\n" +
                "Location: " + location.toString() + "\n" +
                "Status: " + status.toString() + "\n" +
                "Responder Type: " + responderType.toString() + "\n" +
                "Description: " + description;
    }

}
