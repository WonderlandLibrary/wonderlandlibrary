/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.math;

import com.jhlabs.math.Function1D;

public class MathFunction1D
implements Function1D {
    public static final int SIN = 1;
    public static final int COS = 2;
    public static final int TAN = 3;
    public static final int SQRT = 4;
    public static final int ASIN = -1;
    public static final int ACOS = -2;
    public static final int ATAN = -3;
    public static final int SQR = -4;
    private int operation;

    public MathFunction1D(int operation) {
        this.operation = operation;
    }

    @Override
    public float evaluate(float v) {
        switch (this.operation) {
            case 1: {
                return (float)Math.sin(v);
            }
            case 2: {
                return (float)Math.cos(v);
            }
            case 3: {
                return (float)Math.tan(v);
            }
            case 4: {
                return (float)Math.sqrt(v);
            }
            case -1: {
                return (float)Math.asin(v);
            }
            case -2: {
                return (float)Math.acos(v);
            }
            case -3: {
                return (float)Math.atan(v);
            }
            case -4: {
                return v * v;
            }
        }
        return v;
    }
}

