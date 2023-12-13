package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public abstract class AbstractMemoryHttpData extends AbstractHttpData {
   private ByteBuf byteBuf;
   private int chunkPosition;
   protected boolean isRenamed;

   protected AbstractMemoryHttpData(String var1, Charset var2, long var3) {
      super(var1, var2, var3);
   }

   public void setContent(ByteBuf var1) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("buffer");
      } else {
         long var2 = (long)var1.readableBytes();
         if (this.definedSize > 0L && this.definedSize < var2) {
            throw new IOException("Out of size: " + var2 + " > " + this.definedSize);
         } else {
            if (this.byteBuf != null) {
               this.byteBuf.release();
            }

            this.byteBuf = var1;
            this.size = var2;
            this.completed = true;
         }
      }
   }

   public void setContent(InputStream var1) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("inputStream");
      } else {
         ByteBuf var2 = Unpooled.buffer();
         byte[] var3 = new byte[16384];
         int var4 = var1.read(var3);

         int var5;
         for(var5 = 0; var4 > 0; var4 = var1.read(var3)) {
            var2.writeBytes((byte[])var3, 0, var4);
            var5 += var4;
         }

         this.size = (long)var5;
         if (this.definedSize > 0L && this.definedSize < this.size) {
            throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
         } else {
            if (this.byteBuf != null) {
               this.byteBuf.release();
            }

            this.byteBuf = var2;
            this.completed = true;
         }
      }
   }

   public void addContent(ByteBuf var1, boolean var2) throws IOException {
      if (var1 != null) {
         long var3 = (long)var1.readableBytes();
         if (this.definedSize > 0L && this.definedSize < this.size + var3) {
            throw new IOException("Out of size: " + (this.size + var3) + " > " + this.definedSize);
         }

         this.size += var3;
         if (this.byteBuf == null) {
            this.byteBuf = var1;
         } else {
            CompositeByteBuf var5;
            if (this.byteBuf instanceof CompositeByteBuf) {
               var5 = (CompositeByteBuf)this.byteBuf;
               var5.addComponent(var1);
               var5.writerIndex(var5.writerIndex() + var1.readableBytes());
            } else {
               var5 = Unpooled.compositeBuffer(Integer.MAX_VALUE);
               var5.addComponents(this.byteBuf, var1);
               var5.writerIndex(this.byteBuf.readableBytes() + var1.readableBytes());
               this.byteBuf = var5;
            }
         }
      }

      if (var2) {
         this.completed = true;
      } else if (var1 == null) {
         throw new NullPointerException("buffer");
      }

   }

   public void setContent(File var1) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("file");
      } else {
         long var2 = var1.length();
         if (var2 > 2147483647L) {
            throw new IllegalArgumentException("File too big to be loaded in memory");
         } else {
            FileInputStream var4 = new FileInputStream(var1);
            FileChannel var5 = var4.getChannel();
            byte[] var6 = new byte[(int)var2];
            ByteBuffer var7 = ByteBuffer.wrap(var6);

            for(int var8 = 0; (long)var8 < var2; var8 += var5.read(var7)) {
            }

            var5.close();
            var4.close();
            var7.flip();
            if (this.byteBuf != null) {
               this.byteBuf.release();
            }

            this.byteBuf = Unpooled.wrappedBuffer(Integer.MAX_VALUE, var7);
            this.size = var2;
            this.completed = true;
         }
      }
   }

   public void delete() {
      if (this.byteBuf != null) {
         this.byteBuf.release();
         this.byteBuf = null;
      }

   }

   public byte[] get() {
      if (this.byteBuf == null) {
         return Unpooled.EMPTY_BUFFER.array();
      } else {
         byte[] var1 = new byte[this.byteBuf.readableBytes()];
         this.byteBuf.getBytes(this.byteBuf.readerIndex(), var1);
         return var1;
      }
   }

   public String getString() {
      return this.getString(HttpConstants.DEFAULT_CHARSET);
   }

   public String getString(Charset var1) {
      if (this.byteBuf == null) {
         return "";
      } else {
         if (var1 == null) {
            var1 = HttpConstants.DEFAULT_CHARSET;
         }

         return this.byteBuf.toString(var1);
      }
   }

   public ByteBuf getByteBuf() {
      return this.byteBuf;
   }

   public ByteBuf getChunk(int var1) throws IOException {
      if (this.byteBuf != null && var1 != 0 && this.byteBuf.readableBytes() != 0) {
         int var2 = this.byteBuf.readableBytes() - this.chunkPosition;
         if (var2 == 0) {
            this.chunkPosition = 0;
            return Unpooled.EMPTY_BUFFER;
         } else {
            int var3 = var1;
            if (var2 < var1) {
               var3 = var2;
            }

            ByteBuf var4 = this.byteBuf.slice(this.chunkPosition, var3).retain();
            this.chunkPosition += var3;
            return var4;
         }
      } else {
         this.chunkPosition = 0;
         return Unpooled.EMPTY_BUFFER;
      }
   }

   public boolean isInMemory() {
      return true;
   }

   public boolean renameTo(File var1) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("dest");
      } else if (this.byteBuf == null) {
         var1.createNewFile();
         this.isRenamed = true;
         return true;
      } else {
         int var2 = this.byteBuf.readableBytes();
         FileOutputStream var3 = new FileOutputStream(var1);
         FileChannel var4 = var3.getChannel();
         int var5 = 0;
         if (this.byteBuf.nioBufferCount() == 1) {
            for(ByteBuffer var6 = this.byteBuf.nioBuffer(); var5 < var2; var5 += var4.write(var6)) {
            }
         } else {
            for(ByteBuffer[] var7 = this.byteBuf.nioBuffers(); var5 < var2; var5 = (int)((long)var5 + var4.write(var7))) {
            }
         }

         var4.force(false);
         var4.close();
         var3.close();
         this.isRenamed = true;
         return var5 == var2;
      }
   }

   public File getFile() throws IOException {
      throw new IOException("Not represented by a file");
   }
}
