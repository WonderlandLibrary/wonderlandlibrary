// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

public final class NoOpTypeParameterMatcher extends TypeParameterMatcher
{
    @Override
    public boolean match(final Object msg) {
        return true;
    }
}
