/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.util;

public class Pair<A, B> {
    private final A first;
    private final B second;

    protected Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<A, B>(a, b);
    }

    public A first() {
        return this.first;
    }

    public B second() {
        return this.second;
    }

    public String mkString(String separator) {
        return String.format("%s%s%s", this.first, separator, this.second);
    }
}

