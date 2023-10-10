// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;

public abstract class CompletionHandlerBase extends LockFreeLinkedListNode implements Function1<Throwable, Unit>
{
    public abstract void invoke(final Throwable p0);
}
