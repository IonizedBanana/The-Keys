package com.model;
import java.util.UUID;

public class Request {
    private UUID id;
    // private User requestee;
    private byte severity;
    private Location location;
    private ResponseStatus status;
    private ResponseType responderType;
    private String description;

    // public Request(UUID id,User requestee, byte severity, Location location, ResponseStatus status, ResponseType responderType, String description) {
    //     this.id = UUID.randomUUID();
    //     this.requestee = requestee;
    //     this.severity = severity;
    //     this.location = location;
    //     this.status = status;
    //     this.responderType = responderType;
    //     this.description = description;
    // }
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
    // public void updateResponderType(ResponseType responderType) { TODO: think about implementing this method is this somthing that the request class will do or another class
    //     this.responderType = responderType;
    // }
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
    // public UUID getId() { TODO: Think about implementing this is this somthing that the uuid needs
    //     return id;
    // }
    // public String toString() {
    //     return "Request ID: " + id + "\n" +
    //             "Requestee: " + requestee.getUsername() + "\n" +
    //             "Severity: " + severity + "\n" +
    //             "Location: " + location.toString() + "\n" +
    //             "Status: " + status.toString() + "\n" +
    //             "Responder Type: " + responderType.toString() + "\n" +
    //             "Description: " + description;
    // }

}
