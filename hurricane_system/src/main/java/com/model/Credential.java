package com.model;
import java.util.Date;

/**
 * Credential
 * @author Jason
 */
public class Credential {
    private String type;
    private String issuer;
    private Date expirationDate;

    public Credential(String type, String issuer, Date expirationDate) {

    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {

    }

    public String getIssuer() {
        return this.issuer;
    }

    public void setIssuer(String issuer) {

    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        
    }
}
