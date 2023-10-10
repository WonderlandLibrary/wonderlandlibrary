// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;

final class IncompleteStateBox
{
    public final Incomplete state;
    
    public IncompleteStateBox(final Incomplete state) {
        Intrinsics.checkParameterIsNotNull(state, "state");
        this.state = state;
    }
}
