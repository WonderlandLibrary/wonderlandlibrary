// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.errorprone.annotations.CompatibleWith;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public interface ValueGraph<N, V> extends Graph<N>
{
    V edgeValue(@CompatibleWith("N") final Object p0, @CompatibleWith("N") final Object p1);
    
    V edgeValueOrDefault(@CompatibleWith("N") final Object p0, @CompatibleWith("N") final Object p1, @Nullable final V p2);
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
}
