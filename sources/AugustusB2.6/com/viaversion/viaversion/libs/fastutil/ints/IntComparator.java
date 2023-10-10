// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.lang.invoke.SerializedLambda;
import java.io.Serializable;
import java.util.Comparator;

@FunctionalInterface
public interface IntComparator extends Comparator<Integer>
{
    int compare(final int p0, final int p1);
    
    default IntComparator reversed() {
        return IntComparators.oppositeComparator(this);
    }
    
    @Deprecated
    default int compare(final Integer ok1, final Integer ok2) {
        return this.compare((int)ok1, (int)ok2);
    }
    
    default IntComparator thenComparing(final IntComparator second) {
        final int comp;
        return (k1, k2) -> {
            comp = this.compare(k1, k2);
            return (comp == 0) ? second.compare(k1, k2) : comp;
        };
    }
    
    default Comparator<Integer> thenComparing(final Comparator<? super Integer> second) {
        if (second instanceof IntComparator) {
            return this.thenComparing((IntComparator)second);
        }
        return super.thenComparing(second);
    }
}
