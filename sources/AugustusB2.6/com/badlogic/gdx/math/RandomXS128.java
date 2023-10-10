// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.math;

import java.util.Random;

public final class RandomXS128 extends Random
{
    private long seed0;
    private long seed1;
    
    public RandomXS128() {
        this.setSeed(new Random().nextLong());
    }
    
    @Override
    public final long nextLong() {
        long s1 = this.seed0;
        final long s2 = this.seed1;
        this.seed0 = s2;
        final long n = s1;
        s1 = (n ^ n << 23);
        return (this.seed1 = (s1 ^ s2 ^ s1 >>> 17 ^ s2 >>> 26)) + s2;
    }
    
    @Override
    protected final int next(final int bits) {
        return (int)(this.nextLong() & (1L << bits) - 1L);
    }
    
    @Override
    public final int nextInt() {
        return (int)this.nextLong();
    }
    
    @Override
    public final int nextInt(final int n) {
        final long n2 = n;
        if (n2 <= 0L) {
            throw new IllegalArgumentException("n must be positive");
        }
        long n4;
        long n3;
        do {
            n3 = (n4 = this.nextLong() >>> 1) % n2;
        } while (n4 - n3 + (n2 - 1L) < 0L);
        return (int)n3;
    }
    
    @Override
    public final double nextDouble() {
        return (this.nextLong() >>> 11) * 1.1102230246251565E-16;
    }
    
    @Override
    public final float nextFloat() {
        return (float)((this.nextLong() >>> 40) * 5.9604644775390625E-8);
    }
    
    @Override
    public final boolean nextBoolean() {
        return (this.nextLong() & 0x1L) != 0x0L;
    }
    
    @Override
    public final void nextBytes(final byte[] bytes) {
        int i = bytes.length;
        while (i != 0) {
            int n = (i < 8) ? i : 8;
            long bits = this.nextLong();
            while (n-- != 0) {
                bytes[--i] = (byte)bits;
                bits >>= 8;
            }
        }
    }
    
    @Override
    public final void setSeed(final long seed) {
        final long murmurHash3;
        final long seed2 = murmurHash3 = murmurHash3((seed == 0L) ? Long.MIN_VALUE : seed);
        final long murmurHash4 = murmurHash3(murmurHash3);
        this.seed0 = murmurHash3;
        this.seed1 = murmurHash4;
    }
    
    private static final long murmurHash3(long x) {
        final long n = x;
        return x = ((x = (x = ((x = (x = (n ^ n >>> 33)) * -49064778989728563L) ^ x >>> 33)) * -4265267296055464877L) ^ x >>> 33);
    }
}
