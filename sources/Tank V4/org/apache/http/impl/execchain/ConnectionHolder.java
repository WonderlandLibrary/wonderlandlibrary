package org.apache.http.impl.execchain;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.HttpClientConnection;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.conn.HttpClientConnectionManager;

@ThreadSafe
class ConnectionHolder implements ConnectionReleaseTrigger, Cancellable, Closeable {
   private final Log log;
   private final HttpClientConnectionManager manager;
   private final HttpClientConnection managedConn;
   private volatile boolean reusable;
   private volatile Object state;
   private volatile long validDuration;
   private volatile TimeUnit tunit;
   private volatile boolean released;

   public ConnectionHolder(Log var1, HttpClientConnectionManager var2, HttpClientConnection var3) {
      this.log = var1;
      this.manager = var2;
      this.managedConn = var3;
   }

   public boolean isReusable() {
      return this.reusable;
   }

   public void markReusable() {
      this.reusable = true;
   }

   public void markNonReusable() {
      this.reusable = false;
   }

   public void setState(Object var1) {
      this.state = var1;
   }

   public void setValidFor(long var1, TimeUnit var3) {
      HttpClientConnection var4;
      synchronized(var4 = this.managedConn){}
      this.validDuration = var1;
      this.tunit = var3;
   }

   public void releaseConnection() {
      HttpClientConnection var1;
      synchronized(var1 = this.managedConn){}
      if (!this.released) {
         this.released = true;
         if (this.reusable) {
            this.manager.releaseConnection(this.managedConn, this.state, this.validDuration, this.tunit);
         } else {
            try {
               this.managedConn.close();
               this.log.debug("Connection discarded");
            } catch (IOException var5) {
               if (this.log.isDebugEnabled()) {
                  this.log.debug(var5.getMessage(), var5);
               }

               this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
               return;
            }

            this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
         }

      }
   }

   public void abortConnection() {
      HttpClientConnection var1;
      synchronized(var1 = this.managedConn){}
      if (!this.released) {
         this.released = true;

         try {
            this.managedConn.shutdown();
            this.log.debug("Connection discarded");
         } catch (IOException var5) {
            if (this.log.isDebugEnabled()) {
               this.log.debug(var5.getMessage(), var5);
            }

            this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
            return;
         }

         this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
      }
   }

   public boolean cancel() {
      boolean var1 = this.released;
      this.log.debug("Cancelling request execution");
      this.abortConnection();
      return !var1;
   }

   public boolean isReleased() {
      return this.released;
   }

   public void close() throws IOException {
      this.abortConnection();
   }
}
