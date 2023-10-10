// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Set;
import com.google.common.collect.Maps;
import java.util.Objects;
import com.google.common.base.Function;
import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Preconditions;
import java.util.Map;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.Immutable;

@Immutable(containerOf = { "N", "V" })
@ElementTypesAreNonnullByDefault
@Beta
public final class ImmutableValueGraph<N, V> extends StandardValueGraph<N, V>
{
    private ImmutableValueGraph(final ValueGraph<N, V> graph) {
        super((AbstractGraphBuilder<? super N>)ValueGraphBuilder.from(graph), getNodeConnections(graph), graph.edges().size());
    }
    
    public static <N, V> ImmutableValueGraph<N, V> copyOf(final ValueGraph<N, V> graph) {
        return (graph instanceof ImmutableValueGraph) ? ((ImmutableValueGraph)graph) : new ImmutableValueGraph<N, V>((ValueGraph<N, V>)graph);
    }
    
    @Deprecated
    public static <N, V> ImmutableValueGraph<N, V> copyOf(final ImmutableValueGraph<N, V> graph) {
        return Preconditions.checkNotNull(graph);
    }
    
    @Override
    public ElementOrder<N> incidentEdgeOrder() {
        return ElementOrder.stable();
    }
    
    @Override
    public ImmutableGraph<N> asGraph() {
        return new ImmutableGraph<N>((BaseGraph<N>)this);
    }
    
    private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(final ValueGraph<N, V> graph) {
        final ImmutableMap.Builder<N, GraphConnections<N, V>> nodeConnections = ImmutableMap.builder();
        for (final N node : graph.nodes()) {
            nodeConnections.put(node, connectionsOf(graph, node));
        }
        return nodeConnections.build();
    }
    
    private static <N, V> GraphConnections<N, V> connectionsOf(final ValueGraph<N, V> graph, final N node) {
        final Function<N, V> successorNodeToValueFn = new Function<N, V>() {
            @Override
            public V apply(final N successorNode) {
                return Objects.requireNonNull((V)graph.edgeValueOrDefault(node, successorNode, null));
            }
        };
        return (GraphConnections<N, V>)(graph.isDirected() ? DirectedGraphConnections.ofImmutable(node, graph.incidentEdges(node), successorNodeToValueFn) : UndirectedGraphConnections.ofImmutable((Map<Object, Object>)Maps.asMap((Set<N>)graph.adjacentNodes(node), (Function<? super N, V>)successorNodeToValueFn)));
    }
    
    public static class Builder<N, V>
    {
        private final MutableValueGraph<N, V> mutableValueGraph;
        
        Builder(final ValueGraphBuilder<N, V> graphBuilder) {
            this.mutableValueGraph = graphBuilder.copy().incidentEdgeOrder(ElementOrder.stable()).build();
        }
        
        @CanIgnoreReturnValue
        public Builder<N, V> addNode(final N node) {
            this.mutableValueGraph.addNode(node);
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<N, V> putEdgeValue(final N nodeU, final N nodeV, final V value) {
            this.mutableValueGraph.putEdgeValue(nodeU, nodeV, value);
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<N, V> putEdgeValue(final EndpointPair<N> endpoints, final V value) {
            this.mutableValueGraph.putEdgeValue(endpoints, value);
            return this;
        }
        
        public ImmutableValueGraph<N, V> build() {
            return ImmutableValueGraph.copyOf(this.mutableValueGraph);
        }
    }
}
