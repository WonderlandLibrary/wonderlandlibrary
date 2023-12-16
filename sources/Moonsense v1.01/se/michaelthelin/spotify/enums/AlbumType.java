// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.enums;

import java.util.HashMap;
import java.util.Map;

public enum AlbumType
{
    ALBUM("album"), 
    COMPILATION("compilation"), 
    SINGLE("single");
    
    private static final Map<String, AlbumType> map;
    public final String type;
    
    private AlbumType(final String type) {
        this.type = type;
    }
    
    public static AlbumType keyOf(final String type) {
        return AlbumType.map.get(type);
    }
    
    public String getType() {
        return this.type;
    }
    
    static {
        map = new HashMap<String, AlbumType>();
        for (final AlbumType albumType : values()) {
            AlbumType.map.put(albumType.type, albumType);
        }
    }
}
