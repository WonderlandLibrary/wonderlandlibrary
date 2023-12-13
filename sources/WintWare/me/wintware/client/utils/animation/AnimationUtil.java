/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.animation;

import net.minecraft.client.Minecraft;

public class AnimationUtil {
    public static float speedTarget = 0.125f;

    public static float animation(float current, float targetAnimation, float speed) {
        return AnimationUtil.animation(current, targetAnimation, speedTarget, speed);
    }

    public static float animation(float animation, float target, float poxyi, float speedTarget) {
        float da = (target - animation) / Math.max((float)Minecraft.getDebugFPS(), 5.0f) * 15.0f;
        if (da > 0.0f) {
            da = Math.max(speedTarget, da);
            da = Math.min(target - animation, da);
        } else if (da < 0.0f) {
            da = Math.min(-speedTarget, da);
            da = Math.max(target - animation, da);
        }
        return animation + da;
    }

    public static double animate(double target, double current, double speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        current = larger ? (current += factor) : (current -= factor);
        return current;
    }
}

