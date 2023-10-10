package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import org.jboss.marshalling.ByteInput;

class ChannelBufferByteInput implements ByteInput {
   private final ByteBuf buffer;

   public ChannelBufferByteInput(ByteBuf var1) {
      this.buffer = var1;
   }

   public void close() throws IOException {
   }

   public int available() throws IOException {
      return this.buffer.readableBytes();
   }

   public int read() throws IOException {
      return this.buffer.isReadable() ? this.buffer.readByte() & 255 : -1;
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.available();
      if (var4 == 0) {
         return -1;
      } else {
         var3 = Math.min(var4, var3);
         this.buffer.readBytes(var1, var2, var3);
         return var3;
      }
   }

   public long skip(long var1) throws IOException {
      int var3 = this.buffer.readableBytes();
      if ((long)var3 < var1) {
         var1 = (long)var3;
      }

      this.buffer.readerIndex((int)((long)this.buffer.readerIndex() + var1));
      return var1;
   }
}
