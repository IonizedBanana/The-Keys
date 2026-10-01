package com.hurricane;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.UUID;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataConstants {
  public static final String USER_FILE_NAME_TEMP = "./json/testing/";

  public static void main(String[] args) {
    String hello = "";
    try {
      File file = new File(USER_FILE_NAME_TEMP + "hello");
      Scanner reader = new Scanner(file);
      hello = reader.nextLine();
      reader.close();
    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    }
    System.out.println(hello);
  }
}
