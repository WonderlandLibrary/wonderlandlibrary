/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public class AnimationUtils2 {
    public static double delta;

    public static double easing(double now, double target, double speed) {
        return Math.abs(target - now) * speed;
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

    public static float lstransition(float now, float desired, double speed) {
        double dif = Math.abs(desired - now);
        float a = (float)Math.abs((double)(desired - (desired - Math.abs(desired - now))) / (100.0 - speed * 10.0));
        float x = now;
        if (dif > 0.0) {
            if (now < desired) {
                x += a * (float)RenderUtils.deltaTime;
            } else if (now > desired) {
                x -= a * (float)RenderUtils.deltaTime;
            }
        } else {
            x = desired;
        }
        if ((double)Math.abs(desired - x) < 0.01 && x != desired) {
            x = desired;
        }
        return x;
    }

    public static float getAnimationState(float animation, float finalState, float speed) {
        float add = (float)(delta * (double)(speed / 1000.0f));
        animation = animation < finalState ? (animation + add < finalState ? (animation += add) : finalState) : (animation - add > finalState ? (animation -= add) : finalState);
        return animation;
    }

    public static float easeOut(float t, float d) {
        t = t / d - 1.0f;
        return t * t * t + 1.0f;
    }
}

