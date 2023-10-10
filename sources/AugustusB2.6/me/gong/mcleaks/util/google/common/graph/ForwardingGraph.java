// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import java.util.Set;

abstract class ForwardingGraph<N> extends AbstractGraph<N>
{
    protected abstract Graph<N> delegate();
    
    @Override
    public Set<N> nodes() {
        return this.delegate().nodes();
    }
    
    @Override
    public Set<EndpointPair<N>> edges() {
        return this.delegate().edges();
    }
    
    @Override
    public boolean isDirected() {
        return this.delegate().isDirected();
    }
    
    @Override
    public boolean allowsSelfLoops() {
        return this.delegate().allowsSelfLoops();
    }
    
    @Override
    public ElementOrder<N> nodeOrder() {
        return this.delegate().nodeOrder();
    }
    
    @Override
    public Set<N> adjacentNodes(final Object node) {
        return this.delegate().adjacentNodes(node);
    }
    
    @Override
    public Set<N> predecessors(final Object node) {
        return this.delegate().predecessors(node);
    }
    
    @Override
    public Set<N> successors(final Object node) {
        return this.delegate().successors(node);
    }
    
    @Override
    public int degree(final Object node) {
        return this.delegate().degree(node);
    }
    
    @Override
    public int inDegree(final Object node) {
        return this.delegate().inDegree(node);
    }
    
    @Override
    public int outDegree(final Object node) {
        return this.delegate().outDegree(node);
    }
}
