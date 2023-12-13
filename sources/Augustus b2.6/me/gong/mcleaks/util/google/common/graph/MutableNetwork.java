// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import me.gong.mcleaks.util.google.errorprone.annotations.CompatibleWith;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public interface MutableNetwork<N, E> extends Network<N, E>
{
    @CanIgnoreReturnValue
    boolean addNode(final N p0);
    
    @CanIgnoreReturnValue
    boolean addEdge(final N p0, final N p1, final E p2);
    
    @CanIgnoreReturnValue
    boolean removeNode(@CompatibleWith("N") final Object p0);
    
    @CanIgnoreReturnValue
    boolean removeEdge(@CompatibleWith("E") final Object p0);
}
