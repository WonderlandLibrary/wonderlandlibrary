// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.enums;

import java.util.HashMap;
import java.util.Map;

public enum AuthorizationScope
{
    APP_REMOTE_CONTROL("APP_REMOTE_CONTROL", 0, "app-remote-control"), 
    PLAYLIST_MODIFY_PRIVATE("PLAYLIST_MODIFY_PRIVATE", 1, "playlist-modify-private"), 
    PLAYLIST_MODIFY_PUBLIC("PLAYLIST_MODIFY_PUBLIC", 2, "playlist-modify-public"), 
    PLAYLIST_READ_COLLABORATIVE("PLAYLIST_READ_COLLABORATIVE", 3, "playlist-read-collaborative"), 
    PLAYLIST_READ_PRIVATE("PLAYLIST_READ_PRIVATE", 4, "playlist-read-private"), 
    STREAMING("STREAMING", 5, "streaming"), 
    UGC_IMAGE_UPLOAD("UGC_IMAGE_UPLOAD", 6, "ugc-image-upload"), 
    USER_FOLLOW_MODIFY("USER_FOLLOW_MODIFY", 7, "user-follow-modify"), 
    USER_FOLLOW_READ("USER_FOLLOW_READ", 8, "user-follow-read"), 
    USER_LIBRARY_MODIFY("USER_LIBRARY_MODIFY", 9, "user-library-modify"), 
    USER_LIBRARY_READ("USER_LIBRARY_READ", 10, "user-library-read"), 
    USER_MODIFY_PLAYBACK_STATE("USER_MODIFY_PLAYBACK_STATE", 11, "user-modify-playback-state"), 
    USER_READ_CURRENTLY_PLAYING("USER_READ_CURRENTLY_PLAYING", 12, "user-read-currently-playing"), 
    USER_READ_EMAIL("USER_READ_EMAIL", 13, "user-read-email"), 
    USER_READ_PLAYBACK_POSITION("USER_READ_PLAYBACK_POSITION", 14, "user-read-playback-position"), 
    USER_READ_PLAYBACK_STATE("USER_READ_PLAYBACK_STATE", 15, "user-read-playback-state"), 
    USER_READ_PRIVATE("USER_READ_PRIVATE", 16, "user-read-private"), 
    USER_READ_RECENTLY_PLAYED("USER_READ_RECENTLY_PLAYED", 17, "user-read-recently-played"), 
    USER_TOP_READ("USER_TOP_READ", 18, "user-top-read");
    
    private static final Map<String, AuthorizationScope> map;
    public final String scope;
    
    static {
        map = new HashMap<String, AuthorizationScope>();
        AuthorizationScope[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final AuthorizationScope authorizationScope = values[i];
            AuthorizationScope.map.put(authorizationScope.scope, authorizationScope);
        }
    }
    
    private AuthorizationScope(final String name, final int ordinal, final String scope) {
        this.scope = scope;
    }
    
    public static AuthorizationScope keyOf(final String type) {
        return AuthorizationScope.map.get(type);
    }
    
    public String GetScope() {
        return this.scope;
    }
}
