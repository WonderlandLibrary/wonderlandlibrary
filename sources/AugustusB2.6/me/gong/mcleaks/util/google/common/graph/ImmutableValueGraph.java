// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import me.gong.mcleaks.util.google.common.collect.Maps;
import me.gong.mcleaks.util.google.common.base.Function;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.collect.ImmutableMap;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public final class ImmutableValueGraph<N, V> extends ValueBackedImpl<N, V> implements ValueGraph<N, V>
{
    private ImmutableValueGraph(final ValueGraph<N, V> graph) {
        super((AbstractGraphBuilder<? super N>)ValueGraphBuilder.from((Graph<Object>)graph), getNodeConnections(graph), graph.edges().size());
    }
    
    public static <N, V> ImmutableValueGraph<N, V> copyOf(final ValueGraph<N, V> graph) {
        return (graph instanceof ImmutableValueGraph) ? ((ImmutableValueGraph)graph) : new ImmutableValueGraph<N, V>((ValueGraph<N, V>)graph);
    }
    
    @Deprecated
    public static <N, V> ImmutableValueGraph<N, V> copyOf(final ImmutableValueGraph<N, V> graph) {
        return Preconditions.checkNotNull(graph);
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
                return graph.edgeValue(node, successorNode);
            }
        };
        return (GraphConnections<N, V>)(graph.isDirected() ? DirectedGraphConnections.ofImmutable(graph.predecessors(node), (Map<N, Object>)Maps.asMap((Set<N>)graph.successors(node), (Function<? super N, V>)successorNodeToValueFn)) : UndirectedGraphConnections.ofImmutable((Map<Object, Object>)Maps.asMap((Set<N>)graph.adjacentNodes(node), (Function<? super N, V>)successorNodeToValueFn)));
    }
    
    @Override
    public V edgeValue(final Object nodeU, final Object nodeV) {
        return this.backingValueGraph.edgeValue(nodeU, nodeV);
    }
    
    @Override
    public V edgeValueOrDefault(final Object nodeU, final Object nodeV, @Nullable final V defaultValue) {
        return this.backingValueGraph.edgeValueOrDefault(nodeU, nodeV, defaultValue);
    }
    
    @Override
    public String toString() {
        return this.backingValueGraph.toString();
    }
}
