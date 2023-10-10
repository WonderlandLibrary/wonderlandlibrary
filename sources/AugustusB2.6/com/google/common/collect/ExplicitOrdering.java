// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.CheckForNull;
import java.util.Collection;
import java.util.List;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true)
final class ExplicitOrdering<T> extends Ordering<T> implements Serializable
{
    final ImmutableMap<T, Integer> rankMap;
    private static final long serialVersionUID = 0L;
    
    ExplicitOrdering(final List<T> valuesInOrder) {
        this(Maps.indexMap(valuesInOrder));
    }
    
    ExplicitOrdering(final ImmutableMap<T, Integer> rankMap) {
        this.rankMap = rankMap;
    }
    
    @Override
    public int compare(final T left, final T right) {
        return this.rank(left) - this.rank(right);
    }
    
    private int rank(final T value) {
        final Integer rank = this.rankMap.get(value);
        if (rank == null) {
            throw new IncomparableValueException(value);
        }
        return rank;
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        if (object instanceof ExplicitOrdering) {
            final ExplicitOrdering<?> that = (ExplicitOrdering<?>)object;
            return this.rankMap.equals(that.rankMap);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.rankMap.hashCode();
    }
    
    @Override
    public String toString() {
        final String value = String.valueOf(this.rankMap.keySet());
        return new StringBuilder(19 + String.valueOf(value).length()).append("Ordering.explicit(").append(value).append(")").toString();
    }
}
