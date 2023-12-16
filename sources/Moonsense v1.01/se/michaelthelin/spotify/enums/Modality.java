// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.enums;

import java.util.HashMap;
import java.util.Map;

public enum Modality
{
    MAJOR(1), 
    MINOR(0);
    
    private static final Map<Integer, Modality> map;
    public final int mode;
    
    private Modality(final int mode) {
        this.mode = mode;
    }
    
    public static Modality keyOf(final int mode) {
        return Modality.map.get(mode);
    }
    
    public int getType() {
        return this.mode;
    }
    
    static {
        map = new HashMap<Integer, Modality>();
        for (final Modality modality : values()) {
            Modality.map.put(modality.mode, modality);
        }
    }
}
