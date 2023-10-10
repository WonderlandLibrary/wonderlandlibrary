package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.util.ReferenceCounted;
import java.io.IOException;

public class MemoryAttribute extends AbstractMemoryHttpData implements Attribute {
   public MemoryAttribute(String var1) {
      super(var1, HttpConstants.DEFAULT_CHARSET, 0L);
   }

   public MemoryAttribute(String var1, String var2) throws IOException {
      super(var1, HttpConstants.DEFAULT_CHARSET, 0L);
      this.setValue(var2);
   }

   public InterfaceHttpData.HttpDataType getHttpDataType() {
      return InterfaceHttpData.HttpDataType.Attribute;
   }

   public String getValue() {
      return this.getByteBuf().toString(this.charset);
   }

   public void setValue(String var1) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("value");
      } else {
         byte[] var2 = var1.getBytes(this.charset.name());
         ByteBuf var3 = Unpooled.wrappedBuffer(var2);
         if (this.definedSize > 0L) {
            this.definedSize = (long)var3.readableBytes();
         }

         this.setContent(var3);
      }
   }

   public void addContent(ByteBuf var1, boolean var2) throws IOException {
      int var3 = var1.readableBytes();
      if (this.definedSize > 0L && this.definedSize < this.size + (long)var3) {
         this.definedSize = this.size + (long)var3;
      }

      super.addContent(var1, var2);
   }

   public int hashCode() {
      return this.getName().hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Attribute)) {
         return false;
      } else {
         Attribute var2 = (Attribute)var1;
         return this.getName().equalsIgnoreCase(var2.getName());
      }
   }

   public int compareTo(InterfaceHttpData var1) {
      if (!(var1 instanceof Attribute)) {
         throw new ClassCastException("Cannot compare " + this.getHttpDataType() + " with " + var1.getHttpDataType());
      } else {
         return this.compareTo((Attribute)var1);
      }
   }

   public int compareTo(Attribute var1) {
      return this.getName().compareToIgnoreCase(var1.getName());
   }

   public String toString() {
      return this.getName() + '=' + this.getValue();
   }

   public Attribute copy() {
      MemoryAttribute var1 = new MemoryAttribute(this.getName());
      var1.setCharset(this.getCharset());
      ByteBuf var2 = this.content();
      if (var2 != null) {
         try {
            var1.setContent(var2.copy());
         } catch (IOException var4) {
            throw new ChannelException(var4);
         }
      }

      return var1;
   }

   public Attribute duplicate() {
      MemoryAttribute var1 = new MemoryAttribute(this.getName());
      var1.setCharset(this.getCharset());
      ByteBuf var2 = this.content();
      if (var2 != null) {
         try {
            var1.setContent(var2.duplicate());
         } catch (IOException var4) {
            throw new ChannelException(var4);
         }
      }

      return var1;
   }

   public Attribute retain() {
      super.retain();
      return this;
   }

   public Attribute retain(int var1) {
      super.retain(var1);
      return this;
   }

   public HttpData retain(int var1) {
      return this.retain(var1);
   }

   public HttpData retain() {
      return this.retain();
   }

   public HttpData duplicate() {
      return this.duplicate();
   }

   public HttpData copy() {
      return this.copy();
   }

   public int compareTo(Object var1) {
      return this.compareTo((InterfaceHttpData)var1);
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
}
