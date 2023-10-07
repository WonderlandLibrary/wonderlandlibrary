/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.Metadata;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u000b\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0014\u0010\t\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0014\u0010\u000b\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0014\u0010\r\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006\u00a8\u0006\u000f"}, d2={"Lkotlin/jvm/internal/DoubleCompanionObject;", "", "()V", "MAX_VALUE", "", "getMAX_VALUE", "()D", "MIN_VALUE", "getMIN_VALUE", "NEGATIVE_INFINITY", "getNEGATIVE_INFINITY", "NaN", "getNaN", "POSITIVE_INFINITY", "getPOSITIVE_INFINITY", "kotlin-stdlib"})
public final class DoubleCompanionObject {
    private static final double MIN_VALUE = Double.MIN_VALUE;
    private static final double MAX_VALUE = Double.MAX_VALUE;
    private static final double POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    private static final double NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
    private static final double NaN = Double.NaN;
    public static final DoubleCompanionObject INSTANCE;

    public final double getMIN_VALUE() {
        return MIN_VALUE;
    }

    public final double getMAX_VALUE() {
        return MAX_VALUE;
    }

    public final double getPOSITIVE_INFINITY() {
        return POSITIVE_INFINITY;
    }

    public final double getNEGATIVE_INFINITY() {
        return NEGATIVE_INFINITY;
    }

    public final double getNaN() {
        return NaN;
    }

    private DoubleCompanionObject() {
    }

    static {
        DoubleCompanionObject doubleCompanionObject;
        INSTANCE = doubleCompanionObject = new DoubleCompanionObject();
        MIN_VALUE = Double.MIN_VALUE;
        MAX_VALUE = Double.MAX_VALUE;
        POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
        NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
        NaN = Double.NaN;
    }
}

