// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Arrays;
import javax.annotation.CheckForNull;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true)
final class CompoundOrdering<T> extends Ordering<T> implements Serializable
{
    final Comparator<? super T>[] comparators;
    private static final long serialVersionUID = 0L;
    
    CompoundOrdering(final Comparator<? super T> primary, final Comparator<? super T> secondary) {
        this.comparators = (Comparator<? super T>[])new Comparator[] { primary, secondary };
    }
    
    CompoundOrdering(final Iterable<? extends Comparator<? super T>> comparators) {
        this.comparators = Iterables.toArray(comparators, new Comparator[0]);
    }
    
    @Override
    public int compare(@ParametricNullness final T left, @ParametricNullness final T right) {
        for (int i = 0; i < this.comparators.length; ++i) {
            final int result = this.comparators[i].compare((Object)left, (Object)right);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CompoundOrdering) {
            final CompoundOrdering<?> that = (CompoundOrdering<?>)object;
            return Arrays.equals(this.comparators, that.comparators);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.comparators);
    }
    
    @Override
    public String toString() {
        final String string = Arrays.toString(this.comparators);
        return new StringBuilder(19 + String.valueOf(string).length()).append("Ordering.compound(").append(string).append(")").toString();
    }
}
