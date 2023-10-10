// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import java.util.Collections;
import java.util.Set;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Map;

abstract class AbstractUndirectedNetworkConnections<N, E> implements NetworkConnections<N, E>
{
    protected final Map<E, N> incidentEdgeMap;
    
    protected AbstractUndirectedNetworkConnections(final Map<E, N> incidentEdgeMap) {
        this.incidentEdgeMap = Preconditions.checkNotNull(incidentEdgeMap);
    }
    
    @Override
    public Set<N> predecessors() {
        return this.adjacentNodes();
    }
    
    @Override
    public Set<N> successors() {
        return this.adjacentNodes();
    }
    
    @Override
    public Set<E> incidentEdges() {
        return Collections.unmodifiableSet((Set<? extends E>)this.incidentEdgeMap.keySet());
    }
    
    @Override
    public Set<E> inEdges() {
        return this.incidentEdges();
    }
    
    @Override
    public Set<E> outEdges() {
        return this.incidentEdges();
    }
    
    @Override
    public N oppositeNode(final Object edge) {
        return Preconditions.checkNotNull(this.incidentEdgeMap.get(edge));
    }
    
    @Override
    public N removeInEdge(final Object edge, final boolean isSelfLoop) {
        if (!isSelfLoop) {
            return this.removeOutEdge(edge);
        }
        return null;
    }
    
    @Override
    public N removeOutEdge(final Object edge) {
        final N previousNode = this.incidentEdgeMap.remove(edge);
        return Preconditions.checkNotNull(previousNode);
    }
    
    @Override
    public void addInEdge(final E edge, final N node, final boolean isSelfLoop) {
        if (!isSelfLoop) {
            this.addOutEdge(edge, node);
        }
    }
    
    @Override
    public void addOutEdge(final E edge, final N node) {
        final N previousNode = this.incidentEdgeMap.put(edge, node);
        Preconditions.checkState(previousNode == null);
    }
}
