// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

@ElementTypesAreNonnullByDefault
final class StandardMutableGraph<N> extends ForwardingGraph<N> implements MutableGraph<N>
{
    private final MutableValueGraph<N, GraphConstants.Presence> backingValueGraph;
    
    StandardMutableGraph(final AbstractGraphBuilder<? super N> builder) {
        this.backingValueGraph = new StandardMutableValueGraph<N, GraphConstants.Presence>(builder);
    }
    
    @Override
    BaseGraph<N> delegate() {
        return this.backingValueGraph;
    }
    
    @Override
    public boolean addNode(final N node) {
        return this.backingValueGraph.addNode(node);
    }
    
    @Override
    public boolean putEdge(final N nodeU, final N nodeV) {
        return this.backingValueGraph.putEdgeValue(nodeU, nodeV, GraphConstants.Presence.EDGE_EXISTS) == null;
    }
    
    @Override
    public boolean putEdge(final EndpointPair<N> endpoints) {
        this.validateEndpoints(endpoints);
        return this.putEdge(endpoints.nodeU(), endpoints.nodeV());
    }
    
    @Override
    public boolean removeNode(final N node) {
        return this.backingValueGraph.removeNode(node);
    }
    
    @Override
    public boolean removeEdge(final N nodeU, final N nodeV) {
        return this.backingValueGraph.removeEdge(nodeU, nodeV) != null;
    }
    
    @Override
    public boolean removeEdge(final EndpointPair<N> endpoints) {
        this.validateEndpoints(endpoints);
        return this.removeEdge(endpoints.nodeU(), endpoints.nodeV());
    }
}
