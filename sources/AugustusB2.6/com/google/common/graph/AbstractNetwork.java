// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.collect.Maps;
import java.util.Map;
import com.google.common.base.Preconditions;
import java.util.Optional;
import com.google.common.base.Predicate;
import java.util.Collections;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import javax.annotation.CheckForNull;
import com.google.common.collect.Iterators;
import com.google.common.base.Function;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.Set;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
public abstract class AbstractNetwork<N, E> implements Network<N, E>
{
    @Override
    public Graph<N> asGraph() {
        return new AbstractGraph<N>() {
            @Override
            public Set<N> nodes() {
                return AbstractNetwork.this.nodes();
            }
            
            @Override
            public Set<EndpointPair<N>> edges() {
                if (AbstractNetwork.this.allowsParallelEdges()) {
                    return (Set<EndpointPair<N>>)super.edges();
                }
                return new AbstractSet<EndpointPair<N>>() {
                    @Override
                    public Iterator<EndpointPair<N>> iterator() {
                        return Iterators.transform((Iterator<Object>)AbstractNetwork.this.edges().iterator(), (Function<? super Object, ? extends EndpointPair<N>>)new Function<E, EndpointPair<N>>() {
                            @Override
                            public EndpointPair<N> apply(final E edge) {
                                return AbstractNetwork.this.incidentNodes(edge);
                            }
                        });
                    }
                    
                    @Override
                    public int size() {
                        return AbstractNetwork.this.edges().size();
                    }
                    
                    @Override
                    public boolean contains(@CheckForNull final Object obj) {
                        if (!(obj instanceof EndpointPair)) {
                            return false;
                        }
                        final EndpointPair<?> endpointPair = (EndpointPair<?>)obj;
                        return AbstractGraph.this.isOrderingCompatible(endpointPair) && AbstractGraph.this.nodes().contains(endpointPair.nodeU()) && AbstractGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
                    }
                };
            }
            
            @Override
            public ElementOrder<N> nodeOrder() {
                return AbstractNetwork.this.nodeOrder();
            }
            
            @Override
            public ElementOrder<N> incidentEdgeOrder() {
                return ElementOrder.unordered();
            }
            
            @Override
            public boolean isDirected() {
                return AbstractNetwork.this.isDirected();
            }
            
            @Override
            public boolean allowsSelfLoops() {
                return AbstractNetwork.this.allowsSelfLoops();
            }
            
            @Override
            public Set<N> adjacentNodes(final N node) {
                return AbstractNetwork.this.adjacentNodes(node);
            }
            
            @Override
            public Set<N> predecessors(final N node) {
                return AbstractNetwork.this.predecessors(node);
            }
            
            @Override
            public Set<N> successors(final N node) {
                return AbstractNetwork.this.successors(node);
            }
        };
    }
    
    @Override
    public int degree(final N node) {
        if (this.isDirected()) {
            return IntMath.saturatedAdd(this.inEdges(node).size(), this.outEdges(node).size());
        }
        return IntMath.saturatedAdd(this.incidentEdges(node).size(), this.edgesConnecting(node, node).size());
    }
    
    @Override
    public int inDegree(final N node) {
        return this.isDirected() ? this.inEdges(node).size() : this.degree(node);
    }
    
    @Override
    public int outDegree(final N node) {
        return this.isDirected() ? this.outEdges(node).size() : this.degree(node);
    }
    
    @Override
    public Set<E> adjacentEdges(final E edge) {
        final EndpointPair<N> endpointPair = this.incidentNodes(edge);
        final Set<E> endpointPairIncidentEdges = (Set<E>)Sets.union(this.incidentEdges(endpointPair.nodeU()), this.incidentEdges(endpointPair.nodeV()));
        return Sets.difference(endpointPairIncidentEdges, ImmutableSet.of(edge));
    }
    
    @Override
    public Set<E> edgesConnecting(final N nodeU, final N nodeV) {
        final Set<E> outEdgesU = this.outEdges(nodeU);
        final Set<E> inEdgesV = this.inEdges(nodeV);
        return (outEdgesU.size() <= inEdgesV.size()) ? Collections.unmodifiableSet((Set<? extends E>)Sets.filter((Set<? extends T>)outEdgesU, this.connectedPredicate(nodeU, nodeV))) : Collections.unmodifiableSet((Set<? extends E>)Sets.filter((Set<? extends T>)inEdgesV, this.connectedPredicate(nodeV, nodeU)));
    }
    
