// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type;

import io.netty.buffer.ByteBuf;

public interface ByteBufReader<T>
{
    T read(final ByteBuf p0) throws Exception;
}
