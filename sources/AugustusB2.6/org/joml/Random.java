// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class Random
{
    private final Xorshiro128 rnd;
    private static long seedHalf;
    private static /* synthetic */ Class class$0;
    
    public static long newSeed() {
        Class class$2;
        Class class$0;
        if (Random.class$0 == null) {
            class$0 = (Random.class$0 = (class$2 = class$("org.joml.Random")));
        }
        else {
            class$2 = (class$0 = Random.class$0);
        }
        final Class clazz = class$0;
        final long newSeedHalf;
        synchronized (class$2) {
            final long oldSeedHalf = Random.seedHalf;
            newSeedHalf = (Random.seedHalf = oldSeedHalf * 3512401965023503517L);
        }
        return newSeedHalf;
    }
    
    private static /* synthetic */ Class class$(final String className) {
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException cause) {
            throw new NoClassDefFoundError().initCause(cause);
        }
    }
    
    public Random() {
        this(newSeed() ^ System.nanoTime());
    }
    
    public Random(final long seed) {
        this.rnd = new Xorshiro128(seed);
    }
    
    public float nextFloat() {
        return this.rnd.nextFloat();
    }
    
    public int nextInt(final int n) {
        return this.rnd.nextInt(n);
    }
    
    static {
        Random.seedHalf = 8020463840L;
    }
    
    private static final class Xorshiro128
    {
        private static final float INT_TO_FLOAT;
        private long _s0;
        private long _s1;
        private long state;
        
        Xorshiro128(final long seed) {
            this.state = seed;
            this._s0 = this.nextSplitMix64();
            this._s1 = this.nextSplitMix64();
        }
        
        private long nextSplitMix64() {
            final long state = this.state - 7046029254386353131L;
            this.state = state;
            long z = state;
            z = (z ^ z >>> 30) * -4658895280553007687L;
            z = (z ^ z >>> 27) * -7723592293110705685L;
            return z ^ z >>> 31;
        }
        
        final float nextFloat() {
            return (this.nextInt() >>> 8) * Xorshiro128.INT_TO_FLOAT;
        }
        
        private int nextInt() {
            final long s0 = this._s0;
            long s2 = this._s1;
            final long result = s0 + s2;
            s2 ^= s0;
            this.rotateLeft(s0, s2);
            return (int)(result & -1L);
        }
        
        private static long rotl_JDK4(final long x, final int k) {
            return x << k | x >>> 64 - k;
        }
        
        private static long rotl_JDK5(final long x, final int k) {
            return Long.rotateLeft(x, k);
        }
        
        private static long rotl(final long x, final int k) {
            if (Runtime.HAS_Long_rotateLeft) {
                return rotl_JDK5(x, k);
            }
            return rotl_JDK4(x, k);
        }
        
        private void rotateLeft(final long s0, final long s1) {
            this._s0 = (rotl(s0, 55) ^ s1 ^ s1 << 14);
            this._s1 = rotl(s1, 36);
        }
        
        final int nextInt(final int n) {
            long r = this.nextInt() >>> 1;
            r = r * n >> 31;
            return (int)r;
        }
        
        static {
            INT_TO_FLOAT = Float.intBitsToFloat(864026624);
        }
    }
}
