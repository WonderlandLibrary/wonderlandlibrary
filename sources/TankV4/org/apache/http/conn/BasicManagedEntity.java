package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class BasicManagedEntity extends HttpEntityWrapper implements ConnectionReleaseTrigger, EofSensorWatcher {
   protected ManagedClientConnection managedConn;
   protected final boolean attemptReuse;

   public BasicManagedEntity(HttpEntity var1, ManagedClientConnection var2, boolean var3) {
      super(var1);
      Args.notNull(var2, "Connection");
      this.managedConn = var2;
      this.attemptReuse = var3;
   }

   public boolean isRepeatable() {
      return false;
   }

   public InputStream getContent() throws IOException {
      return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
   }

   private void ensureConsumed() throws IOException {
      if (this.managedConn != null) {
         if (this.attemptReuse) {
            EntityUtils.consume(this.wrappedEntity);
            this.managedConn.markReusable();
         } else {
            this.managedConn.unmarkReusable();
         }

         this.releaseManagedConnection();
      }
   }

   /** @deprecated */
   @Deprecated
   public void consumeContent() throws IOException {
      this.ensureConsumed();
   }

   public void writeTo(OutputStream var1) throws IOException {
      super.writeTo(var1);
      this.ensureConsumed();
   }

   public void releaseConnection() throws IOException {
      this.ensureConsumed();
   }

   public void abortConnection() throws IOException {
      if (this.managedConn != null) {
         this.managedConn.abortConnection();
         this.managedConn = null;
      }

   }

   public boolean eofDetected(InputStream var1) throws IOException {
      if (this.managedConn != null) {
         if (this.attemptReuse) {
            var1.close();
            this.managedConn.markReusable();
         } else {
            this.managedConn.unmarkReusable();
         }
      }

      this.releaseManagedConnection();
      return false;
   }

   public boolean streamClosed(InputStream var1) throws IOException {
      if (this.managedConn != null) {
         if (this.attemptReuse) {
            boolean var2 = this.managedConn.isOpen();

            try {
               var1.close();
               this.managedConn.markReusable();
            } catch (SocketException var5) {
               if (var2) {
                  throw var5;
               }
            }
         } else {
            this.managedConn.unmarkReusable();
         }
      }

      this.releaseManagedConnection();
      return false;
   }

   public boolean streamAbort(InputStream var1) throws IOException {
      if (this.managedConn != null) {
         this.managedConn.abortConnection();
      }

      return false;
   }

   protected void releaseManagedConnection() throws IOException {
      if (this.managedConn != null) {
         this.managedConn.releaseConnection();
         this.managedConn = null;
      }

   }
}
