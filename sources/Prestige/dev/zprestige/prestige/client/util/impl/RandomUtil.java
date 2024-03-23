package dev.zprestige.prestige.client.util.impl;

import java.util.Random;

public class RandomUtil {
    public Random random = new Random();
    public static RandomUtil INSTANCE = new RandomUtil();

    public Random getRandom() {
        return random;
    }

    public int randomInRange(int n, int n2) {
        return n + random.nextInt(n2 - n + 1);
    }

    public float randomInRange(float f, float f2) {
        return MathUtil.findMiddleValue(f + random.nextFloat() * f2, f, f2);
    }
}
