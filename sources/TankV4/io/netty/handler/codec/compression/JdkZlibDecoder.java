package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class JdkZlibDecoder extends ZlibDecoder {
   private static final int FHCRC = 2;
   private static final int FEXTRA = 4;
   private static final int FNAME = 8;
   private static final int FCOMMENT = 16;
   private static final int FRESERVED = 224;
   private final Inflater inflater;
   private final byte[] dictionary;
   private final CRC32 crc;
   private JdkZlibDecoder.GzipState gzipState;
   private int flags;
   private int xlen;
   private volatile boolean finished;

   public JdkZlibDecoder() {
      this(ZlibWrapper.ZLIB, (byte[])null);
   }

   public JdkZlibDecoder(byte[] var1) {
      this(ZlibWrapper.ZLIB, var1);
   }

   public JdkZlibDecoder(ZlibWrapper var1) {
      this(var1, (byte[])null);
   }

   private JdkZlibDecoder(ZlibWrapper var1, byte[] var2) {
      this.gzipState = JdkZlibDecoder.GzipState.HEADER_START;
      this.flags = -1;
      this.xlen = -1;
      if (var1 == null) {
         throw new NullPointerException("wrapper");
      } else {
         switch(var1) {
         case GZIP:
            this.inflater = new Inflater(true);
            this.crc = new CRC32();
            break;
         case NONE:
            this.inflater = new Inflater(true);
            this.crc = null;
            break;
         case ZLIB:
            this.inflater = new Inflater();
            this.crc = null;
            break;
         default:
            throw new IllegalArgumentException("Only GZIP or ZLIB is supported, but you used " + var1);
         }

         this.dictionary = var2;
      }
   }

   public boolean isClosed() {
      return this.finished;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      if (this.finished) {
         var2.skipBytes(var2.readableBytes());
      } else if (var2.isReadable()) {
         if (this.crc != null) {
            switch(this.gzipState) {
            case FOOTER_START:
               if (this < var2) {
                  this.finished = true;
               }

               return;
            default:
               if (this.gzipState != JdkZlibDecoder.GzipState.HEADER_END && this < var2) {
                  return;
               }
            }
         }

         int var4 = var2.readableBytes();
         if (var2.hasArray()) {
            this.inflater.setInput(var2.array(), var2.arrayOffset() + var2.readerIndex(), var2.readableBytes());
         } else {
            byte[] var5 = new byte[var2.readableBytes()];
            var2.getBytes(var2.readerIndex(), var5);
            this.inflater.setInput(var5);
         }

         int var14 = this.inflater.getRemaining() << 1;
         ByteBuf var6 = var1.alloc().heapBuffer(var14);

         try {
            boolean var7 = false;
            byte[] var8 = var6.array();

            while(!this.inflater.needsInput()) {
               int var9 = var6.arrayOffset() + var6.writerIndex();
               int var10 = var8.length - var9;
               if (var10 == 0) {
                  var3.add(var6);
                  var6 = var1.alloc().heapBuffer(var14);
                  var8 = var6.array();
               } else {
                  int var11 = this.inflater.inflate(var8, var9, var10);
                  if (var11 > 0) {
                     var6.writerIndex(var6.writerIndex() + var11);
                     if (this.crc != null) {
                        this.crc.update(var8, var9, var11);
                     }
                  } else if (this.inflater.needsDictionary()) {
                     if (this.dictionary == null) {
                        throw new DecompressionException("decompression failure, unable to set dictionary as non was specified");
                     }

                     this.inflater.setDictionary(this.dictionary);
                  }

                  if (this.inflater.finished()) {
                     if (this.crc == null) {
                        this.finished = true;
                     } else {
                        var7 = true;
                     }
                     break;
                  }
               }
            }

            var2.skipBytes(var4 - this.inflater.getRemaining());
            if (var7) {
               this.gzipState = JdkZlibDecoder.GzipState.FOOTER_START;
               if (this < var2) {
                  this.finished = true;
               }
            }
         } catch (DataFormatException var13) {
            throw new DecompressionException("decompression failure", var13);
         }

         if (var6.isReadable()) {
            var3.add(var6);
         } else {
            var6.release();
         }

      }
   }

   protected void handlerRemoved0(ChannelHandlerContext var1) throws Exception {
      super.handlerRemoved0(var1);
      this.inflater.end();
   }

   private void verifyCrc(ByteBuf var1) {
      long var2 = 0L;

      for(int var4 = 0; var4 < 4; ++var4) {
         var2 |= (long)var1.readUnsignedByte() << var4 * 8;
      }

      long var6 = this.crc.getValue();
      if (var2 != var6) {
         throw new CompressionException("CRC value missmatch. Expected: " + var2 + ", Got: " + var6);
      }
   }

   private static enum GzipState {
      HEADER_START,
      HEADER_END,
      FLG_READ,
      XLEN_READ,
      SKIP_FNAME,
      SKIP_COMMENT,
      PROCESS_FHCRC,
      FOOTER_START;

      private static final JdkZlibDecoder.GzipState[] $VALUES = new JdkZlibDecoder.GzipState[]{HEADER_START, HEADER_END, FLG_READ, XLEN_READ, SKIP_FNAME, SKIP_COMMENT, PROCESS_FHCRC, FOOTER_START};
   }
}
