// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.base.Optional;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public final class GraphBuilder<N> extends AbstractGraphBuilder<N>
{
    private GraphBuilder(final boolean directed) {
        super(directed);
    }
    
    public static GraphBuilder<Object> directed() {
        return new GraphBuilder<Object>(true);
    }
    
    public static GraphBuilder<Object> undirected() {
        return new GraphBuilder<Object>(false);
    }
    
    public static <N> GraphBuilder<N> from(final Graph<N> graph) {
        return (GraphBuilder<N>)new GraphBuilder(graph.isDirected()).allowsSelfLoops(graph.allowsSelfLoops()).nodeOrder(graph.nodeOrder());
    }
    
    public GraphBuilder<N> allowsSelfLoops(final boolean allowsSelfLoops) {
        this.allowsSelfLoops = allowsSelfLoops;
        return this;
    }
    
    public GraphBuilder<N> expectedNodeCount(final int expectedNodeCount) {
        this.expectedNodeCount = Optional.of(Graphs.checkNonNegative(expectedNodeCount));
        return this;
    }
    
    public <N1 extends N> GraphBuilder<N1> nodeOrder(final ElementOrder<N1> nodeOrder) {
        final GraphBuilder<N1> newBuilder = this.cast();
        newBuilder.nodeOrder = (ElementOrder<N1>)Preconditions.checkNotNull((ElementOrder<N>)nodeOrder);
        return newBuilder;
    }
    
    public <N1 extends N> MutableGraph<N1> build() {
        return new ConfigurableMutableGraph<N1>(this);
    }
    
    private <N1 extends N> GraphBuilder<N1> cast() {
        return (GraphBuilder<N1>)this;
    }
}
