package org.apache.commons.compress.compressors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactory {
   public static final String BZIP2 = "bzip2";
   public static final String GZIP = "gz";
   public static final String PACK200 = "pack200";
   public static final String XZ = "xz";
   public static final String LZMA = "lzma";
   public static final String SNAPPY_FRAMED = "snappy-framed";
   public static final String SNAPPY_RAW = "snappy-raw";
   public static final String Z = "z";
   private boolean decompressConcatenated = false;

   public void setDecompressConcatenated(boolean var1) {
      this.decompressConcatenated = var1;
   }

   public CompressorInputStream createCompressorInputStream(InputStream var1) throws CompressorException {
      if (var1 == null) {
         throw new IllegalArgumentException("Stream must not be null.");
      } else if (!var1.markSupported()) {
         throw new IllegalArgumentException("Mark is not supported.");
      } else {
         byte[] var2 = new byte[12];
         var1.mark(var2.length);

         try {
            int var3 = IOUtils.readFully(var1, var2);
            var1.reset();
            if (BZip2CompressorInputStream.matches(var2, var3)) {
               return new BZip2CompressorInputStream(var1, this.decompressConcatenated);
            }

            if (GzipCompressorInputStream.matches(var2, var3)) {
               return new GzipCompressorInputStream(var1, this.decompressConcatenated);
            }

            if (XZUtils.isXZCompressionAvailable() && XZCompressorInputStream.matches(var2, var3)) {
               return new XZCompressorInputStream(var1, this.decompressConcatenated);
            }

            if (Pack200CompressorInputStream.matches(var2, var3)) {
               return new Pack200CompressorInputStream(var1);
            }

            if (FramedSnappyCompressorInputStream.matches(var2, var3)) {
               return new FramedSnappyCompressorInputStream(var1);
            }

            if (ZCompressorInputStream.matches(var2, var3)) {
               return new ZCompressorInputStream(var1);
            }
         } catch (IOException var4) {
            throw new CompressorException("Failed to detect Compressor from InputStream.", var4);
         }

         throw new CompressorException("No Compressor found for the stream signature.");
      }
   }

   public CompressorInputStream createCompressorInputStream(String var1, InputStream var2) throws CompressorException {
      if (var1 != null && var2 != null) {
         try {
            if ("gz".equalsIgnoreCase(var1)) {
               return new GzipCompressorInputStream(var2, this.decompressConcatenated);
            }

            if ("bzip2".equalsIgnoreCase(var1)) {
               return new BZip2CompressorInputStream(var2, this.decompressConcatenated);
            }

            if ("xz".equalsIgnoreCase(var1)) {
               return new XZCompressorInputStream(var2, this.decompressConcatenated);
            }

            if ("lzma".equalsIgnoreCase(var1)) {
               return new LZMACompressorInputStream(var2);
            }

            if ("pack200".equalsIgnoreCase(var1)) {
               return new Pack200CompressorInputStream(var2);
            }

            if ("snappy-raw".equalsIgnoreCase(var1)) {
               return new SnappyCompressorInputStream(var2);
            }

            if ("snappy-framed".equalsIgnoreCase(var1)) {
               return new FramedSnappyCompressorInputStream(var2);
            }

            if ("z".equalsIgnoreCase(var1)) {
               return new ZCompressorInputStream(var2);
            }
         } catch (IOException var4) {
            throw new CompressorException("Could not create CompressorInputStream.", var4);
         }

         throw new CompressorException("Compressor: " + var1 + " not found.");
      } else {
         throw new IllegalArgumentException("Compressor name and stream must not be null.");
      }
   }

   public CompressorOutputStream createCompressorOutputStream(String var1, OutputStream var2) throws CompressorException {
      if (var1 != null && var2 != null) {
         try {
            if ("gz".equalsIgnoreCase(var1)) {
               return new GzipCompressorOutputStream(var2);
            }

            if ("bzip2".equalsIgnoreCase(var1)) {
               return new BZip2CompressorOutputStream(var2);
            }

            if ("xz".equalsIgnoreCase(var1)) {
               return new XZCompressorOutputStream(var2);
            }

            if ("pack200".equalsIgnoreCase(var1)) {
               return new Pack200CompressorOutputStream(var2);
            }
         } catch (IOException var4) {
            throw new CompressorException("Could not create CompressorOutputStream", var4);
         }

         throw new CompressorException("Compressor: " + var1 + " not found.");
      } else {
         throw new IllegalArgumentException("Compressor name and stream must not be null.");
      }
   }
}
