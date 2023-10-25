/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

public class MouseFilter {
    private float targetValue;
    private float remainingValue;
    private float lastAmount;

    public float smooth(float p_76333_1_, float p_76333_2_) {
        this.targetValue += p_76333_1_;
        p_76333_1_ = (this.targetValue - this.remainingValue) * p_76333_2_;
        this.lastAmount += (p_76333_1_ - this.lastAmount) * 0.5f;
        if (p_76333_1_ > 0.0f && p_76333_1_ > this.lastAmount || p_76333_1_ < 0.0f && p_76333_1_ < this.lastAmount) {
            p_76333_1_ = this.lastAmount;
        }
        this.remainingValue += p_76333_1_;
        return p_76333_1_;
    }

    public void reset() {
        this.targetValue = 0.0f;
        this.remainingValue = 0.0f;
        this.lastAmount = 0.0f;
    }
}

