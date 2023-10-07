package koks.api.utils;

import io.netty.util.internal.MathUtil;
import koks.api.Methods;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
public class RandomUtil implements Methods {

    private final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public double getRandomDouble(double min, double max) {
        return threadLocalRandom.nextDouble(min, max);
    }

    public int getRandomInteger(int min, int max) {
        return threadLocalRandom.nextInt(min, max);
    }

    public double getRandomGaussian(double average) {
        return threadLocalRandom.nextGaussian() * average;
    }

    public float getRandomFloat(float min, float max) {
        return (float) threadLocalRandom.nextDouble(min, max);
    }

    /*by AdvancedCode*/
    public double smooth (double max, double min, double time, boolean randomizing, double randomStrength) {
        min += 1;
        double radians = Math.toRadians((System.currentTimeMillis() * time % 360) - 180);
        double base = (Math.tanh(radians) + 1) / 2;
        double delta = max - min;
        delta *= base;
        double value = min + delta;
        if(randomizing)value *= ThreadLocalRandom.current().nextDouble(randomStrength,1);
        return Math.ceil(value *1000) / 1000;
    }

    private static RandomUtil randomUtil;

    public static RandomUtil getInstance() {
        if(randomUtil == null) {
            randomUtil = new RandomUtil();
        }
        return randomUtil;
    }
}
