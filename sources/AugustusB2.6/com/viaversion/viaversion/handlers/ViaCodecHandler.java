// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;

public interface ViaCodecHandler
{
    void transform(final ByteBuf p0) throws Exception;
    
    void exceptionCaught(final ChannelHandlerContext p0, final Throwable p1) throws Exception;
}
