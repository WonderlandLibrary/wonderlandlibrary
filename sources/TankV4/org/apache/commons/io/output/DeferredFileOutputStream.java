package org.apache.commons.io.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class DeferredFileOutputStream extends ThresholdingOutputStream {
   private ByteArrayOutputStream memoryOutputStream;
   private OutputStream currentOutputStream;
   private File outputFile;
   private final String prefix;
   private final String suffix;
   private final File directory;
   private boolean closed;

   public DeferredFileOutputStream(int var1, File var2) {
      this(var1, var2, (String)null, (String)null, (File)null);
   }

   public DeferredFileOutputStream(int var1, String var2, String var3, File var4) {
      this(var1, (File)null, var2, var3, var4);
      if (var2 == null) {
         throw new IllegalArgumentException("Temporary file prefix is missing");
      }
   }

   private DeferredFileOutputStream(int var1, File var2, String var3, String var4, File var5) {
      super(var1);
      this.closed = false;
      this.outputFile = var2;
      this.memoryOutputStream = new ByteArrayOutputStream();
      this.currentOutputStream = this.memoryOutputStream;
      this.prefix = var3;
      this.suffix = var4;
      this.directory = var5;
   }

   protected OutputStream getStream() throws IOException {
      return this.currentOutputStream;
   }

   protected void thresholdReached() throws IOException {
      if (this.prefix != null) {
         this.outputFile = File.createTempFile(this.prefix, this.suffix, this.directory);
      }

      FileOutputStream var1 = new FileOutputStream(this.outputFile);
      this.memoryOutputStream.writeTo(var1);
      this.currentOutputStream = var1;
      this.memoryOutputStream = null;
   }

   public byte[] getData() {
      return this.memoryOutputStream != null ? this.memoryOutputStream.toByteArray() : null;
   }

   public File getFile() {
      return this.outputFile;
   }

   public void close() throws IOException {
      super.close();
      this.closed = true;
   }

   public void writeTo(OutputStream var1) throws IOException {
      if (!this.closed) {
         throw new IOException("Stream not closed");
      } else {
         if (this == false) {
            this.memoryOutputStream.writeTo(var1);
         } else {
            FileInputStream var2 = new FileInputStream(this.outputFile);
            IOUtils.copy((InputStream)var2, (OutputStream)var1);
            IOUtils.closeQuietly((InputStream)var2);
         }

      }
   }
}
