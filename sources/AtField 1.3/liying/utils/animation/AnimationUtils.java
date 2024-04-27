/*
 * Decompiled with CFR 0.152.
 */
package liying.utils.animation;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public class AnimationUtils {
    public static float rotateDirection = 0.0f;
    public static double delta;

    public static float lstransition(float f, float f2, double d) {
        double d2 = Math.abs(f2 - f);
        float f3 = (float)Math.abs((double)(f2 - (f2 - Math.abs(f2 - f))) / (100.0 - d * 10.0));
        float f4 = f;
        if (d2 > 0.0) {
            if (f < f2) {
                f4 += f3 * (float)RenderUtils.deltaTime;
            } else if (f > f2) {
                f4 -= f3 * (float)RenderUtils.deltaTime;
            }
        } else {
            f4 = f2;
        }
        if ((double)Math.abs(f2 - f4) < 0.01 && f4 != f2) {
            f4 = f2;
        }
        return f4;
    }

    public static float smoothAnimation(float f, float f2, float f3, float f4) {
        return AnimationUtils.getAnimationState(f, f2, Math.max(10.0f, Math.abs(f - f2) * f3) * f4);
    }

    public static float getAnimationState(float f, float f2, float f3) {
        float f4 = (float)(delta * (double)(f3 / 1000.0f));
        f = f < f2 ? (f + f4 < f2 ? (f += f4) : f2) : (f - f4 > f2 ? (f -= f4) : f2);
        return f;
    }
}

