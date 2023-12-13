// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import me.gong.mcleaks.util.google.common.collect.Maps;
import java.util.Map;
import me.gong.mcleaks.util.google.common.collect.ImmutableSet;
import me.gong.mcleaks.util.google.common.collect.Sets;
import me.gong.mcleaks.util.google.common.math.IntMath;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.collect.Iterators;
import me.gong.mcleaks.util.google.common.base.Function;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.Set;
import me.gong.mcleaks.util.google.common.annotations.Beta;

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
                    return super.edges();
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
                    public boolean contains(@Nullable final Object obj) {
                        if (!(obj instanceof EndpointPair)) {
                            return false;
                        }
                        final EndpointPair<?> endpointPair = (EndpointPair<?>)obj;
                        return AbstractGraph.this.isDirected() == endpointPair.isOrdered() && AbstractGraph.this.nodes().contains(endpointPair.nodeU()) && AbstractGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
                    }
                };
            }
            
            @Override
            public ElementOrder<N> nodeOrder() {
                return AbstractNetwork.this.nodeOrder();
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
            public Set<N> adjacentNodes(final Object node) {
                return AbstractNetwork.this.adjacentNodes(node);
            }
            
            @Override
            public Set<N> predecessors(final Object node) {
                return AbstractNetwork.this.predecessors(node);
            }
            
            @Override
            public Set<N> successors(final Object node) {
                return AbstractNetwork.this.successors(node);
            }
        };
    }
    
    @Override
    public int degree(final Object node) {
        if (this.isDirected()) {
            return IntMath.saturatedAdd(this.inEdges(node).size(), this.outEdges(node).size());
        }
        return IntMath.saturatedAdd(this.incidentEdges(node).size(), this.edgesConnecting(node, node).size());
    }
    
    @Override
    public int inDegree(final Object node) {
        return this.isDirected() ? this.inEdges(node).size() : this.degree(node);
    }
    
    @Override
    public int outDegree(final Object node) {
        return this.isDirected() ? this.outEdges(node).size() : this.degree(node);
    }
    
    @Override
    public Set<E> adjacentEdges(final Object edge) {
        final EndpointPair<?> endpointPair = this.incidentNodes(edge);
        final Set<E> endpointPairIncidentEdges = (Set<E>)Sets.union(this.incidentEdges(endpointPair.nodeU()), this.incidentEdges(endpointPair.nodeV()));
        return Sets.difference(endpointPairIncidentEdges, ImmutableSet.of(edge));
    }
    
    @Override
    public String toString() {
        final String propertiesString = String.format("isDirected: %s, allowsParallelEdges: %s, allowsSelfLoops: %s", this.isDirected(), this.allowsParallelEdges(), this.allowsSelfLoops());
        return String.format("%s, nodes: %s, edges: %s", propertiesString, this.nodes(), this.edgeIncidentNodesMap());
    }
    
    private Map<E, EndpointPair<N>> edgeIncidentNodesMap() {
        final Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function<E, EndpointPair<N>>() {
            @Override
            public EndpointPair<N> apply(final E edge) {
                return AbstractNetwork.this.incidentNodes(edge);
            }
        };
        return Maps.asMap(this.edges(), edgeToIncidentNodesFn);
    }
}
