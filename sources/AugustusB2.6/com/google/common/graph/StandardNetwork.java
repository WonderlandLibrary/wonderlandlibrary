// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;

@ElementTypesAreNonnullByDefault
class StandardNetwork<N, E> extends AbstractNetwork<N, E>
{
    private final boolean isDirected;
    private final boolean allowsParallelEdges;
    private final boolean allowsSelfLoops;
    private final ElementOrder<N> nodeOrder;
    private final ElementOrder<E> edgeOrder;
    final MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;
    final MapIteratorCache<E, N> edgeToReferenceNode;
    
    StandardNetwork(final NetworkBuilder<? super N, ? super E> builder) {
        this((NetworkBuilder<? super Object, ? super Object>)builder, builder.nodeOrder.createMap(builder.expectedNodeCount.or(10)), builder.edgeOrder.createMap(builder.expectedEdgeCount.or(20)));
    }
    
    StandardNetwork(final NetworkBuilder<? super N, ? super E> builder, final Map<N, NetworkConnections<N, E>> nodeConnections, final Map<E, N> edgeToReferenceNode) {
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
    public Set<E> incidentEdges(final N node) {
        return this.checkedConnections(node).incidentEdges();
    }
    
    @Override
    public EndpointPair<N> incidentNodes(final E edge) {
        final N nodeU = this.checkedReferenceNode(edge);
        final N nodeV = Objects.requireNonNull(this.nodeConnections.get(nodeU)).adjacentNode(edge);
        return EndpointPair.of(this, nodeU, nodeV);
    }
    
    @Override
    public Set<N> adjacentNodes(final N node) {
        return this.checkedConnections(node).adjacentNodes();
    }
    
    @Override
    public Set<E> edgesConnecting(final N nodeU, final N nodeV) {
        final NetworkConnections<N, E> connectionsU = this.checkedConnections(nodeU);
        if (!this.allowsSelfLoops && nodeU == nodeV) {
            return (Set<E>)ImmutableSet.of();
        }
        Preconditions.checkArgument(this.containsNode(nodeV), "Node %s is not an element of this graph.", nodeV);
        return connectionsU.edgesConnecting(nodeV);
    }
    
    @Override
    public Set<E> inEdges(final N node) {
        return this.checkedConnections(node).inEdges();
    }
    
    @Override
    public Set<E> outEdges(final N node) {
        return this.checkedConnections(node).outEdges();
    }
    
    @Override
    public Set<N> predecessors(final N node) {
        return this.checkedConnections(node).predecessors();
    }
    
    @Override
    public Set<N> successors(final N node) {
        return this.checkedConnections(node).successors();
    }
    
    final NetworkConnections<N, E> checkedConnections(final N node) {
        final NetworkConnections<N, E> connections = this.nodeConnections.get(node);
        if (connections == null) {
            Preconditions.checkNotNull(node);
            throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", node));
        }
        return connections;
    }
    
    final N checkedReferenceNode(final E edge) {
        final N referenceNode = this.edgeToReferenceNode.get(edge);
        if (referenceNode == null) {
            Preconditions.checkNotNull(edge);
            throw new IllegalArgumentException(String.format("Edge %s is not an element of this graph.", edge));
        }
        return referenceNode;
    }
    
    final boolean containsNode(final N node) {
        return this.nodeConnections.containsKey(node);
    }
    
    final boolean containsEdge(final E edge) {
        return this.edgeToReferenceNode.containsKey(edge);
    }
}
