package io.netty.channel.socket;

import io.netty.channel.ServerChannel;
import java.net.InetSocketAddress;

public interface ServerSocketChannel extends ServerChannel {
   ServerSocketChannelConfig config();

   InetSocketAddress localAddress();

   InetSocketAddress remoteAddress();
}
