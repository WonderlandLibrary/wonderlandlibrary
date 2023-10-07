// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.marshalling;

import org.jboss.marshalling.Marshaller;
import io.netty.channel.ChannelHandlerContext;

public interface MarshallerProvider
{
    Marshaller getMarshaller(final ChannelHandlerContext p0) throws Exception;
}
