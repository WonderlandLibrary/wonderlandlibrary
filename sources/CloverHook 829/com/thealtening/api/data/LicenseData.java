package com.thealtening.api.data;

import com.google.gson.annotations.SerializedName;

public class LicenseData {

    private String username;

    private boolean premium;

    @SerializedName("premium_name")
    private String premiumName;

    @SerializedName("expires")
    private String expiryDate;

    public String getUsername() {
        return username;
    }

    public boolean isPremium() {
        return premium;
    }

    public String getPremiumName() {
        return premiumName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }


    @Override
    public String toString() {
        return String.format("LicenseData[%s:%s:%s:%s]", username, premium, premiumName, expiryDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LicenseData)) {
            return false;
        }
        LicenseData castedLicenseInfo = (LicenseData) obj;

        return castedLicenseInfo.getExpiryDate().equals(getExpiryDate()) && castedLicenseInfo.getPremiumName().equals(getPremiumName()) && castedLicenseInfo.isPremium() == isPremium() && castedLicenseInfo.getUsername().equals(getUsername());
    }
}
