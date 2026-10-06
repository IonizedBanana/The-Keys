package com.model;
import java.util.ArrayList;

public class Victim extends User {
    public int age;
    private char sex;
    private String description;
    private int partySize;
    private ArrayList<Request> createdRequests;

    public Victim(String firstName, String lastName, String username, String emailAddress, String password, Address address) {
        super(firstName, lastName, username, emailAddress, password, address);
        this.setType(UserType.VICTIM);
        this.age = -1;
        this.sex = ' ';
        this.description = "No description";
        this.partySize = 1;
    }

    public Victim(User user) {
        super(user.firstName, user.lastName, user.username, user.emailAddress, user.password, user.address);
        this.setType(UserType.VICTIM);
        this.age = -1;
        this.sex = ' ';
        this.description = "No description";
        this.partySize = 1;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        if(age >= 0) {
            this.age = age;
        }
    }

    public char getSex() {
        return this.sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public int getPartySize() {
        return this.partySize;
    }

    public void setPartySize(int partySize) {
        if (partySize >= 1) {
            this.partySize = partySize;
        }
    }

    /*

    Removed because this method was added to UML with a console based interface in mind. Setters should be used instead.

    public void addInfo() {

    }
    */

    public String getInfo() {
        //A string of the attributes of victim (ONLY age, sex, description)
        return this.getAge() + "\n" + this.getSex() + "\n" + this.getDescription();
    }

    public void notifyShelter(Shelter shelter, int partySize) {
        shelter.notify(partySize);
    }

    public void createRequest() {
        //TODO
    }

    public void editRequest() {
        //TODO
    }
}
