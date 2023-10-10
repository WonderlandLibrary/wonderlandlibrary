package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/** @deprecated */
@Deprecated
public abstract class AbstractPoolEntry {
   protected final ClientConnectionOperator connOperator;
   protected final OperatedClientConnection connection;
   protected volatile HttpRoute route;
   protected volatile Object state;
   protected volatile RouteTracker tracker;

   protected AbstractPoolEntry(ClientConnectionOperator var1, HttpRoute var2) {
      Args.notNull(var1, "Connection operator");
      this.connOperator = var1;
      this.connection = var1.createConnection();
      this.route = var2;
      this.tracker = null;
   }

   public Object getState() {
      return this.state;
   }

   public void setState(Object var1) {
      this.state = var1;
   }

   public void open(HttpRoute var1, HttpContext var2, HttpParams var3) throws IOException {
      Args.notNull(var1, "Route");
      Args.notNull(var3, "HTTP parameters");
      if (this.tracker != null) {
         Asserts.check(!this.tracker.isConnected(), "Connection already open");
      }

      this.tracker = new RouteTracker(var1);
      HttpHost var4 = var1.getProxyHost();
      this.connOperator.openConnection(this.connection, var4 != null ? var4 : var1.getTargetHost(), var1.getLocalAddress(), var2, var3);
      RouteTracker var5 = this.tracker;
      if (var5 == null) {
         throw new InterruptedIOException("Request aborted");
      } else {
         if (var4 == null) {
            var5.connectTarget(this.connection.isSecure());
         } else {
            var5.connectProxy(var4, this.connection.isSecure());
         }

      }
   }

   public void tunnelTarget(boolean var1, HttpParams var2) throws IOException {
      Args.notNull(var2, "HTTP parameters");
      Asserts.notNull(this.tracker, "Route tracker");
      Asserts.check(this.tracker.isConnected(), "Connection not open");
      Asserts.check(!this.tracker.isTunnelled(), "Connection is already tunnelled");
      this.connection.update((Socket)null, this.tracker.getTargetHost(), var1, var2);
      this.tracker.tunnelTarget(var1);
   }

   public void tunnelProxy(HttpHost var1, boolean var2, HttpParams var3) throws IOException {
      Args.notNull(var1, "Next proxy");
      Args.notNull(var3, "Parameters");
      Asserts.notNull(this.tracker, "Route tracker");
      Asserts.check(this.tracker.isConnected(), "Connection not open");
      this.connection.update((Socket)null, var1, var2, var3);
      this.tracker.tunnelProxy(var1, var2);
   }

   public void layerProtocol(HttpContext var1, HttpParams var2) throws IOException {
      Args.notNull(var2, "HTTP parameters");
      Asserts.notNull(this.tracker, "Route tracker");
      Asserts.check(this.tracker.isConnected(), "Connection not open");
      Asserts.check(this.tracker.isTunnelled(), "Protocol layering without a tunnel not supported");
      Asserts.check(!this.tracker.isLayered(), "Multiple protocol layering not supported");
      HttpHost var3 = this.tracker.getTargetHost();
      this.connOperator.updateSecureConnection(this.connection, var3, var1, var2);
      this.tracker.layerProtocol(this.connection.isSecure());
   }

   protected void shutdownEntry() {
      this.tracker = null;
      this.state = null;
   }
}
