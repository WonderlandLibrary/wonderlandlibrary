/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.ccbluex.liquidbounce.utils.render.animation;

import net.minecraft.client.Minecraft;

public class AnimationHelper {
    public static int deltaTime;
    public static float speedTarget;

    public static float animation(float current, float targetAnimation, float speed) {
        return AnimationHelper.animation(current, targetAnimation, speedTarget, speed);
    }

    public static float animation(float animation, float target, float poxyi, float speedTarget) {
        float da = (target - animation) / Math.max((float)Minecraft.func_175610_ah(), 5.0f) * 15.0f;
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
        boolean larger = target > current;
        boolean bl = larger;
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
        current = larger ? (current = current + factor) : (current = current - factor);
        return current;
    }

    public static float calculateCompensation(float target, float current, long delta, double speed) {
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (delta > 1000L) {
            delta = 16L;
        }
        double max = Math.max(speed * (double)delta / 16.0, 0.5);
        if ((double)diff > speed) {
            double xD = max;
            if ((current = (float)((double)current - xD)) < target) {
                current = target;
            }
        } else if ((double)diff < -speed) {
            double xD = max;
            if ((current = (float)((double)current + xD)) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    static {
        speedTarget = 0.125f;
    }
}

