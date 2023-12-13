// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil
{
    public static int nextInt(final int origin, final int bound) {
        return (origin == bound) ? origin : ThreadLocalRandom.current().nextInt(origin, bound);
    }
    
    public static long nextLong(final long origin, final long bound) {
        return (origin == bound) ? origin : ThreadLocalRandom.current().nextLong(origin, bound);
    }
    
    public static float nextFloat(final double origin, final double bound) {
        return (origin == bound) ? ((float)origin) : ((float)ThreadLocalRandom.current().nextDouble((float)origin, (float)bound));
    }
    
    public static float nextFloat(final float origin, final float bound) {
        return (origin == bound) ? origin : ((float)ThreadLocalRandom.current().nextDouble(origin, bound));
    }
    
    public static double nextDouble(final double origin, final double bound) {
        return (origin == bound) ? origin : ThreadLocalRandom.current().nextDouble(origin, bound);
    }
    
    public static double nextSecureInt(final int origin, final int bound) {
        if (origin == bound) {
            return origin;
        }
        final SecureRandom secureRandom = new SecureRandom();
        final int difference = bound - origin;
        return origin + secureRandom.nextInt(difference);
    }
    
    public static double nextSecureDouble(final double origin, final double bound) {
        if (origin == bound) {
            return origin;
        }
        final SecureRandom secureRandom = new SecureRandom();
        final double difference = bound - origin;
        return origin + secureRandom.nextDouble() * difference;
    }
    
    public static float nextSecureFloat(final double origin, final double bound) {
        if (origin == bound) {
            return (float)origin;
        }
        final SecureRandom secureRandom = new SecureRandom();
        final float difference = (float)(bound - origin);
        return (float)(origin + secureRandom.nextFloat() * difference);
    }
    
    public static double randomSin() {
        return Math.sin(nextDouble(0.0, 6.283185307179586));
    }
}
