// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import java.util.Set;

@ElementTypesAreNonnullByDefault
interface BaseGraph<N> extends SuccessorsFunction<N>, PredecessorsFunction<N>
{
    Set<N> nodes();
    
    Set<EndpointPair<N>> edges();
    
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
}
