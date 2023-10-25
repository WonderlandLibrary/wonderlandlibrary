/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.geom.PathIterator;

class PathIteratorHelper {
    private PathIterator itr;
    private float[] f = new float[6];

    public PathIteratorHelper(PathIterator pathIterator) {
        this.itr = pathIterator;
    }

    public int getWindingRule() {
        return this.itr.getWindingRule();
    }

    public boolean isDone() {
        return this.itr.isDone();
    }

    public void next() {
        this.itr.next();
    }

    public int currentSegment(Struct struct) {
        int n = this.itr.currentSegment(this.f);
        struct.f0 = this.f[0];
        struct.f1 = this.f[1];
        struct.f2 = this.f[2];
        struct.f3 = this.f[3];
        struct.f4 = this.f[4];
        struct.f5 = this.f[5];
        return n;
    }

    public static final class Struct {
        float f0;
        float f1;
        float f2;
        float f3;
        float f4;
        float f5;
    }
}

