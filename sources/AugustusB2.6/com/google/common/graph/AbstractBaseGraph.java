// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.math.IntMath;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.base.Function;
import javax.annotation.CheckForNull;
import com.google.common.primitives.Ints;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;
import com.google.common.base.Preconditions;

@ElementTypesAreNonnullByDefault
abstract class AbstractBaseGraph<N> implements BaseGraph<N>
{
    protected long edgeCount() {
        long degreeSum = 0L;
        for (final N node : this.nodes()) {
            degreeSum += this.degree(node);
        }
        Preconditions.checkState((degreeSum & 0x1L) == 0x0L);
        return degreeSum >>> 1;
    }
    
    @Override
    public Set<EndpointPair<N>> edges() {
        return new AbstractSet<EndpointPair<N>>() {
            @Override
            public UnmodifiableIterator<EndpointPair<N>> iterator() {
                return (UnmodifiableIterator<EndpointPair<N>>)EndpointPairIterator.of((BaseGraph<Object>)AbstractBaseGraph.this);
            }
            
            @Override
            public int size() {
                return Ints.saturatedCast(AbstractBaseGraph.this.edgeCount());
            }
            
            @Override
            public boolean remove(@CheckForNull final Object o) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean contains(@CheckForNull final Object obj) {
                if (!(obj instanceof EndpointPair)) {
                    return false;
                }
                final EndpointPair<?> endpointPair = (EndpointPair<?>)obj;
                return AbstractBaseGraph.this.isOrderingCompatible(endpointPair) && AbstractBaseGraph.this.nodes().contains(endpointPair.nodeU()) && AbstractBaseGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
            }
        };
    }
    
    @Override
    public ElementOrder<N> incidentEdgeOrder() {
        return ElementOrder.unordered();
    }
    
    @Override
    public Set<EndpointPair<N>> incidentEdges(final N node) {
        Preconditions.checkNotNull(node);
        Preconditions.checkArgument(this.nodes().contains(node), "Node %s is not an element of this graph.", node);
        return (Set<EndpointPair<N>>)new IncidentEdgeSet<N>(this, this, node) {
            @Override
            public UnmodifiableIterator<EndpointPair<N>> iterator() {
                if (this.graph.isDirected()) {
                    return Iterators.unmodifiableIterator(Iterators.concat(Iterators.transform(this.graph.predecessors(this.node).iterator(), (Function<? super N, ? extends EndpointPair<N>>)new Function<N, EndpointPair<N>>() {
                        @Override
                        public EndpointPair<N> apply(final N predecessor) {
                            return EndpointPair.ordered(predecessor, IncidentEdgeSet.this.node);
                        }
                    }), Iterators.transform((Iterator<N>)Sets.difference(this.graph.successors(this.node), ImmutableSet.of(this.node)).iterator(), (Function<? super N, ? extends EndpointPair<N>>)new Function<N, EndpointPair<N>>() {
                        @Override
                        public EndpointPair<N> apply(final N successor) {
                            return EndpointPair.ordered(IncidentEdgeSet.this.node, successor);
                        }
                    })));
                }
                return Iterators.unmodifiableIterator(Iterators.transform(this.graph.adjacentNodes(this.node).iterator(), (Function<? super N, ? extends EndpointPair<N>>)new Function<N, EndpointPair<N>>() {
                    @Override
                    public EndpointPair<N> apply(final N adjacentNode) {
                        return EndpointPair.unordered(IncidentEdgeSet.this.node, adjacentNode);
                    }
                }));
            }
        };
    }
    
    @Override
    public int degree(final N node) {
        if (this.isDirected()) {
            return IntMath.saturatedAdd(this.predecessors(node).size(), this.successors(node).size());
        }
        final Set<N> neighbors = this.adjacentNodes(node);
        final int selfLoopCount = (this.allowsSelfLoops() && neighbors.contains(node)) ? 1 : 0;
        return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
    }
    
    @Override
    public int inDegree(final N node) {
        return this.isDirected() ? this.predecessors(node).size() : this.degree(node);
    }
    
    @Override
    public int outDegree(final N node) {
        return this.isDirected() ? this.successors(node).size() : this.degree(node);
    }
    
    @Override
    public boolean hasEdgeConnecting(final N nodeU, final N nodeV) {
        Preconditions.checkNotNull(nodeU);
        Preconditions.checkNotNull(nodeV);
        return this.nodes().contains(nodeU) && this.successors(nodeU).contains(nodeV);
    }
    
    @Override
    public boolean hasEdgeConnecting(final EndpointPair<N> endpoints) {
        Preconditions.checkNotNull(endpoints);
        if (!this.isOrderingCompatible(endpoints)) {
            return false;
        }
        final N nodeU = endpoints.nodeU();
        final N nodeV = endpoints.nodeV();
        return this.nodes().contains(nodeU) && this.successors(nodeU).contains(nodeV);
    }
    
    protected final void validateEndpoints(final EndpointPair<?> endpoints) {
        Preconditions.checkNotNull(endpoints);
        Preconditions.checkArgument(this.isOrderingCompatible(endpoints), (Object)"Mismatch: unordered endpoints cannot be used with directed graphs");
    }
    
    protected final boolean isOrderingCompatible(final EndpointPair<?> endpoints) {
        return endpoints.isOrdered() || !this.isDirected();
    }
}
