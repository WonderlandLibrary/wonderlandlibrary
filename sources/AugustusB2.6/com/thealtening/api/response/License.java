// 
// Decompiled by Procyon v0.5.36
// 

package com.thealtening.api.response;

public class License
{
    private String username;
    private boolean premium;
    private String premiumName;
    private String expiryDate;
    
    public String getUsername() {
        return this.username;
    }
    
    public boolean isPremium() {
        return this.premium;
    }
    
    public String getPremiumName() {
        return this.premiumName;
    }
    
    public String getExpiryDate() {
        return this.expiryDate;
    }
    
    @Override
    public String toString() {
        return String.format("License[%s:%s:%s:%s]", this.username, this.premium, this.premiumName, this.expiryDate);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof License)) {
            return false;
        }
        final License other = (License)obj;
        return other.getExpiryDate().equals(this.getExpiryDate()) && other.getPremiumName().equals(this.getPremiumName()) && other.isPremium() == this.isPremium() && other.getUsername().equals(this.getUsername());
    }
}
