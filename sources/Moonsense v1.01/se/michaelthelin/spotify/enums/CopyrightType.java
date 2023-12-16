// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.enums;

import java.util.HashMap;
import java.util.Map;

public enum CopyrightType
{
    C("c"), 
    P("p");
    
    private static final Map<String, CopyrightType> map;
    public final String type;
    
    private CopyrightType(final String type) {
        this.type = type;
    }
    
    public static CopyrightType keyOf(final String type) {
        return CopyrightType.map.get(type);
    }
    
    public String getType() {
        return this.type;
    }
    
    static {
        map = new HashMap<String, CopyrightType>();
        for (final CopyrightType copyrightType : values()) {
            CopyrightType.map.put(copyrightType.type, copyrightType);
        }
    }
}
