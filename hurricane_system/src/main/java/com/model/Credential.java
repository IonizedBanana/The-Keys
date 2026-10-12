package com.model;
import java.util.Date;

/**
 * Credential
 * @author Jason, Logan
 * 
 */
public class Credential {
    private String type;
    private String issuer;
    private Date expirationDate;
    private boolean verified;

    public Credential(String type, String issuer, Date expirationDate, boolean verified) {
        this.type = type;
        this.issuer = issuer;
        this.expirationDate = expirationDate;
        this.verified = verified;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        else {
            this.type = type;
        }
        
    }

    public String getIssuer() {
        return this.issuer;
    }

    public void setIssuer(String issuer) {
        if (issuer == null || issuer.isEmpty()) {
            throw new IllegalArgumentException("Issuer cannot be null or empty");
        }
        else {
            this.issuer = issuer;
        }
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        if (expirationDate == null) {
            throw new IllegalArgumentException("Expiration date cannot be null");
        }
        else {
            this.expirationDate = expirationDate;
        }
    }

	public boolean isVerified() {
        return verified;
	}

	public void setVerified(boolean verified) {
        if (verified != true && verified != false) {
            throw new IllegalArgumentException("Verified must be a boolean value");
        }
        else {
            this.verified = verified;
        }
	}
}
