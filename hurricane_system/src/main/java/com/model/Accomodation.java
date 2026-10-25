package com.model;

public enum Accomodation {
  PET_FRIENDLY("Pet Friendly"),
  MEDICAL_STAFF("Medical Staff");

  public final String accomodation;
  private Accomodation(String accomodation) {
    this.accomodation = accomodation;
  }
}
