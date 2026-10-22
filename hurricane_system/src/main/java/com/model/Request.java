package com.model;
import java.io.File;
import java.io.FileWriter;
import java.util.UUID;
import com.model.users.*;

/**
 * 
 * Request
 * @author LoganH627, SynthwaveFox
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

    public User getRequesteeUser() {
        UserList users = UserList.getInstance();
        return users.getUser(requestee);
    }

    /**
     * Writes a printable summary of this request to a .txt file, for a responder to take with them.
     * @param fileName The file to write to (".txt" is added if missing).
     * @param responder The user responding to the request (used for directions).
     * @param shelter The shelter the party is being taken to.
     * @return true if the file was written.
     */
    public boolean exportToTxt(String fileName, User responder, Shelter shelter) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }
        try {
            FileWriter writer = new FileWriter(new File(fileName));
            writer.write(toPrintableText(responder, shelter));
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Builds the printable summary used by {@link #exportToTxt}.
     * @param responder The user responding to the request (used for directions).
     * @param shelter The shelter the party is being taken to.
     * @return The formatted text.
     */
    public String toPrintableText(User responder, Shelter shelter) {
        User user = getRequesteeUser();
        String divider = "----------------------------------------\n";
        StringBuilder text = new StringBuilder();

        text.append("========================================\n");
        text.append("RESCUE REQUEST SUMMARY\n");
        text.append("========================================\n");
        text.append("Request ID: " + id + "\n");
        text.append("Severity: " + severity + " / 5\n");
        text.append("Status: " + (status == null ? "Unknown" : status.ASCII) + "\n");
        text.append("Responder Type: " + (responderType == null ? "Unknown" : responderType.ASCII) + "\n\n");

        text.append("PARTY MEMBERS\n").append(divider);
        if (user == null) {
            text.append("Requestee: " + requestee + " (user not found)\n");
        } else if (user instanceof Victim) {
            Victim victim = (Victim) user;
            text.append("- " + user.getFirstName() + " " + user.getLastName()
                    + ", age " + (victim.getAge() < 0 ? "unknown" : victim.getAge()) + "\n");
            // TODO list the rest of the family's names and ages once party members are stored
            text.append("Party size: " + victim.getPartySize() + "\n");
        } else {
            text.append("- " + user.getFirstName() + " " + user.getLastName() + "\n");
        }
        // TODO list pets (name and species, e.g. "Buddy (dog)") once pets are implemented
        text.append("Pets: TODO\n\n");

        text.append("CONTACT INFORMATION\n").append(divider);
        if (user != null) {
            text.append("Email: " + user.getEmailAddress() + "\n");
        }
        // TODO add phone number once User stores one
        text.append("Phone: TODO\n");
        // TODO switch to the new address handling once Location/Address changes are merged
        Location pickup = (user != null && user.getAddress() != null) ? user.getAddress() : location;
        text.append("Pickup Address: " + (pickup == null ? "Unknown" : pickup.toString()) + "\n\n");

        text.append("MEDICAL NOTES\n").append(divider);
        // TODO use a dedicated medical needs field once one exists; for now this relies on the responder type and description
        if (responderType == ResponseType.MEDICAL) {
            text.append("Medical assistance required.\n");
        } else {
            text.append("No medical assistance requested.\n");
        }
        text.append("Details: " + (description == null ? "None" : description) + "\n\n");

        text.append("DIRECTIONS: RESPONDER TO PICKUP\n").append(divider);
        Location start = (responder == null) ? null : responder.getAddress();
        text.append("From: " + (start == null ? "Unknown" : start.toString()) + "\n");
        text.append("To: " + (pickup == null ? "Unknown" : pickup.toString()) + "\n");
        // TODO generate turn-by-turn directions once routing is implemented
        text.append("Directions: TODO\n\n");

        text.append("DIRECTIONS: PICKUP TO SHELTER\n").append(divider);
        Location shelterAddress = (shelter == null) ? null : shelter.getAddress();
        text.append("From: " + (pickup == null ? "Unknown" : pickup.toString()) + "\n");
        text.append("To: " + (shelterAddress == null ? "Unknown" : shelterAddress.toString()) + "\n");
        // TODO generate turn-by-turn directions once routing is implemented
        text.append("Directions: TODO\n\n");

        text.append("SHELTER\n").append(divider);
        if (shelter == null) {
            text.append("No shelter selected.\n");
        } else {
            text.append("Name: " + shelter.getName() + "\n");
            text.append("Address: " + (shelterAddress == null ? "Unknown" : shelterAddress.toString()) + "\n");
        }

        return text.toString();
    }

}
