/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

public class FrameTimer {
    private final long[] field_181752_a = new long[240];
    private int field_181753_b;
    private int field_181755_d;
    private int field_181754_c;

    public int func_181751_b(int n) {
        return n % 240;
    }

    public int func_181748_a(long l, int n) {
        double d = (double)l / 1.6666666E7;
        return (int)(d * (double)n);
    }

    public int func_181749_a() {
        return this.field_181753_b;
    }

    public int func_181750_b() {
        return this.field_181755_d;
    }

    public long[] func_181746_c() {
        return this.field_181752_a;
    }

    public void func_181747_a(long l) {
        this.field_181752_a[this.field_181755_d] = l;
        ++this.field_181755_d;
        if (this.field_181755_d == 240) {
            this.field_181755_d = 0;
        }
        if (this.field_181754_c < 240) {
            this.field_181753_b = 0;
            ++this.field_181754_c;
        } else {
            this.field_181753_b = this.func_181751_b(this.field_181755_d + 1);
        }
    }
}

