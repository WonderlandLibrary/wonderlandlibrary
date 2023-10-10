// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import java.util.concurrent.atomic.AtomicBoolean;
import com.google.common.collect.Iterators;
import com.google.common.collect.AbstractIterator;
import java.util.HashSet;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Set;
import java.util.Iterator;
import com.google.common.collect.ImmutableList;
import com.google.common.base.Function;
import java.util.HashMap;
import java.util.ArrayList;
import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

@ElementTypesAreNonnullByDefault
final class DirectedGraphConnections<N, V> implements GraphConnections<N, V>
{
    private static final Object PRED;
    private final Map<N, Object> adjacentNodeValues;
    @CheckForNull
    private final List<NodeConnection<N>> orderedNodeConnections;
    private int predecessorCount;
    private int successorCount;
    
    private DirectedGraphConnections(final Map<N, Object> adjacentNodeValues, @CheckForNull final List<NodeConnection<N>> orderedNodeConnections, final int predecessorCount, final int successorCount) {
        this.adjacentNodeValues = Preconditions.checkNotNull(adjacentNodeValues);
        this.orderedNodeConnections = orderedNodeConnections;
        this.predecessorCount = Graphs.checkNonNegative(predecessorCount);
        this.successorCount = Graphs.checkNonNegative(successorCount);
        Preconditions.checkState(predecessorCount <= adjacentNodeValues.size() && successorCount <= adjacentNodeValues.size());
    }
    
    static <N, V> DirectedGraphConnections<N, V> of(final ElementOrder<N> incidentEdgeOrder) {
        final int initialCapacity = 4;
        List<NodeConnection<N>> orderedNodeConnections = null;
        switch (incidentEdgeOrder.type()) {
            case UNORDERED: {
                orderedNodeConnections = null;
                break;
            }
            case STABLE: {
                orderedNodeConnections = new ArrayList<NodeConnection<N>>();
                break;
            }
            default: {
                throw new AssertionError(incidentEdgeOrder.type());
            }
        }
        return new DirectedGraphConnections<N, V>(new HashMap<N, Object>(initialCapacity, 1.0f), orderedNodeConnections, 0, 0);
    }
    
    static <N, V> DirectedGraphConnections<N, V> ofImmutable(final N thisNode, final Iterable<EndpointPair<N>> incidentEdges, final Function<N, V> successorNodeToValueFn) {
        Preconditions.checkNotNull(thisNode);
        Preconditions.checkNotNull(successorNodeToValueFn);
        final Map<N, Object> adjacentNodeValues = new HashMap<N, Object>();
        final ImmutableList.Builder<NodeConnection<N>> orderedNodeConnectionsBuilder = ImmutableList.builder();
        int predecessorCount = 0;
        int successorCount = 0;
        for (final EndpointPair<N> incidentEdge : incidentEdges) {
            if (incidentEdge.nodeU().equals(thisNode) && incidentEdge.nodeV().equals(thisNode)) {
                adjacentNodeValues.put(thisNode, new PredAndSucc(successorNodeToValueFn.apply(thisNode)));
                orderedNodeConnectionsBuilder.add(new NodeConnection.Pred<N>(thisNode));
                orderedNodeConnectionsBuilder.add(new NodeConnection.Succ<N>(thisNode));
                ++predecessorCount;
                ++successorCount;
            }
            else if (incidentEdge.nodeV().equals(thisNode)) {
                final N predecessor = incidentEdge.nodeU();
                final Object existingValue = adjacentNodeValues.put(predecessor, DirectedGraphConnections.PRED);
                if (existingValue != null) {
                    adjacentNodeValues.put(predecessor, new PredAndSucc(existingValue));
                }
                orderedNodeConnectionsBuilder.add(new NodeConnection.Pred<N>(predecessor));
                ++predecessorCount;
            }
            else {
                Preconditions.checkArgument(incidentEdge.nodeU().equals(thisNode));
                final N successor = incidentEdge.nodeV();
                final V value = successorNodeToValueFn.apply(successor);
                final Object existingValue2 = adjacentNodeValues.put(successor, value);
                if (existingValue2 != null) {
                    Preconditions.checkArgument(existingValue2 == DirectedGraphConnections.PRED);
                    adjacentNodeValues.put(successor, new PredAndSucc(value));
                }
                orderedNodeConnectionsBuilder.add(new NodeConnection.Succ<N>(successor));
                ++successorCount;
            }
        }
        return new DirectedGraphConnections<N, V>(adjacentNodeValues, orderedNodeConnectionsBuilder.build(), predecessorCount, successorCount);
    }
    
