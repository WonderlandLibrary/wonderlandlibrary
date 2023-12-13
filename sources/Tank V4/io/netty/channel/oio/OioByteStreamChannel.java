package io.netty.channel.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.FileRegion;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.WritableByteChannel;

public abstract class OioByteStreamChannel extends AbstractOioByteChannel {
   private static final InputStream CLOSED_IN = new InputStream() {
      public int read() {
         return -1;
      }
   };
   private static final OutputStream CLOSED_OUT = new OutputStream() {
      public void write(int var1) throws IOException {
         throw new ClosedChannelException();
      }
   };
   private InputStream is;
   private OutputStream os;
   private WritableByteChannel outChannel;

   protected OioByteStreamChannel(Channel var1) {
      super(var1);
   }

   protected final void activate(InputStream var1, OutputStream var2) {
      if (this.is != null) {
         throw new IllegalStateException("input was set already");
      } else if (this.os != null) {
         throw new IllegalStateException("output was set already");
      } else if (var1 == null) {
         throw new NullPointerException("is");
      } else if (var2 == null) {
         throw new NullPointerException("os");
      } else {
         this.is = var1;
         this.os = var2;
      }
   }

   public boolean isActive() {
      InputStream var1 = this.is;
      if (var1 != null && var1 != CLOSED_IN) {
         OutputStream var2 = this.os;
         return var2 != null && var2 != CLOSED_OUT;
      } else {
         return false;
      }
   }

   protected int available() {
      try {
         return this.is.available();
      } catch (IOException var2) {
         return 0;
      }
   }

   protected int doReadBytes(ByteBuf var1) throws Exception {
      int var2 = Math.max(1, Math.min(this.available(), var1.maxWritableBytes()));
      return var1.writeBytes(this.is, var2);
   }

   protected void doWriteBytes(ByteBuf var1) throws Exception {
      OutputStream var2 = this.os;
      if (var2 == null) {
         throw new NotYetConnectedException();
      } else {
         var1.readBytes(var2, var1.readableBytes());
      }
   }

   protected void doWriteFileRegion(FileRegion var1) throws Exception {
      OutputStream var2 = this.os;
      if (var2 == null) {
         throw new NotYetConnectedException();
      } else {
         if (this.outChannel == null) {
            this.outChannel = Channels.newChannel(var2);
         }

         long var3 = 0L;

         do {
            long var5 = var1.transferTo(this.outChannel, var3);
            if (var5 == -1L) {
               checkEOF(var1);
               return;
            }

            var3 += var5;
         } while(var3 < var1.count());

      }
   }

   protected void doClose() throws Exception {
      InputStream var1 = this.is;
      OutputStream var2 = this.os;
      this.is = CLOSED_IN;
      this.os = CLOSED_OUT;
      if (var1 != null) {
         var1.close();
      }

      if (var2 != null) {
         var2.close();
      }

   }
}
