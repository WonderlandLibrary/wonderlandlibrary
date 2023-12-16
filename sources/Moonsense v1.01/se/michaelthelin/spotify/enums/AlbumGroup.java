// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.enums;

import java.util.HashMap;
import java.util.Map;

public enum AlbumGroup
{
    ALBUM("album"), 
    APPEARS_ON("appears_on"), 
    COMPILATION("compilation"), 
    SINGLE("single");
    
    private static final Map<String, AlbumGroup> map;
    public final String group;
    
    private AlbumGroup(final String group) {
        this.group = group;
    }
    
    public static AlbumGroup keyOf(final String type) {
        return AlbumGroup.map.get(type);
    }
    
    public String getGroup() {
        return this.group;
    }
    
    static {
        map = new HashMap<String, AlbumGroup>();
        for (final AlbumGroup albumGroup : values()) {
            AlbumGroup.map.put(albumGroup.group, albumGroup);
        }
    }
}
