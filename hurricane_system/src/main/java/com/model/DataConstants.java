package com.model;

public class DataConstants {
  // shelter consts
  public static final String SHELTER_FILE_PATH = "../json/shelter.json";
  public static final String SHELTER_UUID = "id";
  public static final String SHELTER_NAME = "name";
  public static final String SHELTER_ADDRESS = "address";
  public static final String SHELTER_TOTAL_CAPACITY = "totalCapacity";
  public static final String SHELTER_USED_CAPACITY = "usedCapacity";
  public static final String SHELTER_EXPECTED_ARRIVALS = "expectedArrivals";
  public static final String SHELTER_RESOURCES = "resources";
  
  // location/address consts
  public static final String LOCATION_CITY = "city";
  public static final String LOCATION_STATE = "state";
  // public static final String ADDRESS_CITY = "city";
  // public static final String ADDRESS_STATE = "state";
  public static final String ADDRESS_ADDRESS = "address";
  
  // resources consts
  public static final String RESOURCE_TYPE = "type";
  public static final String RESOURCE_DESCRIPTION = "description";
  public static final String RESOURCE_QUANTITY = "quantity";
  public static final String RESOURCE_UNIT = "unit";

  // user consts
  public static final String USER_FILE_PATH = "../json/user.json";
  public static final String USER_UUID = "id";
  public static final String USER_TYPE = "type";
  public static final String USER_FIRST_NAME = "firstName";
  public static final String USER_LAST_NAME = "lastName";
  public static final String USER_USERNAME = "username";
  public static final String USER_EMAIL_ADDRESS = "emailAddress";
  public static final String USER_PASSWORD = "password";
  public static final String USER_ADDRESS = "address";
  public static final String USER_RECIEVED_ALERTS = "recievedAlerts";
  public static final String VOLUNTEER_CREDENTIALS = "credentials";
  public static final String VOLUNTEER_FIELDS_OF_EXPERTISE = "fieldsOfExpertise";
  public static final String VOLUNTEER_IDENTITY_VERIFIED = "identityVerified";
  public static final String VOLUNTEER_AVAILABLE = "available";
  public static final String VICTIM_AGE = "age";
  public static final String VICTIM_SEX = "sex";
  public static final String VICTIM_DESCRIPTION = "description";
  public static final String VICTIM_PARTY_SIZE = "partySize";

  // request consts
  public static final String REQUEST_FILE_PATH = "../json/requests.json";
  public static final String REQUEST_UUID = "id";
  public static final String REQUEST_REQUESTEE = "requestee";
  public static final String REQUEST_SEVERITY = "severity";
  public static final String REQUEST_LOCATION = "location";
  public static final String REQUEST_STATUS = "status";
  public static final String REQUEST_RESPONSE_TYPE = "responderType";
  public static final String REQUEST_DESCRIPTION = "description";

  // hurricane consts
  public static final String HURRICANE_FILE_PATH = "../json/hurricane.json";
  public static final String HURRICANE_UUID = "id";
  public static final String HURRICANE_NAME = "name";
  public static final String HURRICANE_CATEGORY = "category";
  public static final String HURRICANE_CURRENT_LOCATION = "currentLocation";
  public static final String HURRICANE_PREDICTED_PATH = "predictedPath";
  public static final String HURRICANE_IMPACT_AREA = "impactArea";
  public static final String HURRICANE_STATUS = "status";

  // Alert consts
  public static final String ALERT_AFFECTED_LOCATIONS = "affectedLocations";
  public static final String ALERT_DESCRIPTION = "description";
  public static final String ALERT_ISSUED_BY = "issuedBy";
  public static final String ALERT_TIMESTAMP = "timestamp";

  // Credential consts
  public static final String CREDENTIAL_TYPE = "type";
  public static final String CREDENTIAL_ISSUER = "issuer";
  public static final String CREDENTIAL_EXPIRATION_DATE = "expirationDate";
  public static final String CREDENTIAL_VERIFIED = "verified";

}
