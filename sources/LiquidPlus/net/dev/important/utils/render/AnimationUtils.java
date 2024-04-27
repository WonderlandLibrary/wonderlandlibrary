/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.utils.render;

import net.dev.important.utils.render.RenderUtils;

public class AnimationUtils {
    public static float easeOut(float t, float d) {
        t = t / d - 1.0f;
        return t * t * t + 1.0f;
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
}

