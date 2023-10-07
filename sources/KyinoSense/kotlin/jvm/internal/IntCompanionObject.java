/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.Metadata;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2={"Lkotlin/jvm/internal/IntCompanionObject;", "", "()V", "MAX_VALUE", "", "MIN_VALUE", "SIZE_BITS", "SIZE_BYTES", "kotlin-stdlib"})
public final class IntCompanionObject {
    public static final int MIN_VALUE = Integer.MIN_VALUE;
    public static final int MAX_VALUE = Integer.MAX_VALUE;
    public static final int SIZE_BYTES = 4;
    public static final int SIZE_BITS = 32;
    public static final IntCompanionObject INSTANCE;

    private IntCompanionObject() {
    }

    static {
        IntCompanionObject intCompanionObject;
        INSTANCE = intCompanionObject = new IntCompanionObject();
    }
}

