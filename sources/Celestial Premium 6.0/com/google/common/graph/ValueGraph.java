/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CompatibleWith
 */
package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.graph.Graph;
import com.google.errorprone.annotations.CompatibleWith;
import javax.annotation.Nullable;

@Beta
public interface ValueGraph<N, V>
extends Graph<N> {
    public V edgeValue(@CompatibleWith(value="N") Object var1, @CompatibleWith(value="N") Object var2);

    public V edgeValueOrDefault(@CompatibleWith(value="N") Object var1, @CompatibleWith(value="N") Object var2, @Nullable V var3);

    @Override
    public boolean equals(@Nullable Object var1);

    @Override
    public int hashCode();
}

