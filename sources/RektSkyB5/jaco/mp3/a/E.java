/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.D;
import jaco.mp3.a.i;
import jaco.mp3.a.o;
import jaco.mp3.a.y;
import jaco.mp3.a.z;

final class E
extends i {
    private int n;
    private int o;
    private float p;
    private float q;
    private float r;
    private int[] s = new int[1];
    private float[] t = new float[]{0.0f};
    private float[] u;
    private float[] v = new float[]{0.0f};
    private float[] w = new float[]{0.0f};

    public E(int n2) {
        super(n2);
        this.u = new float[3];
    }

    @Override
    public final void a(z z2, D d2, o o2) {
        int n2 = this.a(d2);
        this.b = z2.d(n2);
        this.n = z2.d(n2);
        if (o2 != null) {
            o2.a(this.b, n2);
            o2.a(this.n, n2);
        }
    }

    @Override
    public final void a(z z2, o o2) {
        if (this.b != 0) {
            this.c = z2.d(2);
            if (o2 != null) {
                o2.a(this.c, 2);
            }
        }
        if (this.n != 0) {
            this.o = z2.d(2);
            if (o2 != null) {
                o2.a(this.o, 2);
            }
        }
    }

    @Override
    public final void a(z z2, D d2) {
        super.a(z2, d2);
        if (this.n != 0) {
            switch (this.o) {
                case 0: {
                    this.p = m[z2.d(6)];
                    this.q = m[z2.d(6)];
                    this.r = m[z2.d(6)];
                    break;
                }
                case 1: {
                    this.p = this.q = m[z2.d(6)];
                    this.r = m[z2.d(6)];
                    break;
                }
                case 2: {
                    this.q = this.r = m[z2.d(6)];
                    this.p = this.r;
                    break;
                }
                case 3: {
                    this.p = m[z2.d(6)];
                    this.q = this.r = m[z2.d(6)];
                }
            }
            this.a(d2, this.n, 1, this.t, this.s, this.v, this.w);
        }
    }

    @Override
    public final boolean a(z z2) {
        boolean bl = super.a(z2);
        if (((E)object).n != 0) {
            if (((E)object).g[1] != null) {
                int n2 = z2.d(((E)object).s[0]);
                n2 += n2 << 1;
                float[] fArray = ((E)object).u;
                Object object = ((E)object).g[1];
                int n3 = n2;
                fArray[0] = (float)object[n2];
                fArray[1] = (float)object[++n3];
                fArray[2] = (float)object[++n3];
            } else {
                ((E)object).u[0] = (float)((double)((float)z2.d(((E)object).s[0]) * ((E)object).t[0]) - 1.0);
                ((E)object).u[1] = (float)((double)((float)z2.d(((E)object).s[0]) * ((E)object).t[0]) - 1.0);
                ((E)object).u[2] = (float)((double)((float)z2.d(((E)object).s[0]) * ((E)object).t[0]) - 1.0);
            }
        }
        return bl;
    }

    @Override
    public final boolean a(int n2, y y2, y y3) {
        boolean bl = super.a(n2, y2, y3);
        if (this.n != 0 && n2 != 1) {
            float f2 = this.u[this.i - 1];
            if (this.g[1] == null) {
                f2 = (f2 + this.w[0]) * this.v[0];
            }
            f2 = this.h <= 4 ? (f2 *= this.p) : (this.h <= 8 ? (f2 *= this.q) : (f2 *= this.r));
            if (n2 == 0) {
                y3.a(f2, this.a);
            } else {
                y2.a(f2, this.a);
            }
        }
        return bl;
    }
}

