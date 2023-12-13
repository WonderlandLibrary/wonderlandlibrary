package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdyPingFrame implements SpdyPingFrame {
   private int id;

   public DefaultSpdyPingFrame(int var1) {
      this.setId(var1);
   }

   public int getId() {
      return this.id;
   }

   public SpdyPingFrame setId(int var1) {
      this.id = var1;
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append(StringUtil.NEWLINE);
      var1.append("--> ID = ");
      var1.append(this.getId());
      return var1.toString();
   }
}
