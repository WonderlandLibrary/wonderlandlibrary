/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CanIgnoreReturnValue
 */
package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;

interface NetworkConnections<N, E> {
    public Set<N> adjacentNodes();

    public Set<N> predecessors();

    public Set<N> successors();

    public Set<E> incidentEdges();

    public Set<E> inEdges();

    public Set<E> outEdges();

    public Set<E> edgesConnecting(Object var1);

    public N oppositeNode(Object var1);

    @CanIgnoreReturnValue
    public N removeInEdge(Object var1, boolean var2);

    @CanIgnoreReturnValue
    public N removeOutEdge(Object var1);

    public void addInEdge(E var1, N var2, boolean var3);

    public void addOutEdge(E var1, N var2);
}

