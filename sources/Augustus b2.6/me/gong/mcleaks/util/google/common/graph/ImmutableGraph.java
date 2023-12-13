// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import java.util.Map;
import me.gong.mcleaks.util.google.common.base.Function;
import java.util.Set;
import me.gong.mcleaks.util.google.common.collect.Maps;
import me.gong.mcleaks.util.google.common.base.Functions;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.collect.ImmutableMap;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public abstract class ImmutableGraph<N> extends ForwardingGraph<N>
{
    ImmutableGraph() {
    }
    
    public static <N> ImmutableGraph<N> copyOf(final Graph<N> graph) {
        return (ImmutableGraph<N>)((graph instanceof ImmutableGraph) ? ((ImmutableGraph)graph) : new ValueBackedImpl(GraphBuilder.from(graph), getNodeConnections(graph), graph.edges().size()));
    }
    
    @Deprecated
    public static <N> ImmutableGraph<N> copyOf(final ImmutableGraph<N> graph) {
        return Preconditions.checkNotNull(graph);
    }
    
    private static <N> ImmutableMap<N, GraphConnections<N, GraphConstants.Presence>> getNodeConnections(final Graph<N> graph) {
        final ImmutableMap.Builder<N, GraphConnections<N, GraphConstants.Presence>> nodeConnections = ImmutableMap.builder();
        for (final N node : graph.nodes()) {
            nodeConnections.put(node, connectionsOf(graph, node));
        }
        return nodeConnections.build();
    }
    
    private static <N> GraphConnections<N, GraphConstants.Presence> connectionsOf(final Graph<N> graph, final N node) {
        final Function<Object, GraphConstants.Presence> edgeValueFn = Functions.constant(GraphConstants.Presence.EDGE_EXISTS);
        return (GraphConnections<N, GraphConstants.Presence>)(graph.isDirected() ? DirectedGraphConnections.ofImmutable(graph.predecessors(node), (Map<N, Object>)Maps.asMap((Set<N>)graph.successors(node), (Function<? super N, V>)edgeValueFn)) : UndirectedGraphConnections.ofImmutable((Map<Object, Object>)Maps.asMap((Set<N>)graph.adjacentNodes(node), (Function<? super N, V>)edgeValueFn)));
    }
    
    static class ValueBackedImpl<N, V> extends ImmutableGraph<N>
    {
        protected final ValueGraph<N, V> backingValueGraph;
        
        ValueBackedImpl(final AbstractGraphBuilder<? super N> builder, final ImmutableMap<N, GraphConnections<N, V>> nodeConnections, final long edgeCount) {
            this.backingValueGraph = new ConfigurableValueGraph<N, V>(builder, nodeConnections, edgeCount);
        }
        
        @Override
        protected Graph<N> delegate() {
            return this.backingValueGraph;
        }
    }
}
