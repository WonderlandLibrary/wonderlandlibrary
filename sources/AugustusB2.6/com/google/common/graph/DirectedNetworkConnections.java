// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import java.util.Collections;
import com.google.common.collect.BiMap;
import java.util.Set;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.HashBiMap;
import java.util.Map;

@ElementTypesAreNonnullByDefault
final class DirectedNetworkConnections<N, E> extends AbstractDirectedNetworkConnections<N, E>
{
    DirectedNetworkConnections(final Map<E, N> inEdgeMap, final Map<E, N> outEdgeMap, final int selfLoopCount) {
        super(inEdgeMap, outEdgeMap, selfLoopCount);
    }
    
    static <N, E> DirectedNetworkConnections<N, E> of() {
        return new DirectedNetworkConnections<N, E>((Map<E, N>)HashBiMap.create(2), (Map<E, N>)HashBiMap.create(2), 0);
    }
    
    static <N, E> DirectedNetworkConnections<N, E> ofImmutable(final Map<E, N> inEdges, final Map<E, N> outEdges, final int selfLoopCount) {
        return new DirectedNetworkConnections<N, E>((Map<E, N>)ImmutableBiMap.copyOf((Map<?, ?>)inEdges), (Map<E, N>)ImmutableBiMap.copyOf((Map<?, ?>)outEdges), selfLoopCount);
    }
    
    @Override
    public Set<N> predecessors() {
        return Collections.unmodifiableSet(((BiMap)this.inEdgeMap).values());
    }
    
    @Override
    public Set<N> successors() {
        return Collections.unmodifiableSet(((BiMap)this.outEdgeMap).values());
    }
    
    @Override
    public Set<E> edgesConnecting(final N node) {
        return new EdgesConnecting<E>(((BiMap)this.outEdgeMap).inverse(), node);
    }
}
