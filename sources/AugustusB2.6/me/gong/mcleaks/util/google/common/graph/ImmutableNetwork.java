// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import java.util.Set;
import me.gong.mcleaks.util.google.common.base.Function;
import me.gong.mcleaks.util.google.common.collect.Maps;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.collect.ImmutableMap;
import java.util.Map;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public final class ImmutableNetwork<N, E> extends ConfigurableNetwork<N, E>
{
    private ImmutableNetwork(final Network<N, E> network) {
        super((NetworkBuilder<? super N, ? super E>)NetworkBuilder.from((Network<? super N, ? super E>)network), getNodeConnections(network), getEdgeToReferenceNode(network));
    }
    
    public static <N, E> ImmutableNetwork<N, E> copyOf(final Network<N, E> network) {
        return (network instanceof ImmutableNetwork) ? ((ImmutableNetwork)network) : new ImmutableNetwork<N, E>((Network<N, E>)network);
    }
    
    @Deprecated
    public static <N, E> ImmutableNetwork<N, E> copyOf(final ImmutableNetwork<N, E> network) {
        return Preconditions.checkNotNull(network);
    }
    
    @Override
    public ImmutableGraph<N> asGraph() {
        final Graph<N> asGraph = super.asGraph();
        return new ImmutableGraph<N>() {
            @Override
            protected Graph<N> delegate() {
                return asGraph;
            }
        };
    }
    
    private static <N, E> Map<N, NetworkConnections<N, E>> getNodeConnections(final Network<N, E> network) {
        final ImmutableMap.Builder<N, NetworkConnections<N, E>> nodeConnections = ImmutableMap.builder();
        for (final N node : network.nodes()) {
            nodeConnections.put(node, connectionsOf(network, node));
        }
        return nodeConnections.build();
    }
    
    private static <N, E> Map<E, N> getEdgeToReferenceNode(final Network<N, E> network) {
        final ImmutableMap.Builder<E, N> edgeToReferenceNode = ImmutableMap.builder();
        for (final E edge : network.edges()) {
            edgeToReferenceNode.put(edge, network.incidentNodes(edge).nodeU());
        }
        return edgeToReferenceNode.build();
    }
    
    private static <N, E> NetworkConnections<N, E> connectionsOf(final Network<N, E> network, final N node) {
        if (network.isDirected()) {
            final Map<E, N> inEdgeMap = Maps.asMap(network.inEdges(node), (Function<? super E, N>)sourceNodeFn((Network<V, ? super K>)network));
            final Map<E, N> outEdgeMap = Maps.asMap(network.outEdges(node), (Function<? super E, N>)targetNodeFn((Network<V, ? super K>)network));
            final int selfLoopCount = network.edgesConnecting(node, node).size();
            return (NetworkConnections<N, E>)(network.allowsParallelEdges() ? DirectedMultiNetworkConnections.ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount) : DirectedNetworkConnections.ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount));
        }
        final Map<E, N> incidentEdgeMap = Maps.asMap(network.incidentEdges(node), (Function<? super E, N>)adjacentNodeFn((Network<V, ? super K>)network, (V)node));
        return (NetworkConnections<N, E>)(network.allowsParallelEdges() ? UndirectedMultiNetworkConnections.ofImmutable(incidentEdgeMap) : UndirectedNetworkConnections.ofImmutable(incidentEdgeMap));
    }
    
    private static <N, E> Function<E, N> sourceNodeFn(final Network<N, E> network) {
        return new Function<E, N>() {
            @Override
            public N apply(final E edge) {
                return network.incidentNodes(edge).source();
            }
        };
    }
    
    private static <N, E> Function<E, N> targetNodeFn(final Network<N, E> network) {
        return new Function<E, N>() {
            @Override
            public N apply(final E edge) {
                return network.incidentNodes(edge).target();
            }
        };
    }
    
    private static <N, E> Function<E, N> adjacentNodeFn(final Network<N, E> network, final N node) {
        return new Function<E, N>() {
            @Override
            public N apply(final E edge) {
                return network.incidentNodes(edge).adjacentNode(node);
            }
        };
    }
}
