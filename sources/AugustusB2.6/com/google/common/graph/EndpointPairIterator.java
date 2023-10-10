// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import javax.annotation.CheckForNull;
import java.util.Iterator;
import com.google.common.collect.AbstractIterator;

@ElementTypesAreNonnullByDefault
abstract class EndpointPairIterator<N> extends AbstractIterator<EndpointPair<N>>
{
    private final BaseGraph<N> graph;
    private final Iterator<N> nodeIterator;
    @CheckForNull
    N node;
    Iterator<N> successorIterator;
    
    static <N> EndpointPairIterator<N> of(final BaseGraph<N> graph) {
        return (EndpointPairIterator<N>)(graph.isDirected() ? new Directed<Object>((BaseGraph)graph) : new Undirected<Object>((BaseGraph)graph));
    }
    
    private EndpointPairIterator(final BaseGraph<N> graph) {
        this.node = null;
        this.successorIterator = (Iterator<N>)ImmutableSet.of().iterator();
        this.graph = graph;
        this.nodeIterator = graph.nodes().iterator();
    }
    
    final boolean advance() {
        Preconditions.checkState(!this.successorIterator.hasNext());
        if (!this.nodeIterator.hasNext()) {
            return false;
        }
        this.node = this.nodeIterator.next();
        this.successorIterator = this.graph.successors(this.node).iterator();
        return true;
    }
    
    private static final class Directed<N> extends EndpointPairIterator<N>
    {
        private Directed(final BaseGraph<N> graph) {
            super(graph, null);
        }
        
        @CheckForNull
        @Override
        protected EndpointPair<N> computeNext() {
            while (!this.successorIterator.hasNext()) {
                if (!this.advance()) {
                    return (EndpointPair<N>)this.endOfData();
                }
            }
            return EndpointPair.ordered((N)Objects.requireNonNull((N)this.node), this.successorIterator.next());
        }
    }
    
    private static final class Undirected<N> extends EndpointPairIterator<N>
    {
        @CheckForNull
        private Set<N> visitedNodes;
        
        private Undirected(final BaseGraph<N> graph) {
            super(graph, null);
            this.visitedNodes = (Set<N>)Sets.newHashSetWithExpectedSize(graph.nodes().size() + 1);
        }
        
        @CheckForNull
        @Override
        protected EndpointPair<N> computeNext() {
            do {
                Objects.requireNonNull(this.visitedNodes);
                while (this.successorIterator.hasNext()) {
                    final N otherNode = this.successorIterator.next();
                    if (!this.visitedNodes.contains(otherNode)) {
                        return EndpointPair.unordered((N)Objects.requireNonNull((N)this.node), otherNode);
                    }
                }
                this.visitedNodes.add(this.node);
            } while (this.advance());
            this.visitedNodes = null;
            return (EndpointPair<N>)this.endOfData();
        }
    }
}
