// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.world;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;

public class RandomUtil
{
    private static final Random RANDOM;
    private static final ThreadLocalRandom r;
    
    public static double getRandomInRange(final double min, final double max) {
        return min + RandomUtil.RANDOM.nextDouble() * (max - min);
    }
    
    public static final double randomDouble(final double min, final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    
    public static double getDouble(final double min, final double max) {
        return RandomUtil.r.nextDouble(min, max);
    }
    
    public static double getRandom(final double min, final double max) {
        final Random random = new Random();
        final double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        double shifted;
        if ((shifted = scaled + min) > max) {
            shifted = max;
        }
        return shifted;
    }
    
    static {
        RANDOM = new Random();
        r = ThreadLocalRandom.current();
    }
}
