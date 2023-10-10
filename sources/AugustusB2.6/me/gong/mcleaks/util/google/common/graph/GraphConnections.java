// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.util.Set;

interface GraphConnections<N, V>
{
    Set<N> adjacentNodes();
    
    Set<N> predecessors();
    
    Set<N> successors();
    
    @Nullable
    V value(final Object p0);
    
    void removePredecessor(final Object p0);
    
    @CanIgnoreReturnValue
    V removeSuccessor(final Object p0);
    
    void addPredecessor(final N p0, final V p1);
    
    @CanIgnoreReturnValue
    V addSuccessor(final N p0, final V p1);
}
