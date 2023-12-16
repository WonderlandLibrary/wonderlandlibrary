// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.enums;

import java.util.HashMap;
import java.util.Map;

public enum ReleaseDatePrecision
{
    DAY("day"), 
    MONTH("month"), 
    YEAR("year");
    
    private static final Map<String, ReleaseDatePrecision> map;
    public final String precision;
    
    private ReleaseDatePrecision(final String precision) {
        this.precision = precision;
    }
    
    public static ReleaseDatePrecision keyOf(final String precision) {
        return ReleaseDatePrecision.map.get(precision);
    }
    
    public String getPrecision() {
        return this.precision;
    }
    
    static {
        map = new HashMap<String, ReleaseDatePrecision>();
        for (final ReleaseDatePrecision releaseDatePrecision : values()) {
            ReleaseDatePrecision.map.put(releaseDatePrecision.precision, releaseDatePrecision);
        }
    }
}
