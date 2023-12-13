/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.animation;

import me.wintware.client.utils.animation.AnimationUtil;

public final class Translate {
    private double x;
    private double y;

    public Translate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public final void interpolate(double targetX, double targetY, double smoothing) {
        this.x = AnimationUtil.animate(targetX, this.x, smoothing);
        this.y = AnimationUtil.animate(targetY, this.y, smoothing);
    }

    public void animate(double newX, double newY) {
        this.x = AnimationUtil.animate(this.x, newX, 1.0);
        this.y = AnimationUtil.animate(this.y, newY, 1.0);
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }
}

