package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

final class HttpHeaderEntity implements CharSequence {
   private final String name;
   private final int hash;
   private final byte[] bytes;

   public HttpHeaderEntity(String var1) {
      this.name = var1;
      this.hash = HttpHeaders.hash(var1);
      this.bytes = var1.getBytes(CharsetUtil.US_ASCII);
   }

   int hash() {
      return this.hash;
   }

   public int length() {
      return this.bytes.length;
   }

   public char charAt(int var1) {
      return (char)this.bytes[var1];
   }

   public CharSequence subSequence(int var1, int var2) {
      return new HttpHeaderEntity(this.name.substring(var1, var2));
   }

   public String toString() {
      return this.name;
   }

   void encode(ByteBuf var1) {
      var1.writeBytes(this.bytes);
   }
}
