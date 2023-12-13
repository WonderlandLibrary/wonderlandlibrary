// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type;

import io.netty.buffer.ByteBuf;

@FunctionalInterface
public interface ByteBufReader<T>
{
    T read(final ByteBuf p0) throws Exception;
}
