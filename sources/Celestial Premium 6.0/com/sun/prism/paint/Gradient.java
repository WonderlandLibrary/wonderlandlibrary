/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.Stop;
import java.util.List;

public abstract class Gradient
extends Paint {
    public static final int PAD = 0;
    public static final int REFLECT = 1;
    public static final int REPEAT = 2;
    private final int numStops;
    private final List<Stop> stops;
    private final BaseTransform gradientTransform;
    private final int spreadMethod;
    private long cacheOffset = -1L;

    protected Gradient(Paint.Type type, BaseTransform baseTransform, boolean bl, int n, List<Stop> list) {
        super(type, bl, false);
        this.gradientTransform = baseTransform != null ? baseTransform.copy() : BaseTransform.IDENTITY_TRANSFORM;
        this.spreadMethod = n;
        this.numStops = list.size();
        this.stops = list;
    }

    public int getSpreadMethod() {
        return this.spreadMethod;
    }

    public BaseTransform getGradientTransformNoClone() {
        return this.gradientTransform;
    }

    public int getNumStops() {
        return this.numStops;
    }

    public List<Stop> getStops() {
        return this.stops;
    }

    public void setGradientOffset(long l) {
        this.cacheOffset = l;
    }

    public long getGradientOffset() {
        return this.cacheOffset;
    }

    @Override
    public boolean isOpaque() {
        for (int i = 0; i < this.numStops; ++i) {
            if (this.stops.get(i).getColor().isOpaque()) continue;
            return false;
        }
        return true;
    }
}

