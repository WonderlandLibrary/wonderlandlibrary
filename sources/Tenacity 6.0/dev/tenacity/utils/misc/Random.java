// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.misc;

import java.security.SecureRandom;

public class Random
{
    public static double dbRandom(final double min, final double max) {
        final SecureRandom rng = new SecureRandom();
        return rng.nextDouble() * (max - min) + min;
    }
    
    public static float flRandom(final float min, final float max) {
        final SecureRandom rng = new SecureRandom();
        return rng.nextFloat() * (max - min) + min;
    }
}
