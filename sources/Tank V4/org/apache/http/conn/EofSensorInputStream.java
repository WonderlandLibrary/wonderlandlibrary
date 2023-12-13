package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class EofSensorInputStream extends InputStream implements ConnectionReleaseTrigger {
   protected InputStream wrappedStream;
   private boolean selfClosed;
   private final EofSensorWatcher eofWatcher;

   public EofSensorInputStream(InputStream var1, EofSensorWatcher var2) {
      Args.notNull(var1, "Wrapped stream");
      this.wrappedStream = var1;
      this.selfClosed = false;
      this.eofWatcher = var2;
   }

   boolean isSelfClosed() {
      return this.selfClosed;
   }

   InputStream getWrappedStream() {
      return this.wrappedStream;
   }

   public int read() throws IOException {
      int var1 = -1;
      if (this != false) {
         try {
            var1 = this.wrappedStream.read();
            this.checkEOF(var1);
         } catch (IOException var3) {
            this.checkAbort();
            throw var3;
         }
      }

      return var1;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = -1;
      if (this != false) {
         try {
            var4 = this.wrappedStream.read(var1, var2, var3);
            this.checkEOF(var4);
         } catch (IOException var6) {
            this.checkAbort();
            throw var6;
         }
      }

      return var4;
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int available() throws IOException {
      int var1 = 0;
      if (this != false) {
         try {
            var1 = this.wrappedStream.available();
         } catch (IOException var3) {
            this.checkAbort();
            throw var3;
         }
      }

      return var1;
   }

   public void close() throws IOException {
      this.selfClosed = true;
      this.checkClose();
   }

   protected void checkEOF(int var1) throws IOException {
      if (this.wrappedStream != null && var1 < 0) {
         boolean var2 = true;
         if (this.eofWatcher != null) {
            var2 = this.eofWatcher.eofDetected(this.wrappedStream);
         }

         if (var2) {
            this.wrappedStream.close();
         }

         this.wrappedStream = null;
      }

   }

   protected void checkClose() throws IOException {
      if (this.wrappedStream != null) {
         boolean var1 = true;
         if (this.eofWatcher != null) {
            var1 = this.eofWatcher.streamClosed(this.wrappedStream);
         }

         if (var1) {
            this.wrappedStream.close();
         }

         this.wrappedStream = null;
      }

   }

   protected void checkAbort() throws IOException {
      if (this.wrappedStream != null) {
         boolean var1 = true;
         if (this.eofWatcher != null) {
            var1 = this.eofWatcher.streamAbort(this.wrappedStream);
         }

         if (var1) {
            this.wrappedStream.close();
         }

         this.wrappedStream = null;
      }

   }

   public void releaseConnection() throws IOException {
      this.close();
   }

   public void abortConnection() throws IOException {
      this.selfClosed = true;
      this.checkAbort();
   }
}
