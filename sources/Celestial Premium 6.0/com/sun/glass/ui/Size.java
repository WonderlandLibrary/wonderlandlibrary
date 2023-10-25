/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

public final class Size {
    public int width;
    public int height;

    public Size(int n, int n2) {
        this.width = n;
        this.height = n2;
    }

    public Size() {
        this(0, 0);
    }

    public String toString() {
        return "Size(" + this.width + ", " + this.height + ")";
    }
}

