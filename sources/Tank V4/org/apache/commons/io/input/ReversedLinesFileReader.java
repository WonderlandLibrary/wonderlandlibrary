package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.io.Charsets;

public class ReversedLinesFileReader implements Closeable {
   private final int blockSize;
   private final Charset encoding;
   private final RandomAccessFile randomAccessFile;
   private final long totalByteLength;
   private final long totalBlockCount;
   private final byte[][] newLineSequences;
   private final int avoidNewlineSplitBufferSize;
   private final int byteDecrement;
   private ReversedLinesFileReader.FilePart currentFilePart;
   private boolean trailingNewlineOfFileSkipped;

   public ReversedLinesFileReader(File var1) throws IOException {
      this(var1, 4096, (String)Charset.defaultCharset().toString());
   }

   public ReversedLinesFileReader(File var1, int var2, Charset var3) throws IOException {
      this.trailingNewlineOfFileSkipped = false;
      this.blockSize = var2;
      this.encoding = var3;
      this.randomAccessFile = new RandomAccessFile(var1, "r");
      this.totalByteLength = this.randomAccessFile.length();
      int var4 = (int)(this.totalByteLength % (long)var2);
      if (var4 > 0) {
         this.totalBlockCount = this.totalByteLength / (long)var2 + 1L;
      } else {
         this.totalBlockCount = this.totalByteLength / (long)var2;
         if (this.totalByteLength > 0L) {
            var4 = var2;
         }
      }

      this.currentFilePart = new ReversedLinesFileReader.FilePart(this, this.totalBlockCount, var4, (byte[])null);
      Charset var5 = Charsets.toCharset(var3);
      CharsetEncoder var6 = var5.newEncoder();
      float var7 = var6.maxBytesPerChar();
      if (var7 == 1.0F) {
         this.byteDecrement = 1;
      } else if (var5 == Charset.forName("UTF-8")) {
         this.byteDecrement = 1;
      } else if (var5 == Charset.forName("Shift_JIS")) {
         this.byteDecrement = 1;
      } else {
         if (var5 != Charset.forName("UTF-16BE") && var5 != Charset.forName("UTF-16LE")) {
            if (var5 == Charset.forName("UTF-16")) {
               throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
            }

            throw new UnsupportedEncodingException("Encoding " + var3 + " is not supported yet (feel free to submit a patch)");
         }

         this.byteDecrement = 2;
      }

      this.newLineSequences = new byte[][]{"\r\n".getBytes(var3), "\n".getBytes(var3), "\r".getBytes(var3)};
      this.avoidNewlineSplitBufferSize = this.newLineSequences[0].length;
   }

   public ReversedLinesFileReader(File var1, int var2, String var3) throws IOException {
      this(var1, var2, Charsets.toCharset(var3));
   }

   public String readLine() throws IOException {
      String var1;
      for(var1 = ReversedLinesFileReader.FilePart.access$100(this.currentFilePart); var1 == null; var1 = ReversedLinesFileReader.FilePart.access$100(this.currentFilePart)) {
         this.currentFilePart = ReversedLinesFileReader.FilePart.access$200(this.currentFilePart);
         if (this.currentFilePart == null) {
            break;
         }
      }

      if ("".equals(var1) && !this.trailingNewlineOfFileSkipped) {
         this.trailingNewlineOfFileSkipped = true;
         var1 = this.readLine();
      }

      return var1;
   }

   public void close() throws IOException {
      this.randomAccessFile.close();
   }

   static int access$300(ReversedLinesFileReader var0) {
      return var0.blockSize;
   }

   static RandomAccessFile access$400(ReversedLinesFileReader var0) {
      return var0.randomAccessFile;
   }

   static Charset access$500(ReversedLinesFileReader var0) {
      return var0.encoding;
   }

   static int access$600(ReversedLinesFileReader var0) {
      return var0.avoidNewlineSplitBufferSize;
   }

   static int access$700(ReversedLinesFileReader var0) {
      return var0.byteDecrement;
   }

   static byte[][] access$800(ReversedLinesFileReader var0) {
      return var0.newLineSequences;
   }

