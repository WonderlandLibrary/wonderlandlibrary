// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.user;

public class ACUser
{
    public String mcName;
    public boolean isUser;
    
    public ACUser(final String mcName, final boolean isUser) {
        this.mcName = mcName;
        this.isUser = isUser;
    }
    
    public String getProperties() {
        return String.valueOf(this.mcName) + ":" + (this.isUser ? "true" : "false");
    }
    
    public String getMcName() {
        return this.mcName;
    }
    
    public boolean isUser() {
        return this.isUser;
    }
    
    public void setMcName(final String mcName) {
        this.mcName = mcName;
    }
    
    public void setUser(final boolean user) {
        this.isUser = user;
    }
}