    @Override
    public Set<E> edgesConnecting(final EndpointPair<N> endpoints) {
        this.validateEndpoints(endpoints);
        return this.edgesConnecting(endpoints.nodeU(), endpoints.nodeV());
    }
    
    private Predicate<E> connectedPredicate(final N nodePresent, final N nodeToCheck) {
        return new Predicate<E>() {
            @Override
            public boolean apply(final E edge) {
                return AbstractNetwork.this.incidentNodes(edge).adjacentNode(nodePresent).equals(nodeToCheck);
            }
        };
    }
    
    @Override
    public Optional<E> edgeConnecting(final N nodeU, final N nodeV) {
        return Optional.ofNullable(this.edgeConnectingOrNull(nodeU, nodeV));
    }
    
    @Override
    public Optional<E> edgeConnecting(final EndpointPair<N> endpoints) {
        this.validateEndpoints(endpoints);
        return this.edgeConnecting(endpoints.nodeU(), endpoints.nodeV());
    }
    
    @CheckForNull
    @Override
    public E edgeConnectingOrNull(final N nodeU, final N nodeV) {
        final Set<E> edgesConnecting = this.edgesConnecting(nodeU, nodeV);
        switch (edgesConnecting.size()) {
            case 0: {
                return null;
            }
            case 1: {
                return edgesConnecting.iterator().next();
            }
            default: {
                throw new IllegalArgumentException(String.format("Cannot call edgeConnecting() when parallel edges exist between %s and %s. Consider calling edgesConnecting() instead.", nodeU, nodeV));
            }
        }
    }
    
    @CheckForNull
    @Override
    public E edgeConnectingOrNull(final EndpointPair<N> endpoints) {
        this.validateEndpoints(endpoints);
        return this.edgeConnectingOrNull(endpoints.nodeU(), endpoints.nodeV());
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
        return this.isOrderingCompatible(endpoints) && this.hasEdgeConnecting(endpoints.nodeU(), endpoints.nodeV());
    }
    
    protected final void validateEndpoints(final EndpointPair<?> endpoints) {
        Preconditions.checkNotNull(endpoints);
        Preconditions.checkArgument(this.isOrderingCompatible(endpoints), (Object)"Mismatch: unordered endpoints cannot be used with directed graphs");
    }
    
    protected final boolean isOrderingCompatible(final EndpointPair<?> endpoints) {
        return endpoints.isOrdered() || !this.isDirected();
    }
    
    @Override
    public final boolean equals(@CheckForNull final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Network)) {
            return false;
        }
        final Network<?, ?> other = (Network<?, ?>)obj;
        return this.isDirected() == other.isDirected() && this.nodes().equals(other.nodes()) && edgeIncidentNodesMap((Network<Object, Object>)this).equals(edgeIncidentNodesMap(other));
    }
    
    @Override
    public final int hashCode() {
        return edgeIncidentNodesMap((Network<Object, Object>)this).hashCode();
    }
    
    @Override
    public String toString() {
        final boolean directed = this.isDirected();
        final boolean allowsParallelEdges = this.allowsParallelEdges();
        final boolean allowsSelfLoops = this.allowsSelfLoops();
        final String value = String.valueOf(this.nodes());
        final String value2 = String.valueOf(edgeIncidentNodesMap((Network<Object, Object>)this));
        return new StringBuilder(87 + String.valueOf(value).length() + String.valueOf(value2).length()).append("isDirected: ").append(directed).append(", allowsParallelEdges: ").append(allowsParallelEdges).append(", allowsSelfLoops: ").append(allowsSelfLoops).append(", nodes: ").append(value).append(", edges: ").append(value2).toString();
    }
    
    private static <N, E> Map<E, EndpointPair<N>> edgeIncidentNodesMap(final Network<N, E> network) {
        final Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function<E, EndpointPair<N>>() {
            @Override
            public EndpointPair<N> apply(final E edge) {
                return network.incidentNodes(edge);
            }
        };
        return Maps.asMap(network.edges(), edgeToIncidentNodesFn);
    }
}
