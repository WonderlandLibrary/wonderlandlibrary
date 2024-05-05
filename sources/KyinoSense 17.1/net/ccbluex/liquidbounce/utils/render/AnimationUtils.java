/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

public class AnimationUtils {
    public static float easeOut(float t, float d) {
        t = t / d - 1.0f;
        return t * t * t + 1.0f;
    }
}

