package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ThresholdingOutputStream extends OutputStream {
   private final int threshold;
   private long written;
   private boolean thresholdExceeded;

   public ThresholdingOutputStream(int var1) {
      this.threshold = var1;
   }

   public void write(int var1) throws IOException {
      this.checkThreshold(1);
      this.getStream().write(var1);
      ++this.written;
   }

   public void write(byte[] var1) throws IOException {
      this.checkThreshold(var1.length);
      this.getStream().write(var1);
      this.written += (long)var1.length;
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.checkThreshold(var3);
      this.getStream().write(var1, var2, var3);
      this.written += (long)var3;
   }

   public void flush() throws IOException {
      this.getStream().flush();
   }

   public void close() throws IOException {
      try {
         this.flush();
      } catch (IOException var2) {
      }

      this.getStream().close();
   }

   public int getThreshold() {
      return this.threshold;
   }

   public long getByteCount() {
      return this.written;
   }

   public boolean isThresholdExceeded() {
      return this.written > (long)this.threshold;
   }

   protected void checkThreshold(int var1) throws IOException {
      if (!this.thresholdExceeded && this.written + (long)var1 > (long)this.threshold) {
         this.thresholdExceeded = true;
         this.thresholdReached();
      }

   }

   protected void resetByteCount() {
      this.thresholdExceeded = false;
      this.written = 0L;
   }

   protected abstract OutputStream getStream() throws IOException;

   protected abstract void thresholdReached() throws IOException;
}
