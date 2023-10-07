// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type;

import io.netty.buffer.ByteBuf;

@FunctionalInterface
public interface ByteBufWriter<T>
{
    void write(final ByteBuf p0, final T p1) throws Exception;
}
