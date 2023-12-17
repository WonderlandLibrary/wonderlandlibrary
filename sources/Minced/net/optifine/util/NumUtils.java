// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

public class NumUtils
{
    public static float limit(final float val, final float min, final float max) {
        if (val < min) {
            return min;
        }
        return (val > max) ? max : val;
    }
    
    public static int mod(final int x, final int y) {
        int i = x % y;
        if (i < 0) {
            i += y;
        }
        return i;
    }
}
