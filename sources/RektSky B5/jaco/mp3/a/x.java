/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.D;
import jaco.mp3.a.o;
import jaco.mp3.a.t;
import jaco.mp3.a.y;
import jaco.mp3.a.z;

final class x
extends t {
    private int j;
    private float k;
    private int l;
    private float n;
    private float o;
    private float p;

    public x(int n2) {
        super(n2);
    }

    @Override
    public final void a(z z2, D d2, o o2) {
        this.d = z2.d(4);
        this.j = z2.d(4);
        if (o2 != null) {
            o2.a(this.d, 4);
            o2.a(this.j, 4);
        }
        if (this.d != 0) {
            this.f = this.d + 1;
            this.h = a[this.d];
            this.i = b[this.d];
        }
        if (this.j != 0) {
            this.l = this.j + 1;
            this.o = a[this.j];
            this.p = b[this.j];
        }
    }

    @Override
    public final void a(z z2, D d2) {
        if (this.d != 0) {
            this.e = m[z2.d(6)];
        }
        if (this.j != 0) {
            this.k = m[z2.d(6)];
        }
    }

    @Override
    public final boolean a(z z2) {
        boolean bl = super.a(z2);
        if (this.j != 0) {
            this.n = z2.d(this.l);
        }
        return bl;
    }

    @Override
    public final boolean a(int n2, y y2, y y3) {
        super.a(n2, y2, y3);
        if (this.j != 0 && n2 != 1) {
            float f2 = (this.n * this.o + this.p) * this.k;
            if (n2 == 0) {
                y3.a(f2, this.c);
            } else {
                y2.a(f2, this.c);
            }
        }
        return true;
    }
}

