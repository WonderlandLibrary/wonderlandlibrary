// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.account;

import com.google.gson.JsonObject;

public class Account
{
    private String username;
    private String email;
    private String password;
    
    public Account(final String username, final String email, final String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    public Account() {
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public JsonObject toJson() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", this.username);
        jsonObject.addProperty("email", this.email);
        jsonObject.addProperty("password", this.password);
        return jsonObject;
    }
    
    public void fromJson(final JsonObject json) {
        this.username = json.get("username").getAsString();
        this.email = json.get("email").getAsString();
        this.password = json.get("password").getAsString();
    }
}
