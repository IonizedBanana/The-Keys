package com.model;

/**
 * Accomodation
 * @author IonizedBanana (Cayden)
 */
public enum Accomodation {
  PET_FRIENDLY("Pet Friendly"),
  MEDICAL_STAFF("Medical Staff");

  public final String accomodation;
  private Accomodation(String accomodation) {
    this.accomodation = accomodation;
  }
}
