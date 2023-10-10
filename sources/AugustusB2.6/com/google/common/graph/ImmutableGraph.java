// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Function;
import java.util.Set;
import com.google.common.collect.Maps;
import com.google.common.base.Functions;
import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Preconditions;
import java.util.Map;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.Immutable;

@Immutable(containerOf = { "N" })
@ElementTypesAreNonnullByDefault
@Beta
public class ImmutableGraph<N> extends ForwardingGraph<N>
{
    private final BaseGraph<N> backingGraph;
    
    ImmutableGraph(final BaseGraph<N> backingGraph) {
        this.backingGraph = backingGraph;
    }
    
    public static <N> ImmutableGraph<N> copyOf(final Graph<N> graph) {
        return (graph instanceof ImmutableGraph) ? ((ImmutableGraph)graph) : new ImmutableGraph<N>((BaseGraph<N>)new StandardValueGraph((AbstractGraphBuilder<? super Object>)GraphBuilder.from(graph), (Map<Object, GraphConnections<Object, Object>>)getNodeConnections(graph), graph.edges().size()));
    }
    
    @Deprecated
    public static <N> ImmutableGraph<N> copyOf(final ImmutableGraph<N> graph) {
        return Preconditions.checkNotNull(graph);
    }
    
    @Override
    public ElementOrder<N> incidentEdgeOrder() {
        return ElementOrder.stable();
    }
    
    private static <N> ImmutableMap<N, GraphConnections<N, GraphConstants.Presence>> getNodeConnections(final Graph<N> graph) {
        final ImmutableMap.Builder<N, GraphConnections<N, GraphConstants.Presence>> nodeConnections = ImmutableMap.builder();
        for (final N node : graph.nodes()) {
            nodeConnections.put(node, connectionsOf(graph, node));
        }
        return nodeConnections.build();
    }
    
    private static <N> GraphConnections<N, GraphConstants.Presence> connectionsOf(final Graph<N> graph, final N node) {
        final Function<N, GraphConstants.Presence> edgeValueFn = (Function<N, GraphConstants.Presence>)Functions.constant(GraphConstants.Presence.EDGE_EXISTS);
        return (GraphConnections<N, GraphConstants.Presence>)(graph.isDirected() ? DirectedGraphConnections.ofImmutable(node, graph.incidentEdges(node), edgeValueFn) : UndirectedGraphConnections.ofImmutable((Map<Object, Object>)Maps.asMap((Set<N>)graph.adjacentNodes(node), (Function<? super N, V>)edgeValueFn)));
    }
    
    @Override
    BaseGraph<N> delegate() {
        return this.backingGraph;
    }
    
    public static class Builder<N>
    {
        private final MutableGraph<N> mutableGraph;
        
        Builder(final GraphBuilder<N> graphBuilder) {
            this.mutableGraph = graphBuilder.copy().incidentEdgeOrder(ElementOrder.stable()).build();
        }
        
        @CanIgnoreReturnValue
        public Builder<N> addNode(final N node) {
            this.mutableGraph.addNode(node);
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<N> putEdge(final N nodeU, final N nodeV) {
            this.mutableGraph.putEdge(nodeU, nodeV);
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<N> putEdge(final EndpointPair<N> endpoints) {
            this.mutableGraph.putEdge(endpoints);
            return this;
        }
        
        public ImmutableGraph<N> build() {
            return ImmutableGraph.copyOf(this.mutableGraph);
        }
    }
}
