// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Set;

@ElementTypesAreNonnullByDefault
interface NetworkConnections<N, E>
{
    Set<N> adjacentNodes();
    
    Set<N> predecessors();
    
    Set<N> successors();
    
    Set<E> incidentEdges();
    
    Set<E> inEdges();
    
    Set<E> outEdges();
    
    Set<E> edgesConnecting(final N p0);
    
    N adjacentNode(final E p0);
    
    @CheckForNull
    @CanIgnoreReturnValue
    N removeInEdge(final E p0, final boolean p1);
    
    @CanIgnoreReturnValue
    N removeOutEdge(final E p0);
    
    void addInEdge(final E p0, final N p1, final boolean p2);
    
    void addOutEdge(final E p0, final N p1);
}
