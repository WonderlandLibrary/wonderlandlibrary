/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.ssl.util;

import io.netty.util.internal.ThreadLocalRandom;
import java.security.SecureRandom;
import java.util.Random;

final class ThreadLocalInsecureRandom
extends SecureRandom {
    private static final SecureRandom INSTANCE = new ThreadLocalInsecureRandom();
    private static final long serialVersionUID = -8209473337192526191L;

    @Override
    public String getAlgorithm() {
        return "insecure";
    }

    private ThreadLocalInsecureRandom() {
    }

    @Override
    public void setSeed(long l) {
    }

    @Override
    public boolean nextBoolean() {
        return ThreadLocalInsecureRandom.random().nextBoolean();
    }

    @Override
    public int nextInt(int n) {
        return ThreadLocalInsecureRandom.random().nextInt(n);
    }

    @Override
    public float nextFloat() {
        return ThreadLocalInsecureRandom.random().nextFloat();
    }

    @Override
    public byte[] generateSeed(int n) {
        byte[] byArray = new byte[n];
        ThreadLocalInsecureRandom.random().nextBytes(byArray);
        return byArray;
    }

    @Override
    public void setSeed(byte[] byArray) {
    }

    @Override
    public double nextDouble() {
        return ThreadLocalInsecureRandom.random().nextDouble();
    }

    @Override
    public void nextBytes(byte[] byArray) {
        ThreadLocalInsecureRandom.random().nextBytes(byArray);
    }

    @Override
    public long nextLong() {
        return ThreadLocalInsecureRandom.random().nextLong();
    }

    private static Random random() {
        return ThreadLocalRandom.current();
    }

    @Override
    public double nextGaussian() {
        return ThreadLocalInsecureRandom.random().nextGaussian();
    }

    static SecureRandom current() {
        return INSTANCE;
    }

    @Override
    public int nextInt() {
        return ThreadLocalInsecureRandom.random().nextInt();
    }
}

