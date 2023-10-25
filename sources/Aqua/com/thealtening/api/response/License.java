package com.thealtening.api.response;

public class License {
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

    public String toString() {
        return String.format((String)"License[%s:%s:%s:%s]", (Object[])new Object[]{this.username, this.premium, this.premiumName, this.expiryDate});
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof License)) {
            return false;
        }
        License other = (License)obj;
        return other.getExpiryDate().equals((Object)this.getExpiryDate()) && other.getPremiumName().equals((Object)this.getPremiumName()) && other.isPremium() == this.isPremium() && other.getUsername().equals((Object)this.getUsername());
    }
}
