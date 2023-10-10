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
final class UndirectedNetworkConnections<N, E> extends AbstractUndirectedNetworkConnections<N, E>
{
    UndirectedNetworkConnections(final Map<E, N> incidentEdgeMap) {
        super(incidentEdgeMap);
    }
    
    static <N, E> UndirectedNetworkConnections<N, E> of() {
        return new UndirectedNetworkConnections<N, E>((Map<E, N>)HashBiMap.create(2));
    }
    
    static <N, E> UndirectedNetworkConnections<N, E> ofImmutable(final Map<E, N> incidentEdges) {
        return new UndirectedNetworkConnections<N, E>((Map<E, N>)ImmutableBiMap.copyOf((Map<?, ?>)incidentEdges));
    }
    
    @Override
    public Set<N> adjacentNodes() {
        return Collections.unmodifiableSet(((BiMap)this.incidentEdgeMap).values());
    }
    
    @Override
    public Set<E> edgesConnecting(final N node) {
        return new EdgesConnecting<E>(((BiMap)this.incidentEdgeMap).inverse(), node);
    }
}
