/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CanIgnoreReturnValue
 *  com.google.errorprone.annotations.CompatibleWith
 */
package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.graph.ValueGraph;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;

@Beta
public interface MutableValueGraph<N, V>
extends ValueGraph<N, V> {
    @CanIgnoreReturnValue
    public boolean addNode(N var1);

    @CanIgnoreReturnValue
    public V putEdgeValue(N var1, N var2, V var3);

    @CanIgnoreReturnValue
    public boolean removeNode(@CompatibleWith(value="N") Object var1);

    @CanIgnoreReturnValue
    public V removeEdge(@CompatibleWith(value="N") Object var1, @CompatibleWith(value="N") Object var2);
}

