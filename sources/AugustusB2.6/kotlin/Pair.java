// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import kotlin.jvm.internal.Intrinsics;
import java.io.Serializable;

public final class Pair<A, B> implements Serializable
{
    private final A first;
    private final B second;
    
    @Override
    public final String toString() {
        return "(" + this.first + ", " + this.second + ')';
    }
    
    public Pair(final A first, final B second) {
        this.first = first;
        this.second = second;
    }
    
    public final A component1() {
        return this.first;
    }
    
    public final B component2() {
        return this.second;
    }
    
    @Override
    public final int hashCode() {
        final A first = this.first;
        final int n = ((first != null) ? first.hashCode() : 0) * 31;
        final B second = this.second;
        return n + ((second != null) ? second.hashCode() : 0);
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof Pair) {
                final Pair pair = (Pair)o;
                if (Intrinsics.areEqual(this.first, pair.first) && Intrinsics.areEqual(this.second, pair.second)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
