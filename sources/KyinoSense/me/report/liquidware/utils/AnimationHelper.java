/*
 * Decompiled with CFR 0.152.
 */
package me.report.liquidware.utils;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.value.BoolValue;

public class AnimationHelper {
    public float animationX;
    public int alpha;

    public int getAlpha() {
        return this.alpha;
    }

    public float getAnimationX() {
        return this.animationX;
    }

    public void resetAlpha() {
        this.alpha = 0;
    }

    public AnimationHelper() {
        this.alpha = 0;
    }

    public void updateAlpha(int speed) {
        if (this.alpha < 255) {
            this.alpha += speed;
        }
    }

    public AnimationHelper(BoolValue value) {
        this.animationX = (Boolean)value.get() != false ? 5.0f : -5.0f;
    }

    public AnimationHelper(Module module) {
        this.animationX = module.getState() ? 5.0f : -5.0f;
    }
}

