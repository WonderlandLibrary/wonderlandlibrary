package io.netty.channel.socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.util.ReferenceCounted;
import java.net.InetSocketAddress;

public final class DatagramPacket extends DefaultAddressedEnvelope implements ByteBufHolder {
   public DatagramPacket(ByteBuf var1, InetSocketAddress var2) {
      super(var1, var2);
   }

   public DatagramPacket(ByteBuf var1, InetSocketAddress var2, InetSocketAddress var3) {
      super(var1, var2, var3);
   }

   public DatagramPacket copy() {
      return new DatagramPacket(((ByteBuf)this.content()).copy(), (InetSocketAddress)this.recipient(), (InetSocketAddress)this.sender());
   }

   public DatagramPacket duplicate() {
      return new DatagramPacket(((ByteBuf)this.content()).duplicate(), (InetSocketAddress)this.recipient(), (InetSocketAddress)this.sender());
   }

   public DatagramPacket retain() {
      super.retain();
      return this;
   }

   public DatagramPacket retain(int var1) {
      super.retain(var1);
      return this;
   }

   public AddressedEnvelope retain(int var1) {
      return this.retain(var1);
   }

   public AddressedEnvelope retain() {
      return this.retain();
   }

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public ReferenceCounted retain() {
      return this.retain();
   }

   public ByteBufHolder retain(int var1) {
      return this.retain(var1);
   }

   public ByteBufHolder retain() {
      return this.retain();
   }

   public ByteBufHolder duplicate() {
      return this.duplicate();
   }

   public ByteBufHolder copy() {
      return this.copy();
   }

   public ByteBuf content() {
      return (ByteBuf)super.content();
   }
}
