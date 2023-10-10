// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.DoNotMock;
import java.util.Iterator;

@DoNotMock("Use Iterators.peekingIterator")
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface PeekingIterator<E> extends Iterator<E>
{
    @ParametricNullness
    E peek();
    
    @ParametricNullness
    @CanIgnoreReturnValue
    E next();
    
    void remove();
}
