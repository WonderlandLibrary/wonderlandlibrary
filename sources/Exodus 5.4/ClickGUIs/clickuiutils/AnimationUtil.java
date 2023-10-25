/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.clickuiutils;

public class AnimationUtil {
    private static float defaultSpeed = 0.125f;

    public static float moveUD(float f, float f2, float f3, float f4) {
        float f5 = (f2 - f) * f3;
        if (f5 > 0.0f) {
            f5 = Math.max(f4, f5);
            f5 = Math.min(f2 - f, f5);
        } else if (f5 < 0.0f) {
            f5 = Math.min(-f4, f5);
            f5 = Math.max(f2 - f, f5);
        }
        return f + f5;
    }

    public static float moveUD(float f, float f2, float f3) {
        return AnimationUtil.moveUD(f, f2, defaultSpeed, f3);
    }
}

