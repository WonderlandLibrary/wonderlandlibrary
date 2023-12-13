// 
// Decompiled by Procyon v0.5.36
// 

package net.mcleaks;

public class RedeemResponse
{
    private final String username;
    private final String token;
    
    RedeemResponse(final String username, final String token) {
        this.username = username;
        this.token = token;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getToken() {
        return this.token;
    }
}
