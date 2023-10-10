// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import me.gong.mcleaks.util.google.common.base.Objects;
import java.util.Iterator;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.collect.Iterators;
import me.gong.mcleaks.util.google.common.collect.UnmodifiableIterator;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public abstract class EndpointPair<N> implements Iterable<N>
{
    private final N nodeU;
    private final N nodeV;
    
    private EndpointPair(final N nodeU, final N nodeV) {
        this.nodeU = Preconditions.checkNotNull(nodeU);
        this.nodeV = Preconditions.checkNotNull(nodeV);
    }
    
    public static <N> EndpointPair<N> ordered(final N source, final N target) {
        return new Ordered<N>((Object)source, (Object)target);
    }
    
    public static <N> EndpointPair<N> unordered(final N nodeU, final N nodeV) {
        return new Unordered<N>((Object)nodeV, (Object)nodeU);
    }
    
    static <N> EndpointPair<N> of(final Graph<?> graph, final N nodeU, final N nodeV) {
        return graph.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
    }
    
    static <N> EndpointPair<N> of(final Network<?, ?> network, final N nodeU, final N nodeV) {
        return network.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
    }
    
    public abstract N source();
    
    public abstract N target();
    
    public final N nodeU() {
        return this.nodeU;
    }
    
    public final N nodeV() {
        return this.nodeV;
    }
    
    public final N adjacentNode(final Object node) {
        if (node.equals(this.nodeU)) {
            return this.nodeV;
        }
        if (node.equals(this.nodeV)) {
            return this.nodeU;
        }
        throw new IllegalArgumentException(String.format("EndpointPair %s does not contain node %s", this, node));
    }
    
    public abstract boolean isOrdered();
    
    @Override
    public final UnmodifiableIterator<N> iterator() {
        return Iterators.forArray(this.nodeU, this.nodeV);
    }
    
    @Override
    public abstract boolean equals(@Nullable final Object p0);
    
    @Override
    public abstract int hashCode();
    
    private static final class Ordered<N> extends EndpointPair<N>
    {
        private Ordered(final N source, final N target) {
            super(source, target, null);
        }
        
        @Override
        public N source() {
            return this.nodeU();
        }
        
        @Override
        public N target() {
            return this.nodeV();
        }
        
        @Override
        public boolean isOrdered() {
            return true;
        }
        
        @Override
        public boolean equals(@Nullable final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof EndpointPair)) {
                return false;
            }
            final EndpointPair<?> other = (EndpointPair<?>)obj;
            return this.isOrdered() == other.isOrdered() && this.source().equals(other.source()) && this.target().equals(other.target());
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.source(), this.target());
        }
        
        @Override
        public String toString() {
            return String.format("<%s -> %s>", this.source(), this.target());
        }
    }
    
    private static final class Unordered<N> extends EndpointPair<N>
    {
        private Unordered(final N nodeU, final N nodeV) {
            super(nodeU, nodeV, null);
        }
        
        @Override
        public N source() {
            throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
        }
        
        @Override
        public N target() {
            throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
        }
        
        @Override
        public boolean isOrdered() {
            return false;
        }
        
        @Override
        public boolean equals(@Nullable final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof EndpointPair)) {
                return false;
            }
            final EndpointPair<?> other = (EndpointPair<?>)obj;
            if (this.isOrdered() != other.isOrdered()) {
                return false;
            }
            if (this.nodeU().equals(other.nodeU())) {
                return this.nodeV().equals(other.nodeV());
            }
            return this.nodeU().equals(other.nodeV()) && this.nodeV().equals(other.nodeU());
        }
        
        @Override
        public int hashCode() {
            return this.nodeU().hashCode() + this.nodeV().hashCode();
        }
        
        @Override
        public String toString() {
            return String.format("[%s, %s]", this.nodeU(), this.nodeV());
        }
    }
}
