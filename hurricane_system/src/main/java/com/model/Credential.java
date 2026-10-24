package com.model;
import java.util.Date;

/**
 * Credential class representing a credential with type, issuer, expiration date, and verification status.
 * @author Jason, Logan
 * 
 */
public class Credential {
    private String type;
    private String issuer;
    private Date expirationDate;
    private boolean verified;
/**
 * Constructor for the Credential class.
 * @param type the type of the credential
 * @param issuer the issuer of the credential
 * @param expirationDate the expiration date of the credential
 * @param verified the verification status of the credential
 */
    public Credential(String type, String issuer, Date expirationDate, boolean verified) {
        this.type = type;
        this.issuer = issuer;
        this.expirationDate = expirationDate;
        this.verified = verified;
    }
/**
 * Returns the type of the credential.
 * @return the type of the credential
 */
    public String getType() {
        return this.type;
    }
/**
 * Sets the type of the credential.
 * @param type the new type for the credential
 * @throws IllegalArgumentException if the type is null or empty
 */
    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        else {
            this.type = type;
        }
        
    }
/**
 * Returns the issuer of the credential.
 * @return  the issuer of the credential
 */
    public String getIssuer() {
        return this.issuer;
    }
/**
 * Sets the issuer of the credential.
 * @param issuer the new issuer for the credential
 * @throws IllegalArgumentException if the issuer is null or empty
 */
    public void setIssuer(String issuer) {
        if (issuer == null || issuer.isEmpty()) {
            throw new IllegalArgumentException("Issuer cannot be null or empty");
        }
        else {
            this.issuer = issuer;
        }
    }
/**
 * Returns the expiration date of the credential.
 * @return the expiration date of the credential
 */
    public Date getExpirationDate() {
        return this.expirationDate;
    }
/**
 * Sets the expiration date of the credential.
 * @param expirationDate the new expiration date for the credential
 * @throws IllegalArgumentException if the expiration date is null
 */
    public void setExpirationDate(Date expirationDate) {
        if (expirationDate == null) {
            throw new IllegalArgumentException("Expiration date cannot be null");
        }
        else {
            this.expirationDate = expirationDate;
        }
    }
/**
 * Returns the verification status of the credential.
 * @return the verification status of the credential
 */
	public boolean isVerified() {
        return verified;
	}
/**
 * Sets the verification status of the credential.
 * @param verified the new verification status for the credential
 * @throws IllegalArgumentException if the verified value is not a boolean
 */
	public void setVerified(boolean verified) {
        if (verified != true && verified != false) {
            throw new IllegalArgumentException("Verified must be a boolean value");
        }
        else {
            this.verified = verified;
        }
	}
}
