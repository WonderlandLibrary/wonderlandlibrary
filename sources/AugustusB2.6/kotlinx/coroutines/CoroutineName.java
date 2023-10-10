// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.AbstractCoroutineContextElement;

public final class CoroutineName extends AbstractCoroutineContextElement
{
    private final String name;
    public static final Key Key;
    
    @Override
    public final String toString() {
        return "CoroutineName(" + this.name + ')';
    }
    
    public final String getName() {
        return this.name;
    }
    
    static {
        Key = new Key((byte)0);
    }
    
    @Override
    public final int hashCode() {
        final String name = this.name;
        if (name != null) {
            return name.hashCode();
        }
        return 0;
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this == o || (o instanceof CoroutineName && Intrinsics.areEqual(this.name, ((CoroutineName)o).name));
    }
    
    public static final class Key implements CoroutineContext.Key<CoroutineName>
    {
        private Key() {
        }
    }
}
