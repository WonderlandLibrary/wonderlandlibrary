/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.math;

import com.jhlabs.math.Function2D;

public abstract class CompoundFunction2D
implements Function2D {
    protected Function2D basis;

    public CompoundFunction2D(Function2D basis) {
        this.basis = basis;
    }

    public void setBasis(Function2D basis) {
        this.basis = basis;
    }

    public Function2D getBasis() {
        return this.basis;
    }
}

