// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.functions;

import kotlin.Function;

public interface Function2<P1, P2, R> extends Function<R>
{
    R invoke(final P1 p0, final P2 p1);
}
