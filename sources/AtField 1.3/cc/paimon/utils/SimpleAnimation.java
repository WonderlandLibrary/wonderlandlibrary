/*
 * Decompiled with CFR 0.152.
 */
package cc.paimon.utils;

import cc.paimon.utils.AnimationUtil;

public class SimpleAnimation {
    private float value;
    private long lastMS;

    public float getValue() {
        return this.value;
    }

    public SimpleAnimation(float f) {
        this.value = f;
        this.lastMS = System.currentTimeMillis();
    }

    public void setAnimation(float f, double d) {
        long l = System.currentTimeMillis();
        long l2 = l - this.lastMS;
        this.lastMS = l;
        double d2 = 0.0;
        if (d > 28.0) {
            d = 28.0;
        }
        if (d != 0.0) {
            d2 = (double)(Math.abs(f - this.value) * 0.35f) / (10.0 / d);
        }
        this.value = AnimationUtil.calculateCompensation(f, this.value, d2, l2);
    }

    public void setValue(float f) {
        this.value = f;
    }
}

