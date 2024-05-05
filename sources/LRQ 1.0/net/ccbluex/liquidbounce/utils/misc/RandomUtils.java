/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 */
package net.ccbluex.liquidbounce.utils.misc;

import java.util.Random;
import kotlin.jvm.JvmStatic;

public final class RandomUtils {
    public static final RandomUtils INSTANCE;

    @JvmStatic
    public static final int nextInt(int startInclusive, int endExclusive) {
        return endExclusive - startInclusive <= 0 ? startInclusive : startInclusive + new Random().nextInt(endExclusive - startInclusive);
    }

    public final double nextDouble(double startInclusive, double endInclusive) {
        return startInclusive == endInclusive || endInclusive - startInclusive <= 0.0 ? startInclusive : startInclusive + (endInclusive - startInclusive) * Math.random();
    }

    public final float nextFloat(float startInclusive, float endInclusive) {
        return startInclusive == endInclusive || endInclusive - startInclusive <= 0.0f ? startInclusive : (float)((double)startInclusive + (double)(endInclusive - startInclusive) * Math.random());
    }

    public final String randomNumber(int length) {
        return this.random(length, "123456789");
    }

    @JvmStatic
    public static final String randomString(int length) {
        return INSTANCE.random(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    public final String random(int length, String chars) {
        String string = chars;
        int n = length;
        RandomUtils randomUtils = this;
        boolean bl = false;
        char[] cArray = string.toCharArray();
        return randomUtils.random(n, cArray);
    }

    /*
     * WARNING - void declaration
     */
    public final String random(int length, char[] chars) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        int n2 = length;
        while (n < n2) {
            void i;
            stringBuilder.append(chars[new Random().nextInt(chars.length)]);
            ++i;
        }
        return stringBuilder.toString();
    }

    private RandomUtils() {
    }

    static {
        RandomUtils randomUtils;
        INSTANCE = randomUtils = new RandomUtils();
    }
}

