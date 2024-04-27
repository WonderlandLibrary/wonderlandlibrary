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

    public final String randomNumber(int n) {
        return this.random(n, "123456789");
    }

    public final String random(int n, String string) {
        String string2 = string;
        int n2 = n;
        RandomUtils randomUtils = this;
        boolean bl = false;
        char[] cArray = string2.toCharArray();
        return randomUtils.random(n2, cArray);
    }

    static {
        RandomUtils randomUtils;
        INSTANCE = randomUtils = new RandomUtils();
    }

    @JvmStatic
    public static final String randomString(int n) {
        return INSTANCE.random(n, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    @JvmStatic
    public static final int nextInt(int n, int n2) {
        return n2 - n <= 0 ? n : n + new Random().nextInt(n2 - n);
    }

    public final double nextDouble(double d, double d2) {
        return d == d2 || d2 - d <= 0.0 ? d : d + (d2 - d) * Math.random();
    }

    public final String random(int n, char[] cArray) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = n;
        for (int i = 0; i < n2; ++i) {
            stringBuilder.append(cArray[new Random().nextInt(cArray.length)]);
        }
        return stringBuilder.toString();
    }

    private RandomUtils() {
    }

    public final float nextFloat(float f, float f2) {
        return f == f2 || f2 - f <= 0.0f ? f : (float)((double)f + (double)(f2 - f) * Math.random());
    }
}

