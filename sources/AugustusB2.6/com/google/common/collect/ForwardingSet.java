// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import java.util.Set;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingSet<E> extends ForwardingCollection<E> implements Set<E>
{
    protected ForwardingSet() {
    }
    
    @Override
    protected abstract Set<E> delegate();
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        return object == this || this.delegate().equals(object);
    }
    
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }
    
    @Override
    protected boolean standardRemoveAll(final Collection<?> collection) {
        return Sets.removeAllImpl(this, Preconditions.checkNotNull(collection));
    }
    
    protected boolean standardEquals(@CheckForNull final Object object) {
        return Sets.equalsImpl(this, object);
    }
    
    protected int standardHashCode() {
        return Sets.hashCodeImpl(this);
    }
}
