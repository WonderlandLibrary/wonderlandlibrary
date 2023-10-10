// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;

public final class NodeList extends LockFreeLinkedListHead implements Incomplete
{
    @Override
    public final boolean isActive() {
        return true;
    }
    
    @Override
    public final NodeList getList() {
        return this;
    }
    
    public final String getString(final String state) {
        Intrinsics.checkParameterIsNotNull(state, "state");
        final StringBuilder sb;
        final StringBuilder $this$buildString;
        ($this$buildString = (sb = new StringBuilder())).append("List{");
        $this$buildString.append(state);
        $this$buildString.append("}[");
        boolean first = true;
        final LockFreeLinkedListHead this_$iv = this;
        final Object next = this.getNext();
        if (next == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
        }
        for (LockFreeLinkedListNode cur$iv = (LockFreeLinkedListNode)next; Intrinsics.areEqual(cur$iv, this_$iv) ^ true; cur$iv = cur$iv.getNextNode()) {
            if (cur$iv instanceof JobNode) {
                final JobNode node = (JobNode)cur$iv;
                if (first) {
                    first = false;
                }
                else {
                    $this$buildString.append(", ");
                }
                $this$buildString.append(node);
            }
        }
        $this$buildString.append("]");
        final String string = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "StringBuilder().apply(builderAction).toString()");
        return string;
    }
    
    @Override
    public final String toString() {
        if (DebugKt.getDEBUG()) {
            return this.getString("Active");
        }
        return super.toString();
    }
}
