package org.apache.commons.compress.compressors.gzip;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import org.apache.commons.compress.compressors.CompressorOutputStream;

public class GzipCompressorOutputStream extends CompressorOutputStream {
   private static final int FNAME = 8;
   private static final int FCOMMENT = 16;
   private final OutputStream out;
   private final Deflater deflater;
   private final byte[] deflateBuffer;
   private boolean closed;
   private final CRC32 crc;

   public GzipCompressorOutputStream(OutputStream var1) throws IOException {
      this(var1, new GzipParameters());
   }

   public GzipCompressorOutputStream(OutputStream var1, GzipParameters var2) throws IOException {
      this.deflateBuffer = new byte[512];
      this.crc = new CRC32();
      this.out = var1;
      this.deflater = new Deflater(var2.getCompressionLevel(), true);
      this.writeHeader(var2);
   }

   private void writeHeader(GzipParameters var1) throws IOException {
      String var2 = var1.getFilename();
      String var3 = var1.getComment();
      ByteBuffer var4 = ByteBuffer.allocate(10);
      var4.order(ByteOrder.LITTLE_ENDIAN);
      var4.putShort((short)-29921);
      var4.put((byte)8);
      var4.put((byte)((var2 != null ? 8 : 0) | (var3 != null ? 16 : 0)));
      var4.putInt((int)(var1.getModificationTime() / 1000L));
      int var5 = var1.getCompressionLevel();
      if (var5 == 9) {
         var4.put((byte)2);
      } else if (var5 == 1) {
         var4.put((byte)4);
      } else {
         var4.put((byte)0);
      }

      var4.put((byte)var1.getOperatingSystem());
      this.out.write(var4.array());
      if (var2 != null) {
         this.out.write(var2.getBytes("ISO-8859-1"));
         this.out.write(0);
      }

      if (var3 != null) {
         this.out.write(var3.getBytes("ISO-8859-1"));
         this.out.write(0);
      }

   }

   private void writeTrailer() throws IOException {
      ByteBuffer var1 = ByteBuffer.allocate(8);
      var1.order(ByteOrder.LITTLE_ENDIAN);
      var1.putInt((int)this.crc.getValue());
      var1.putInt(this.deflater.getTotalIn());
      this.out.write(var1.array());
   }

   public void write(int var1) throws IOException {
      this.write(new byte[]{(byte)(var1 & 255)}, 0, 1);
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.deflater.finished()) {
         throw new IOException("Cannot write more data, the end of the compressed data stream has been reached");
      } else {
         if (var3 > 0) {
            this.deflater.setInput(var1, var2, var3);

            while(!this.deflater.needsInput()) {
               this.deflate();
            }

            this.crc.update(var1, var2, var3);
         }

      }
   }

   private void deflate() throws IOException {
      int var1 = this.deflater.deflate(this.deflateBuffer, 0, this.deflateBuffer.length);
      if (var1 > 0) {
         this.out.write(this.deflateBuffer, 0, var1);
      }

   }

   public void finish() throws IOException {
      if (!this.deflater.finished()) {
         this.deflater.finish();

         while(!this.deflater.finished()) {
            this.deflate();
         }

         this.writeTrailer();
      }

   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.finish();
         this.deflater.end();
         this.out.close();
         this.closed = true;
      }

   }
}
