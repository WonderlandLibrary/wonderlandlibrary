// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import java.util.Objects;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;

@ElementTypesAreNonnullByDefault
final class StandardMutableNetwork<N, E> extends StandardNetwork<N, E> implements MutableNetwork<N, E>
{
    StandardMutableNetwork(final NetworkBuilder<? super N, ? super E> builder) {
        super(builder);
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
    private NetworkConnections<N, E> addNodeInternal(final N node) {
        final NetworkConnections<N, E> connections = this.newConnections();
        Preconditions.checkState(this.nodeConnections.put(node, connections) == null);
        return connections;
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean addEdge(final N nodeU, final N nodeV, final E edge) {
        Preconditions.checkNotNull(nodeU, (Object)"nodeU");
        Preconditions.checkNotNull(nodeV, (Object)"nodeV");
        Preconditions.checkNotNull(edge, (Object)"edge");
        if (this.containsEdge(edge)) {
            final EndpointPair<N> existingIncidentNodes = this.incidentNodes(edge);
            final EndpointPair<N> newIncidentNodes = EndpointPair.of(this, nodeU, nodeV);
            Preconditions.checkArgument(existingIncidentNodes.equals(newIncidentNodes), "Edge %s already exists between the following nodes: %s, so it cannot be reused to connect the following nodes: %s.", edge, existingIncidentNodes, newIncidentNodes);
            return false;
        }
        NetworkConnections<N, E> connectionsU = this.nodeConnections.get(nodeU);
        if (!this.allowsParallelEdges()) {
            Preconditions.checkArgument(connectionsU == null || !connectionsU.successors().contains(nodeV), "Nodes %s and %s are already connected by a different edge. To construct a graph that allows parallel edges, call allowsParallelEdges(true) on the Builder.", nodeU, nodeV);
        }
        final boolean isSelfLoop = nodeU.equals(nodeV);
        if (!this.allowsSelfLoops()) {
            Preconditions.checkArgument(!isSelfLoop, "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", nodeU);
        }
        if (connectionsU == null) {
            connectionsU = this.addNodeInternal(nodeU);
        }
        connectionsU.addOutEdge(edge, nodeV);
        NetworkConnections<N, E> connectionsV = this.nodeConnections.get(nodeV);
        if (connectionsV == null) {
            connectionsV = this.addNodeInternal(nodeV);
        }
        connectionsV.addInEdge(edge, nodeU, isSelfLoop);
        this.edgeToReferenceNode.put(edge, nodeU);
        return true;
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean addEdge(final EndpointPair<N> endpoints, final E edge) {
        this.validateEndpoints(endpoints);
        return this.addEdge(endpoints.nodeU(), endpoints.nodeV(), edge);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean removeNode(final N node) {
        Preconditions.checkNotNull(node, (Object)"node");
        final NetworkConnections<N, E> connections = this.nodeConnections.get(node);
        if (connections == null) {
            return false;
        }
        for (final E edge : ImmutableList.copyOf((Collection<? extends E>)connections.incidentEdges())) {
            this.removeEdge(edge);
        }
        this.nodeConnections.remove(node);
        return true;
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean removeEdge(final E edge) {
        Preconditions.checkNotNull(edge, (Object)"edge");
        final N nodeU = this.edgeToReferenceNode.get(edge);
        if (nodeU == null) {
            return false;
        }
        final NetworkConnections<N, E> connectionsU = Objects.requireNonNull(this.nodeConnections.get(nodeU));
        final N nodeV = connectionsU.adjacentNode(edge);
        final NetworkConnections<N, E> connectionsV = Objects.requireNonNull(this.nodeConnections.get(nodeV));
        connectionsU.removeOutEdge(edge);
        connectionsV.removeInEdge(edge, this.allowsSelfLoops() && nodeU.equals(nodeV));
        this.edgeToReferenceNode.remove(edge);
        return true;
    }
    
    private NetworkConnections<N, E> newConnections() {
        return (NetworkConnections<N, E>)(this.isDirected() ? (this.allowsParallelEdges() ? DirectedMultiNetworkConnections.of() : DirectedNetworkConnections.of()) : (this.allowsParallelEdges() ? UndirectedMultiNetworkConnections.of() : UndirectedNetworkConnections.of()));
    }
}
