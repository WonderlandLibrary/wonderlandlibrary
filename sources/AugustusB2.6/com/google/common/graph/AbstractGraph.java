// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import java.util.Set;
import javax.annotation.CheckForNull;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
public abstract class AbstractGraph<N> extends AbstractBaseGraph<N> implements Graph<N>
{
    @Override
    public final boolean equals(@CheckForNull final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Graph)) {
            return false;
        }
        final Graph<?> other = (Graph<?>)obj;
        return this.isDirected() == other.isDirected() && this.nodes().equals(other.nodes()) && this.edges().equals(other.edges());
    }
    
    @Override
    public final int hashCode() {
        return this.edges().hashCode();
    }
    
    @Override
    public String toString() {
        final boolean directed = this.isDirected();
        final boolean allowsSelfLoops = this.allowsSelfLoops();
        final String value = String.valueOf(this.nodes());
        final String value2 = String.valueOf(this.edges());
        return new StringBuilder(59 + String.valueOf(value).length() + String.valueOf(value2).length()).append("isDirected: ").append(directed).append(", allowsSelfLoops: ").append(allowsSelfLoops).append(", nodes: ").append(value).append(", edges: ").append(value2).toString();
    }
}
