// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.collect.AbstractIterator;
import me.gong.mcleaks.util.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.collect.ImmutableMap;
import java.util.Set;
import java.util.HashMap;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Map;

final class DirectedGraphConnections<N, V> implements GraphConnections<N, V>
{
    private static final Object PRED;
    private final Map<N, Object> adjacentNodeValues;
    private int predecessorCount;
    private int successorCount;
    
    private DirectedGraphConnections(final Map<N, Object> adjacentNodeValues, final int predecessorCount, final int successorCount) {
        this.adjacentNodeValues = Preconditions.checkNotNull(adjacentNodeValues);
        this.predecessorCount = Graphs.checkNonNegative(predecessorCount);
        this.successorCount = Graphs.checkNonNegative(successorCount);
        Preconditions.checkState(predecessorCount <= adjacentNodeValues.size() && successorCount <= adjacentNodeValues.size());
    }
    
    static <N, V> DirectedGraphConnections<N, V> of() {
        final int initialCapacity = 4;
        return new DirectedGraphConnections<N, V>(new HashMap<N, Object>(initialCapacity, 1.0f), 0, 0);
    }
    
    static <N, V> DirectedGraphConnections<N, V> ofImmutable(final Set<N> predecessors, final Map<N, V> successorValues) {
        final Map<N, Object> adjacentNodeValues = new HashMap<N, Object>();
        adjacentNodeValues.putAll((Map<? extends N, ?>)successorValues);
        for (final N predecessor : predecessors) {
            final Object value = adjacentNodeValues.put(predecessor, DirectedGraphConnections.PRED);
            if (value != null) {
                adjacentNodeValues.put(predecessor, new PredAndSucc(value));
            }
        }
        return new DirectedGraphConnections<N, V>((Map<N, Object>)ImmutableMap.copyOf((Map<?, ?>)adjacentNodeValues), predecessors.size(), successorValues.size());
    }
    
    @Override
    public Set<N> adjacentNodes() {
        return Collections.unmodifiableSet((Set<? extends N>)this.adjacentNodeValues.keySet());
    }
    
    @Override
    public Set<N> predecessors() {
        return new AbstractSet<N>() {
            @Override
            public UnmodifiableIterator<N> iterator() {
                final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
                return new AbstractIterator<N>() {
                    @Override
                    protected N computeNext() {
                        while (entries.hasNext()) {
                            final Map.Entry<N, Object> entry = entries.next();
                            if (isPredecessor(entry.getValue())) {
                                return entry.getKey();
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            @Override
            public int size() {
                return DirectedGraphConnections.this.predecessorCount;
            }
            
            @Override
            public boolean contains(@Nullable final Object obj) {
                return isPredecessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
            }
        };
    }
    
    @Override
    public Set<N> successors() {
        return new AbstractSet<N>() {
            @Override
            public UnmodifiableIterator<N> iterator() {
                final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
                return new AbstractIterator<N>() {
                    @Override
                    protected N computeNext() {
                        while (entries.hasNext()) {
                            final Map.Entry<N, Object> entry = entries.next();
                            if (isSuccessor(entry.getValue())) {
                                return entry.getKey();
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            @Override
            public int size() {
                return DirectedGraphConnections.this.successorCount;
            }
            
            @Override
            public boolean contains(@Nullable final Object obj) {
                return isSuccessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
            }
        };
    }
    
    @Override
    public V value(final Object node) {
        final Object value = this.adjacentNodeValues.get(node);
        if (value == DirectedGraphConnections.PRED) {
            return null;
        }
        if (value instanceof PredAndSucc) {
            return (V)((PredAndSucc)value).successorValue;
        }
        return (V)value;
    }
    
    @Override
    public void removePredecessor(final Object node) {
        final Object previousValue = this.adjacentNodeValues.get(node);
        if (previousValue == DirectedGraphConnections.PRED) {
            this.adjacentNodeValues.remove(node);
            Graphs.checkNonNegative(--this.predecessorCount);
        }
        else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put((N)node, ((PredAndSucc)previousValue).successorValue);
            Graphs.checkNonNegative(--this.predecessorCount);
        }
    }
    
    @Override
    public V removeSuccessor(final Object node) {
        final Object previousValue = this.adjacentNodeValues.get(node);
        if (previousValue == null || previousValue == DirectedGraphConnections.PRED) {
            return null;
        }
        if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put((N)node, DirectedGraphConnections.PRED);
            Graphs.checkNonNegative(--this.successorCount);
            return (V)((PredAndSucc)previousValue).successorValue;
        }
        this.adjacentNodeValues.remove(node);
        Graphs.checkNonNegative(--this.successorCount);
        return (V)previousValue;
    }
    
    @Override
    public void addPredecessor(final N node, final V unused) {
        final Object previousValue = this.adjacentNodeValues.put(node, DirectedGraphConnections.PRED);
        if (previousValue == null) {
            Graphs.checkPositive(++this.predecessorCount);
        }
        else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, previousValue);
        }
        else if (previousValue != DirectedGraphConnections.PRED) {
            this.adjacentNodeValues.put(node, new PredAndSucc(previousValue));
            Graphs.checkPositive(++this.predecessorCount);
        }
    }
    
    @Override
    public V addSuccessor(final N node, final V value) {
        final Object previousValue = this.adjacentNodeValues.put(node, value);
        if (previousValue == null) {
            Graphs.checkPositive(++this.successorCount);
            return null;
        }
        if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, new PredAndSucc(value));
            return (V)((PredAndSucc)previousValue).successorValue;
        }
        if (previousValue == DirectedGraphConnections.PRED) {
            this.adjacentNodeValues.put(node, new PredAndSucc(value));
            Graphs.checkPositive(++this.successorCount);
            return null;
        }
        return (V)previousValue;
    }
    
    private static boolean isPredecessor(@Nullable final Object value) {
        return value == DirectedGraphConnections.PRED || value instanceof PredAndSucc;
    }
    
    private static boolean isSuccessor(@Nullable final Object value) {
        return value != DirectedGraphConnections.PRED && value != null;
    }
    
    static {
        PRED = new Object();
    }
    
    private static final class PredAndSucc
    {
        private final Object successorValue;
        
        PredAndSucc(final Object successorValue) {
            this.successorValue = successorValue;
        }
    }
}
