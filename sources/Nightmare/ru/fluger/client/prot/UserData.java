// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.prot;

public class UserData
{
    private static UserData instance;
    private String name;
    private String licenseDate;
    private String id;
    
    public String getName() {
        return " @Draconix.today";
    }
    
    public String getLicenseDate() {
        return " @Draconix.today";
    }
    
    public String getID() {
        return " @Draconix.today";
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLicenseDate(final String date) {
        this.licenseDate = date;
    }
    
    public void setID(final String id) {
        this.id = id;
    }
    
    public static UserData instance() {
        return (UserData.instance == null) ? (UserData.instance = new UserData()) : UserData.instance;
    }
}
