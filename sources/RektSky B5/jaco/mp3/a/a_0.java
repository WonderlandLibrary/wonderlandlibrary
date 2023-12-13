/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.c_0;

/*
 * Renamed from jaco.mp3.a.A
 */
public final class a_0
extends c_0 {
    private short[] a = new short[2304];
    private int[] b = new int[2];
    private int c;

    public a_0(int n2, int n3) {
        this.c = n3;
        n2 = 0;
        while (n2 < n3) {
            this.b[n2] = (short)n2;
            ++n2;
        }
    }

    public final short[] a() {
        return this.a;
    }

    public final int b() {
        return this.b[0];
    }

    @Override
    public final void a(int n2, short s2) {
        this.a[this.b[n2]] = s2;
        int n3 = n2;
        this.b[n3] = this.b[n3] + this.c;
    }

    @Override
    public final void a(int n2, float[] fArray) {
        int n3 = this.b[n2];
        int n4 = 0;
        while (n4 < 32) {
            short s2;
            float f2;
            float f3 = fArray[n4++];
            f3 = f2 > 32767.0f ? 32767.0f : (f3 < -32767.0f ? -32767.0f : f3);
            this.a[n3] = s2 = (short)f3;
            n3 += this.c;
        }
        this.b[n2] = n3;
    }

    @Override
    public final void c() {
        int n2 = 0;
        while (n2 < this.c) {
            this.b[n2] = (short)n2;
            ++n2;
        }
    }
}

