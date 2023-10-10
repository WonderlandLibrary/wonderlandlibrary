// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
public final class NetworkBuilder<N, E> extends AbstractGraphBuilder<N>
{
    boolean allowsParallelEdges;
    ElementOrder<? super E> edgeOrder;
    Optional<Integer> expectedEdgeCount;
    
    private NetworkBuilder(final boolean directed) {
        super(directed);
        this.allowsParallelEdges = false;
        this.edgeOrder = ElementOrder.insertion();
        this.expectedEdgeCount = Optional.absent();
    }
    
    public static NetworkBuilder<Object, Object> directed() {
        return new NetworkBuilder<Object, Object>(true);
    }
    
    public static NetworkBuilder<Object, Object> undirected() {
        return new NetworkBuilder<Object, Object>(false);
    }
    
    public static <N, E> NetworkBuilder<N, E> from(final Network<N, E> network) {
        return new NetworkBuilder<Object, Object>(network.isDirected()).allowsParallelEdges(network.allowsParallelEdges()).allowsSelfLoops(network.allowsSelfLoops()).nodeOrder(network.nodeOrder()).edgeOrder(network.edgeOrder());
    }
    
    public <N1 extends N, E1 extends E> ImmutableNetwork.Builder<N1, E1> immutable() {
        final NetworkBuilder<N1, E1> castBuilder = this.cast();
        return new ImmutableNetwork.Builder<N1, E1>(castBuilder);
    }
    
    public NetworkBuilder<N, E> allowsParallelEdges(final boolean allowsParallelEdges) {
        this.allowsParallelEdges = allowsParallelEdges;
        return this;
    }
    
    public NetworkBuilder<N, E> allowsSelfLoops(final boolean allowsSelfLoops) {
        this.allowsSelfLoops = allowsSelfLoops;
        return this;
    }
    
    public NetworkBuilder<N, E> expectedNodeCount(final int expectedNodeCount) {
        this.expectedNodeCount = Optional.of(Graphs.checkNonNegative(expectedNodeCount));
        return this;
    }
    
    public NetworkBuilder<N, E> expectedEdgeCount(final int expectedEdgeCount) {
        this.expectedEdgeCount = Optional.of(Graphs.checkNonNegative(expectedEdgeCount));
        return this;
    }
    
    public <N1 extends N> NetworkBuilder<N1, E> nodeOrder(final ElementOrder<N1> nodeOrder) {
        final NetworkBuilder<N1, E> newBuilder = this.cast();
        newBuilder.nodeOrder = Preconditions.checkNotNull((ElementOrder<N>)nodeOrder);
        return newBuilder;
    }
    
    public <E1 extends E> NetworkBuilder<N, E1> edgeOrder(final ElementOrder<E1> edgeOrder) {
        final NetworkBuilder<N, E1> newBuilder = this.cast();
        newBuilder.edgeOrder = Preconditions.checkNotNull((ElementOrder<? super E>)edgeOrder);
        return newBuilder;
    }
    
    public <N1 extends N, E1 extends E> MutableNetwork<N1, E1> build() {
        return new StandardMutableNetwork<N1, E1>(this);
    }
    
    private <N1 extends N, E1 extends E> NetworkBuilder<N1, E1> cast() {
        return (NetworkBuilder<N1, E1>)this;
    }
}
