// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.layer;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;

public class IntCache
{
    private static int intCacheSize;
    private static List freeSmallArrays;
    private static List inUseSmallArrays;
    private static List freeLargeArrays;
    private static List inUseLargeArrays;
    private static final String __OBFID = "CL_00000557";
    
    public static synchronized int[] getIntCache(final int p_76445_0_) {
        if (p_76445_0_ <= 256) {
            if (IntCache.freeSmallArrays.isEmpty()) {
                final int[] var1 = new int[256];
                IntCache.inUseSmallArrays.add(var1);
                return var1;
            }
            final int[] var1 = IntCache.freeSmallArrays.remove(IntCache.freeSmallArrays.size() - 1);
            IntCache.inUseSmallArrays.add(var1);
            return var1;
        }
        else {
            if (p_76445_0_ > IntCache.intCacheSize) {
                IntCache.intCacheSize = p_76445_0_;
                IntCache.freeLargeArrays.clear();
                IntCache.inUseLargeArrays.clear();
                final int[] var1 = new int[IntCache.intCacheSize];
                IntCache.inUseLargeArrays.add(var1);
                return var1;
            }
            if (IntCache.freeLargeArrays.isEmpty()) {
                final int[] var1 = new int[IntCache.intCacheSize];
                IntCache.inUseLargeArrays.add(var1);
                return var1;
            }
            final int[] var1 = IntCache.freeLargeArrays.remove(IntCache.freeLargeArrays.size() - 1);
            IntCache.inUseLargeArrays.add(var1);
            return var1;
        }
    }
    
    public static synchronized void resetIntCache() {
        if (!IntCache.freeLargeArrays.isEmpty()) {
            IntCache.freeLargeArrays.remove(IntCache.freeLargeArrays.size() - 1);
        }
        if (!IntCache.freeSmallArrays.isEmpty()) {
            IntCache.freeSmallArrays.remove(IntCache.freeSmallArrays.size() - 1);
        }
        IntCache.freeLargeArrays.addAll(IntCache.inUseLargeArrays);
        IntCache.freeSmallArrays.addAll(IntCache.inUseSmallArrays);
        IntCache.inUseLargeArrays.clear();
        IntCache.inUseSmallArrays.clear();
    }
    
    public static synchronized String getCacheSizes() {
        return "cache: " + IntCache.freeLargeArrays.size() + ", tcache: " + IntCache.freeSmallArrays.size() + ", allocated: " + IntCache.inUseLargeArrays.size() + ", tallocated: " + IntCache.inUseSmallArrays.size();
    }
    
    static {
        IntCache.intCacheSize = 256;
        IntCache.freeSmallArrays = Lists.newArrayList();
        IntCache.inUseSmallArrays = Lists.newArrayList();
        IntCache.freeLargeArrays = Lists.newArrayList();
        IntCache.inUseLargeArrays = Lists.newArrayList();
    }
}
