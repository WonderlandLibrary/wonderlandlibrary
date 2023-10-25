/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

public class MouseFilter {
    private float field_76335_c;
    private float field_76336_a;
    private float field_76334_b;

    public void reset() {
        this.field_76336_a = 0.0f;
        this.field_76334_b = 0.0f;
        this.field_76335_c = 0.0f;
    }

    public float smooth(float f, float f2) {
        this.field_76336_a += f;
        f = (this.field_76336_a - this.field_76334_b) * f2;
        this.field_76335_c += (f - this.field_76335_c) * 0.5f;
        if (f > 0.0f && f > this.field_76335_c || f < 0.0f && f < this.field_76335_c) {
            f = this.field_76335_c;
        }
        this.field_76334_b += f;
        return f;
    }
}

