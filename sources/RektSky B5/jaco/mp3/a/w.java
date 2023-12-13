/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.D;
import jaco.mp3.a.a;
import jaco.mp3.a.c_0;
import jaco.mp3.a.k;
import jaco.mp3.a.o;
import jaco.mp3.a.p;
import jaco.mp3.a.t;
import jaco.mp3.a.x;
import jaco.mp3.a.y;
import jaco.mp3.a.z;

class w
implements k {
    protected z a;
    protected D b;
    private y g;
    private y h;
    private c_0 i;
    private int j;
    protected int c;
    protected int d;
    protected p[] e;
    protected o f = new o();

    public final void a(z z2, D d2, y y2, y y3, c_0 c_02, int n2) {
        this.a = z2;
        this.b = d2;
        this.g = y2;
        this.h = y3;
        this.i = c_02;
        this.j = 0;
    }

    @Override
    public final void a() {
        this.d = this.b.j();
        this.e = new p[32];
        this.c = this.b.f();
        this.b();
        w w2 = this;
        int n2 = 0;
        while (n2 < w2.d) {
            w2.e[n2].a(w2.a, w2.b, w2.f);
            ++n2;
        }
        this.c();
        if (this.f != null || this.b.g()) {
            w2 = this;
            n2 = 0;
            while (n2 < w2.d) {
                w2.e[n2].a(w2.a, w2.b);
                ++n2;
            }
            w2 = this;
            n2 = 0;
            boolean bl = false;
            int n3 = w2.b.f();
            do {
                int n4 = 0;
                while (n4 < w2.d) {
                    n2 = w2.e[n4].a(w2.a) ? 1 : 0;
                    ++n4;
                }
                do {
                    n4 = 0;
                    while (n4 < w2.d) {
                        bl = w2.e[n4].a(w2.j, w2.g, w2.h);
                        ++n4;
                    }
                    w2.g.a(w2.i);
                    if (w2.j != 0 || n3 == 3) continue;
                    w2.h.a(w2.i);
                } while (!bl);
            } while (n2 == 0);
        }
    }

    protected void b() {
        if (this.c == 3) {
            int n2 = 0;
            while (n2 < this.d) {
                this.e[n2] = new t(n2);
                ++n2;
            }
            return;
        }
        if (this.c == 1) {
            int n3 = 0;
            while (n3 < this.b.k()) {
                this.e[n3] = new x(n3);
                ++n3;
            }
            while (n3 < this.d) {
                this.e[n3] = new a(n3);
                ++n3;
            }
            return;
        }
        int n4 = 0;
        while (n4 < this.d) {
            this.e[n4] = new x(n4);
            ++n4;
        }
    }

    protected void c() {
    }
}

