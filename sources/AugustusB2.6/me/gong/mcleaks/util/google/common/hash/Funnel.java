// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.hash;

import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.io.Serializable;

@Beta
public interface Funnel<T> extends Serializable
{
    void funnel(final T p0, final PrimitiveSink p1);
}
