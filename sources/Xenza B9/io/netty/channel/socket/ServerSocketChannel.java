// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.socket;

import java.net.InetSocketAddress;
import io.netty.channel.ServerChannel;

public interface ServerSocketChannel extends ServerChannel
{
    ServerSocketChannelConfig config();
    
    InetSocketAddress localAddress();
    
    InetSocketAddress remoteAddress();
}
