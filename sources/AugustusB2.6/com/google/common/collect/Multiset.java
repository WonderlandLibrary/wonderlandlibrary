// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.Iterator;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.function.ObjIntConsumer;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Multiset<E> extends Collection<E>
{
    int size();
    
    int count(@CheckForNull @CompatibleWith("E") final Object p0);
    
    @CanIgnoreReturnValue
    int add(@ParametricNullness final E p0, final int p1);
    
    @CanIgnoreReturnValue
    boolean add(@ParametricNullness final E p0);
    
    @CanIgnoreReturnValue
    int remove(@CheckForNull @CompatibleWith("E") final Object p0, final int p1);
    
    @CanIgnoreReturnValue
    boolean remove(@CheckForNull final Object p0);
    
    @CanIgnoreReturnValue
    int setCount(@ParametricNullness final E p0, final int p1);
    
    @CanIgnoreReturnValue
    boolean setCount(@ParametricNullness final E p0, final int p1, final int p2);
    
    Set<E> elementSet();
    
    Set<Entry<E>> entrySet();
    
    @Beta
    default void forEachEntry(final ObjIntConsumer<? super E> action) {
        Preconditions.checkNotNull(action);
        this.entrySet().forEach(entry -> action.accept(entry.getElement(), entry.getCount()));
    }
    
    boolean equals(@CheckForNull final Object p0);
    
    int hashCode();
    
    String toString();
    
    Iterator<E> iterator();
    
    boolean contains(@CheckForNull final Object p0);
    
    boolean containsAll(final Collection<?> p0);
    
    @CanIgnoreReturnValue
    boolean removeAll(final Collection<?> p0);
    
    @CanIgnoreReturnValue
    boolean retainAll(final Collection<?> p0);
    
    default void forEach(final Consumer<? super E> action) {
        Preconditions.checkNotNull(action);
        final E elem;
        int count;
        int i;
        this.entrySet().forEach(entry -> {
            elem = entry.getElement();
            for (count = entry.getCount(), i = 0; i < count; ++i) {
                action.accept((Object)elem);
            }
        });
    }
    
    default Spliterator<E> spliterator() {
        return Multisets.spliteratorImpl(this);
    }
    
    public interface Entry<E>
    {
        @ParametricNullness
        E getElement();
        
        int getCount();
        
        boolean equals(@CheckForNull final Object p0);
        
        int hashCode();
        
        String toString();
    }
}
