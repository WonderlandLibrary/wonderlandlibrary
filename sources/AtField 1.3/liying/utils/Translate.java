/*
 * Decompiled with CFR 0.152.
 */
package liying.utils;

import liying.utils.animation.AnimationUtil;
import liying.utils.animation.AnimationUtils;

public final class Translate {
    private float x;
    private float y;
    private boolean first = false;

    public final void interpolate(float f, float f2, double d) {
        if (this.first) {
            this.x = AnimationUtil.animate(f, this.x, d);
            this.y = AnimationUtil.animate(f2, this.y, d);
        } else {
            this.x = f;
            this.y = f2;
            this.first = true;
        }
    }

    public void translate(float f, float f2, double d) {
        this.x = AnimationUtils.lstransition(this.x, f, d);
        this.y = AnimationUtils.lstransition(this.y, f2, d);
    }

    public Translate(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void setY(float f) {
        this.y = f;
    }

    public void setX(float f) {
        this.x = f;
    }

    public void translate(float f, float f2) {
        this.x = AnimationUtils.lstransition(this.x, f, 0.0);
        this.y = AnimationUtils.lstransition(this.y, f2, 0.0);
    }

    public final void interpolate2(float f, float f2, double d) {
        this.x = f;
        this.y = AnimationUtil.animate(f2, this.y, d);
    }

    public float getY() {
        return this.y;
    }

    public float getX() {
        return this.x;
    }
}

