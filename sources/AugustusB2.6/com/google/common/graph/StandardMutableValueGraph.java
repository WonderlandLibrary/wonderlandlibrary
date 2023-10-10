// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import java.util.Iterator;
import java.util.Objects;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;

@ElementTypesAreNonnullByDefault
final class StandardMutableValueGraph<N, V> extends StandardValueGraph<N, V> implements MutableValueGraph<N, V>
{
    private final ElementOrder<N> incidentEdgeOrder;
    
    StandardMutableValueGraph(final AbstractGraphBuilder<? super N> builder) {
        super(builder);
        this.incidentEdgeOrder = builder.incidentEdgeOrder.cast();
    }
    
    @Override
    public ElementOrder<N> incidentEdgeOrder() {
        return this.incidentEdgeOrder;
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean addNode(final N node) {
        Preconditions.checkNotNull(node, (Object)"node");
        if (this.containsNode(node)) {
            return false;
        }
        this.addNodeInternal(node);
        return true;
    }
    
    @CanIgnoreReturnValue
    private GraphConnections<N, V> addNodeInternal(final N node) {
        final GraphConnections<N, V> connections = this.newConnections();
        Preconditions.checkState(this.nodeConnections.put(node, connections) == null);
        return connections;
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V putEdgeValue(final N nodeU, final N nodeV, final V value) {
        Preconditions.checkNotNull(nodeU, (Object)"nodeU");
        Preconditions.checkNotNull(nodeV, (Object)"nodeV");
        Preconditions.checkNotNull(value, (Object)"value");
        if (!this.allowsSelfLoops()) {
            Preconditions.checkArgument(!nodeU.equals(nodeV), "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", nodeU);
        }
        GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
        if (connectionsU == null) {
            connectionsU = this.addNodeInternal(nodeU);
        }
        final V previousValue = connectionsU.addSuccessor(nodeV, value);
        GraphConnections<N, V> connectionsV = this.nodeConnections.get(nodeV);
        if (connectionsV == null) {
            connectionsV = this.addNodeInternal(nodeV);
        }
        connectionsV.addPredecessor(nodeU, value);
        if (previousValue == null) {
            Graphs.checkPositive(++this.edgeCount);
        }
        return previousValue;
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V putEdgeValue(final EndpointPair<N> endpoints, final V value) {
        this.validateEndpoints(endpoints);
        return this.putEdgeValue(endpoints.nodeU(), endpoints.nodeV(), value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean removeNode(final N node) {
        Preconditions.checkNotNull(node, (Object)"node");
        final GraphConnections<N, V> connections = this.nodeConnections.get(node);
        if (connections == null) {
            return false;
        }
        if (this.allowsSelfLoops() && connections.removeSuccessor(node) != null) {
            connections.removePredecessor(node);
            --this.edgeCount;
        }
        for (final N successor : connections.successors()) {
            Objects.requireNonNull(this.nodeConnections.getWithoutCaching(successor)).removePredecessor(node);
            --this.edgeCount;
        }
        if (this.isDirected()) {
            for (final N predecessor : connections.predecessors()) {
                Preconditions.checkState(Objects.requireNonNull(this.nodeConnections.getWithoutCaching(predecessor)).removeSuccessor(node) != null);
                --this.edgeCount;
            }
        }
        this.nodeConnections.remove(node);
        Graphs.checkNonNegative(this.edgeCount);
        return true;
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V removeEdge(final N nodeU, final N nodeV) {
        Preconditions.checkNotNull(nodeU, (Object)"nodeU");
        Preconditions.checkNotNull(nodeV, (Object)"nodeV");
        final GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
        final GraphConnections<N, V> connectionsV = this.nodeConnections.get(nodeV);
        if (connectionsU == null || connectionsV == null) {
            return null;
        }
        final V previousValue = connectionsU.removeSuccessor(nodeV);
        if (previousValue != null) {
            connectionsV.removePredecessor(nodeU);
            Graphs.checkNonNegative(--this.edgeCount);
        }
        return previousValue;
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V removeEdge(final EndpointPair<N> endpoints) {
        this.validateEndpoints(endpoints);
        return this.removeEdge(endpoints.nodeU(), endpoints.nodeV());
    }
    
    private GraphConnections<N, V> newConnections() {
        return (GraphConnections<N, V>)(this.isDirected() ? DirectedGraphConnections.of(this.incidentEdgeOrder) : UndirectedGraphConnections.of(this.incidentEdgeOrder));
    }
}
