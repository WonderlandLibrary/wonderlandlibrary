package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ScatteringByteChannel;

public class ChunkedNioFile implements ChunkedInput {
   private final FileChannel in;
   private final long startOffset;
   private final long endOffset;
   private final int chunkSize;
   private long offset;

   public ChunkedNioFile(File var1) throws IOException {
      this((new FileInputStream(var1)).getChannel());
   }

   public ChunkedNioFile(File var1, int var2) throws IOException {
      this((new FileInputStream(var1)).getChannel(), var2);
   }

   public ChunkedNioFile(FileChannel var1) throws IOException {
      this((FileChannel)var1, 8192);
   }

   public ChunkedNioFile(FileChannel var1, int var2) throws IOException {
      this(var1, 0L, var1.size(), var2);
   }

   public ChunkedNioFile(FileChannel var1, long var2, long var4, int var6) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("in");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException("offset: " + var2 + " (expected: 0 or greater)");
      } else if (var4 < 0L) {
         throw new IllegalArgumentException("length: " + var4 + " (expected: 0 or greater)");
      } else if (var6 <= 0) {
         throw new IllegalArgumentException("chunkSize: " + var6 + " (expected: a positive integer)");
      } else {
         if (var2 != 0L) {
            var1.position(var2);
         }

         this.in = var1;
         this.chunkSize = var6;
         this.offset = this.startOffset = var2;
         this.endOffset = var2 + var4;
      }
   }

   public long startOffset() {
      return this.startOffset;
   }

   public long endOffset() {
      return this.endOffset;
   }

   public long currentOffset() {
      return this.offset;
   }

   public boolean isEndOfInput() throws Exception {
      return this.offset >= this.endOffset || !this.in.isOpen();
   }

   public void close() throws Exception {
      this.in.close();
   }

   public ByteBuf readChunk(ChannelHandlerContext var1) throws Exception {
      long var2 = this.offset;
      if (var2 >= this.endOffset) {
         return null;
      } else {
         int var4 = (int)Math.min((long)this.chunkSize, this.endOffset - var2);
         ByteBuf var5 = var1.alloc().buffer(var4);
         boolean var6 = true;
         int var7 = 0;

         do {
            int var8 = var5.writeBytes((ScatteringByteChannel)this.in, var4 - var7);
            if (var8 < 0) {
               break;
            }

            var7 += var8;
         } while(var7 != var4);

         this.offset += (long)var7;
         var6 = false;
         if (var6) {
            var5.release();
         }

         return var5;
      }
   }

   public Object readChunk(ChannelHandlerContext var1) throws Exception {
      return this.readChunk(var1);
   }
}
