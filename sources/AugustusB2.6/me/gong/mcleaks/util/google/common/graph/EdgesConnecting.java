// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import java.util.Iterator;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.collect.Iterators;
import me.gong.mcleaks.util.google.common.collect.ImmutableSet;
import me.gong.mcleaks.util.google.common.collect.UnmodifiableIterator;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Map;
import java.util.AbstractSet;

final class EdgesConnecting<E> extends AbstractSet<E>
{
    private final Map<?, E> nodeToOutEdge;
    private final Object targetNode;
    
    EdgesConnecting(final Map<?, E> nodeToEdgeMap, final Object targetNode) {
        this.nodeToOutEdge = Preconditions.checkNotNull(nodeToEdgeMap);
        this.targetNode = Preconditions.checkNotNull(targetNode);
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        final E connectingEdge = this.getConnectingEdge();
        return (connectingEdge == null) ? ImmutableSet.of().iterator() : Iterators.singletonIterator(connectingEdge);
    }
    
    @Override
    public int size() {
        return (this.getConnectingEdge() != null) ? 1 : 0;
    }
    
    @Override
    public boolean contains(@Nullable final Object edge) {
        final E connectingEdge = this.getConnectingEdge();
        return connectingEdge != null && connectingEdge.equals(edge);
    }
    
    @Nullable
    private E getConnectingEdge() {
        return this.nodeToOutEdge.get(this.targetNode);
    }
}
