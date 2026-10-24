package com.model;
import java.util.UUID;
import com.model.users.*;
import com.model.location.*;

/**
 * 
 * Request represents a request for assistance in the hurricane response system.
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
    private String MedicalNote;
/**
 * Request is a constructor that takes in parameters for the request's ID, requestee, severity, address, status, responder type, description, and medical note.
 * @param id the unique identifier for the request
 * @param requestee the user who submitted the request
 * @param severity the severity level of the request
 * @param address the address associated with the request
 * @param status the current status of the request
 * @param responderType the type of responder assigned to the request
 * @param description the description of the request
 * @param MedicalNote the medical note for the request
 */
    public Request(UUID id, String requestee, byte severity, Address address, ResponseStatus status, ResponseType responderType, String description, String MedicalNote) {
        this.id = id;
        this.requestee = requestee;
        this.severity = severity;
        this.address = address;
        this.status = status;
        this.responderType = responderType;
        this.description = description;
        this.MedicalNote = MedicalNote;
    }
    /**
     * Request is a constructor that takes in parameters for the requestee, severity, address, and description. The ID is generated automatically, and the status and responder type are set to default values.
     * @param requestee the user who submitted the request
     * @param severity the severity level of the request
     * @param address the address associated with the request
     * @param description the description of the request
     */
    public Request(String requestee, byte severity, Address address, String description) {
        this.requestee = requestee;
        this.severity = severity;
        this.address = address;
        this.description = description;
        this.MedicalNote = null;
    }
    /**
     * Edits the severity of the request.
     * @param severity the new severity level for the request
     * @throws IllegalArgumentException if the severity is not between 1 and 5
     */
    public void editSeverity(byte severity) {
        if(severity < 1 || severity > 5) {
            throw new IllegalArgumentException("Severity must be between 1 and 5");
        }
        else {
            this.severity = severity;
        }
    }
    /**
     * Updates the status of the request.
     * @param status the new status for the request
     * @throws IllegalArgumentException if the status is null
     */
    public void updateStatus(ResponseStatus status) {
        this.status = status;
    }
    /**
     * Returns the ID of the request.
     * @return the ID of the request
     */
    public UUID getId() {
      return this.id;
    }
    /**
     * Returns the requestee of the request.
     * @return the requestee of the request
     */
    public String getRequestee() {
      return this.requestee;
    }
    /**
     * Returns the description of the request.
     * @return the description of the request
     */
    public String getDescription() {
        return description;
    }
    /**
     * Returns the severity of the request.
     * @return the severity of the request
     */
    public byte getSeverity() {
        return severity;
    }
    /**
     * Returns the status of the request.
     * @return the status of the request
     */
    public ResponseStatus getStatus() {
        return status;
    }
    /**
     * Returns the type of responder for the request.
     * @return the type of responder for the request
     */
    public ResponseType getResponderType() {
        return responderType;
    }
    /**
     * Returns the address of the request.
     * @return the address of the request
     */
    public Address getAddress() {
        return address;
    }
    /**
     * Sets the description of the request.
     * @param description the new description for the request
     * @throws IllegalArgumentException if the description is null or empty
     */
    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        else {
            this.description = description;
        }
    }
    /**
     * Sets the address of the request.
     * @param address the new address for the request
     * @throws IllegalArgumentException if the address is null
     */
    public void setAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        else {
            this.address = address;
        }
    }
    /**
     * Sets the severity of the request.
     * @param severity the new severity for the request
     * @throws IllegalArgumentException if the severity is not between 1 and 5
     */
    public void setSeverity(byte severity) {
        if(severity < 1 || severity > 5) {
            throw new IllegalArgumentException("Severity must be between 1 and 5");
        }
        else {
            this.severity = severity;
        }
    }
    /**
     * Sets the status of the request.
     * @param status the new status for the request
     * @throws IllegalArgumentException if the status is null
     */
    public void setStatus(ResponseStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        else {
            this.status = status;
        }
    }
    /**
     * Sets the type of responder for the request.
     * @param responderType the new responder type for the request
     * @throws IllegalArgumentException if the responder type is null
     */
    public void setResponderType(ResponseType responderType) {
        if (responderType == null) {
            throw new IllegalArgumentException("Responder type cannot be null");
        }
        else {
            this.responderType = responderType;
        }
    }
    /**
     * Sets the requestee for the request.
     * @param requestee the new requestee for the request
     * @throws IllegalArgumentException if the requestee is null
     */
    public void setRequestee(String requestee) {
        if (requestee == null) {
            throw new IllegalArgumentException("Requestee cannot be null");
        }
        else {
            this.requestee = requestee;
        }
    }
    /**
     * Returns a string representation of the request.
     * @return a string representation of the request
     */
    public String toString() {
        return "Request ID: " + id + "\n" +
                "Requestee: " + requestee + "\n" +
                "Severity: " + severity + "\n" +
                "Address: " + address.toString() + "\n" +
                "Status: " + status.toString() + "\n" +
                "Responder Type: " + responderType.toString() + "\n" +
                "Description: " + description;
    }

    /**
     * Returns the user associated with the requestee.
     * @return the user associated with the requestee
     */
    public User getRequesteeUser() {
        UserList users = UserList.getInstance();
        return users.getUser(requestee);
    }
    /**
     * Returns the medical note for the request.
     * @return the medical note for the request
     */
    public String getMedicalNote() {
        return MedicalNote;
    }
    /**
     * Sets the medical note for the request.
     * @param MedicalNote the new medical note for the request
     */
    public void setMedicalNote(String MedicalNote) {    
        this.MedicalNote = MedicalNote;
    }

}
