/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CanIgnoreReturnValue
 */
package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.graph.AbstractGraph;
import com.google.common.graph.AbstractNetwork;
import com.google.common.graph.AbstractValueGraph;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.Network;
import com.google.common.graph.NetworkBuilder;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public final class Graphs {
    private Graphs() {
    }

    public static boolean hasCycle(Graph<?> graph) {
        int numEdges = graph.edges().size();
        if (numEdges == 0) {
            return false;
        }
        if (!graph.isDirected() && numEdges >= graph.nodes().size()) {
            return true;
        }
        HashMap<Object, NodeVisitState> visitedNodes = Maps.newHashMapWithExpectedSize(graph.nodes().size());
        for (Object node : graph.nodes()) {
            if (!Graphs.subgraphHasCycle(graph, visitedNodes, node, null)) continue;
            return true;
        }
        return false;
    }

    public static boolean hasCycle(Network<?, ?> network) {
        if (!network.isDirected() && network.allowsParallelEdges() && network.edges().size() > network.asGraph().edges().size()) {
            return true;
        }
        return Graphs.hasCycle(network.asGraph());
    }

    private static boolean subgraphHasCycle(Graph<?> graph, Map<Object, NodeVisitState> visitedNodes, Object node, @Nullable Object previousNode) {
        NodeVisitState state = visitedNodes.get(node);
        if (state == NodeVisitState.COMPLETE) {
            return false;
        }
        if (state == NodeVisitState.PENDING) {
            return true;
        }
        visitedNodes.put(node, NodeVisitState.PENDING);
        for (Object nextNode : graph.successors(node)) {
            if (!Graphs.canTraverseWithoutReusingEdge(graph, nextNode, previousNode) || !Graphs.subgraphHasCycle(graph, visitedNodes, nextNode, node)) continue;
            return true;
        }
        visitedNodes.put(node, NodeVisitState.COMPLETE);
        return false;
    }

    private static boolean canTraverseWithoutReusingEdge(Graph<?> graph, Object nextNode, @Nullable Object previousNode) {
        return graph.isDirected() || !Objects.equal(previousNode, nextNode);
    }

    public static <N> Graph<N> transitiveClosure(Graph<N> graph) {
        MutableGraph transitiveClosure = GraphBuilder.from(graph).allowsSelfLoops(true).build();
        if (graph.isDirected()) {
            for (N node : graph.nodes()) {
                for (N reachableNode : Graphs.reachableNodes(graph, node)) {
                    transitiveClosure.putEdge(node, reachableNode);
                }
            }
        } else {
            HashSet<N> visitedNodes = new HashSet<N>();
            for (N node : graph.nodes()) {
                if (visitedNodes.contains(node)) continue;
                Set<N> reachableNodes = Graphs.reachableNodes(graph, node);
                visitedNodes.addAll(reachableNodes);
                int pairwiseMatch = 1;
                for (N nodeU : reachableNodes) {
                    for (N nodeV : Iterables.limit(reachableNodes, pairwiseMatch++)) {
                        transitiveClosure.putEdge(nodeU, nodeV);
                    }
                }
            }
        }
        return transitiveClosure;
    }

    public static <N> Set<N> reachableNodes(Graph<N> graph, Object node) {
        Preconditions.checkArgument(graph.nodes().contains(node), "Node %s is not an element of this graph.", node);
        LinkedHashSet<Object> visitedNodes = new LinkedHashSet<Object>();
        ArrayDeque<Object> queuedNodes = new ArrayDeque<Object>();
        visitedNodes.add(node);
        queuedNodes.add(node);
        while (!queuedNodes.isEmpty()) {
            Object currentNode = queuedNodes.remove();
            for (N successor : graph.successors(currentNode)) {
                if (!visitedNodes.add(successor)) continue;
                queuedNodes.add(successor);
            }
        }
        return Collections.unmodifiableSet(visitedNodes);
    }

    public static boolean equivalent(@Nullable Graph<?> graphA, @Nullable Graph<?> graphB) {
        if (graphA == graphB) {
            return true;
        }
        if (graphA == null || graphB == null) {
            return false;
        }
        return graphA.isDirected() == graphB.isDirected() && graphA.nodes().equals(graphB.nodes()) && graphA.edges().equals(graphB.edges());
    }

    public static boolean equivalent(@Nullable ValueGraph<?, ?> graphA, @Nullable ValueGraph<?, ?> graphB) {
        if (graphA == graphB) {
            return true;
        }
        if (graphA == null || graphB == null) {
            return false;
        }
        if (graphA.isDirected() != graphB.isDirected() || !graphA.nodes().equals(graphB.nodes()) || !graphA.edges().equals(graphB.edges())) {
            return false;
        }
        for (EndpointPair edge : graphA.edges()) {
            if (graphA.edgeValue(edge.nodeU(), edge.nodeV()).equals(graphB.edgeValue(edge.nodeU(), edge.nodeV()))) continue;
            return false;
        }
        return true;
    }

    public static boolean equivalent(@Nullable Network<?, ?> networkA, @Nullable Network<?, ?> networkB) {
        if (networkA == networkB) {
            return true;
        }
        if (networkA == null || networkB == null) {
            return false;
        }
        if (networkA.isDirected() != networkB.isDirected() || !networkA.nodes().equals(networkB.nodes()) || !networkA.edges().equals(networkB.edges())) {
            return false;
        }
        for (Object edge : networkA.edges()) {
            if (networkA.incidentNodes(edge).equals(networkB.incidentNodes(edge))) continue;
            return false;
        }
        return true;
    }

    public static <N> Graph<N> transpose(Graph<N> graph) {
        if (!graph.isDirected()) {
            return graph;
        }
        if (graph instanceof TransposedGraph) {
            return ((TransposedGraph)graph).graph;
        }
        return new TransposedGraph<N>(graph);
    }

    public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> graph) {
        if (!graph.isDirected()) {
            return graph;
        }
        if (graph instanceof TransposedValueGraph) {
            return ((TransposedValueGraph)graph).graph;
        }
        return new TransposedValueGraph<N, V>(graph);
    }

    public static <N, E> Network<N, E> transpose(Network<N, E> network) {
        if (!network.isDirected()) {
            return network;
        }
        if (network instanceof TransposedNetwork) {
            return ((TransposedNetwork)network).network;
        }
        return new TransposedNetwork<N, E>(network);
    }

    public static <N> MutableGraph<N> inducedSubgraph(Graph<N> graph, Iterable<? extends N> nodes) {
        MutableGraph subgraph = GraphBuilder.from(graph).build();
        for (Object node : nodes) {
            subgraph.addNode(node);
        }
        for (Object node : subgraph.nodes()) {
            for (N successorNode : graph.successors(node)) {
                if (!subgraph.nodes().contains(successorNode)) continue;
                subgraph.putEdge(node, successorNode);
            }
        }
        return subgraph;
    }

    public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> graph, Iterable<? extends N> nodes) {
        MutableValueGraph subgraph = ValueGraphBuilder.from(graph).build();
        for (Object node : nodes) {
            subgraph.addNode(node);
        }
        for (Object node : subgraph.nodes()) {
            for (Object successorNode : graph.successors(node)) {
                if (!subgraph.nodes().contains(successorNode)) continue;
                subgraph.putEdgeValue(node, successorNode, graph.edgeValue(node, successorNode));
            }
        }
        return subgraph;
    }

    public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> network, Iterable<? extends N> nodes) {
        MutableNetwork subgraph = NetworkBuilder.from(network).build();
        for (Object node : nodes) {
            subgraph.addNode(node);
        }
        for (Object node : subgraph.nodes()) {
            for (E edge : network.outEdges(node)) {
                N successorNode = network.incidentNodes(edge).adjacentNode(node);
                if (!subgraph.nodes().contains(successorNode)) continue;
                subgraph.addEdge(node, successorNode, edge);
            }
        }
        return subgraph;
    }

    public static <N> MutableGraph<N> copyOf(Graph<N> graph) {
        MutableGraph copy = GraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
        for (N n : graph.nodes()) {
            copy.addNode(n);
        }
        for (EndpointPair endpointPair : graph.edges()) {
            copy.putEdge(endpointPair.nodeU(), endpointPair.nodeV());
        }
        return copy;
    }

    public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
        MutableValueGraph copy = ValueGraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
        for (Object n : graph.nodes()) {
            copy.addNode(n);
        }
        for (EndpointPair endpointPair : graph.edges()) {
            copy.putEdgeValue(endpointPair.nodeU(), endpointPair.nodeV(), graph.edgeValue(endpointPair.nodeU(), endpointPair.nodeV()));
        }
        return copy;
    }

    public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> network) {
        MutableNetwork copy = NetworkBuilder.from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).build();
        for (N node : network.nodes()) {
            copy.addNode(node);
        }
        for (Object edge : network.edges()) {
            EndpointPair<N> endpointPair = network.incidentNodes(edge);
            copy.addEdge(endpointPair.nodeU(), endpointPair.nodeV(), edge);
        }
        return copy;
    }

    @CanIgnoreReturnValue
    static int checkNonNegative(int value) {
        Preconditions.checkArgument(value >= 0, "Not true that %s is non-negative.", value);
        return value;
    }

    @CanIgnoreReturnValue
    static int checkPositive(int value) {
        Preconditions.checkArgument(value > 0, "Not true that %s is positive.", value);
        return value;
    }

    @CanIgnoreReturnValue
    static long checkNonNegative(long value) {
        Preconditions.checkArgument(value >= 0L, "Not true that %s is non-negative.", value);
        return value;
    }

    @CanIgnoreReturnValue
    static long checkPositive(long value) {
        Preconditions.checkArgument(value > 0L, "Not true that %s is positive.", value);
        return value;
    }

    private static enum NodeVisitState {
        PENDING,
        COMPLETE;

    }

    private static class TransposedNetwork<N, E>
    extends AbstractNetwork<N, E> {
        private final Network<N, E> network;

        TransposedNetwork(Network<N, E> network) {
            this.network = network;
        }

        @Override
        public Set<N> nodes() {
            return this.network.nodes();
        }

        @Override
        public Set<E> edges() {
            return this.network.edges();
        }

        @Override
        public boolean isDirected() {
            return this.network.isDirected();
        }

        @Override
        public boolean allowsParallelEdges() {
            return this.network.allowsParallelEdges();
        }

        @Override
        public boolean allowsSelfLoops() {
            return this.network.allowsSelfLoops();
        }

        @Override
        public ElementOrder<N> nodeOrder() {
            return this.network.nodeOrder();
        }

        @Override
        public ElementOrder<E> edgeOrder() {
            return this.network.edgeOrder();
        }

        @Override
        public Set<N> adjacentNodes(Object node) {
            return this.network.adjacentNodes(node);
        }

        @Override
        public Set<N> predecessors(Object node) {
            return this.network.successors(node);
        }

        @Override
        public Set<N> successors(Object node) {
            return this.network.predecessors(node);
        }

        @Override
        public Set<E> incidentEdges(Object node) {
            return this.network.incidentEdges(node);
        }

        @Override
        public Set<E> inEdges(Object node) {
            return this.network.outEdges(node);
        }

        @Override
        public Set<E> outEdges(Object node) {
            return this.network.inEdges(node);
        }

        @Override
        public EndpointPair<N> incidentNodes(Object edge) {
            EndpointPair<N> endpointPair = this.network.incidentNodes(edge);
            return EndpointPair.of(this.network, endpointPair.nodeV(), endpointPair.nodeU());
        }

        @Override
        public Set<E> adjacentEdges(Object edge) {
            return this.network.adjacentEdges(edge);
        }

        @Override
        public Set<E> edgesConnecting(Object nodeU, Object nodeV) {
            return this.network.edgesConnecting(nodeV, nodeU);
        }
    }

    private static class TransposedValueGraph<N, V>
    extends AbstractValueGraph<N, V> {
        private final ValueGraph<N, V> graph;

        TransposedValueGraph(ValueGraph<N, V> graph) {
            this.graph = graph;
        }

        @Override
        public Set<N> nodes() {
            return this.graph.nodes();
        }

        @Override
        protected long edgeCount() {
            return this.graph.edges().size();
        }

        @Override
        public boolean isDirected() {
            return this.graph.isDirected();
        }

        @Override
        public boolean allowsSelfLoops() {
            return this.graph.allowsSelfLoops();
        }

        @Override
        public ElementOrder<N> nodeOrder() {
            return this.graph.nodeOrder();
        }

        @Override
        public Set<N> adjacentNodes(Object node) {
            return this.graph.adjacentNodes(node);
        }

        @Override
        public Set<N> predecessors(Object node) {
            return this.graph.successors(node);
        }

        @Override
        public Set<N> successors(Object node) {
            return this.graph.predecessors(node);
        }

        @Override
        public V edgeValue(Object nodeU, Object nodeV) {
            return this.graph.edgeValue(nodeV, nodeU);
        }

        @Override
        public V edgeValueOrDefault(Object nodeU, Object nodeV, @Nullable V defaultValue) {
            return this.graph.edgeValueOrDefault(nodeV, nodeU, defaultValue);
        }
    }

    private static class TransposedGraph<N>
    extends AbstractGraph<N> {
        private final Graph<N> graph;

        TransposedGraph(Graph<N> graph) {
            this.graph = graph;
        }

        @Override
        public Set<N> nodes() {
            return this.graph.nodes();
        }

        @Override
        protected long edgeCount() {
            return this.graph.edges().size();
        }

        @Override
        public boolean isDirected() {
            return this.graph.isDirected();
        }

        @Override
        public boolean allowsSelfLoops() {
            return this.graph.allowsSelfLoops();
        }

        @Override
        public ElementOrder<N> nodeOrder() {
            return this.graph.nodeOrder();
        }

        @Override
        public Set<N> adjacentNodes(Object node) {
            return this.graph.adjacentNodes(node);
        }

        @Override
        public Set<N> predecessors(Object node) {
            return this.graph.successors(node);
        }

        @Override
        public Set<N> successors(Object node) {
            return this.graph.predecessors(node);
        }
    }
}

