// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;

interface NetworkConnections<N, E>
{
    Set<N> adjacentNodes();
    
    Set<N> predecessors();
    
    Set<N> successors();
    
    Set<E> incidentEdges();
    
    Set<E> inEdges();
    
    Set<E> outEdges();
    
    Set<E> edgesConnecting(final Object p0);
    
    N oppositeNode(final Object p0);
    
    @CanIgnoreReturnValue
    N removeInEdge(final Object p0, final boolean p1);
    
    @CanIgnoreReturnValue
    N removeOutEdge(final Object p0);
    
    void addInEdge(final E p0, final N p1, final boolean p2);
    
    void addOutEdge(final E p0, final N p1);
}
