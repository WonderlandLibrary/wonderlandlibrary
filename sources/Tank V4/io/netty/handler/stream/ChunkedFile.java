package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ChunkedFile implements ChunkedInput {
   private final RandomAccessFile file;
   private final long startOffset;
   private final long endOffset;
   private final int chunkSize;
   private long offset;

   public ChunkedFile(File var1) throws IOException {
      this((File)var1, 8192);
   }

   public ChunkedFile(File var1, int var2) throws IOException {
      this(new RandomAccessFile(var1, "r"), var2);
   }

   public ChunkedFile(RandomAccessFile var1) throws IOException {
      this((RandomAccessFile)var1, 8192);
   }

   public ChunkedFile(RandomAccessFile var1, int var2) throws IOException {
      this(var1, 0L, var1.length(), var2);
   }

   public ChunkedFile(RandomAccessFile var1, long var2, long var4, int var6) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("file");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException("offset: " + var2 + " (expected: 0 or greater)");
      } else if (var4 < 0L) {
         throw new IllegalArgumentException("length: " + var4 + " (expected: 0 or greater)");
      } else if (var6 <= 0) {
         throw new IllegalArgumentException("chunkSize: " + var6 + " (expected: a positive integer)");
      } else {
         this.file = var1;
         this.offset = this.startOffset = var2;
         this.endOffset = var2 + var4;
         this.chunkSize = var6;
         var1.seek(var2);
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
      return this.offset >= this.endOffset || !this.file.getChannel().isOpen();
   }

   public void close() throws Exception {
      this.file.close();
   }

   public ByteBuf readChunk(ChannelHandlerContext var1) throws Exception {
      long var2 = this.offset;
      if (var2 >= this.endOffset) {
         return null;
      } else {
         int var4 = (int)Math.min((long)this.chunkSize, this.endOffset - var2);
         ByteBuf var5 = var1.alloc().heapBuffer(var4);
         boolean var6 = true;
         this.file.readFully(var5.array(), var5.arrayOffset(), var4);
         var5.writerIndex(var4);
         this.offset = var2 + (long)var4;
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
