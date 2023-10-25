package de.gerrygames.viarewind.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

public class EmptyChannelHandler implements ChannelHandler {
  public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {}
  
  public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {}
  
  public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\netty\EmptyChannelHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */