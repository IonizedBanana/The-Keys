package com.model;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.UUID;
import com.model.users.*;
import com.model.users.Victim.Person;
import com.model.location.*;

/**
 * 
 * Request
 * @author LoganH627, SynthwaveFox
 */
public class Request {
    private UUID id;
    private String requestee;
    private byte severity;
    private Address address;
    private ResponseStatus status;
    private ResponseType responderType;
    private String description;

    public Request(UUID id, String requestee, byte severity, Address address, ResponseStatus status, ResponseType responderType, String description) {
        this.id = UUID.randomUUID();
        this.requestee = requestee;
        this.severity = severity;
        this.address = address;
        this.status = status;
        this.responderType = responderType;
        this.description = description;
    }
    public Request(String requestee, byte severity, Address address, String description) {
        this.requestee = requestee;
        this.severity = severity;
        this.address = address;
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
        Victim victim = (user instanceof Victim) ? (Victim) user : null;
        String divider = "----------------------------------------\n";
        StringBuilder text = new StringBuilder();

        text.append("========================================\n");
        text.append("RESCUE TRIP SHEET\n");
        text.append("========================================\n");
        text.append("Request ID: " + id + "\n");
        text.append("Severity: " + severity + " / 5\n");
        text.append("Status: " + (status == null ? "Unknown" : status.ASCII) + "\n");
        text.append("Help Needed: " + (responderType == null ? "Unknown" : responderType.ASCII) + "\n");
        if (responder != null) {
            text.append("Responder: " + responder.getFirstName() + " " + responder.getLastName() + "\n");
        }
        text.append("\n");

        text.append("PEOPLE\n").append(divider);
        ArrayList<Person> pets = new ArrayList<Person>();
        if (victim != null) {
            for (Person person : victim.getFullParty()) {
                if (person.getIsPet()) {
                    pets.add(person);
                } else {
                    text.append("- " + person.getName() + ", age " + ageText(person.getAge())
                            + describe(person.getDescription()) + "\n");
                }
            }
            text.append("Total people: " + victim.getPartySize() + "\n\n");
        } else if (user != null) {
            text.append("- " + user.getFirstName() + " " + user.getLastName() + "\n\n");
        } else {
            text.append("- " + requestee + " (account not found)\n\n");
        }

        text.append("PETS\n").append(divider);
        if (pets.isEmpty()) {
            text.append("None\n");
        }
        for (Person pet : pets) {
            text.append("- " + pet.getName() + " (" + (isBlank(pet.getDescription()) ? "pet" : pet.getDescription().toLowerCase())
                    + "), age " + ageText(pet.getAge()) + "\n");
        }
        text.append("\n");

        text.append("CONTACT AND PICKUP\n").append(divider);
        if (user != null) {
            text.append("Name: " + user.getFirstName() + " " + user.getLastName() + "\n");
            text.append("Email: " + user.getEmailAddress() + "\n");
        }
        if (victim != null) {
            text.append("Phone: " + (isBlank(victim.getPhoneNumber()) ? "Not given" : victim.getPhoneNumber()) + "\n");
        }
        text.append("Pickup Address: " + (address == null ? "Unknown" : address.toString()) + "\n\n");

        text.append("MEDICAL\n").append(divider);
        // TODO use the request's medical note once Logan's Request changes are merged
        // text.append(isBlank(medicalNote) ? "No medical assistance requested.\n" : "MEDICAL ASSISTANCE NEEDED: " + medicalNote + "\n");
        text.append(responderType == ResponseType.MEDICAL ? "Medical assistance required.\n" : "See details below.\n");
        text.append("Details: " + (isBlank(description) ? "None" : description) + "\n\n");

        Address start = (responder == null) ? null : responder.getAddress();
        appendDirections(text, divider, "DIRECTIONS: RESPONDER TO PICKUP", start, address);

        Address shelterAddress = (shelter == null) ? null : shelter.getAddress();
        appendDirections(text, divider, "DIRECTIONS: PICKUP TO SHELTER", address, shelterAddress);

        text.append("SHELTER\n").append(divider);
        if (shelter == null) {
            text.append("No shelter selected.\n");
        } else {
            text.append("Name: " + shelter.getName() + "\n");
            text.append("Address: " + (shelterAddress == null ? "Unknown" : shelterAddress.toString()) + "\n");
        }

        return text.toString();
    }

    /**
     * Adds one leg of the trip, with turn by turn directions from the map api, to the trip sheet.
     * @param text The trip sheet being built.
     * @param divider The line printed under the heading.
     * @param heading The heading for this leg.
     * @param from Where the leg starts.
     * @param to Where the leg ends.
     */
    private static void appendDirections(StringBuilder text, String divider, String heading, Address from, Address to) {
        text.append(heading + "\n").append(divider);
        text.append("From: " + (from == null ? "Unknown" : from.toString()) + "\n");
        text.append("To: " + (to == null ? "Unknown" : to.toString()) + "\n");
        String[] steps = null;
        if (from != null && to != null) {
            try {
                steps = MapTools.getInstance().getDrivingDirections(from, to);
            } catch (Exception e) {
                steps = null;
            }
        }
        if (steps == null || steps.length == 0) {
            text.append("Directions: not available\n\n");
            return;
        }
        for (int i = 0; i < steps.length; i++) {
            text.append("  " + (i + 1) + ". " + steps[i] + "\n");
        }
        text.append("\n");
    }

    /**
     * Formats an age for the trip sheet.
     * @param age The age, or a negative number if unknown.
     * @return The age, or "unknown".
     */
    private static String ageText(int age) {
        return age < 0 ? "unknown" : String.valueOf(age);
    }

    /**
     * Formats a party member's description to follow their name and age.
     * @param description The description.
     * @return ". " and the description, or nothing if there isn't one.
     */
    private static String describe(String description) {
        return isBlank(description) ? "" : ". " + description;
    }

    /**
     * Reports whether text is missing or only whitespace.
     * @param text The text to check.
     * @return true if the text is null or blank.
     */
    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

}
