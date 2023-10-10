// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;

final class Removed
{
    public final LockFreeLinkedListNode ref;
    
    @Override
    public final String toString() {
        return "Removed[" + this.ref + ']';
    }
    
    public Removed(final LockFreeLinkedListNode ref) {
        Intrinsics.checkParameterIsNotNull(ref, "ref");
        this.ref = ref;
    }
}
