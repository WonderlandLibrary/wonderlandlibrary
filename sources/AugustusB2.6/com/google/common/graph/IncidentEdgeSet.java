// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import java.util.Set;
import javax.annotation.CheckForNull;
import java.util.AbstractSet;

@ElementTypesAreNonnullByDefault
abstract class IncidentEdgeSet<N> extends AbstractSet<EndpointPair<N>>
{
    final N node;
    final BaseGraph<N> graph;
    
    IncidentEdgeSet(final BaseGraph<N> graph, final N node) {
        this.graph = graph;
        this.node = node;
    }
    
    @Override
    public boolean remove(@CheckForNull final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int size() {
        if (this.graph.isDirected()) {
            return this.graph.inDegree(this.node) + this.graph.outDegree(this.node) - (this.graph.successors(this.node).contains(this.node) ? 1 : 0);
        }
        return this.graph.adjacentNodes(this.node).size();
    }
    
    @Override
    public boolean contains(@CheckForNull final Object obj) {
        if (!(obj instanceof EndpointPair)) {
            return false;
        }
        final EndpointPair<?> endpointPair = (EndpointPair<?>)obj;
        if (this.graph.isDirected()) {
            if (!endpointPair.isOrdered()) {
                return false;
            }
            final Object source = endpointPair.source();
            final Object target = endpointPair.target();
            return (this.node.equals(source) && this.graph.successors(this.node).contains(target)) || (this.node.equals(target) && this.graph.predecessors(this.node).contains(source));
        }
        else {
            if (endpointPair.isOrdered()) {
                return false;
            }
            final Set<N> adjacent = this.graph.adjacentNodes(this.node);
            final Object nodeU = endpointPair.nodeU();
            final Object nodeV = endpointPair.nodeV();
            return (this.node.equals(nodeV) && adjacent.contains(nodeU)) || (this.node.equals(nodeU) && adjacent.contains(nodeV));
        }
    }
}
