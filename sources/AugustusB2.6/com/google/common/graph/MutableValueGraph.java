// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
public interface MutableValueGraph<N, V> extends ValueGraph<N, V>
{
    @CanIgnoreReturnValue
    boolean addNode(final N p0);
    
    @CheckForNull
    @CanIgnoreReturnValue
    V putEdgeValue(final N p0, final N p1, final V p2);
    
    @CheckForNull
    @CanIgnoreReturnValue
    V putEdgeValue(final EndpointPair<N> p0, final V p1);
    
    @CanIgnoreReturnValue
    boolean removeNode(final N p0);
    
    @CheckForNull
    @CanIgnoreReturnValue
    V removeEdge(final N p0, final N p1);
    
    @CheckForNull
    @CanIgnoreReturnValue
    V removeEdge(final EndpointPair<N> p0);
}
