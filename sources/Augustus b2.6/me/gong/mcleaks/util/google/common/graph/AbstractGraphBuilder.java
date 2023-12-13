// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import me.gong.mcleaks.util.google.common.base.Optional;

abstract class AbstractGraphBuilder<N>
{
    final boolean directed;
    boolean allowsSelfLoops;
    ElementOrder<N> nodeOrder;
    Optional<Integer> expectedNodeCount;
    
    AbstractGraphBuilder(final boolean directed) {
        this.allowsSelfLoops = false;
        this.nodeOrder = ElementOrder.insertion();
        this.expectedNodeCount = Optional.absent();
        this.directed = directed;
    }
}
