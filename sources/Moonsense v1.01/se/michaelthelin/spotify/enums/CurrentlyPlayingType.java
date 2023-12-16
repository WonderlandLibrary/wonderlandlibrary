// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.enums;

import java.util.HashMap;
import java.util.Map;

public enum CurrentlyPlayingType
{
    TRACK("track"), 
    EPISODE("episode"), 
    AD("ad"), 
    UNKNOWN("unknown");
    
    private static final Map<String, CurrentlyPlayingType> map;
    private final String type;
    
    private CurrentlyPlayingType(final String type) {
        this.type = type;
    }
    
    public static CurrentlyPlayingType keyOf(final String type) {
        return CurrentlyPlayingType.map.get(type);
    }
    
    public String getType() {
        return this.type;
    }
    
    static {
        map = new HashMap<String, CurrentlyPlayingType>();
        for (final CurrentlyPlayingType currentlyPlayingType : values()) {
            CurrentlyPlayingType.map.put(currentlyPlayingType.type, currentlyPlayingType);
        }
    }
}
