// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.collect.Maps;
import java.util.Objects;
import com.google.common.base.Function;
import java.util.Map;
import javax.annotation.CheckForNull;
import java.util.Optional;
import java.util.Set;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
public abstract class AbstractValueGraph<N, V> extends AbstractBaseGraph<N> implements ValueGraph<N, V>
{
    @Override
    public Graph<N> asGraph() {
        return new AbstractGraph<N>() {
            @Override
            public Set<N> nodes() {
                return AbstractValueGraph.this.nodes();
            }
            
            @Override
            public Set<EndpointPair<N>> edges() {
                return (Set<EndpointPair<N>>)AbstractValueGraph.this.edges();
            }
            
            @Override
            public boolean isDirected() {
                return AbstractValueGraph.this.isDirected();
            }
            
            @Override
            public boolean allowsSelfLoops() {
                return AbstractValueGraph.this.allowsSelfLoops();
            }
            
            @Override
            public ElementOrder<N> nodeOrder() {
                return AbstractValueGraph.this.nodeOrder();
            }
            
            @Override
            public ElementOrder<N> incidentEdgeOrder() {
                return (ElementOrder<N>)AbstractValueGraph.this.incidentEdgeOrder();
            }
            
            @Override
            public Set<N> adjacentNodes(final N node) {
                return AbstractValueGraph.this.adjacentNodes(node);
            }
            
            @Override
            public Set<N> predecessors(final N node) {
                return AbstractValueGraph.this.predecessors(node);
            }
            
            @Override
            public Set<N> successors(final N node) {
                return AbstractValueGraph.this.successors(node);
            }
            
            @Override
            public int degree(final N node) {
                return AbstractValueGraph.this.degree(node);
            }
            
            @Override
            public int inDegree(final N node) {
                return AbstractValueGraph.this.inDegree(node);
            }
            
            @Override
            public int outDegree(final N node) {
                return AbstractValueGraph.this.outDegree(node);
            }
        };
    }
    
    @Override
    public Optional<V> edgeValue(final N nodeU, final N nodeV) {
        return Optional.ofNullable(this.edgeValueOrDefault(nodeU, nodeV, null));
    }
    
    @Override
    public Optional<V> edgeValue(final EndpointPair<N> endpoints) {
        return Optional.ofNullable(this.edgeValueOrDefault(endpoints, null));
    }
    
    @Override
    public final boolean equals(@CheckForNull final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ValueGraph)) {
            return false;
        }
        final ValueGraph<?, ?> other = (ValueGraph<?, ?>)obj;
        return this.isDirected() == other.isDirected() && this.nodes().equals(other.nodes()) && edgeValueMap((ValueGraph<Object, Object>)this).equals(edgeValueMap(other));
    }
    
    @Override
    public final int hashCode() {
        return edgeValueMap((ValueGraph<Object, Object>)this).hashCode();
    }
    
    @Override
    public String toString() {
        final boolean directed = this.isDirected();
        final boolean allowsSelfLoops = this.allowsSelfLoops();
        final String value = String.valueOf(this.nodes());
        final String value2 = String.valueOf(edgeValueMap((ValueGraph<Object, Object>)this));
        return new StringBuilder(59 + String.valueOf(value).length() + String.valueOf(value2).length()).append("isDirected: ").append(directed).append(", allowsSelfLoops: ").append(allowsSelfLoops).append(", nodes: ").append(value).append(", edges: ").append(value2).toString();
    }
    
    private static <N, V> Map<EndpointPair<N>, V> edgeValueMap(final ValueGraph<N, V> graph) {
        final Function<EndpointPair<N>, V> edgeToValueFn = new Function<EndpointPair<N>, V>() {
            @Override
            public V apply(final EndpointPair<N> edge) {
                return Objects.requireNonNull((V)graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), null));
            }
        };
        return Maps.asMap(graph.edges(), edgeToValueFn);
    }
}
