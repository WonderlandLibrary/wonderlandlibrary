package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.conn.EofSensorWatcher;
import org.apache.http.entity.HttpEntityWrapper;

@NotThreadSafe
class ResponseEntityWrapper extends HttpEntityWrapper implements EofSensorWatcher {
   private final ConnectionHolder connReleaseTrigger;

   public ResponseEntityWrapper(HttpEntity var1, ConnectionHolder var2) {
      super(var1);
      this.connReleaseTrigger = var2;
   }

   private void cleanup() {
      if (this.connReleaseTrigger != null) {
         this.connReleaseTrigger.abortConnection();
      }

   }

   public void releaseConnection() throws IOException {
      if (this.connReleaseTrigger != null) {
         if (this.connReleaseTrigger.isReusable()) {
            this.connReleaseTrigger.releaseConnection();
         }

         this.cleanup();
      }

   }

   public boolean isRepeatable() {
      return false;
   }

   public InputStream getContent() throws IOException {
      return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
   }

   /** @deprecated */
   @Deprecated
   public void consumeContent() throws IOException {
      this.releaseConnection();
   }

   public void writeTo(OutputStream var1) throws IOException {
      this.wrappedEntity.writeTo(var1);
      this.releaseConnection();
      this.cleanup();
   }

   public boolean eofDetected(InputStream var1) throws IOException {
      var1.close();
      this.releaseConnection();
      this.cleanup();
      return false;
   }

   public boolean streamClosed(InputStream var1) throws IOException {
      boolean var2 = this.connReleaseTrigger != null && !this.connReleaseTrigger.isReleased();

      try {
         var1.close();
         this.releaseConnection();
      } catch (SocketException var5) {
         if (var2) {
            throw var5;
         }
      }

      this.cleanup();
      return false;
   }

   public boolean streamAbort(InputStream var1) throws IOException {
      this.cleanup();
      return false;
   }
}
