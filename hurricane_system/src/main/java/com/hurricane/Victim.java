package com.hurricane;

public class Victim extends User {
    public int age;
    private char sex;
    private String description;
    private int partySize;

    public Victim(String firstName, String lastName, String username, String emailAddress, String password, Address address) {

    }

    public Victim(User user) {

    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {

    }

    public char getSex() {
        return this.sex;
    }

    public void setSex(char sex) {

    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription() {

    }

    public int getPartySize() {
        return this.partySize;
    }

    public void setPartySize(int partySize) {

    }

    public void addInfo() {

    }

    public String getInfo() {

    }

    public void notifyShelter(Shelter shelter, int partySize) {
        
    }

    public void createRequest() {

    }

    public void editRequest() {

    }
}
