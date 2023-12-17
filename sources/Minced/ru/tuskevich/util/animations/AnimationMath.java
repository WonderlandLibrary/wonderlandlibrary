// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.animations;

import net.minecraft.util.math.MathHelper;
import ru.tuskevich.util.math.MathUtility;
import net.minecraft.client.Minecraft;

public class AnimationMath
{
    public static double deltaTime() {
        return (Minecraft.getDebugFPS() > 0) ? (1.0 / Minecraft.getDebugFPS()) : 1.0;
    }
    
    public static float fast(final float end, final float start, final float multiple) {
        return (1.0f - MathUtility.clamp((float)(deltaTime() * multiple), 0.0f, 1.0f)) * end + MathUtility.clamp((float)(deltaTime() * multiple), 0.0f, 1.0f) * start;
    }
    
    public static float animation(final float animation, final float target, final float speedTarget) {
        float dif = (target - animation) / Math.max((float)Minecraft.getDebugFPS(), 5.0f) * 15.0f;
        if (dif > 0.0f) {
            dif = Math.max(speedTarget, dif);
            dif = Math.min(target - animation, dif);
        }
        else if (dif < 0.0f) {
            dif = Math.min(-speedTarget, dif);
            dif = Math.max(target - animation, dif);
        }
        return animation + dif;
    }
    
    public static double Interpolate(final double current, final double old, final double scale) {
        return old + (current - old) * scale;
    }
    
    public static float calculateCompensation(final float target, float current, float delta, final double speed) {
        final float diff = current - target;
        if (delta < 1.0f) {
            delta = 1.0f;
        }
        if (delta > 1000.0f) {
            delta = 16.0f;
        }
        final double dif = Math.max(speed * delta / 16.66666603088379, 0.5);
        if (diff > speed) {
            if ((current -= (float)dif) < target) {
                current = target;
            }
        }
        else if (diff < -speed) {
            if ((current += (float)dif) > target) {
                current = target;
            }
        }
        else {
            current = target;
        }
        return current;
    }
    
    public static float Move(final float from, final float to, final float minstep, final float maxstep, final float factor) {
        float f = (to - from) * MathHelper.clamp(factor, 0.0f, 1.0f);
        if (f < 0.0f) {
            f = MathHelper.clamp(f, -maxstep, -minstep);
        }
        else {
            f = MathHelper.clamp(f, minstep, maxstep);
        }
        if (Math.abs(f) > Math.abs(to - from)) {
            return to;
        }
        return from + f;
    }
    
    public static double animate(final double target, double current, double speed) {
        final boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        }
        else if (speed > 1.0) {
            speed = 1.0;
        }
        final double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            current += factor;
        }
        else {
            current -= factor;
        }
        return current;
    }
}
