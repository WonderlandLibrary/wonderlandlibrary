// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import me.gong.mcleaks.util.google.common.math.IntMath;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.primitives.Ints;
import me.gong.mcleaks.util.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public abstract class AbstractGraph<N> implements Graph<N>
{
    protected long edgeCount() {
        long degreeSum = 0L;
        for (final N node : this.nodes()) {
            degreeSum += this.degree(node);
        }
        Preconditions.checkState((degreeSum & 0x1L) == 0x0L);
        return degreeSum >>> 1;
    }
    
    @Override
    public Set<EndpointPair<N>> edges() {
        return new AbstractSet<EndpointPair<N>>() {
            @Override
            public UnmodifiableIterator<EndpointPair<N>> iterator() {
                return (UnmodifiableIterator<EndpointPair<N>>)EndpointPairIterator.of((Graph<Object>)AbstractGraph.this);
            }
            
            @Override
            public int size() {
                return Ints.saturatedCast(AbstractGraph.this.edgeCount());
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
    public int degree(final Object node) {
        if (this.isDirected()) {
            return IntMath.saturatedAdd(this.predecessors(node).size(), this.successors(node).size());
        }
        final Set<N> neighbors = this.adjacentNodes(node);
        final int selfLoopCount = (this.allowsSelfLoops() && neighbors.contains(node)) ? 1 : 0;
        return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
    }
    
    @Override
    public int inDegree(final Object node) {
        return this.isDirected() ? this.predecessors(node).size() : this.degree(node);
    }
    
    @Override
    public int outDegree(final Object node) {
        return this.isDirected() ? this.successors(node).size() : this.degree(node);
    }
    
    @Override
    public String toString() {
        final String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", this.isDirected(), this.allowsSelfLoops());
        return String.format("%s, nodes: %s, edges: %s", propertiesString, this.nodes(), this.edges());
    }
}
