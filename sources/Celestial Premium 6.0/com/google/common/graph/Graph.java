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
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public interface Graph<N> {
    public Set<N> nodes();

    public Set<EndpointPair<N>> edges();

    public boolean isDirected();

    public boolean allowsSelfLoops();

    public ElementOrder<N> nodeOrder();

    public Set<N> adjacentNodes(@CompatibleWith(value="N") Object var1);

    public Set<N> predecessors(@CompatibleWith(value="N") Object var1);

    public Set<N> successors(@CompatibleWith(value="N") Object var1);

    public int degree(@CompatibleWith(value="N") Object var1);

    public int inDegree(@CompatibleWith(value="N") Object var1);

    public int outDegree(@CompatibleWith(value="N") Object var1);

    public boolean equals(@Nullable Object var1);

    public int hashCode();
}

