package io.netty.handler.codec.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class CompatibleObjectEncoder extends MessageToByteEncoder {
   private static final AttributeKey OOS = AttributeKey.valueOf(CompatibleObjectEncoder.class.getName() + ".OOS");
   private final int resetInterval;
   private int writtenObjects;

   public CompatibleObjectEncoder() {
      this(16);
   }

   public CompatibleObjectEncoder(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("resetInterval: " + var1);
      } else {
         this.resetInterval = var1;
      }
   }

   protected ObjectOutputStream newObjectOutputStream(OutputStream var1) throws Exception {
      return new ObjectOutputStream(var1);
   }

   protected void encode(ChannelHandlerContext var1, Serializable var2, ByteBuf var3) throws Exception {
      Attribute var4 = var1.attr(OOS);
      ObjectOutputStream var5 = (ObjectOutputStream)var4.get();
      if (var5 == null) {
         var5 = this.newObjectOutputStream(new ByteBufOutputStream(var3));
         ObjectOutputStream var6 = (ObjectOutputStream)var4.setIfAbsent(var5);
         if (var6 != null) {
            var5 = var6;
         }
      }

      synchronized(var5){}
      if (this.resetInterval != 0) {
         ++this.writtenObjects;
         if (this.writtenObjects % this.resetInterval == 0) {
            var5.reset();
         }
      }

      var5.writeObject(var2);
      var5.flush();
   }

   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception {
      this.encode(var1, (Serializable)var2, var3);
   }
}
