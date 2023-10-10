// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

public final class LockFreeLinkedListKt
{
    private static final Object CONDITION_FALSE;
    
    public static final Object getCONDITION_FALSE() {
        return LockFreeLinkedListKt.CONDITION_FALSE;
    }
    
    public static final LockFreeLinkedListNode unwrap(final Object $this$unwrap) {
        Intrinsics.checkParameterIsNotNull($this$unwrap, "$this$unwrap");
        Object o = $this$unwrap;
        if (!($this$unwrap instanceof Removed)) {
            o = null;
        }
        final Removed removed = (Removed)o;
        LockFreeLinkedListNode ref;
        if (removed == null || (ref = removed.ref) == null) {
            ref = (LockFreeLinkedListNode)$this$unwrap;
        }
        return ref;
    }
    
    static {
        CONDITION_FALSE = new CoroutineContext.Element.DefaultImpls("CONDITION_FALSE");
        new CoroutineContext.Element.DefaultImpls("ALREADY_REMOVED");
        new CoroutineContext.Element.DefaultImpls("LIST_EMPTY");
        new CoroutineContext.Element.DefaultImpls("REMOVE_PREPARED");
    }
}
