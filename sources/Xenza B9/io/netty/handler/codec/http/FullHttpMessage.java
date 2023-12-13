// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

public interface FullHttpMessage extends HttpMessage, LastHttpContent
{
    FullHttpMessage copy();
    
    FullHttpMessage retain(final int p0);
    
    FullHttpMessage retain();
}
