/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.patcher.misc;

public class LightUtilHook {
    public static float diffuseLight(float x, float y, float z) {
        return Math.min(x * x * 0.6f + y * y * ((3.0f + y) / 4.0f) + z * z * 0.8f, 1.0f);
    }
}

