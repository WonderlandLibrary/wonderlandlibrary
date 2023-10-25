/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CanIgnoreReturnValue
 *  com.google.errorprone.annotations.CompatibleWith
 */
package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.graph.Network;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;

@Beta
public interface MutableNetwork<N, E>
extends Network<N, E> {
    @CanIgnoreReturnValue
    public boolean addNode(N var1);

    @CanIgnoreReturnValue
    public boolean addEdge(N var1, N var2, E var3);

    @CanIgnoreReturnValue
    public boolean removeNode(@CompatibleWith(value="N") Object var1);

    @CanIgnoreReturnValue
    public boolean removeEdge(@CompatibleWith(value="E") Object var1);
}

