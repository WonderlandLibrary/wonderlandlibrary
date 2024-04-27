/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.utils.timer;

import net.dev.important.utils.misc.RandomUtils;

public final class TimeUtils {
    public static long randomDelay(int minDelay, int maxDelay) {
        return RandomUtils.nextInt(minDelay, maxDelay);
    }

    public static long randomClickDelay(int minCPS2, int maxCPS2) {
        return (long)(Math.random() * (double)(1000 / minCPS2 - 1000 / maxCPS2 + 1) + (double)(1000 / maxCPS2));
    }
}

