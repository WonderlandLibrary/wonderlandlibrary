/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.QuadCurve2D;
import java.util.NoSuchElementException;

public class FlatteningPathIterator
implements PathIterator {
    static final int GROW_SIZE = 24;
    PathIterator src;
    float squareflat;
    int limit;
    volatile float[] hold = new float[14];
    float curx;
    float cury;
    float movx;
    float movy;
    int holdType;
    int holdEnd;
    int holdIndex;
    int[] levels;
    int levelIndex;
    boolean done;

    public FlatteningPathIterator(PathIterator pathIterator, float f) {
        this(pathIterator, f, 10);
    }

    public FlatteningPathIterator(PathIterator pathIterator, float f, int n) {
        if (f < 0.0f) {
            throw new IllegalArgumentException("flatness must be >= 0");
        }
        if (n < 0) {
            throw new IllegalArgumentException("limit must be >= 0");
        }
        this.src = pathIterator;
        this.squareflat = f * f;
        this.limit = n;
        this.levels = new int[n + 1];
        this.next(false);
    }

    public float getFlatness() {
        return (float)Math.sqrt(this.squareflat);
    }

    public int getRecursionLimit() {
        return this.limit;
    }

    @Override
    public int getWindingRule() {
        return this.src.getWindingRule();
    }

    @Override
    public boolean isDone() {
        return this.done;
    }

    void ensureHoldCapacity(int n) {
        if (this.holdIndex - n < 0) {
            int n2 = this.hold.length - this.holdIndex;
            int n3 = this.hold.length + 24;
            float[] arrf = new float[n3];
            System.arraycopy(this.hold, this.holdIndex, arrf, this.holdIndex + 24, n2);
            this.hold = arrf;
            this.holdIndex += 24;
            this.holdEnd += 24;
        }
    }

    @Override
    public void next() {
        this.next(true);
    }

    private void next(boolean bl) {
        if (this.holdIndex >= this.holdEnd) {
            if (bl) {
                this.src.next();
            }
            if (this.src.isDone()) {
                this.done = true;
                return;
            }
            this.holdType = this.src.currentSegment(this.hold);
            this.levelIndex = 0;
            this.levels[0] = 0;
        }
        switch (this.holdType) {
            case 0: 
            case 1: {
                this.curx = this.hold[0];
                this.cury = this.hold[1];
                if (this.holdType == 0) {
                    this.movx = this.curx;
                    this.movy = this.cury;
                }
                this.holdIndex = 0;
                this.holdEnd = 0;
                break;
            }
            case 4: {
                this.curx = this.movx;
                this.cury = this.movy;
                this.holdIndex = 0;
                this.holdEnd = 0;
                break;
            }
            case 2: {
                if (this.holdIndex >= this.holdEnd) {
                    this.holdIndex = this.hold.length - 6;
                    this.holdEnd = this.hold.length - 2;
                    this.hold[this.holdIndex + 0] = this.curx;
                    this.hold[this.holdIndex + 1] = this.cury;
                    this.hold[this.holdIndex + 2] = this.hold[0];
                    this.hold[this.holdIndex + 3] = this.hold[1];
                    this.hold[this.holdIndex + 4] = this.curx = this.hold[2];
                    this.hold[this.holdIndex + 5] = this.cury = this.hold[3];
                }
                int n = this.levels[this.levelIndex];
                while (n < this.limit && !(QuadCurve2D.getFlatnessSq(this.hold, this.holdIndex) < this.squareflat)) {
                    this.ensureHoldCapacity(4);
                    QuadCurve2D.subdivide(this.hold, this.holdIndex, this.hold, this.holdIndex - 4, this.hold, this.holdIndex);
                    this.holdIndex -= 4;
                    this.levels[this.levelIndex] = ++n;
                    ++this.levelIndex;
                    this.levels[this.levelIndex] = n;
                }
                this.holdIndex += 4;
                --this.levelIndex;
                break;
            }
            case 3: {
                if (this.holdIndex >= this.holdEnd) {
                    this.holdIndex = this.hold.length - 8;
                    this.holdEnd = this.hold.length - 2;
                    this.hold[this.holdIndex + 0] = this.curx;
                    this.hold[this.holdIndex + 1] = this.cury;
                    this.hold[this.holdIndex + 2] = this.hold[0];
                    this.hold[this.holdIndex + 3] = this.hold[1];
                    this.hold[this.holdIndex + 4] = this.hold[2];
                    this.hold[this.holdIndex + 5] = this.hold[3];
                    this.hold[this.holdIndex + 6] = this.curx = this.hold[4];
                    this.hold[this.holdIndex + 7] = this.cury = this.hold[5];
                }
                int n = this.levels[this.levelIndex];
                while (n < this.limit && !(CubicCurve2D.getFlatnessSq(this.hold, this.holdIndex) < this.squareflat)) {
                    this.ensureHoldCapacity(6);
                    CubicCurve2D.subdivide(this.hold, this.holdIndex, this.hold, this.holdIndex - 6, this.hold, this.holdIndex);
                    this.holdIndex -= 6;
                    this.levels[this.levelIndex] = ++n;
                    ++this.levelIndex;
                    this.levels[this.levelIndex] = n;
                }
                this.holdIndex += 6;
                --this.levelIndex;
            }
        }
    }

    @Override
    public int currentSegment(float[] arrf) {
        if (this.isDone()) {
            throw new NoSuchElementException("flattening iterator out of bounds");
        }
        int n = this.holdType;
        if (n != 4) {
            arrf[0] = this.hold[this.holdIndex + 0];
            arrf[1] = this.hold[this.holdIndex + 1];
            if (n != 0) {
                n = 1;
            }
        }
        return n;
    }
}

