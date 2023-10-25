/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

public class Tuple<A, B> {
    private B b;
    private A a;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getFirst() {
        return this.a;
    }

    public B getSecond() {
        return this.b;
    }
}

