/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

public class Animation {
    private final float speed;
    private float target;
    private float value;

    public Animation(float start, float speed) {
        this.speed = speed;
        this.value = this.target = start;
    }

    public void update(float target, double deltaTime) {
        this.updateTarget(target);
        this.updateAnimation(deltaTime);
    }

    public void updateAnimation(double deltaTime) {
        if (this.target == this.value) {
            return;
        }
        if (this.value < this.target) {
            this.value = (float)((double)this.value + (double)(this.target - this.value) * deltaTime * (double)this.speed);
            this.value = (float)((double)this.value + (double)this.speed * 0.75 * deltaTime);
            this.value = Math.min(this.target, this.value);
            return;
        }
        this.value = (float)((double)this.value - (double)(this.value - this.target) * deltaTime * (double)this.speed);
        this.value = (float)((double)this.value - (double)this.speed * 0.75 * deltaTime);
        this.value = Math.max(this.target, this.value);
    }

    public boolean done() {
        if (this.value != this.target) return false;
        return true;
    }

    public void updateTarget(float target) {
        this.target = target;
    }

    public float value() {
        return this.value;
    }

    public void forceValue(float value) {
        this.value = value;
    }
}

