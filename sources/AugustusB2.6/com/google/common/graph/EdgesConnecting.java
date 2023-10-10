// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import java.util.Iterator;
import javax.annotation.CheckForNull;
import com.google.common.collect.Iterators;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.AbstractSet;

@ElementTypesAreNonnullByDefault
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
    public boolean contains(@CheckForNull final Object edge) {
        final E connectingEdge = this.getConnectingEdge();
        return connectingEdge != null && connectingEdge.equals(edge);
    }
    
    @CheckForNull
    private E getConnectingEdge() {
        return this.nodeToOutEdge.get(this.targetNode);
    }
}
