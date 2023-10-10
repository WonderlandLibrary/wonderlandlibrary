// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import javax.annotation.CheckForNull;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;

@ElementTypesAreNonnullByDefault
class StandardValueGraph<N, V> extends AbstractValueGraph<N, V>
{
    private final boolean isDirected;
    private final boolean allowsSelfLoops;
    private final ElementOrder<N> nodeOrder;
    final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
    long edgeCount;
    
    StandardValueGraph(final AbstractGraphBuilder<? super N> builder) {
        this((AbstractGraphBuilder<? super Object>)builder, builder.nodeOrder.createMap(builder.expectedNodeCount.or(10)), 0L);
    }
    
    StandardValueGraph(final AbstractGraphBuilder<? super N> builder, final Map<N, GraphConnections<N, V>> nodeConnections, final long edgeCount) {
        this.isDirected = builder.directed;
        this.allowsSelfLoops = builder.allowsSelfLoops;
        this.nodeOrder = builder.nodeOrder.cast();
        this.nodeConnections = ((nodeConnections instanceof TreeMap) ? new MapRetrievalCache<N, GraphConnections<N, V>>(nodeConnections) : new MapIteratorCache<N, GraphConnections<N, V>>(nodeConnections));
        this.edgeCount = Graphs.checkNonNegative(edgeCount);
    }
    
    @Override
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }
    
    @Override
    public boolean isDirected() {
        return this.isDirected;
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
    public Set<N> adjacentNodes(final N node) {
        return this.checkedConnections(node).adjacentNodes();
    }
    
    @Override
    public Set<N> predecessors(final N node) {
        return this.checkedConnections(node).predecessors();
    }
    
    @Override
    public Set<N> successors(final N node) {
        return this.checkedConnections(node).successors();
    }
    
    @Override
    public Set<EndpointPair<N>> incidentEdges(final N node) {
        final GraphConnections<N, V> connections = this.checkedConnections(node);
        return (Set<EndpointPair<N>>)new IncidentEdgeSet<N>(this, this, node) {
            @Override
            public Iterator<EndpointPair<N>> iterator() {
                return connections.incidentEdgeIterator(this.node);
            }
        };
    }
    
    @Override
    public boolean hasEdgeConnecting(final N nodeU, final N nodeV) {
        return this.hasEdgeConnectingInternal(Preconditions.checkNotNull(nodeU), Preconditions.checkNotNull(nodeV));
    }
    
    @Override
    public boolean hasEdgeConnecting(final EndpointPair<N> endpoints) {
        Preconditions.checkNotNull(endpoints);
        return this.isOrderingCompatible(endpoints) && this.hasEdgeConnectingInternal(endpoints.nodeU(), endpoints.nodeV());
    }
    
    @CheckForNull
    @Override
    public V edgeValueOrDefault(final N nodeU, final N nodeV, @CheckForNull final V defaultValue) {
        return this.edgeValueOrDefaultInternal(Preconditions.checkNotNull(nodeU), Preconditions.checkNotNull(nodeV), defaultValue);
    }
    
    @CheckForNull
    @Override
    public V edgeValueOrDefault(final EndpointPair<N> endpoints, @CheckForNull final V defaultValue) {
        this.validateEndpoints(endpoints);
        return this.edgeValueOrDefaultInternal(endpoints.nodeU(), endpoints.nodeV(), defaultValue);
    }
    
    @Override
    protected long edgeCount() {
        return this.edgeCount;
    }
    
    private final GraphConnections<N, V> checkedConnections(final N node) {
        final GraphConnections<N, V> connections = this.nodeConnections.get(node);
        if (connections == null) {
            Preconditions.checkNotNull(node);
            final String value = String.valueOf(node);
            throw new IllegalArgumentException(new StringBuilder(38 + String.valueOf(value).length()).append("Node ").append(value).append(" is not an element of this graph.").toString());
        }
        return connections;
    }
    
    final boolean containsNode(@CheckForNull final N node) {
        return this.nodeConnections.containsKey(node);
    }
    
    private final boolean hasEdgeConnectingInternal(final N nodeU, final N nodeV) {
        final GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
        return connectionsU != null && connectionsU.successors().contains(nodeV);
    }
    
    @CheckForNull
    private final V edgeValueOrDefaultInternal(final N nodeU, final N nodeV, @CheckForNull final V defaultValue) {
        final GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
        final V value = (connectionsU == null) ? null : connectionsU.value(nodeV);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
