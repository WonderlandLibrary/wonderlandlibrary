// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.lang.ref.SoftReference;
import com.google.common.collect.HashMultiset;
import java.util.Collections;
import java.util.Set;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import com.google.errorprone.annotations.concurrent.LazyInit;
import javax.annotation.CheckForNull;
import com.google.common.collect.Multiset;
import java.lang.ref.Reference;

@ElementTypesAreNonnullByDefault
final class UndirectedMultiNetworkConnections<N, E> extends AbstractUndirectedNetworkConnections<N, E>
{
    @CheckForNull
    @LazyInit
    private transient Reference<Multiset<N>> adjacentNodesReference;
    
    private UndirectedMultiNetworkConnections(final Map<E, N> incidentEdges) {
        super(incidentEdges);
    }
    
    static <N, E> UndirectedMultiNetworkConnections<N, E> of() {
        return new UndirectedMultiNetworkConnections<N, E>(new HashMap<E, N>(2, 1.0f));
    }
    
    static <N, E> UndirectedMultiNetworkConnections<N, E> ofImmutable(final Map<E, N> incidentEdges) {
        return new UndirectedMultiNetworkConnections<N, E>((Map<E, N>)ImmutableMap.copyOf((Map<?, ?>)incidentEdges));
    }
    
    @Override
    public Set<N> adjacentNodes() {
        return Collections.unmodifiableSet((Set<? extends N>)this.adjacentNodesMultiset().elementSet());
    }
    
    private Multiset<N> adjacentNodesMultiset() {
        Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
        if (adjacentNodes == null) {
            adjacentNodes = (Multiset<N>)HashMultiset.create((Iterable<?>)this.incidentEdgeMap.values());
            this.adjacentNodesReference = new SoftReference<Multiset<N>>(adjacentNodes);
        }
        return adjacentNodes;
    }
    
    @Override
    public Set<E> edgesConnecting(final N node) {
        return new MultiEdgesConnecting<E>(this.incidentEdgeMap, node) {
            @Override
            public int size() {
                return UndirectedMultiNetworkConnections.this.adjacentNodesMultiset().count(node);
            }
        };
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
        final N node = super.removeOutEdge(edge);
        final Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
        if (adjacentNodes != null) {
            Preconditions.checkState(adjacentNodes.remove(node));
        }
        return node;
    }
    
    @Override
    public void addInEdge(final E edge, final N node, final boolean isSelfLoop) {
        if (!isSelfLoop) {
            this.addOutEdge(edge, node);
        }
    }
    
    @Override
    public void addOutEdge(final E edge, final N node) {
        super.addOutEdge(edge, node);
        final Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
        if (adjacentNodes != null) {
            Preconditions.checkState(adjacentNodes.add(node));
        }
    }
    
    @CheckForNull
    private static <T> T getReference(@CheckForNull final Reference<T> reference) {
        return (reference == null) ? null : reference.get();
    }
}
