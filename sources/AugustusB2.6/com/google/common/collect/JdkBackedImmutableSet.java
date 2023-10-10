// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.CheckForNull;
import java.util.Set;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true)
final class JdkBackedImmutableSet<E> extends IndexedImmutableSet<E>
{
    private final Set<?> delegate;
    private final ImmutableList<E> delegateList;
    
    JdkBackedImmutableSet(final Set<?> delegate, final ImmutableList<E> delegateList) {
        this.delegate = delegate;
        this.delegateList = delegateList;
    }
    
    @Override
    E get(final int index) {
        return this.delegateList.get(index);
    }
    
    @Override
    public boolean contains(@CheckForNull final Object object) {
        return this.delegate.contains(object);
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public int size() {
        return this.delegateList.size();
    }
}
