/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.utils.render.AnimationUtil;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils2;

public final class Translate {
    private float x;
    private float y;
    private boolean first = false;

    public Translate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public final void interpolate(float targetX, float targetY, double smoothing) {
        if (this.first) {
            this.x = AnimationUtil.animate(targetX, this.x, smoothing);
            this.y = AnimationUtil.animate(targetY, this.y, smoothing);
        } else {
            this.x = targetX;
            this.y = targetY;
            this.first = true;
        }
    }

    public void translate(float targetX, float targetY) {
        this.x = AnimationUtils2.lstransition(this.x, targetX, 0.0);
        this.y = AnimationUtils2.lstransition(this.y, targetY, 0.0);
    }

    public void translate(float targetX, float targetY, double speed) {
        this.x = AnimationUtils2.lstransition(this.x, targetX, speed);
        this.y = AnimationUtils2.lstransition(this.y, targetY, speed);
    }

    public final void interpolate2(float targetX, float targetY, double smoothing) {
        this.x = targetX;
        this.y = AnimationUtil.animate(targetY, this.y, smoothing);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

