// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;

public final class InactiveNodeList implements Incomplete
{
    private final NodeList list;
    
    @Override
    public final boolean isActive() {
        return false;
    }
    
    @Override
    public final String toString() {
        if (DebugKt.getDEBUG()) {
            return this.list.getString("New");
        }
        return super.toString();
    }
    
    @Override
    public final NodeList getList() {
        return this.list;
    }
    
    public InactiveNodeList(final NodeList list) {
        Intrinsics.checkParameterIsNotNull(list, "list");
        this.list = list;
    }
}
