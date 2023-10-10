// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import javax.annotation.CheckForNull;
import com.google.common.collect.Iterators;
import com.google.common.base.Function;
import java.util.Iterator;
import java.util.Collections;
import java.util.Set;
import com.google.common.collect.ImmutableMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import com.google.common.base.Preconditions;
import java.util.Map;

@ElementTypesAreNonnullByDefault
final class UndirectedGraphConnections<N, V> implements GraphConnections<N, V>
{
    private final Map<N, V> adjacentNodeValues;
    
    private UndirectedGraphConnections(final Map<N, V> adjacentNodeValues) {
        this.adjacentNodeValues = Preconditions.checkNotNull(adjacentNodeValues);
    }
    
    static <N, V> UndirectedGraphConnections<N, V> of(final ElementOrder<N> incidentEdgeOrder) {
        switch (incidentEdgeOrder.type()) {
            case UNORDERED: {
                return new UndirectedGraphConnections<N, V>(new HashMap<N, V>(2, 1.0f));
            }
            case STABLE: {
                return new UndirectedGraphConnections<N, V>(new LinkedHashMap<N, V>(2, 1.0f));
            }
            default: {
                throw new AssertionError(incidentEdgeOrder.type());
            }
        }
    }
    
    static <N, V> UndirectedGraphConnections<N, V> ofImmutable(final Map<N, V> adjacentNodeValues) {
        return new UndirectedGraphConnections<N, V>((Map<N, V>)ImmutableMap.copyOf((Map<?, ?>)adjacentNodeValues));
    }
    
    @Override
    public Set<N> adjacentNodes() {
        return Collections.unmodifiableSet((Set<? extends N>)this.adjacentNodeValues.keySet());
    }
    
    @Override
    public Set<N> predecessors() {
        return this.adjacentNodes();
    }
    
    @Override
    public Set<N> successors() {
        return this.adjacentNodes();
    }
    
    @Override
    public Iterator<EndpointPair<N>> incidentEdgeIterator(final N thisNode) {
        return Iterators.transform(this.adjacentNodeValues.keySet().iterator(), (Function<? super N, ? extends EndpointPair<N>>)new Function<N, EndpointPair<N>>(this) {
            @Override
            public EndpointPair<N> apply(final N incidentNode) {
                return EndpointPair.unordered(thisNode, incidentNode);
            }
        });
    }
    
    @CheckForNull
    @Override
    public V value(final N node) {
        return this.adjacentNodeValues.get(node);
    }
    
    @Override
    public void removePredecessor(final N node) {
        final V unused = this.removeSuccessor(node);
    }
    
    @CheckForNull
    @Override
    public V removeSuccessor(final N node) {
        return this.adjacentNodeValues.remove(node);
    }
    
    @Override
    public void addPredecessor(final N node, final V value) {
        final V unused = this.addSuccessor(node, value);
    }
    
    @CheckForNull
    @Override
    public V addSuccessor(final N node, final V value) {
        return this.adjacentNodeValues.put(node, value);
    }
}
