// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.errorprone.annotations.DoNotCall;
import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class UnmodifiableIterator<E> implements Iterator<E>
{
    protected UnmodifiableIterator() {
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
