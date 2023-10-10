// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.Iterator;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use ImmutableRangeSet or TreeRangeSet")
@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public interface RangeSet<C extends Comparable>
{
    boolean contains(final C p0);
    
    @CheckForNull
    Range<C> rangeContaining(final C p0);
    
    boolean intersects(final Range<C> p0);
    
    boolean encloses(final Range<C> p0);
    
    boolean enclosesAll(final RangeSet<C> p0);
    
    default boolean enclosesAll(final Iterable<Range<C>> other) {
        for (final Range<C> range : other) {
            if (!this.encloses(range)) {
                return false;
            }
        }
        return true;
    }
    
    boolean isEmpty();
    
    Range<C> span();
    
    Set<Range<C>> asRanges();
    
    Set<Range<C>> asDescendingSetOfRanges();
    
    RangeSet<C> complement();
    
    RangeSet<C> subRangeSet(final Range<C> p0);
    
    void add(final Range<C> p0);
    
    void remove(final Range<C> p0);
    
    void clear();
    
    void addAll(final RangeSet<C> p0);
    
    default void addAll(final Iterable<Range<C>> ranges) {
        for (final Range<C> range : ranges) {
            this.add(range);
        }
    }
    
    void removeAll(final RangeSet<C> p0);
    
    default void removeAll(final Iterable<Range<C>> ranges) {
        for (final Range<C> range : ranges) {
            this.remove(range);
        }
    }
    
    boolean equals(@CheckForNull final Object p0);
    
    int hashCode();
    
    String toString();
}
