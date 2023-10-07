// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

public class IntegerCache
{
    private static final int CACHE_SIZE = 4096;
    private static final Integer[] cache;
    
    private static Integer[] makeCache(final int size) {
        final Integer[] arr = new Integer[size];
        for (int i = 0; i < size; ++i) {
            arr[i] = new Integer(i);
        }
        return arr;
    }
    
    public static Integer valueOf(final int value) {
        return (value >= 0 && value < 4096) ? IntegerCache.cache[value] : new Integer(value);
    }
    
    static {
        cache = makeCache(4096);
    }
}
