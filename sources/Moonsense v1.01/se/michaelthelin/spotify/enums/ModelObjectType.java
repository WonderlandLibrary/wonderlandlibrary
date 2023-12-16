// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.enums;

import java.util.HashMap;
import java.util.Map;

public enum ModelObjectType
{
    ALBUM("album"), 
    ARTIST("artist"), 
    AUDIO_FEATURES("audio_features"), 
    EPISODE("episode"), 
    GENRE("genre"), 
    PLAYLIST("playlist"), 
    SHOW("show"), 
    TRACK("track"), 
    USER("user");
    
    private static final Map<String, ModelObjectType> map;
    public final String type;
    
    private ModelObjectType(final String type) {
        this.type = type;
    }
    
    public static ModelObjectType keyOf(final String type) {
        return ModelObjectType.map.get(type);
    }
    
    public String getType() {
        return this.type;
    }
    
    static {
        map = new HashMap<String, ModelObjectType>();
        for (final ModelObjectType modelObjectType : values()) {
            ModelObjectType.map.put(modelObjectType.type, modelObjectType);
        }
    }
}
