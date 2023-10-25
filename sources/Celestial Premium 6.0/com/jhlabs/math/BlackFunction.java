/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.math;

import com.jhlabs.math.BinaryFunction;

public class BlackFunction
implements BinaryFunction {
    @Override
    public boolean isBlack(int rgb) {
        return rgb == -16777216;
    }
}

