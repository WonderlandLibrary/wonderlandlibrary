// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.errorprone.annotations.CompatibleWith;
import java.util.Set;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public interface Network<N, E>
{
    Set<N> nodes();
    
    Set<E> edges();
    
    Graph<N> asGraph();
    
    boolean isDirected();
    
    boolean allowsParallelEdges();
    
    boolean allowsSelfLoops();
    
    ElementOrder<N> nodeOrder();
    
    ElementOrder<E> edgeOrder();
    
    Set<N> adjacentNodes(@CompatibleWith("N") final Object p0);
    
    Set<N> predecessors(@CompatibleWith("N") final Object p0);
    
    Set<N> successors(@CompatibleWith("N") final Object p0);
    
    Set<E> incidentEdges(@CompatibleWith("N") final Object p0);
    
    Set<E> inEdges(@CompatibleWith("N") final Object p0);
    
    Set<E> outEdges(@CompatibleWith("N") final Object p0);
    
    int degree(@CompatibleWith("N") final Object p0);
    
    int inDegree(@CompatibleWith("N") final Object p0);
    
    int outDegree(@CompatibleWith("N") final Object p0);
    
    EndpointPair<N> incidentNodes(@CompatibleWith("E") final Object p0);
    
    Set<E> adjacentEdges(@CompatibleWith("E") final Object p0);
    
    Set<E> edgesConnecting(@CompatibleWith("N") final Object p0, @CompatibleWith("N") final Object p1);
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
}
