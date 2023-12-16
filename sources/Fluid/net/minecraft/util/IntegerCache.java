// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

public class IntegerCache
{
    private static final Integer[] CACHE;
    
    static {
        CACHE = new Integer[65535];
        for (int i = 0, j = IntegerCache.CACHE.length; i < j; ++i) {
            IntegerCache.CACHE[i] = i;
        }
    }
    
    public static Integer getInteger(final int value) {
        return (value >= 0 && value < IntegerCache.CACHE.length) ? IntegerCache.CACHE[value] : new Integer(value);
    }
}
