// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import java.util.Set;

@ElementTypesAreNonnullByDefault
abstract class ForwardingGraph<N> extends AbstractGraph<N>
{
    abstract BaseGraph<N> delegate();
    
    @Override
    public Set<N> nodes() {
        return this.delegate().nodes();
    }
    
    @Override
    protected long edgeCount() {
        return this.delegate().edges().size();
    }
    
    @Override
    public boolean isDirected() {
        return this.delegate().isDirected();
    }
    
    @Override
    public boolean allowsSelfLoops() {
        return this.delegate().allowsSelfLoops();
    }
    
    @Override
    public ElementOrder<N> nodeOrder() {
        return this.delegate().nodeOrder();
    }
    
    @Override
    public ElementOrder<N> incidentEdgeOrder() {
        return this.delegate().incidentEdgeOrder();
    }
    
    @Override
    public Set<N> adjacentNodes(final N node) {
        return this.delegate().adjacentNodes(node);
    }
    
    @Override
    public Set<N> predecessors(final N node) {
        return this.delegate().predecessors(node);
    }
    
    @Override
    public Set<N> successors(final N node) {
        return this.delegate().successors(node);
    }
    
    @Override
    public Set<EndpointPair<N>> incidentEdges(final N node) {
        return this.delegate().incidentEdges(node);
    }
    
    @Override
    public int degree(final N node) {
        return this.delegate().degree(node);
    }
    
    @Override
    public int inDegree(final N node) {
        return this.delegate().inDegree(node);
    }
    
    @Override
    public int outDegree(final N node) {
        return this.delegate().outDegree(node);
    }
    
    @Override
    public boolean hasEdgeConnecting(final N nodeU, final N nodeV) {
        return this.delegate().hasEdgeConnecting(nodeU, nodeV);
    }
    
    @Override
    public boolean hasEdgeConnecting(final EndpointPair<N> endpoints) {
        return this.delegate().hasEdgeConnecting(endpoints);
    }
}
