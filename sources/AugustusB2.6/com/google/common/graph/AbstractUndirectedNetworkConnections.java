// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import javax.annotation.CheckForNull;
import java.util.Objects;
import java.util.Collections;
import java.util.Set;
import com.google.common.base.Preconditions;
import java.util.Map;

@ElementTypesAreNonnullByDefault
abstract class AbstractUndirectedNetworkConnections<N, E> implements NetworkConnections<N, E>
{
    final Map<E, N> incidentEdgeMap;
    
    AbstractUndirectedNetworkConnections(final Map<E, N> incidentEdgeMap) {
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
    public N adjacentNode(final E edge) {
        return Objects.requireNonNull(this.incidentEdgeMap.get(edge));
    }
    
    @CheckForNull
    @Override
    public N removeInEdge(final E edge, final boolean isSelfLoop) {
        if (!isSelfLoop) {
            return this.removeOutEdge(edge);
        }
        return null;
    }
    
    @Override
    public N removeOutEdge(final E edge) {
        final N previousNode = this.incidentEdgeMap.remove(edge);
        return Objects.requireNonNull(previousNode);
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
