package org.apache.commons.compress.archivers.sevenz;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.tukaani.xz.ARMOptions;
import org.tukaani.xz.ARMThumbOptions;
import org.tukaani.xz.FilterOptions;
import org.tukaani.xz.FinishableOutputStream;
import org.tukaani.xz.FinishableWrapperOutputStream;
import org.tukaani.xz.IA64Options;
import org.tukaani.xz.LZMAInputStream;
import org.tukaani.xz.PowerPCOptions;
import org.tukaani.xz.SPARCOptions;
import org.tukaani.xz.X86Options;

class Coders {
   private static final Map CODER_MAP = new HashMap() {
      private static final long serialVersionUID = 1664829131806520867L;

      {
         this.put(SevenZMethod.COPY, new Coders.CopyDecoder());
         this.put(SevenZMethod.LZMA, new Coders.LZMADecoder());
         this.put(SevenZMethod.LZMA2, new LZMA2Decoder());
         this.put(SevenZMethod.DEFLATE, new Coders.DeflateDecoder());
         this.put(SevenZMethod.BZIP2, new Coders.BZIP2Decoder());
         this.put(SevenZMethod.AES256SHA256, new AES256SHA256Decoder());
         this.put(SevenZMethod.BCJ_X86_FILTER, new Coders.BCJDecoder(new X86Options()));
         this.put(SevenZMethod.BCJ_PPC_FILTER, new Coders.BCJDecoder(new PowerPCOptions()));
         this.put(SevenZMethod.BCJ_IA64_FILTER, new Coders.BCJDecoder(new IA64Options()));
         this.put(SevenZMethod.BCJ_ARM_FILTER, new Coders.BCJDecoder(new ARMOptions()));
         this.put(SevenZMethod.BCJ_ARM_THUMB_FILTER, new Coders.BCJDecoder(new ARMThumbOptions()));
         this.put(SevenZMethod.BCJ_SPARC_FILTER, new Coders.BCJDecoder(new SPARCOptions()));
         this.put(SevenZMethod.DELTA_FILTER, new DeltaDecoder());
      }
   };

   static CoderBase findByMethod(SevenZMethod var0) {
      return (CoderBase)CODER_MAP.get(var0);
   }

   static InputStream addDecoder(InputStream var0, Coder var1, byte[] var2) throws IOException {
      CoderBase var3 = findByMethod(SevenZMethod.byId(var1.decompressionMethodId));
      if (var3 == null) {
         throw new IOException("Unsupported compression method " + Arrays.toString(var1.decompressionMethodId));
      } else {
         return var3.decode(var0, var1, var2);
      }
   }

   static OutputStream addEncoder(OutputStream var0, SevenZMethod var1, Object var2) throws IOException {
      CoderBase var3 = findByMethod(var1);
      if (var3 == null) {
         throw new IOException("Unsupported compression method " + var1);
      } else {
         return var3.encode(var0, var2);
      }
   }

   private static class DummyByteAddingInputStream extends FilterInputStream {
      private boolean addDummyByte;

      private DummyByteAddingInputStream(InputStream var1) {
         super(var1);
         this.addDummyByte = true;
      }

      public int read() throws IOException {
         int var1 = super.read();
         if (var1 == -1 && this.addDummyByte) {
            this.addDummyByte = false;
            var1 = 0;
         }

         return var1;
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         int var4 = super.read(var1, var2, var3);
         if (var4 == -1 && this.addDummyByte) {
            this.addDummyByte = false;
            var1[var2] = 0;
            return 1;
         } else {
            return var4;
         }
      }

      DummyByteAddingInputStream(InputStream var1, Object var2) {
         this(var1);
      }
   }

   static class BZIP2Decoder extends CoderBase {
      BZIP2Decoder() {
         super(Number.class);
      }

      InputStream decode(InputStream var1, Coder var2, byte[] var3) throws IOException {
         return new BZip2CompressorInputStream(var1);
      }

      OutputStream encode(OutputStream var1, Object var2) throws IOException {
         int var3 = numberOptionOrDefault(var2, 9);
         return new BZip2CompressorOutputStream(var1, var3);
      }
   }

   static class DeflateDecoder extends CoderBase {
      DeflateDecoder() {
         super(Number.class);
      }

      InputStream decode(InputStream var1, Coder var2, byte[] var3) throws IOException {
         return new InflaterInputStream(new Coders.DummyByteAddingInputStream(var1), new Inflater(true));
      }

      OutputStream encode(OutputStream var1, Object var2) {
         int var3 = numberOptionOrDefault(var2, 9);
         return new DeflaterOutputStream(var1, new Deflater(var3, true));
      }
   }

   static class BCJDecoder extends CoderBase {
      private final FilterOptions opts;

      BCJDecoder(FilterOptions var1) {
         super();
         this.opts = var1;
      }

      InputStream decode(InputStream var1, Coder var2, byte[] var3) throws IOException {
         try {
            return this.opts.getInputStream(var1);
         } catch (AssertionError var6) {
            IOException var5 = new IOException("BCJ filter needs XZ for Java > 1.4 - see http://commons.apache.org/proper/commons-compress/limitations.html#7Z");
            var5.initCause(var6);
            throw var5;
         }
      }

      OutputStream encode(OutputStream var1, Object var2) {
         FinishableOutputStream var3 = this.opts.getOutputStream(new FinishableWrapperOutputStream(var1));
         return new FilterOutputStream(this, var3) {
            final Coders.BCJDecoder this$0;

            {
               this.this$0 = var1;
            }

            public void flush() {
            }
         };
      }
   }

   static class LZMADecoder extends CoderBase {
      LZMADecoder() {
         super();
      }

      InputStream decode(InputStream var1, Coder var2, byte[] var3) throws IOException {
         byte var4 = var2.properties[0];
         long var5 = (long)var2.properties[1];

         for(int var7 = 1; var7 < 4; ++var7) {
            var5 |= ((long)var2.properties[var7 + 1] & 255L) << 8 * var7;
         }

         if (var5 > 2147483632L) {
            throw new IOException("Dictionary larger than 4GiB maximum size");
         } else {
            return new LZMAInputStream(var1, -1L, var4, (int)var5);
         }
      }
   }

   static class CopyDecoder extends CoderBase {
      CopyDecoder() {
         super();
      }

      InputStream decode(InputStream var1, Coder var2, byte[] var3) throws IOException {
         return var1;
      }

      OutputStream encode(OutputStream var1, Object var2) {
         return var1;
      }
   }
}
