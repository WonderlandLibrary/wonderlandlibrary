/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.utils.render.AnimationUtils2;

public class SmoothAnimationTimer {
    public float target;
    public float speed = 0.3f;
    private float value = 0.0f;

    public SmoothAnimationTimer(float target) {
        this.target = target;
    }

    public SmoothAnimationTimer(float target, float speed) {
        this.target = target;
        this.speed = speed;
    }

    public boolean update(boolean increment) {
        this.value = AnimationUtils2.getAnimationState(this.value, increment ? this.target : 0.0f, Math.max(10.0f, Math.abs(this.value - (increment ? this.target : 0.0f)) * 40.0f) * this.speed);
        return this.value == this.target;
    }

    public void setValue(float f) {
        this.value = f;
    }

    public void setTarget(float scrollY) {
        this.target = scrollY;
    }

    public int getValue() {
        return (int)this.value;
    }

    public float getTarget() {
        return this.target;
    }
}

