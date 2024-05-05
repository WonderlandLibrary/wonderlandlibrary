// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.render;

public class AnimationHelper
{
    public static float delta;
    
    public static float customAnim(float current, final float add, final float min, final float max) {
        if ((current += add) > max) {
            current = max;
        }
        if (current < min) {
            current = min;
        }
        return current;
    }
    
    public static float Move(final float from, final float to, final float minstep, final float maxstep, final float factor) {
        float f = (to - from) * rk.a(factor, 0.0f, 1.0f);
        if (f < 0.0f) {
            f = rk.a(f, -maxstep, -minstep);
        }
        else {
            f = rk.a(f, minstep, maxstep);
        }
        if (Math.abs(f) > Math.abs(to - from)) {
            return to;
        }
        return from + f;
    }
    
    public static double easeInOutQuart(final double x) {
        return (x < 0.5) ? (8.0 * x * x * x * x) : (1.0 - Math.pow(-2.0 * x + 2.0, 4.0) / 2.0);
    }
    
    public static float animation2(final float animation, final float target, final float speed) {
        float dif = (target - animation) / Math.max((float)bib.af(), 5.0f) * 15.0f;
        if (dif > 0.0f) {
            dif = Math.max(speed, dif);
            dif = Math.min(target - animation, dif);
        }
        else if (dif < 0.0f) {
            dif = Math.min(-speed, dif);
            dif = Math.max(target - animation, dif);
        }
        return animation + dif;
    }
    
    public static float animation(final float animation, final float target, final float speedTarget) {
        float dif = (target - animation) / Math.max((float)bib.af(), 5.0f) * speedTarget;
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
    
    public static double animation(final double animation, final double target, final double speedTarget) {
        double dif = (target - animation) / Math.max(bib.af(), 5) * speedTarget;
        if (dif > 0.0) {
            dif = Math.max(speedTarget, dif);
            dif = Math.min(target - animation, dif);
        }
        else if (dif < 0.0) {
            dif = Math.min(-speedTarget, dif);
            dif = Math.max(target - animation, dif);
        }
        return animation + dif;
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
    
    public static double getAnimationState(double animation, final double finalState, final double speed) {
        final float add = (float)(AnimationHelper.delta * speed);
        animation = ((animation < finalState) ? ((animation + add < finalState) ? (animation += add) : finalState) : ((animation - add > finalState) ? (animation -= add) : finalState));
        return animation;
    }
}
