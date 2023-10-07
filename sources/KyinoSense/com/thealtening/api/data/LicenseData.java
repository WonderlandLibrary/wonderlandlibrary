/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package com.thealtening.api.data;

import com.google.gson.annotations.SerializedName;

public class LicenseData {
    private String username;
    private boolean premium;
    @SerializedName(value="premium_name")
    private String premiumName;
    @SerializedName(value="expires")
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
        return String.format("LicenseData[%s:%s:%s:%s]", this.username, this.premium, this.premiumName, this.expiryDate);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LicenseData)) {
            return false;
        }
        LicenseData castedLicenseInfo = (LicenseData)obj;
        return castedLicenseInfo.getExpiryDate().equals(this.getExpiryDate()) && castedLicenseInfo.getPremiumName().equals(this.getPremiumName()) && castedLicenseInfo.isPremium() == this.isPremium() && castedLicenseInfo.getUsername().equals(this.getUsername());
    }
}

