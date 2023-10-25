/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.EndpointPairIterator;
import com.google.common.graph.Graph;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.util.AbstractSet;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractGraph<N>
implements Graph<N> {
    protected long edgeCount() {
        long degreeSum = 0L;
        for (Object node : this.nodes()) {
            degreeSum += (long)this.degree(node);
        }
        Preconditions.checkState((degreeSum & 1L) == 0L);
        return degreeSum >>> 1;
    }

    @Override
    public Set<EndpointPair<N>> edges() {
        return new AbstractSet<EndpointPair<N>>(){

            @Override
            public UnmodifiableIterator<EndpointPair<N>> iterator() {
                return EndpointPairIterator.of(AbstractGraph.this);
            }

            @Override
            public int size() {
                return Ints.saturatedCast(AbstractGraph.this.edgeCount());
            }

            @Override
            public boolean contains(@Nullable Object obj) {
                if (!(obj instanceof EndpointPair)) {
                    return false;
                }
                EndpointPair endpointPair = (EndpointPair)obj;
                return AbstractGraph.this.isDirected() == endpointPair.isOrdered() && AbstractGraph.this.nodes().contains(endpointPair.nodeU()) && AbstractGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
            }
        };
    }

    @Override
    public int degree(Object node) {
        if (this.isDirected()) {
            return IntMath.saturatedAdd(this.predecessors(node).size(), this.successors(node).size());
        }
        Set neighbors = this.adjacentNodes(node);
        int selfLoopCount = this.allowsSelfLoops() && neighbors.contains(node) ? 1 : 0;
        return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
    }

    @Override
    public int inDegree(Object node) {
        return this.isDirected() ? this.predecessors(node).size() : this.degree(node);
    }

    @Override
    public int outDegree(Object node) {
        return this.isDirected() ? this.successors(node).size() : this.degree(node);
    }

    public String toString() {
        String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", this.isDirected(), this.allowsSelfLoops());
        return String.format("%s, nodes: %s, edges: %s", propertiesString, this.nodes(), this.edges());
    }
}

