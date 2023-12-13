// 
// Decompiled by Procyon v0.6.0
// 

package fr.litarvan.openauth.model.response;

import fr.litarvan.openauth.model.AuthProfile;

public class RefreshResponse
{
    private String accessToken;
    private String clientToken;
    private AuthProfile selectedProfile;
    
    public RefreshResponse(final String accessToken, final String clientToken, final AuthProfile selectedProfile) {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
        this.selectedProfile = selectedProfile;
    }
    
    public String getAccessToken() {
        return this.accessToken;
    }
    
    public String getClientToken() {
        return this.clientToken;
    }
    
    public AuthProfile getSelectedProfile() {
        return this.selectedProfile;
    }
}
