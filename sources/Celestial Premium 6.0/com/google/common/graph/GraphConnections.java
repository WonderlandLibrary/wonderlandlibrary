/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CanIgnoreReturnValue
 */
package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;
import javax.annotation.Nullable;

interface GraphConnections<N, V> {
    public Set<N> adjacentNodes();

    public Set<N> predecessors();

    public Set<N> successors();

    @Nullable
    public V value(Object var1);

    public void removePredecessor(Object var1);

    @CanIgnoreReturnValue
    public V removeSuccessor(Object var1);

    public void addPredecessor(N var1, V var2);

    @CanIgnoreReturnValue
    public V addSuccessor(N var1, V var2);
}

