package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class SocksInitRequest extends SocksRequest {
   private final List authSchemes;

   public SocksInitRequest(List var1) {
      super(SocksRequestType.INIT);
      if (var1 == null) {
         throw new NullPointerException("authSchemes");
      } else {
         this.authSchemes = var1;
      }
   }

   public List authSchemes() {
      return Collections.unmodifiableList(this.authSchemes);
   }

   public void encodeAsByteBuf(ByteBuf var1) {
      var1.writeByte(this.protocolVersion().byteValue());
      var1.writeByte(this.authSchemes.size());
      Iterator var2 = this.authSchemes.iterator();

      while(var2.hasNext()) {
         SocksAuthScheme var3 = (SocksAuthScheme)var2.next();
         var1.writeByte(var3.byteValue());
      }

   }
}
