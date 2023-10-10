/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.E;
import jaco.mp3.a.i;
import jaco.mp3.a.k;
import jaco.mp3.a.v;
import jaco.mp3.a.w;

/*
 * Renamed from jaco.mp3.a.B
 */
final class b_0
extends w
implements k {
    @Override
    protected final void b() {
        if (this.c == 3) {
            int n2 = 0;
            while (n2 < this.d) {
                this.e[n2] = new i(n2);
                ++n2;
            }
            return;
        }
        if (this.c == 1) {
            int n3 = 0;
            while (n3 < this.b.k()) {
                this.e[n3] = new E(n3);
                ++n3;
            }
            while (n3 < this.d) {
                this.e[n3] = new v(n3);
                ++n3;
            }
            return;
        }
        int n4 = 0;
        while (n4 < this.d) {
            this.e[n4] = new E(n4);
            ++n4;
        }
    }

    @Override
    protected final void c() {
        int n2 = 0;
        while (n2 < this.d) {
            ((i)this.e[n2]).a(this.a, this.f);
            ++n2;
        }
    }
}

