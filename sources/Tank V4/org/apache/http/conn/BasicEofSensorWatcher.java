package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class BasicEofSensorWatcher implements EofSensorWatcher {
   protected final ManagedClientConnection managedConn;
   protected final boolean attemptReuse;

   public BasicEofSensorWatcher(ManagedClientConnection var1, boolean var2) {
      Args.notNull(var1, "Connection");
      this.managedConn = var1;
      this.attemptReuse = var2;
   }

   public boolean eofDetected(InputStream var1) throws IOException {
      if (this.attemptReuse) {
         var1.close();
         this.managedConn.markReusable();
      }

      this.managedConn.releaseConnection();
      return false;
   }

   public boolean streamClosed(InputStream var1) throws IOException {
      if (this.attemptReuse) {
         var1.close();
         this.managedConn.markReusable();
      }

      this.managedConn.releaseConnection();
      return false;
   }

   public boolean streamAbort(InputStream var1) throws IOException {
      this.managedConn.abortConnection();
      return false;
   }
}
