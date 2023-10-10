// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import java.util.Collections;
import java.util.Set;
import me.gong.mcleaks.util.google.common.collect.ImmutableMap;
import java.util.HashMap;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Map;

final class UndirectedGraphConnections<N, V> implements GraphConnections<N, V>
{
    private final Map<N, V> adjacentNodeValues;
    
    private UndirectedGraphConnections(final Map<N, V> adjacentNodeValues) {
        this.adjacentNodeValues = Preconditions.checkNotNull(adjacentNodeValues);
    }
    
    static <N, V> UndirectedGraphConnections<N, V> of() {
        return new UndirectedGraphConnections<N, V>(new HashMap<N, V>(2, 1.0f));
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
    public V value(final Object node) {
        return this.adjacentNodeValues.get(node);
    }
    
    @Override
    public void removePredecessor(final Object node) {
        final V unused = this.removeSuccessor(node);
    }
    
    @Override
    public V removeSuccessor(final Object node) {
        return this.adjacentNodeValues.remove(node);
    }
    
    @Override
    public void addPredecessor(final N node, final V value) {
        final V unused = this.addSuccessor(node, value);
    }
    
    @Override
    public V addSuccessor(final N node, final V value) {
        return this.adjacentNodeValues.put(node, value);
    }
}
