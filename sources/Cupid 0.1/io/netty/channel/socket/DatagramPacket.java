package io.netty.channel.socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.util.ReferenceCounted;
import java.net.InetSocketAddress;

public final class DatagramPacket extends DefaultAddressedEnvelope<ByteBuf, InetSocketAddress> implements ByteBufHolder {
  public DatagramPacket(ByteBuf data, InetSocketAddress recipient) {
    super(data, recipient);
  }
  
  public DatagramPacket(ByteBuf data, InetSocketAddress recipient, InetSocketAddress sender) {
    super(data, recipient, sender);
  }
  
  public DatagramPacket copy() {
    return new DatagramPacket(((ByteBuf)content()).copy(), (InetSocketAddress)recipient(), (InetSocketAddress)sender());
  }
  
  public DatagramPacket duplicate() {
    return new DatagramPacket(((ByteBuf)content()).duplicate(), (InetSocketAddress)recipient(), (InetSocketAddress)sender());
  }
  
  public DatagramPacket retain() {
    super.retain();
    return this;
  }
  
  public DatagramPacket retain(int increment) {
    super.retain(increment);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\socket\DatagramPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */