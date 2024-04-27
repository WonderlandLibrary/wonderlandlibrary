/*
 * Decompiled with CFR 0.152.
 */
package liying.utils.animation;

import liying.utils.animation.AnimationUtils;

public class SmoothAnimationTimer {
    public float value = 0.0f;
    public float speed = 0.3f;
    public float target;

    public SmoothAnimationTimer(float f) {
        this.target = f;
    }

    public boolean update(boolean bl) {
        this.value = AnimationUtils.smoothAnimation(this.value, this.target, 60.0f, 0.3f);
        return this.value == this.target;
    }

    public SmoothAnimationTimer(float f, float f2) {
        this.target = f;
        this.speed = f2;
    }
}

