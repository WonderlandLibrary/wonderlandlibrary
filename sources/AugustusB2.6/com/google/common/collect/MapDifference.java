// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.CheckForNull;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use Maps.difference")
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface MapDifference<K, V>
{
    boolean areEqual();
    
    Map<K, V> entriesOnlyOnLeft();
    
    Map<K, V> entriesOnlyOnRight();
    
    Map<K, V> entriesInCommon();
    
    Map<K, ValueDifference<V>> entriesDiffering();
    
    boolean equals(@CheckForNull final Object p0);
    
    int hashCode();
    
    @DoNotMock("Use Maps.difference")
    public interface ValueDifference<V>
    {
        @ParametricNullness
        V leftValue();
        
        @ParametricNullness
        V rightValue();
        
        boolean equals(@CheckForNull final Object p0);
        
        int hashCode();
    }
}
