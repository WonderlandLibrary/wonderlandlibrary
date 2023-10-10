/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.D;
import jaco.mp3.a.o;
import jaco.mp3.a.t;
import jaco.mp3.a.y;
import jaco.mp3.a.z;

final class a
extends t {
    private float j;

    public a(int n2) {
        super(n2);
    }

    @Override
    public final void a(z z2, D d2, o o2) {
        super.a(z2, d2, o2);
    }

    @Override
    public final void a(z z2, D d2) {
        if (this.d != 0) {
            this.e = m[z2.d(6)];
            this.j = m[z2.d(6)];
        }
    }

    @Override
    public final boolean a(z z2) {
        return super.a(z2);
    }

    @Override
    public final boolean a(int n2, y y2, y y3) {
        if (this.d != 0) {
            this.g = this.g * this.h + this.i;
            if (n2 == 0) {
                float f2 = this.g * this.e;
                float f3 = this.g * this.j;
                y2.a(f2, this.c);
                y3.a(f3, this.c);
            } else if (n2 == 1) {
                float f4 = this.g * this.e;
                y2.a(f4, this.c);
            } else {
                float f5 = this.g * this.j;
                y2.a(f5, this.c);
            }
        }
        return true;
    }
}

