/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.Metadata;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u000b\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0014\u0010\t\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0014\u0010\u000b\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0014\u0010\r\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006\u00a8\u0006\u000f"}, d2={"Lkotlin/jvm/internal/FloatCompanionObject;", "", "()V", "MAX_VALUE", "", "getMAX_VALUE", "()F", "MIN_VALUE", "getMIN_VALUE", "NEGATIVE_INFINITY", "getNEGATIVE_INFINITY", "NaN", "getNaN", "POSITIVE_INFINITY", "getPOSITIVE_INFINITY", "kotlin-stdlib"})
public final class FloatCompanionObject {
    private static final float MIN_VALUE = Float.MIN_VALUE;
    private static final float MAX_VALUE = Float.MAX_VALUE;
    private static final float POSITIVE_INFINITY = Float.POSITIVE_INFINITY;
    private static final float NEGATIVE_INFINITY = Float.NEGATIVE_INFINITY;
    private static final float NaN = Float.NaN;
    public static final FloatCompanionObject INSTANCE;

    public final float getMIN_VALUE() {
        return MIN_VALUE;
    }

    public final float getMAX_VALUE() {
        return MAX_VALUE;
    }

    public final float getPOSITIVE_INFINITY() {
        return POSITIVE_INFINITY;
    }

    public final float getNEGATIVE_INFINITY() {
        return NEGATIVE_INFINITY;
    }

    public final float getNaN() {
        return NaN;
    }

    private FloatCompanionObject() {
    }

    static {
        FloatCompanionObject floatCompanionObject;
        INSTANCE = floatCompanionObject = new FloatCompanionObject();
        MIN_VALUE = Float.MIN_VALUE;
        MAX_VALUE = Float.MAX_VALUE;
        POSITIVE_INFINITY = Float.POSITIVE_INFINITY;
        NEGATIVE_INFINITY = Float.NEGATIVE_INFINITY;
        NaN = Float.NaN;
    }
}

