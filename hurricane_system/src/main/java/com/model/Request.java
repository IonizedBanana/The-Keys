package com.model;
import java.util.UUID;
import com.model.users.*;
import com.model.location.*;

/**
 * 
 * Request
 * @author LoganH627
 */
public class Request {
    private UUID id;
    private String requestee;
    private byte severity;
    private Address address;
    private ResponseStatus status;
    private ResponseType responderType;
    private String description;
    private String needsMedical;

    public Request(UUID id, String requestee, byte severity, Address address, ResponseStatus status, ResponseType responderType, String description, String needsMedical) {
        this.id = id;
        this.requestee = requestee;
        this.severity = severity;
        this.address = address;
        this.status = status;
        this.responderType = responderType;
        this.description = description;
        this.needsMedical = needsMedical;
    }
    public Request(String requestee, byte severity, Address address, String description, String needsMedical) {
        this.requestee = requestee;
        this.severity = severity;
        this.address = address;
        this.description = description;
        this.needsMedical = needsMedical;
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
    public Address getAddress() {
        return address;
    }
    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        else {
            this.description = description;
        }
    }
    public void setAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        else {
            this.address = address;
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
                "Address: " + address.toString() + "\n" +
                "Status: " + status.toString() + "\n" +
                "Responder Type: " + responderType.toString() + "\n" +
                "Description: " + description;
    }

    public User getRequesteeUser() {
        UserList users = UserList.getInstance();
        return users.getUser(requestee);
    }
    public String getNeedsMedical() {
        return needsMedical;
    }
    public void setNeedsMedical(String needsMedical) {
        if (needsMedical == null || needsMedical.isEmpty()) {
            throw new IllegalArgumentException("Needs medical cannot be null or empty");
        }
        else {
            this.needsMedical = needsMedical;
        }
    }

}
