package io.netty.channel;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;

public class DefaultAddressedEnvelope implements AddressedEnvelope {
   private final Object message;
   private final SocketAddress sender;
   private final SocketAddress recipient;

   public DefaultAddressedEnvelope(Object var1, SocketAddress var2, SocketAddress var3) {
      if (var1 == null) {
         throw new NullPointerException("message");
      } else {
         this.message = var1;
         this.sender = var3;
         this.recipient = var2;
      }
   }

   public DefaultAddressedEnvelope(Object var1, SocketAddress var2) {
      this(var1, var2, (SocketAddress)null);
   }

   public Object content() {
      return this.message;
   }

   public SocketAddress sender() {
      return this.sender;
   }

   public SocketAddress recipient() {
      return this.recipient;
   }

   public int refCnt() {
      return this.message instanceof ReferenceCounted ? ((ReferenceCounted)this.message).refCnt() : 1;
   }

   public AddressedEnvelope retain() {
      ReferenceCountUtil.retain(this.message);
      return this;
   }

   public AddressedEnvelope retain(int var1) {
      ReferenceCountUtil.retain(this.message, var1);
      return this;
   }

   public boolean release() {
      return ReferenceCountUtil.release(this.message);
   }

   public boolean release(int var1) {
      return ReferenceCountUtil.release(this.message, var1);
   }

   public String toString() {
      return this.sender != null ? StringUtil.simpleClassName((Object)this) + '(' + this.sender + " => " + this.recipient + ", " + this.message + ')' : StringUtil.simpleClassName((Object)this) + "(=> " + this.recipient + ", " + this.message + ')';
   }

   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   public ReferenceCounted retain() {
      return this.retain();
   }
}
