/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.Metadata;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lkotlin/jvm/internal/ShortCompanionObject;", "", "()V", "MAX_VALUE", "", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"})
public final class ShortCompanionObject {
    public static final short MIN_VALUE = Short.MIN_VALUE;
    public static final short MAX_VALUE = Short.MAX_VALUE;
    public static final int SIZE_BYTES = 2;
    public static final int SIZE_BITS = 16;
    public static final ShortCompanionObject INSTANCE;

    private ShortCompanionObject() {
    }

    static {
        ShortCompanionObject shortCompanionObject;
        INSTANCE = shortCompanionObject = new ShortCompanionObject();
    }
}

