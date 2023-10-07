/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.Metadata;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0002\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lkotlin/jvm/internal/CharCompanionObject;", "", "()V", "MAX_HIGH_SURROGATE", "", "MAX_LOW_SURROGATE", "MAX_SURROGATE", "MAX_VALUE", "MIN_HIGH_SURROGATE", "MIN_LOW_SURROGATE", "MIN_SURROGATE", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"})
public final class CharCompanionObject {
    public static final char MIN_VALUE = '\u0000';
    public static final char MAX_VALUE = '\uffff';
    public static final char MIN_HIGH_SURROGATE = '\ud800';
    public static final char MAX_HIGH_SURROGATE = '\udbff';
    public static final char MIN_LOW_SURROGATE = '\udc00';
    public static final char MAX_LOW_SURROGATE = '\udfff';
    public static final char MIN_SURROGATE = '\ud800';
    public static final char MAX_SURROGATE = '\udfff';
    public static final int SIZE_BYTES = 2;
    public static final int SIZE_BITS = 16;
    public static final CharCompanionObject INSTANCE;

    private CharCompanionObject() {
    }

    static {
        CharCompanionObject charCompanionObject;
        INSTANCE = charCompanionObject = new CharCompanionObject();
    }
}

