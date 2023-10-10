// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import javax.annotation.CheckForNull;
import java.util.Optional;
import java.util.Set;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use NetworkBuilder to create a real instance")
@ElementTypesAreNonnullByDefault
@Beta
public interface Network<N, E> extends SuccessorsFunction<N>, PredecessorsFunction<N>
{
    Set<N> nodes();
    
    Set<E> edges();
    
    Graph<N> asGraph();
    
    boolean isDirected();
    
    boolean allowsParallelEdges();
    
    boolean allowsSelfLoops();
    
    ElementOrder<N> nodeOrder();
    
    ElementOrder<E> edgeOrder();
    
    Set<N> adjacentNodes(final N p0);
    
    Set<N> predecessors(final N p0);
    
    Set<N> successors(final N p0);
    
    Set<E> incidentEdges(final N p0);
    
    Set<E> inEdges(final N p0);
    
    Set<E> outEdges(final N p0);
    
    int degree(final N p0);
    
    int inDegree(final N p0);
    
    int outDegree(final N p0);
    
    EndpointPair<N> incidentNodes(final E p0);
    
    Set<E> adjacentEdges(final E p0);
    
    Set<E> edgesConnecting(final N p0, final N p1);
    
    Set<E> edgesConnecting(final EndpointPair<N> p0);
    
    Optional<E> edgeConnecting(final N p0, final N p1);
    
    Optional<E> edgeConnecting(final EndpointPair<N> p0);
    
    @CheckForNull
    E edgeConnectingOrNull(final N p0, final N p1);
    
    @CheckForNull
    E edgeConnectingOrNull(final EndpointPair<N> p0);
    
    boolean hasEdgeConnecting(final N p0, final N p1);
    
    boolean hasEdgeConnecting(final EndpointPair<N> p0);
    
    boolean equals(@CheckForNull final Object p0);
    
    int hashCode();
}
