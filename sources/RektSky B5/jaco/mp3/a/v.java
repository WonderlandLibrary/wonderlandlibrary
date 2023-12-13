/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.D;
import jaco.mp3.a.i;
import jaco.mp3.a.o;
import jaco.mp3.a.y;
import jaco.mp3.a.z;

final class v
extends i {
    private int n;
    private float o;
    private float p;
    private float q;

    public v(int n2) {
        super(n2);
    }

    @Override
    public final void a(z z2, D d2, o o2) {
        super.a(z2, d2, o2);
    }

    @Override
    public final void a(z z2, o o2) {
        if (this.b != 0) {
            this.c = z2.d(2);
            this.n = z2.d(2);
            if (o2 != null) {
                o2.a(this.c, 2);
                o2.a(this.n, 2);
            }
        }
    }

    @Override
    public final void a(z z2, D d2) {
        if (this.b != 0) {
            super.a(z2, d2);
            switch (this.n) {
                case 0: {
                    this.o = m[z2.d(6)];
                    this.p = m[z2.d(6)];
                    this.q = m[z2.d(6)];
                    return;
                }
                case 1: {
                    this.o = this.p = m[z2.d(6)];
                    this.q = m[z2.d(6)];
                    return;
                }
                case 2: {
                    this.p = this.q = m[z2.d(6)];
                    this.o = this.q;
                    return;
                }
                case 3: {
                    this.o = m[z2.d(6)];
                    this.p = this.q = m[z2.d(6)];
                }
            }
        }
    }

    @Override
    public final boolean a(z z2) {
        return super.a(z2);
    }

    @Override
    public final boolean a(int n2, y y2, y y3) {
        if (this.b != 0) {
            float f2 = this.j[this.i];
            if (this.g[0] == null) {
                f2 = (f2 + this.l[0]) * this.k[0];
            }
            if (n2 == 0) {
                float f3 = f2;
                if (this.h <= 4) {
                    f2 *= this.d;
                    f3 *= this.o;
                } else if (this.h <= 8) {
                    f2 *= this.e;
                    f3 *= this.p;
                } else {
                    f2 *= this.f;
                    f3 *= this.q;
                }
                y2.a(f2, this.a);
                y3.a(f3, this.a);
            } else if (n2 == 1) {
                f2 = this.h <= 4 ? (f2 *= this.d) : (this.h <= 8 ? (f2 *= this.e) : (f2 *= this.f));
                y2.a(f2, this.a);
            } else {
                f2 = this.h <= 4 ? (f2 *= this.o) : (this.h <= 8 ? (f2 *= this.p) : (f2 *= this.q));
                y2.a(f2, this.a);
            }
        }
        return ++this.i == 3;
    }
}

