package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import org.jboss.marshalling.ByteOutput;

class ChannelBufferByteOutput implements ByteOutput {
   private final ByteBuf buffer;

   public ChannelBufferByteOutput(ByteBuf var1) {
      this.buffer = var1;
   }

   public void close() throws IOException {
   }

   public void flush() throws IOException {
   }

   public void write(int var1) throws IOException {
      this.buffer.writeByte(var1);
   }

   public void write(byte[] var1) throws IOException {
      this.buffer.writeBytes(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.buffer.writeBytes(var1, var2, var3);
   }

   ByteBuf getBuffer() {
      return this.buffer;
   }
}
