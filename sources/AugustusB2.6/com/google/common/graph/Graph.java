// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import javax.annotation.CheckForNull;
import java.util.Set;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use GraphBuilder to create a real instance")
@ElementTypesAreNonnullByDefault
@Beta
public interface Graph<N> extends BaseGraph<N>
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
    
    boolean equals(@CheckForNull final Object p0);
    
    int hashCode();
}
