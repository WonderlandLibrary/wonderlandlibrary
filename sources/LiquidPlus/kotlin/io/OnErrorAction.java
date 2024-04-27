/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import kotlin.Metadata;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2={"Lkotlin/io/OnErrorAction;", "", "(Ljava/lang/String;I)V", "SKIP", "TERMINATE", "kotlin-stdlib"})
public final class OnErrorAction
extends Enum<OnErrorAction> {
    public static final /* enum */ OnErrorAction SKIP = new OnErrorAction();
    public static final /* enum */ OnErrorAction TERMINATE = new OnErrorAction();
    private static final /* synthetic */ OnErrorAction[] $VALUES;

    public static OnErrorAction[] values() {
        return (OnErrorAction[])$VALUES.clone();
    }

    public static OnErrorAction valueOf(String value) {
        return Enum.valueOf(OnErrorAction.class, value);
    }

    static {
        $VALUES = onErrorActionArray = new OnErrorAction[]{OnErrorAction.SKIP, OnErrorAction.TERMINATE};
    }
}

