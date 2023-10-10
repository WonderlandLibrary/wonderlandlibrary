// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock
@ElementTypesAreNonnullByDefault
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
        return (GraphBuilder<N>)new GraphBuilder(graph.isDirected()).allowsSelfLoops(graph.allowsSelfLoops()).nodeOrder(graph.nodeOrder()).incidentEdgeOrder(graph.incidentEdgeOrder());
    }
    
    public <N1 extends N> ImmutableGraph.Builder<N1> immutable() {
        final GraphBuilder<N1> castBuilder = this.cast();
        return new ImmutableGraph.Builder<N1>(castBuilder);
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
    
    public <N1 extends N> GraphBuilder<N1> incidentEdgeOrder(final ElementOrder<N1> incidentEdgeOrder) {
        Preconditions.checkArgument(incidentEdgeOrder.type() == ElementOrder.Type.UNORDERED || incidentEdgeOrder.type() == ElementOrder.Type.STABLE, "The given elementOrder (%s) is unsupported. incidentEdgeOrder() only supports ElementOrder.unordered() and ElementOrder.stable().", incidentEdgeOrder);
        final GraphBuilder<N1> newBuilder = this.cast();
        newBuilder.incidentEdgeOrder = (ElementOrder<N1>)Preconditions.checkNotNull((ElementOrder<N>)incidentEdgeOrder);
        return newBuilder;
    }
    
    public <N1 extends N> MutableGraph<N1> build() {
        return new StandardMutableGraph<N1>(this);
    }
    
    GraphBuilder<N> copy() {
        final GraphBuilder<N> newBuilder = new GraphBuilder<N>(this.directed);
        newBuilder.allowsSelfLoops = this.allowsSelfLoops;
        newBuilder.nodeOrder = this.nodeOrder;
        newBuilder.expectedNodeCount = this.expectedNodeCount;
        newBuilder.incidentEdgeOrder = this.incidentEdgeOrder;
        return newBuilder;
    }
    
    private <N1 extends N> GraphBuilder<N1> cast() {
        return (GraphBuilder<N1>)this;
    }
}
