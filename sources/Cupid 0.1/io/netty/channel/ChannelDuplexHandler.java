package io.netty.channel;

import java.net.SocketAddress;

public class ChannelDuplexHandler extends ChannelInboundHandlerAdapter implements ChannelOutboundHandler {
  public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise future) throws Exception {
    ctx.bind(localAddress, future);
  }
  
  public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise future) throws Exception {
    ctx.connect(remoteAddress, localAddress, future);
  }
  
  public void disconnect(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
    ctx.disconnect(future);
  }
  
  public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
    ctx.close(future);
  }
  
  public void deregister(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
    ctx.deregister(future);
  }
  
  public void read(ChannelHandlerContext ctx) throws Exception {
    ctx.read();
  }
  
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    ctx.write(msg, promise);
  }
  
  public void flush(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\ChannelDuplexHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */