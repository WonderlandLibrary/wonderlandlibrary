// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.base.Optional;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public final class ValueGraphBuilder<N, V> extends AbstractGraphBuilder<N>
{
    private ValueGraphBuilder(final boolean directed) {
        super(directed);
    }
    
    public static ValueGraphBuilder<Object, Object> directed() {
        return new ValueGraphBuilder<Object, Object>(true);
    }
    
    public static ValueGraphBuilder<Object, Object> undirected() {
        return new ValueGraphBuilder<Object, Object>(false);
    }
    
    public static <N> ValueGraphBuilder<N, Object> from(final Graph<N> graph) {
        return (ValueGraphBuilder<N, Object>)new ValueGraphBuilder<Object, V>(graph.isDirected()).allowsSelfLoops(graph.allowsSelfLoops()).nodeOrder(graph.nodeOrder());
    }
    
    public ValueGraphBuilder<N, V> allowsSelfLoops(final boolean allowsSelfLoops) {
        this.allowsSelfLoops = allowsSelfLoops;
        return this;
    }
    
    public ValueGraphBuilder<N, V> expectedNodeCount(final int expectedNodeCount) {
        this.expectedNodeCount = Optional.of(Graphs.checkNonNegative(expectedNodeCount));
        return this;
    }
    
    public <N1 extends N> ValueGraphBuilder<N1, V> nodeOrder(final ElementOrder<N1> nodeOrder) {
        final ValueGraphBuilder<N1, V> newBuilder = this.cast();
        newBuilder.nodeOrder = Preconditions.checkNotNull((ElementOrder<N>)nodeOrder);
        return newBuilder;
    }
    
    public <N1 extends N, V1 extends V> MutableValueGraph<N1, V1> build() {
        return new ConfigurableMutableValueGraph<N1, V1>(this);
    }
    
    private <N1 extends N, V1 extends V> ValueGraphBuilder<N1, V1> cast() {
        return (ValueGraphBuilder<N1, V1>)this;
    }
}
