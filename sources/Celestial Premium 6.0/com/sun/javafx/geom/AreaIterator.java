/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Curve;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;
import java.util.Vector;

class AreaIterator
implements PathIterator {
    private BaseTransform transform;
    private Vector curves;
    private int index;
    private Curve prevcurve;
    private Curve thiscurve;

    public AreaIterator(Vector vector, BaseTransform baseTransform) {
        this.curves = vector;
        this.transform = baseTransform;
        if (vector.size() >= 1) {
            this.thiscurve = (Curve)vector.get(0);
        }
    }

    @Override
    public int getWindingRule() {
        return 1;
    }

    @Override
    public boolean isDone() {
        return this.prevcurve == null && this.thiscurve == null;
    }

    @Override
    public void next() {
        if (this.prevcurve != null) {
            this.prevcurve = null;
        } else {
            this.prevcurve = this.thiscurve;
            ++this.index;
            if (this.index < this.curves.size()) {
                this.thiscurve = (Curve)this.curves.get(this.index);
                if (this.thiscurve.getOrder() != 0 && this.prevcurve.getX1() == this.thiscurve.getX0() && this.prevcurve.getY1() == this.thiscurve.getY0()) {
                    this.prevcurve = null;
                }
            } else {
                this.thiscurve = null;
            }
        }
    }

    @Override
    public int currentSegment(float[] arrf) {
        int n;
        int n2;
        if (this.prevcurve != null) {
            if (this.thiscurve == null || this.thiscurve.getOrder() == 0) {
                return 4;
            }
            arrf[0] = (float)this.thiscurve.getX0();
            arrf[1] = (float)this.thiscurve.getY0();
            n2 = 1;
            n = 1;
        } else {
            if (this.thiscurve == null) {
                throw new NoSuchElementException("area iterator out of bounds");
            }
            n2 = this.thiscurve.getSegment(arrf);
            n = this.thiscurve.getOrder();
            if (n == 0) {
                n = 1;
            }
        }
        if (this.transform != null) {
            this.transform.transform(arrf, 0, arrf, 0, n);
        }
        return n2;
    }
}