    @Override
    public Set<N> adjacentNodes() {
        if (this.orderedNodeConnections == null) {
            return Collections.unmodifiableSet((Set<? extends N>)this.adjacentNodeValues.keySet());
        }
        return new AbstractSet<N>() {
            @Override
            public UnmodifiableIterator<N> iterator() {
                final Iterator<NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                final Set<N> seenNodes = new HashSet<N>();
                return new AbstractIterator<N>(this) {
                    @CheckForNull
                    @Override
                    protected N computeNext() {
                        while (nodeConnections.hasNext()) {
                            final NodeConnection<N> nodeConnection = nodeConnections.next();
                            final boolean added = seenNodes.add(nodeConnection.node);
                            if (added) {
                                return nodeConnection.node;
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            @Override
            public int size() {
                return DirectedGraphConnections.this.adjacentNodeValues.size();
            }
            
            @Override
            public boolean contains(@CheckForNull final Object obj) {
                return DirectedGraphConnections.this.adjacentNodeValues.containsKey(obj);
            }
        };
    }
    
    @Override
    public Set<N> predecessors() {
        return new AbstractSet<N>() {
            @Override
            public UnmodifiableIterator<N> iterator() {
                if (DirectedGraphConnections.this.orderedNodeConnections == null) {
                    final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
                    return new AbstractIterator<N>(this) {
                        @CheckForNull
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
                final Iterator<NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                return new AbstractIterator<N>(this) {
                    @CheckForNull
                    @Override
                    protected N computeNext() {
                        while (nodeConnections.hasNext()) {
                            final NodeConnection<N> nodeConnection = nodeConnections.next();
                            if (nodeConnection instanceof NodeConnection.Pred) {
                                return nodeConnection.node;
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
            public boolean contains(@CheckForNull final Object obj) {
                return isPredecessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
            }
        };
    }
    
    @Override
    public Set<N> successors() {
        return new AbstractSet<N>() {
            @Override
            public UnmodifiableIterator<N> iterator() {
                if (DirectedGraphConnections.this.orderedNodeConnections == null) {
                    final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
                    return new AbstractIterator<N>(this) {
                        @CheckForNull
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
                final Iterator<NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
                return new AbstractIterator<N>(this) {
                    @CheckForNull
                    @Override
                    protected N computeNext() {
                        while (nodeConnections.hasNext()) {
                            final NodeConnection<N> nodeConnection = nodeConnections.next();
                            if (nodeConnection instanceof NodeConnection.Succ) {
                                return nodeConnection.node;
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
            public boolean contains(@CheckForNull final Object obj) {
                return isSuccessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
            }
        };
    }
    
    @Override
    public Iterator<EndpointPair<N>> incidentEdgeIterator(final N thisNode) {
        Preconditions.checkNotNull(thisNode);
        Iterator<EndpointPair<N>> resultWithDoubleSelfLoop;
        if (this.orderedNodeConnections == null) {
            resultWithDoubleSelfLoop = Iterators.concat(Iterators.transform(this.predecessors().iterator(), (Function<? super N, ? extends EndpointPair<N>>)new Function<N, EndpointPair<N>>(this) {
                @Override
                public EndpointPair<N> apply(final N predecessor) {
                    return EndpointPair.ordered(predecessor, thisNode);
                }
            }), Iterators.transform(this.successors().iterator(), (Function<? super N, ? extends EndpointPair<N>>)new Function<N, EndpointPair<N>>(this) {
                @Override
                public EndpointPair<N> apply(final N successor) {
                    return EndpointPair.ordered(thisNode, successor);
                }
            }));
        }
        else {
            resultWithDoubleSelfLoop = Iterators.transform(this.orderedNodeConnections.iterator(), (Function<? super NodeConnection<N>, ? extends EndpointPair<N>>)new Function<NodeConnection<N>, EndpointPair<N>>(this) {
                @Override
                public EndpointPair<N> apply(final NodeConnection<N> connection) {
                    if (connection instanceof NodeConnection.Succ) {
                        return EndpointPair.ordered(thisNode, connection.node);
                    }
                    return EndpointPair.ordered(connection.node, thisNode);
                }
            });
        }
        final AtomicBoolean alreadySeenSelfLoop = new AtomicBoolean(false);
        return new AbstractIterator<EndpointPair<N>>(this) {
            @CheckForNull
            @Override
            protected EndpointPair<N> computeNext() {
                while (resultWithDoubleSelfLoop.hasNext()) {
                    final EndpointPair<N> edge = resultWithDoubleSelfLoop.next();
                    if (!edge.nodeU().equals(edge.nodeV())) {
                        return edge;
                    }
                    if (!alreadySeenSelfLoop.getAndSet(true)) {
                        return edge;
                    }
                }
                return this.endOfData();
            }
        };
    }
    
    @CheckForNull
    @Override
    public V value(final N node) {
        Preconditions.checkNotNull(node);
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
    public void removePredecessor(final N node) {
        Preconditions.checkNotNull(node);
        final Object previousValue = this.adjacentNodeValues.get(node);
        boolean removedPredecessor;
        if (previousValue == DirectedGraphConnections.PRED) {
            this.adjacentNodeValues.remove(node);
            removedPredecessor = true;
        }
        else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, ((PredAndSucc)previousValue).successorValue);
            removedPredecessor = true;
        }
        else {
            removedPredecessor = false;
        }
        if (removedPredecessor) {
            Graphs.checkNonNegative(--this.predecessorCount);
            if (this.orderedNodeConnections != null) {
                this.orderedNodeConnections.remove(new NodeConnection.Pred(node));
            }
        }
    }
    
    @CheckForNull
    @Override
    public V removeSuccessor(final Object node) {
        Preconditions.checkNotNull(node);
        final Object previousValue = this.adjacentNodeValues.get(node);
        Object removedValue;
        if (previousValue == null || previousValue == DirectedGraphConnections.PRED) {
            removedValue = null;
        }
        else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put((N)node, DirectedGraphConnections.PRED);
            removedValue = ((PredAndSucc)previousValue).successorValue;
        }
        else {
            this.adjacentNodeValues.remove(node);
            removedValue = previousValue;
        }
        if (removedValue != null) {
            Graphs.checkNonNegative(--this.successorCount);
            if (this.orderedNodeConnections != null) {
                this.orderedNodeConnections.remove(new NodeConnection.Succ(node));
            }
        }
        return (V)((removedValue == null) ? null : removedValue);
    }
    
    @Override
    public void addPredecessor(final N node, final V unused) {
        final Object previousValue = this.adjacentNodeValues.put(node, DirectedGraphConnections.PRED);
        boolean addedPredecessor;
        if (previousValue == null) {
            addedPredecessor = true;
        }
        else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, previousValue);
            addedPredecessor = false;
        }
        else if (previousValue != DirectedGraphConnections.PRED) {
            this.adjacentNodeValues.put(node, new PredAndSucc(previousValue));
            addedPredecessor = true;
        }
        else {
            addedPredecessor = false;
        }
        if (addedPredecessor) {
            Graphs.checkPositive(++this.predecessorCount);
            if (this.orderedNodeConnections != null) {
                this.orderedNodeConnections.add(new NodeConnection.Pred<N>(node));
            }
        }
    }
    
    @CheckForNull
    @Override
    public V addSuccessor(final N node, final V value) {
        final Object previousValue = this.adjacentNodeValues.put(node, value);
        Object previousSuccessor;
        if (previousValue == null) {
            previousSuccessor = null;
        }
        else if (previousValue instanceof PredAndSucc) {
            this.adjacentNodeValues.put(node, new PredAndSucc(value));
            previousSuccessor = ((PredAndSucc)previousValue).successorValue;
        }
        else if (previousValue == DirectedGraphConnections.PRED) {
            this.adjacentNodeValues.put(node, new PredAndSucc(value));
            previousSuccessor = null;
        }
        else {
            previousSuccessor = previousValue;
        }
        if (previousSuccessor == null) {
            Graphs.checkPositive(++this.successorCount);
            if (this.orderedNodeConnections != null) {
                this.orderedNodeConnections.add(new NodeConnection.Succ<N>(node));
            }
        }
        return (V)((previousSuccessor == null) ? null : previousSuccessor);
    }
    
    private static boolean isPredecessor(@CheckForNull final Object value) {
        return value == DirectedGraphConnections.PRED || value instanceof PredAndSucc;
    }
    
    private static boolean isSuccessor(@CheckForNull final Object value) {
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
    
    private abstract static class NodeConnection<N>
    {
        final N node;
        
        NodeConnection(final N node) {
            this.node = Preconditions.checkNotNull(node);
        }
        
        static final class Pred<N> extends NodeConnection<N>
        {
            Pred(final N node) {
                super(node);
            }
            
            @Override
            public boolean equals(@CheckForNull final Object that) {
                return that instanceof Pred && this.node.equals(((Pred)that).node);
            }
            
            @Override
            public int hashCode() {
                return Pred.class.hashCode() + this.node.hashCode();
            }
        }
        
        static final class Succ<N> extends NodeConnection<N>
        {
            Succ(final N node) {
                super(node);
            }
            
            @Override
            public boolean equals(@CheckForNull final Object that) {
                return that instanceof Succ && this.node.equals(((Succ)that).node);
            }
            
            @Override
            public int hashCode() {
                return Succ.class.hashCode() + this.node.hashCode();
            }
        }
    }
}
