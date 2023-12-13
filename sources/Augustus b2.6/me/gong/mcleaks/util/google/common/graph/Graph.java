// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.errorprone.annotations.CompatibleWith;
import java.util.Set;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public interface Graph<N>
{
    Set<N> nodes();
    
    Set<EndpointPair<N>> edges();
    
    boolean isDirected();
    
    boolean allowsSelfLoops();
    
    ElementOrder<N> nodeOrder();
    
    Set<N> adjacentNodes(@CompatibleWith("N") final Object p0);
    
    Set<N> predecessors(@CompatibleWith("N") final Object p0);
    
    Set<N> successors(@CompatibleWith("N") final Object p0);
    
    int degree(@CompatibleWith("N") final Object p0);
    
    int inDegree(@CompatibleWith("N") final Object p0);
    
    int outDegree(@CompatibleWith("N") final Object p0);
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
}
