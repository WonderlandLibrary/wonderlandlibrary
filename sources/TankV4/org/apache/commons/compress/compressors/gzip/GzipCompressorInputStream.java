package org.apache.commons.compress.compressors.gzip;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.apache.commons.compress.compressors.CompressorInputStream;

public class GzipCompressorInputStream extends CompressorInputStream {
   private static final int FHCRC = 2;
   private static final int FEXTRA = 4;
   private static final int FNAME = 8;
   private static final int FCOMMENT = 16;
   private static final int FRESERVED = 224;
   private final InputStream in;
   private final boolean decompressConcatenated;
   private final byte[] buf;
   private int bufUsed;
   private Inflater inf;
   private final CRC32 crc;
   private int memberSize;
   private boolean endReached;
   private final byte[] oneByte;
   private final GzipParameters parameters;
   static final boolean $assertionsDisabled = !GzipCompressorInputStream.class.desiredAssertionStatus();

   public GzipCompressorInputStream(InputStream var1) throws IOException {
      this(var1, false);
   }

   public GzipCompressorInputStream(InputStream var1, boolean var2) throws IOException {
      this.buf = new byte[8192];
      this.bufUsed = 0;
      this.inf = new Inflater(true);
      this.crc = new CRC32();
      this.endReached = false;
      this.oneByte = new byte[1];
      this.parameters = new GzipParameters();
      if (var1.markSupported()) {
         this.in = var1;
      } else {
         this.in = new BufferedInputStream(var1);
      }

      this.decompressConcatenated = var2;
      this.init(true);
   }

   public GzipParameters getMetaData() {
      return this.parameters;
   }

   private byte[] readToNull(DataInputStream var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      boolean var3 = false;

      int var4;
      while((var4 = var1.readUnsignedByte()) != 0) {
         var2.write(var4);
      }

      return var2.toByteArray();
   }

   private int readLittleEndianInt(DataInputStream var1) throws IOException {
      return var1.readUnsignedByte() | var1.readUnsignedByte() << 8 | var1.readUnsignedByte() << 16 | var1.readUnsignedByte() << 24;
   }

   public int read() throws IOException {
      return this.read(this.oneByte, 0, 1) == -1 ? -1 : this.oneByte[0] & 255;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.endReached) {
         return -1;
      } else {
         int var4 = 0;

         do {
            if (var3 <= 0) {
               return var4;
            }

            if (this.inf.needsInput()) {
               this.in.mark(this.buf.length);
               this.bufUsed = this.in.read(this.buf);
               if (this.bufUsed == -1) {
                  throw new EOFException();
               }

               this.inf.setInput(this.buf, 0, this.bufUsed);
            }

            int var5;
            try {
               var5 = this.inf.inflate(var1, var2, var3);
            } catch (DataFormatException var12) {
               throw new IOException("Gzip-compressed data is corrupt");
            }

            this.crc.update(var1, var2, var5);
            this.memberSize += var5;
            var2 += var5;
            var3 -= var5;
            var4 += var5;
            this.count(var5);
         } while(!this.inf.finished());

         this.in.reset();
         int var6 = this.bufUsed - this.inf.getRemaining();
         if (this.in.skip((long)var6) != (long)var6) {
            throw new IOException();
         } else {
            this.bufUsed = 0;
            DataInputStream var7 = new DataInputStream(this.in);
            long var8 = 0L;

            int var10;
            for(var10 = 0; var10 < 4; ++var10) {
               var8 |= (long)var7.readUnsignedByte() << var10 * 8;
            }

            if (var8 != this.crc.getValue()) {
               throw new IOException("Gzip-compressed data is corrupt (CRC32 error)");
            } else {
               var10 = 0;

               for(int var11 = 0; var11 < 4; ++var11) {
                  var10 |= var7.readUnsignedByte() << var11 * 8;
               }

               if (var10 != this.memberSize) {
                  throw new IOException("Gzip-compressed data is corrupt(uncompressed size mismatch)");
               } else {
                  if (this.decompressConcatenated) {
                     ;
                  }

                  this.inf.end();
                  this.inf = null;
                  this.endReached = true;
                  return var4 == 0 ? -1 : var4;
               }
            }
         }
      }
   }

   public static boolean matches(byte[] var0, int var1) {
      if (var1 < 2) {
         return false;
      } else if (var0[0] != 31) {
         return false;
      } else {
         return var0[1] == -117;
      }
   }

   public void close() throws IOException {
      if (this.inf != null) {
         this.inf.end();
         this.inf = null;
      }

      if (this.in != System.in) {
         this.in.close();
      }

   }
}
