// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.errorprone.annotations.DoNotCall;
import com.google.common.annotations.GwtCompatible;
import java.util.ListIterator;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class UnmodifiableListIterator<E> extends UnmodifiableIterator<E> implements ListIterator<E>
{
    protected UnmodifiableListIterator() {
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final void add(@ParametricNullness final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final void set(@ParametricNullness final E e) {
        throw new UnsupportedOperationException();
    }
}
