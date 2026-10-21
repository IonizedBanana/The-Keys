package com.model;

import java.util.HashMap;
import com.model.users.*;

public class UserList {
  private static UserList userList;
  private HashMap<String, User> users;

  private UserList() {
    users = DataReader.getUsers();
  }

  public static UserList getInstance() {
    if (userList == null) {
      userList = new UserList();
      return userList;
    }
    return userList;
  }

  public User getUser(String name) {
    return users.get(name);
  }

  public void addUser(User user) {
    users.put(user.getUsername(), user);
  }

  public boolean save() {
    return DataWriter.saveUsers(users);
  }
}
