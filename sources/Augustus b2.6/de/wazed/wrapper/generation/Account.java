// 
// Decompiled by Procyon v0.5.36
// 

package de.wazed.wrapper.generation;

import java.util.HashMap;

public class Account
{
    private String token;
    private String password;
    private String username;
    private boolean limit;
    private HashMap<String, String> info;
    
    public Account(final String token, final String password, final String username, final boolean limit, final HashMap<String, String> info) {
        this.token = token;
        this.password = password;
        this.username = username;
        this.limit = limit;
        this.info = info;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public boolean isLimit() {
        return this.limit;
    }
    
    public HashMap<String, String> getInfo() {
        return this.info;
    }
}
