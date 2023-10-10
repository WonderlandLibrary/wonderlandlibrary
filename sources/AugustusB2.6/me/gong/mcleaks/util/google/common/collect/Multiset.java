// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.function.ObjIntConsumer;
import java.util.Set;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.errorprone.annotations.CompatibleWith;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.util.Collection;

@GwtCompatible
public interface Multiset<E> extends Collection<E>
{
    int size();
    
    int count(@Nullable @CompatibleWith("E") final Object p0);
    
    @CanIgnoreReturnValue
    int add(@Nullable final E p0, final int p1);
    
    @CanIgnoreReturnValue
    int remove(@Nullable @CompatibleWith("E") final Object p0, final int p1);
    
    @CanIgnoreReturnValue
    int setCount(final E p0, final int p1);
    
    @CanIgnoreReturnValue
    boolean setCount(final E p0, final int p1, final int p2);
    
    Set<E> elementSet();
    
    Set<Entry<E>> entrySet();
    
    @Beta
    default void forEachEntry(final ObjIntConsumer<? super E> action) {
        Preconditions.checkNotNull(action);
        this.entrySet().forEach(entry -> action.accept(entry.getElement(), entry.getCount()));
    }
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
    
    String toString();
    
    Iterator<E> iterator();
    
    boolean contains(@Nullable final Object p0);
    
    boolean containsAll(final Collection<?> p0);
    
    @CanIgnoreReturnValue
    boolean add(final E p0);
    
    @CanIgnoreReturnValue
    boolean remove(@Nullable final Object p0);
    
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
        E getElement();
        
        int getCount();
        
        boolean equals(final Object p0);
        
        int hashCode();
        
        String toString();
    }
}
