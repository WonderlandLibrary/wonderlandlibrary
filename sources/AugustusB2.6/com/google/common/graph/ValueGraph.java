// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import javax.annotation.CheckForNull;
import java.util.Optional;
import java.util.Set;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
public interface ValueGraph<N, V> extends BaseGraph<N>
{
    Set<N> nodes();
    
    Set<EndpointPair<N>> edges();
    
    Graph<N> asGraph();
    
    boolean isDirected();
    
    boolean allowsSelfLoops();
    
    ElementOrder<N> nodeOrder();
    
    ElementOrder<N> incidentEdgeOrder();
    
    Set<N> adjacentNodes(final N p0);
    
    Set<N> predecessors(final N p0);
    
    Set<N> successors(final N p0);
    
    Set<EndpointPair<N>> incidentEdges(final N p0);
    
    int degree(final N p0);
    
    int inDegree(final N p0);
    
    int outDegree(final N p0);
    
    boolean hasEdgeConnecting(final N p0, final N p1);
    
    boolean hasEdgeConnecting(final EndpointPair<N> p0);
    
    Optional<V> edgeValue(final N p0, final N p1);
    
    Optional<V> edgeValue(final EndpointPair<N> p0);
    
    @CheckForNull
    V edgeValueOrDefault(final N p0, final N p1, @CheckForNull final V p2);
    
    @CheckForNull
    V edgeValueOrDefault(final EndpointPair<N> p0, @CheckForNull final V p1);
    
    boolean equals(@CheckForNull final Object p0);
    
    int hashCode();
}
