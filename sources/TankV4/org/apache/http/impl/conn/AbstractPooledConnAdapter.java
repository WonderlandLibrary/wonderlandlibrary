package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/** @deprecated */
@Deprecated
public abstract class AbstractPooledConnAdapter extends AbstractClientConnAdapter {
   protected volatile AbstractPoolEntry poolEntry;

   protected AbstractPooledConnAdapter(ClientConnectionManager var1, AbstractPoolEntry var2) {
      super(var1, var2.connection);
      this.poolEntry = var2;
   }

   public String getId() {
      return null;
   }

   /** @deprecated */
   @Deprecated
   protected AbstractPoolEntry getPoolEntry() {
      return this.poolEntry;
   }

   protected void assertValid(AbstractPoolEntry var1) {
      if (this.isReleased() || var1 == null) {
         throw new ConnectionShutdownException();
      }
   }

   /** @deprecated */
   @Deprecated
   protected final void assertAttached() {
      if (this.poolEntry == null) {
         throw new ConnectionShutdownException();
      }
   }

   protected synchronized void detach() {
      this.poolEntry = null;
      super.detach();
   }

   public HttpRoute getRoute() {
      AbstractPoolEntry var1 = this.getPoolEntry();
      this.assertValid(var1);
      return var1.tracker == null ? null : var1.tracker.toRoute();
   }

   public void open(HttpRoute var1, HttpContext var2, HttpParams var3) throws IOException {
      AbstractPoolEntry var4 = this.getPoolEntry();
      this.assertValid(var4);
      var4.open(var1, var2, var3);
   }

   public void tunnelTarget(boolean var1, HttpParams var2) throws IOException {
      AbstractPoolEntry var3 = this.getPoolEntry();
      this.assertValid(var3);
      var3.tunnelTarget(var1, var2);
   }

   public void tunnelProxy(HttpHost var1, boolean var2, HttpParams var3) throws IOException {
      AbstractPoolEntry var4 = this.getPoolEntry();
      this.assertValid(var4);
      var4.tunnelProxy(var1, var2, var3);
   }

   public void layerProtocol(HttpContext var1, HttpParams var2) throws IOException {
      AbstractPoolEntry var3 = this.getPoolEntry();
      this.assertValid(var3);
      var3.layerProtocol(var1, var2);
   }

   public void close() throws IOException {
      AbstractPoolEntry var1 = this.getPoolEntry();
      if (var1 != null) {
         var1.shutdownEntry();
      }

      OperatedClientConnection var2 = this.getWrappedConnection();
      if (var2 != null) {
         var2.close();
      }

   }

   public void shutdown() throws IOException {
      AbstractPoolEntry var1 = this.getPoolEntry();
      if (var1 != null) {
         var1.shutdownEntry();
      }

      OperatedClientConnection var2 = this.getWrappedConnection();
      if (var2 != null) {
         var2.shutdown();
      }

   }

   public Object getState() {
      AbstractPoolEntry var1 = this.getPoolEntry();
      this.assertValid(var1);
      return var1.getState();
   }

   public void setState(Object var1) {
      AbstractPoolEntry var2 = this.getPoolEntry();
      this.assertValid(var2);
      var2.setState(var1);
   }
}
