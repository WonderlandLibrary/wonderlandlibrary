// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Iterator;
import java.util.Set;

@ElementTypesAreNonnullByDefault
interface GraphConnections<N, V>
{
    Set<N> adjacentNodes();
    
    Set<N> predecessors();
    
    Set<N> successors();
    
    Iterator<EndpointPair<N>> incidentEdgeIterator(final N p0);
    
    @CheckForNull
    V value(final N p0);
    
    void removePredecessor(final N p0);
    
    @CheckForNull
    @CanIgnoreReturnValue
    V removeSuccessor(final N p0);
    
    void addPredecessor(final N p0, final V p1);
    
    @CheckForNull
    @CanIgnoreReturnValue
    V addSuccessor(final N p0, final V p1);
}
