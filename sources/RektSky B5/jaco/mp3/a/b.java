/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.D;
import jaco.mp3.a.a_0;
import jaco.mp3.a.b_0;
import jaco.mp3.a.c;
import jaco.mp3.a.c_0;
import jaco.mp3.a.f;
import jaco.mp3.a.g;
import jaco.mp3.a.u;
import jaco.mp3.a.w;
import jaco.mp3.a.y;
import jaco.mp3.a.z;

public final class b {
    private static final g a = new g();
    private c_0 b;
    private y c;
    private y d;
    private c e;
    private b_0 f;
    private w g;
    private int h;
    private int i;
    private f j = new f();
    private g k;
    private boolean l;

    public b() {
        this(null);
    }

    private b(g object) {
        object = a;
        this.k = object;
        object = this.k.a();
        if (object != null) {
            this.j.a((f)object);
        }
    }

    public final c_0 a(D object, z z2) {
        int n2;
        D d2;
        if (!b4.l) {
            d2 = object;
            b b2 = b4;
            float f2 = 32700.0f;
            n2 = d2.f();
            int n3 = n2 = n2 == 3 ? 1 : 2;
            if (b2.b == null) {
                b2.b = new a_0(d2.e(), n2);
            }
            b2.j.a();
            b2.c = new y(0, f2);
            if (n2 == 2) {
                b2.d = new y(1, f2);
            }
            b2.i = n2;
            b2.h = d2.e();
            b2.l = true;
        }
        int n4 = ((D)object).b();
        b4.b.c();
        n2 = n4;
        z z3 = z2;
        d2 = object;
        b b3 = b4;
        object = null;
        switch (n2) {
            case 3: {
                if (b3.e == null) {
                    b3.e = new c(z3, d2, b3.c, b3.d, b3.b, 0);
                }
                object = b3.e;
                break;
            }
            case 2: {
                if (b3.f == null) {
                    b3.f = new b_0();
                    b3.f.a(z3, d2, b3.c, b3.d, b3.b, 0);
                }
                object = b3.f;
                break;
            }
            case 1: {
                if (b3.g == null) {
                    b3.g = new w();
                    b3.g.a(z3, d2, b3.c, b3.d, b3.b, 0);
                }
                object = b3.g;
            }
        }
        if (object == null) {
            b b4 = null;
            n2 = 513;
            throw new u(513, null);
        }
        object.a();
        return b4.b;
    }

    public final int a() {
        return this.h;
    }

    public final int b() {
        return this.i;
    }
}

