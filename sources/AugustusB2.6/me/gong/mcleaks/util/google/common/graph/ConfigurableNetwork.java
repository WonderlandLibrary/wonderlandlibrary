// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;

class ConfigurableNetwork<N, E> extends AbstractNetwork<N, E>
{
    private final boolean isDirected;
    private final boolean allowsParallelEdges;
    private final boolean allowsSelfLoops;
    private final ElementOrder<N> nodeOrder;
    private final ElementOrder<E> edgeOrder;
    protected final MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;
    protected final MapIteratorCache<E, N> edgeToReferenceNode;
    
    ConfigurableNetwork(final NetworkBuilder<? super N, ? super E> builder) {
        this((NetworkBuilder<? super Object, ? super Object>)builder, builder.nodeOrder.createMap(builder.expectedNodeCount.or(10)), builder.edgeOrder.createMap(builder.expectedEdgeCount.or(20)));
    }
    
    ConfigurableNetwork(final NetworkBuilder<? super N, ? super E> builder, final Map<N, NetworkConnections<N, E>> nodeConnections, final Map<E, N> edgeToReferenceNode) {
        this.isDirected = builder.directed;
        this.allowsParallelEdges = builder.allowsParallelEdges;
        this.allowsSelfLoops = builder.allowsSelfLoops;
        this.nodeOrder = builder.nodeOrder.cast();
        this.edgeOrder = builder.edgeOrder.cast();
        this.nodeConnections = ((nodeConnections instanceof TreeMap) ? new MapRetrievalCache<N, NetworkConnections<N, E>>(nodeConnections) : new MapIteratorCache<N, NetworkConnections<N, E>>(nodeConnections));
        this.edgeToReferenceNode = new MapIteratorCache<E, N>(edgeToReferenceNode);
    }
    
    @Override
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }
    
    @Override
    public Set<E> edges() {
        return this.edgeToReferenceNode.unmodifiableKeySet();
    }
    
    @Override
    public boolean isDirected() {
        return this.isDirected;
    }
    
    @Override
    public boolean allowsParallelEdges() {
        return this.allowsParallelEdges;
    }
    
    @Override
    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }
    
    @Override
    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }
    
    @Override
    public ElementOrder<E> edgeOrder() {
        return this.edgeOrder;
    }
    
    @Override
    public Set<E> incidentEdges(final Object node) {
        return this.checkedConnections(node).incidentEdges();
    }
    
    @Override
    public EndpointPair<N> incidentNodes(final Object edge) {
        final N nodeU = this.checkedReferenceNode(edge);
        final N nodeV = this.nodeConnections.get(nodeU).oppositeNode(edge);
        return EndpointPair.of(this, nodeU, nodeV);
    }
    
    @Override
    public Set<N> adjacentNodes(final Object node) {
        return this.checkedConnections(node).adjacentNodes();
    }
    
    @Override
    public Set<E> edgesConnecting(final Object nodeU, final Object nodeV) {
        final NetworkConnections<N, E> connectionsU = this.checkedConnections(nodeU);
        if (!this.allowsSelfLoops && nodeU == nodeV) {
            return (Set<E>)ImmutableSet.of();
        }
        Preconditions.checkArgument(this.containsNode(nodeV), "Node %s is not an element of this graph.", nodeV);
        return connectionsU.edgesConnecting(nodeV);
    }
    
    @Override
    public Set<E> inEdges(final Object node) {
        return this.checkedConnections(node).inEdges();
    }
    
    @Override
    public Set<E> outEdges(final Object node) {
        return this.checkedConnections(node).outEdges();
    }
    
    @Override
    public Set<N> predecessors(final Object node) {
        return this.checkedConnections(node).predecessors();
    }
    
    @Override
    public Set<N> successors(final Object node) {
        return this.checkedConnections(node).successors();
    }
    
    protected final NetworkConnections<N, E> checkedConnections(final Object node) {
        final NetworkConnections<N, E> connections = this.nodeConnections.get(node);
        if (connections == null) {
            Preconditions.checkNotNull(node);
            throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", node));
        }
        return connections;
    }
    
    protected final N checkedReferenceNode(final Object edge) {
        final N referenceNode = this.edgeToReferenceNode.get(edge);
        if (referenceNode == null) {
            Preconditions.checkNotNull(edge);
            throw new IllegalArgumentException(String.format("Edge %s is not an element of this graph.", edge));
        }
        return referenceNode;
    }
    
    protected final boolean containsNode(@Nullable final Object node) {
        return this.nodeConnections.containsKey(node);
    }
    
    protected final boolean containsEdge(@Nullable final Object edge) {
        return this.edgeToReferenceNode.containsKey(edge);
    }
}
