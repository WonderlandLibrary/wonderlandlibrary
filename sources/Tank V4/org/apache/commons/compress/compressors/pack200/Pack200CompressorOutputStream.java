package org.apache.commons.compress.compressors.pack200;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Packer;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class Pack200CompressorOutputStream extends CompressorOutputStream {
   private boolean finished;
   private final OutputStream originalOutput;
   private final StreamBridge streamBridge;
   private final Map properties;

   public Pack200CompressorOutputStream(OutputStream var1) throws IOException {
      this(var1, Pack200Strategy.IN_MEMORY);
   }

   public Pack200CompressorOutputStream(OutputStream var1, Pack200Strategy var2) throws IOException {
      this(var1, var2, (Map)null);
   }

   public Pack200CompressorOutputStream(OutputStream var1, Map var2) throws IOException {
      this(var1, Pack200Strategy.IN_MEMORY, var2);
   }

   public Pack200CompressorOutputStream(OutputStream var1, Pack200Strategy var2, Map var3) throws IOException {
      this.finished = false;
      this.originalOutput = var1;
      this.streamBridge = var2.newStreamBridge();
      this.properties = var3;
   }

   public void write(int var1) throws IOException {
      this.streamBridge.write(var1);
   }

   public void write(byte[] var1) throws IOException {
      this.streamBridge.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.streamBridge.write(var1, var2, var3);
   }

   public void close() throws IOException {
      this.finish();
      this.streamBridge.stop();
      this.originalOutput.close();
   }

   public void finish() throws IOException {
      if (!this.finished) {
         this.finished = true;
         Packer var1 = Pack200.newPacker();
         if (this.properties != null) {
            var1.properties().putAll(this.properties);
         }

         JarInputStream var2 = null;
         boolean var3 = false;
         var1.pack(var2 = new JarInputStream(this.streamBridge.getInput()), this.originalOutput);
         var3 = true;
         if (!var3) {
            IOUtils.closeQuietly(var2);
         }
      }

   }
}
