// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import java.util.Objects;
import java.util.Collections;
import javax.annotation.CheckForNull;
import com.google.common.math.IntMath;
import java.util.Iterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import com.google.common.collect.Sets;
import java.util.Set;
import com.google.common.base.Preconditions;
import java.util.Map;

@ElementTypesAreNonnullByDefault
abstract class AbstractDirectedNetworkConnections<N, E> implements NetworkConnections<N, E>
{
    final Map<E, N> inEdgeMap;
    final Map<E, N> outEdgeMap;
    private int selfLoopCount;
    
    AbstractDirectedNetworkConnections(final Map<E, N> inEdgeMap, final Map<E, N> outEdgeMap, final int selfLoopCount) {
        this.inEdgeMap = Preconditions.checkNotNull(inEdgeMap);
        this.outEdgeMap = Preconditions.checkNotNull(outEdgeMap);
        this.selfLoopCount = Graphs.checkNonNegative(selfLoopCount);
        Preconditions.checkState(selfLoopCount <= inEdgeMap.size() && selfLoopCount <= outEdgeMap.size());
    }
    
    @Override
    public Set<N> adjacentNodes() {
        return (Set<N>)Sets.union(this.predecessors(), this.successors());
    }
    
    @Override
    public Set<E> incidentEdges() {
        return new AbstractSet<E>() {
            @Override
            public UnmodifiableIterator<E> iterator() {
                final Iterable<E> incidentEdges = (Iterable<E>)((AbstractDirectedNetworkConnections.this.selfLoopCount == 0) ? Iterables.concat((Iterable<?>)AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), (Iterable<?>)AbstractDirectedNetworkConnections.this.outEdgeMap.keySet()) : Sets.union((Set<?>)AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), (Set<?>)AbstractDirectedNetworkConnections.this.outEdgeMap.keySet()));
                return Iterators.unmodifiableIterator((Iterator<? extends E>)incidentEdges.iterator());
            }
            
            @Override
            public int size() {
                return IntMath.saturatedAdd(AbstractDirectedNetworkConnections.this.inEdgeMap.size(), AbstractDirectedNetworkConnections.this.outEdgeMap.size() - AbstractDirectedNetworkConnections.this.selfLoopCount);
            }
            
            @Override
            public boolean contains(@CheckForNull final Object obj) {
                return AbstractDirectedNetworkConnections.this.inEdgeMap.containsKey(obj) || AbstractDirectedNetworkConnections.this.outEdgeMap.containsKey(obj);
            }
        };
    }
    
    @Override
    public Set<E> inEdges() {
        return Collections.unmodifiableSet((Set<? extends E>)this.inEdgeMap.keySet());
    }
    
    @Override
    public Set<E> outEdges() {
        return Collections.unmodifiableSet((Set<? extends E>)this.outEdgeMap.keySet());
    }
    
    @Override
    public N adjacentNode(final E edge) {
        return Objects.requireNonNull(this.outEdgeMap.get(edge));
    }
    
    @Override
    public N removeInEdge(final E edge, final boolean isSelfLoop) {
        if (isSelfLoop) {
            Graphs.checkNonNegative(--this.selfLoopCount);
        }
        final N previousNode = this.inEdgeMap.remove(edge);
        return Objects.requireNonNull(previousNode);
    }
    
    @Override
    public N removeOutEdge(final E edge) {
        final N previousNode = this.outEdgeMap.remove(edge);
        return Objects.requireNonNull(previousNode);
    }
    
    @Override
    public void addInEdge(final E edge, final N node, final boolean isSelfLoop) {
        Preconditions.checkNotNull(edge);
        Preconditions.checkNotNull(node);
        if (isSelfLoop) {
            Graphs.checkPositive(++this.selfLoopCount);
        }
        final N previousNode = this.inEdgeMap.put(edge, node);
        Preconditions.checkState(previousNode == null);
    }
    
    @Override
    public void addOutEdge(final E edge, final N node) {
        Preconditions.checkNotNull(edge);
        Preconditions.checkNotNull(node);
        final N previousNode = this.outEdgeMap.put(edge, node);
        Preconditions.checkState(previousNode == null);
    }
}
