// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.base.Optional;

@ElementTypesAreNonnullByDefault
abstract class AbstractGraphBuilder<N>
{
    final boolean directed;
    boolean allowsSelfLoops;
    ElementOrder<N> nodeOrder;
    ElementOrder<N> incidentEdgeOrder;
    Optional<Integer> expectedNodeCount;
    
    AbstractGraphBuilder(final boolean directed) {
        this.allowsSelfLoops = false;
        this.nodeOrder = ElementOrder.insertion();
        this.incidentEdgeOrder = ElementOrder.unordered();
        this.expectedNodeCount = Optional.absent();
        this.directed = directed;
    }
}
