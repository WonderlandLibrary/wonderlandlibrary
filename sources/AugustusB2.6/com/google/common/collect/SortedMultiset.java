// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.SortedSet;
import java.util.Iterator;
import java.util.Set;
import java.util.NavigableSet;
import javax.annotation.CheckForNull;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public interface SortedMultiset<E> extends SortedMultisetBridge<E>, SortedIterable<E>
{
    Comparator<? super E> comparator();
    
    @CheckForNull
    Multiset.Entry<E> firstEntry();
    
    @CheckForNull
    Multiset.Entry<E> lastEntry();
    
    @CheckForNull
    Multiset.Entry<E> pollFirstEntry();
    
    @CheckForNull
    Multiset.Entry<E> pollLastEntry();
    
    NavigableSet<E> elementSet();
    
    Set<Multiset.Entry<E>> entrySet();
    
    Iterator<E> iterator();
    
    SortedMultiset<E> descendingMultiset();
    
    SortedMultiset<E> headMultiset(@ParametricNullness final E p0, final BoundType p1);
    
    SortedMultiset<E> subMultiset(@ParametricNullness final E p0, final BoundType p1, @ParametricNullness final E p2, final BoundType p3);
    
    SortedMultiset<E> tailMultiset(@ParametricNullness final E p0, final BoundType p1);
}