   private class FilePart {
      private final long no;
      private final byte[] data;
      private byte[] leftOver;
      private int currentLastBytePos;
      final ReversedLinesFileReader this$0;

      private FilePart(ReversedLinesFileReader var1, long var2, int var4, byte[] var5) throws IOException {
         this.this$0 = var1;
         this.no = var2;
         int var6 = var4 + (var5 != null ? var5.length : 0);
         this.data = new byte[var6];
         long var7 = (var2 - 1L) * (long)ReversedLinesFileReader.access$300(var1);
         if (var2 > 0L) {
            ReversedLinesFileReader.access$400(var1).seek(var7);
            int var9 = ReversedLinesFileReader.access$400(var1).read(this.data, 0, var4);
            if (var9 != var4) {
               throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
            }
         }

         if (var5 != null) {
            System.arraycopy(var5, 0, this.data, var4, var5.length);
         }

         this.currentLastBytePos = this.data.length - 1;
         this.leftOver = null;
      }

      private ReversedLinesFileReader.FilePart rollOver() throws IOException {
         if (this.currentLastBytePos > -1) {
            throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
         } else if (this.no > 1L) {
            return this.this$0.new FilePart(this.this$0, this.no - 1L, ReversedLinesFileReader.access$300(this.this$0), this.leftOver);
         } else if (this.leftOver != null) {
            throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(this.leftOver, ReversedLinesFileReader.access$500(this.this$0)));
         } else {
            return null;
         }
      }

      private String readLine() throws IOException {
         String var1 = null;
         boolean var3 = this.no == 1L;
         int var4 = this.currentLastBytePos;

         while(var4 > -1) {
            if (!var3 && var4 < ReversedLinesFileReader.access$600(this.this$0)) {
               this.createLeftOver();
               break;
            }

            int var2;
            if ((var2 = this.getNewLineMatchByteCount(this.data, var4)) > 0) {
               int var5 = var4 + 1;
               int var6 = this.currentLastBytePos - var5 + 1;
               if (var6 < 0) {
                  throw new IllegalStateException("Unexpected negative line length=" + var6);
               }

               byte[] var7 = new byte[var6];
               System.arraycopy(this.data, var5, var7, 0, var6);
               var1 = new String(var7, ReversedLinesFileReader.access$500(this.this$0));
               this.currentLastBytePos = var4 - var2;
               break;
            }

            var4 -= ReversedLinesFileReader.access$700(this.this$0);
            if (var4 < 0) {
               this.createLeftOver();
               break;
            }
         }

         if (var3 && this.leftOver != null) {
            var1 = new String(this.leftOver, ReversedLinesFileReader.access$500(this.this$0));
            this.leftOver = null;
         }

         return var1;
      }

      private void createLeftOver() {
         int var1 = this.currentLastBytePos + 1;
         if (var1 > 0) {
            this.leftOver = new byte[var1];
            System.arraycopy(this.data, 0, this.leftOver, 0, var1);
         } else {
            this.leftOver = null;
         }

         this.currentLastBytePos = -1;
      }

      private int getNewLineMatchByteCount(byte[] var1, int var2) {
         byte[][] var3 = ReversedLinesFileReader.access$800(this.this$0);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            byte[] var6 = var3[var5];
            boolean var7 = true;

            for(int var8 = var6.length - 1; var8 >= 0; --var8) {
               int var9 = var2 + var8 - (var6.length - 1);
               var7 &= var9 >= 0 && var1[var9] == var6[var8];
            }

            if (var7) {
               return var6.length;
            }
         }

         return 0;
      }

      FilePart(ReversedLinesFileReader var1, long var2, int var4, byte[] var5, Object var6) throws IOException {
         this(var1, var2, var4, var5);
      }

      static String access$100(ReversedLinesFileReader.FilePart var0) throws IOException {
         return var0.readLine();
      }

      static ReversedLinesFileReader.FilePart access$200(ReversedLinesFileReader.FilePart var0) throws IOException {
         return var0.rollOver();
      }
   }
}
