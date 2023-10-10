// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

final class ConfigurableMutableGraph<N> extends ForwardingGraph<N> implements MutableGraph<N>
{
    private final MutableValueGraph<N, GraphConstants.Presence> backingValueGraph;
    
    ConfigurableMutableGraph(final AbstractGraphBuilder<? super N> builder) {
        this.backingValueGraph = new ConfigurableMutableValueGraph<N, GraphConstants.Presence>(builder);
    }
    
    @Override
    protected Graph<N> delegate() {
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
    public boolean removeNode(final Object node) {
        return this.backingValueGraph.removeNode(node);
    }
    
    @Override
    public boolean removeEdge(final Object nodeU, final Object nodeV) {
        return this.backingValueGraph.removeEdge(nodeU, nodeV) != null;
    }
}
