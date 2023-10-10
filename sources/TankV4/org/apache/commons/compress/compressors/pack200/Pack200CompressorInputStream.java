package org.apache.commons.compress.compressors.pack200;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Unpacker;
import org.apache.commons.compress.compressors.CompressorInputStream;

public class Pack200CompressorInputStream extends CompressorInputStream {
   private final InputStream originalInput;
   private final StreamBridge streamBridge;
   private static final byte[] CAFE_DOOD = new byte[]{-54, -2, -48, 13};
   private static final int SIG_LENGTH;

   public Pack200CompressorInputStream(InputStream var1) throws IOException {
      this(var1, Pack200Strategy.IN_MEMORY);
   }

   public Pack200CompressorInputStream(InputStream var1, Pack200Strategy var2) throws IOException {
      this(var1, (File)null, var2, (Map)null);
   }

   public Pack200CompressorInputStream(InputStream var1, Map var2) throws IOException {
      this(var1, Pack200Strategy.IN_MEMORY, var2);
   }

   public Pack200CompressorInputStream(InputStream var1, Pack200Strategy var2, Map var3) throws IOException {
      this(var1, (File)null, var2, var3);
   }

   public Pack200CompressorInputStream(File var1) throws IOException {
      this(var1, Pack200Strategy.IN_MEMORY);
   }

   public Pack200CompressorInputStream(File var1, Pack200Strategy var2) throws IOException {
      this((InputStream)null, var1, var2, (Map)null);
   }

   public Pack200CompressorInputStream(File var1, Map var2) throws IOException {
      this(var1, Pack200Strategy.IN_MEMORY, var2);
   }

   public Pack200CompressorInputStream(File var1, Pack200Strategy var2, Map var3) throws IOException {
      this((InputStream)null, var1, var2, var3);
   }

   private Pack200CompressorInputStream(InputStream var1, File var2, Pack200Strategy var3, Map var4) throws IOException {
      this.originalInput = var1;
      this.streamBridge = var3.newStreamBridge();
      JarOutputStream var5 = new JarOutputStream(this.streamBridge);
      Unpacker var6 = Pack200.newUnpacker();
      if (var4 != null) {
         var6.properties().putAll(var4);
      }

      if (var2 == null) {
         var6.unpack(new FilterInputStream(this, var1) {
            final Pack200CompressorInputStream this$0;

            {
               this.this$0 = var1;
            }

            public void close() {
            }
         }, var5);
      } else {
         var6.unpack(var2, var5);
      }

      var5.close();
   }

   public int read() throws IOException {
      return this.streamBridge.getInput().read();
   }

   public int read(byte[] var1) throws IOException {
      return this.streamBridge.getInput().read(var1);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      return this.streamBridge.getInput().read(var1, var2, var3);
   }

   public int available() throws IOException {
      return this.streamBridge.getInput().available();
   }

   public boolean markSupported() {
      try {
         return this.streamBridge.getInput().markSupported();
      } catch (IOException var2) {
         return false;
      }
   }

   public void mark(int var1) {
      try {
         this.streamBridge.getInput().mark(var1);
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void reset() throws IOException {
      this.streamBridge.getInput().reset();
   }

   public long skip(long var1) throws IOException {
      return this.streamBridge.getInput().skip(var1);
   }

   public void close() throws IOException {
      this.streamBridge.stop();
      if (this.originalInput != null) {
         this.originalInput.close();
      }

   }

   public static boolean matches(byte[] var0, int var1) {
      if (var1 < SIG_LENGTH) {
         return false;
      } else {
         for(int var2 = 0; var2 < SIG_LENGTH; ++var2) {
            if (var0[var2] != CAFE_DOOD[var2]) {
               return false;
            }
         }

         return true;
      }
   }

   static {
      SIG_LENGTH = CAFE_DOOD.length;
   }
}
