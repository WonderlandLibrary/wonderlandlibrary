/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CompatibleWith
 */
package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public interface Network<N, E> {
    public Set<N> nodes();

    public Set<E> edges();

    public Graph<N> asGraph();

    public boolean isDirected();

    public boolean allowsParallelEdges();

    public boolean allowsSelfLoops();

    public ElementOrder<N> nodeOrder();

    public ElementOrder<E> edgeOrder();

    public Set<N> adjacentNodes(@CompatibleWith(value="N") Object var1);

    public Set<N> predecessors(@CompatibleWith(value="N") Object var1);

    public Set<N> successors(@CompatibleWith(value="N") Object var1);

    public Set<E> incidentEdges(@CompatibleWith(value="N") Object var1);

    public Set<E> inEdges(@CompatibleWith(value="N") Object var1);

    public Set<E> outEdges(@CompatibleWith(value="N") Object var1);

    public int degree(@CompatibleWith(value="N") Object var1);

    public int inDegree(@CompatibleWith(value="N") Object var1);

    public int outDegree(@CompatibleWith(value="N") Object var1);

    public EndpointPair<N> incidentNodes(@CompatibleWith(value="E") Object var1);

    public Set<E> adjacentEdges(@CompatibleWith(value="E") Object var1);

    public Set<E> edgesConnecting(@CompatibleWith(value="N") Object var1, @CompatibleWith(value="N") Object var2);

    public boolean equals(@Nullable Object var1);

    public int hashCode();
}

