/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a.a;

import jaco.mp3.a.b;

public abstract class a {
    private boolean a = false;
    private b b = null;

    public final synchronized void a(b b2) {
        if (!this.f()) {
            boolean bl;
            this.b = b2;
            v0.a = bl = true;
        }
    }

    private synchronized boolean f() {
        return this.a;
    }

    public final synchronized void a() {
        if (this.f()) {
            this.b();
            boolean bl = false;
            a a2 = this;
            this.a = bl;
            this.b = null;
        }
    }

    protected void b() {
    }

    public final void a(short[] sArray, int n2, int n3) {
        if (this.f()) {
            this.b(sArray, 0, n3);
        }
    }

    protected void b(short[] sArray, int n2, int n3) {
    }

    public final void c() {
        if (this.f()) {
            this.d();
        }
    }

    protected void d() {
    }

    protected final b e() {
        return this.b;
    }
}

