// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import javax.annotation.CheckForNull;
import java.util.Optional;
import java.util.Set;

@ElementTypesAreNonnullByDefault
abstract class ForwardingNetwork<N, E> extends AbstractNetwork<N, E>
{
    abstract Network<N, E> delegate();
    
    @Override
    public Set<N> nodes() {
        return this.delegate().nodes();
    }
    
    @Override
    public Set<E> edges() {
        return this.delegate().edges();
    }
    
    @Override
    public boolean isDirected() {
        return this.delegate().isDirected();
    }
    
    @Override
    public boolean allowsParallelEdges() {
        return this.delegate().allowsParallelEdges();
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
    public ElementOrder<E> edgeOrder() {
        return this.delegate().edgeOrder();
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
    public Set<E> incidentEdges(final N node) {
        return this.delegate().incidentEdges(node);
    }
    
    @Override
    public Set<E> inEdges(final N node) {
        return this.delegate().inEdges(node);
    }
    
    @Override
    public Set<E> outEdges(final N node) {
        return this.delegate().outEdges(node);
    }
    
    @Override
    public EndpointPair<N> incidentNodes(final E edge) {
        return this.delegate().incidentNodes(edge);
    }
    
    @Override
    public Set<E> adjacentEdges(final E edge) {
        return this.delegate().adjacentEdges(edge);
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
    public Set<E> edgesConnecting(final N nodeU, final N nodeV) {
        return this.delegate().edgesConnecting(nodeU, nodeV);
    }
    
    @Override
    public Set<E> edgesConnecting(final EndpointPair<N> endpoints) {
        return this.delegate().edgesConnecting(endpoints);
    }
    
    @Override
    public Optional<E> edgeConnecting(final N nodeU, final N nodeV) {
        return this.delegate().edgeConnecting(nodeU, nodeV);
    }
    
    @Override
    public Optional<E> edgeConnecting(final EndpointPair<N> endpoints) {
        return this.delegate().edgeConnecting(endpoints);
    }
    
    @CheckForNull
    @Override
    public E edgeConnectingOrNull(final N nodeU, final N nodeV) {
        return this.delegate().edgeConnectingOrNull(nodeU, nodeV);
    }
    
    @CheckForNull
    @Override
    public E edgeConnectingOrNull(final EndpointPair<N> endpoints) {
        return this.delegate().edgeConnectingOrNull(endpoints);
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
