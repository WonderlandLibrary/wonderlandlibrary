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
final class DirectedMultiNetworkConnections<N, E> extends AbstractDirectedNetworkConnections<N, E>
{
    @CheckForNull
    @LazyInit
    private transient Reference<Multiset<N>> predecessorsReference;
    @CheckForNull
    @LazyInit
    private transient Reference<Multiset<N>> successorsReference;
    
    private DirectedMultiNetworkConnections(final Map<E, N> inEdges, final Map<E, N> outEdges, final int selfLoopCount) {
        super(inEdges, outEdges, selfLoopCount);
    }
    
    static <N, E> DirectedMultiNetworkConnections<N, E> of() {
        return new DirectedMultiNetworkConnections<N, E>(new HashMap<E, N>(2, 1.0f), new HashMap<E, N>(2, 1.0f), 0);
    }
    
    static <N, E> DirectedMultiNetworkConnections<N, E> ofImmutable(final Map<E, N> inEdges, final Map<E, N> outEdges, final int selfLoopCount) {
        return new DirectedMultiNetworkConnections<N, E>((Map<E, N>)ImmutableMap.copyOf((Map<?, ?>)inEdges), (Map<E, N>)ImmutableMap.copyOf((Map<?, ?>)outEdges), selfLoopCount);
    }
    
    @Override
    public Set<N> predecessors() {
        return Collections.unmodifiableSet((Set<? extends N>)this.predecessorsMultiset().elementSet());
    }
    
    private Multiset<N> predecessorsMultiset() {
        Multiset<N> predecessors = getReference(this.predecessorsReference);
        if (predecessors == null) {
            predecessors = (Multiset<N>)HashMultiset.create((Iterable<?>)this.inEdgeMap.values());
            this.predecessorsReference = new SoftReference<Multiset<N>>(predecessors);
        }
        return predecessors;
    }
    
    @Override
    public Set<N> successors() {
        return Collections.unmodifiableSet((Set<? extends N>)this.successorsMultiset().elementSet());
    }
    
    private Multiset<N> successorsMultiset() {
        Multiset<N> successors = getReference(this.successorsReference);
        if (successors == null) {
            successors = (Multiset<N>)HashMultiset.create((Iterable<?>)this.outEdgeMap.values());
            this.successorsReference = new SoftReference<Multiset<N>>(successors);
        }
        return successors;
    }
    
    @Override
    public Set<E> edgesConnecting(final N node) {
        return new MultiEdgesConnecting<E>(this.outEdgeMap, node) {
            @Override
            public int size() {
                return DirectedMultiNetworkConnections.this.successorsMultiset().count(node);
            }
        };
    }
    
    @Override
    public N removeInEdge(final E edge, final boolean isSelfLoop) {
        final N node = super.removeInEdge(edge, isSelfLoop);
        final Multiset<N> predecessors = getReference(this.predecessorsReference);
        if (predecessors != null) {
            Preconditions.checkState(predecessors.remove(node));
        }
        return node;
    }
    
    @Override
    public N removeOutEdge(final E edge) {
        final N node = super.removeOutEdge(edge);
        final Multiset<N> successors = getReference(this.successorsReference);
        if (successors != null) {
            Preconditions.checkState(successors.remove(node));
        }
        return node;
    }
    
    @Override
    public void addInEdge(final E edge, final N node, final boolean isSelfLoop) {
        super.addInEdge(edge, node, isSelfLoop);
        final Multiset<N> predecessors = getReference(this.predecessorsReference);
        if (predecessors != null) {
            Preconditions.checkState(predecessors.add(node));
        }
    }
    
    @Override
    public void addOutEdge(final E edge, final N node) {
        super.addOutEdge(edge, node);
        final Multiset<N> successors = getReference(this.successorsReference);
        if (successors != null) {
            Preconditions.checkState(successors.add(node));
        }
    }
    
    @CheckForNull
    private static <T> T getReference(@CheckForNull final Reference<T> reference) {
        return (reference == null) ? null : reference.get();
    }
}
