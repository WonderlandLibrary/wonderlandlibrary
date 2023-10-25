package com.github.creeper123123321.viafabric.platform;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import us.myles.ViaVersion.api.data.UserConnection;

public class VRClientSideUserConnection extends UserConnection {
  public VRClientSideUserConnection(Channel socketChannel) {
    super(socketChannel);
  }
  
  public void sendRawPacket(ByteBuf packet, boolean currentThread) {
    Runnable act = () -> getChannel().pipeline().context("via-decoder").fireChannelRead(packet);
    if (currentThread) {
      act.run();
    } else {
      getChannel().eventLoop().execute(act);
    } 
  }
  
  public ChannelFuture sendRawPacketFuture(ByteBuf packet) {
    getChannel().pipeline().context("via-decoder").fireChannelRead(packet);
    return getChannel().newSucceededFuture();
  }
  
  public void sendRawPacketToServer(ByteBuf packet, boolean currentThread) {
    if (currentThread) {
      getChannel().pipeline().context("via-encoder").writeAndFlush(packet);
    } else {
      getChannel().eventLoop().submit(() -> getChannel().pipeline().context("via-encoder").writeAndFlush(packet));
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabric\platform\VRClientSideUserConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */