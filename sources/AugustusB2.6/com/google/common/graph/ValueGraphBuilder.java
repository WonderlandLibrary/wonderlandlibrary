// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
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
    
    public static <N, V> ValueGraphBuilder<N, V> from(final ValueGraph<N, V> graph) {
        return new ValueGraphBuilder<Object, V>(graph.isDirected()).allowsSelfLoops(graph.allowsSelfLoops()).nodeOrder(graph.nodeOrder()).incidentEdgeOrder(graph.incidentEdgeOrder());
    }
    
    public <N1 extends N, V1 extends V> ImmutableValueGraph.Builder<N1, V1> immutable() {
        final ValueGraphBuilder<N1, V1> castBuilder = this.cast();
        return new ImmutableValueGraph.Builder<N1, V1>(castBuilder);
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
    
    public <N1 extends N> ValueGraphBuilder<N1, V> incidentEdgeOrder(final ElementOrder<N1> incidentEdgeOrder) {
        Preconditions.checkArgument(incidentEdgeOrder.type() == ElementOrder.Type.UNORDERED || incidentEdgeOrder.type() == ElementOrder.Type.STABLE, "The given elementOrder (%s) is unsupported. incidentEdgeOrder() only supports ElementOrder.unordered() and ElementOrder.stable().", incidentEdgeOrder);
        final ValueGraphBuilder<N1, V> newBuilder = this.cast();
        newBuilder.incidentEdgeOrder = Preconditions.checkNotNull((ElementOrder<N>)incidentEdgeOrder);
        return newBuilder;
    }
    
    public <N1 extends N, V1 extends V> MutableValueGraph<N1, V1> build() {
        return new StandardMutableValueGraph<N1, V1>(this);
    }
    
    ValueGraphBuilder<N, V> copy() {
        final ValueGraphBuilder<N, V> newBuilder = new ValueGraphBuilder<N, V>(this.directed);
        newBuilder.allowsSelfLoops = this.allowsSelfLoops;
        newBuilder.nodeOrder = this.nodeOrder;
        newBuilder.expectedNodeCount = this.expectedNodeCount;
        newBuilder.incidentEdgeOrder = this.incidentEdgeOrder;
        return newBuilder;
    }
    
    private <N1 extends N, V1 extends V> ValueGraphBuilder<N1, V1> cast() {
        return (ValueGraphBuilder<N1, V1>)this;
    }
}
