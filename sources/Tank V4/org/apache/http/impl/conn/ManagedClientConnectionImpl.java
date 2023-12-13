package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

/** @deprecated */
@Deprecated
@NotThreadSafe
class ManagedClientConnectionImpl implements ManagedClientConnection {
   private final ClientConnectionManager manager;
   private final ClientConnectionOperator operator;
   private volatile HttpPoolEntry poolEntry;
   private volatile boolean reusable;
   private volatile long duration;

   ManagedClientConnectionImpl(ClientConnectionManager var1, ClientConnectionOperator var2, HttpPoolEntry var3) {
      Args.notNull(var1, "Connection manager");
      Args.notNull(var2, "Connection operator");
      Args.notNull(var3, "HTTP pool entry");
      this.manager = var1;
      this.operator = var2;
      this.poolEntry = var3;
      this.reusable = false;
      this.duration = Long.MAX_VALUE;
   }

   public String getId() {
      return null;
   }

   HttpPoolEntry getPoolEntry() {
      return this.poolEntry;
   }

   HttpPoolEntry detach() {
      HttpPoolEntry var1 = this.poolEntry;
      this.poolEntry = null;
      return var1;
   }

   public ClientConnectionManager getManager() {
      return this.manager;
   }

   private OperatedClientConnection getConnection() {
      HttpPoolEntry var1 = this.poolEntry;
      return var1 == null ? null : (OperatedClientConnection)var1.getConnection();
   }

   private OperatedClientConnection ensureConnection() {
      HttpPoolEntry var1 = this.poolEntry;
      if (var1 == null) {
         throw new ConnectionShutdownException();
      } else {
         return (OperatedClientConnection)var1.getConnection();
      }
   }

   private HttpPoolEntry ensurePoolEntry() {
      HttpPoolEntry var1 = this.poolEntry;
      if (var1 == null) {
         throw new ConnectionShutdownException();
      } else {
         return var1;
      }
   }

   public void close() throws IOException {
      HttpPoolEntry var1 = this.poolEntry;
      if (var1 != null) {
         OperatedClientConnection var2 = (OperatedClientConnection)var1.getConnection();
         var1.getTracker().reset();
         var2.close();
      }

   }

   public void shutdown() throws IOException {
      HttpPoolEntry var1 = this.poolEntry;
      if (var1 != null) {
         OperatedClientConnection var2 = (OperatedClientConnection)var1.getConnection();
         var1.getTracker().reset();
         var2.shutdown();
      }

   }

   public boolean isOpen() {
      OperatedClientConnection var1 = this.getConnection();
      return var1 != null ? var1.isOpen() : false;
   }

   public boolean isStale() {
      OperatedClientConnection var1 = this.getConnection();
      return var1 != null ? var1.isStale() : true;
   }

   public void setSocketTimeout(int var1) {
      OperatedClientConnection var2 = this.ensureConnection();
      var2.setSocketTimeout(var1);
   }

   public int getSocketTimeout() {
      OperatedClientConnection var1 = this.ensureConnection();
      return var1.getSocketTimeout();
   }

   public HttpConnectionMetrics getMetrics() {
      OperatedClientConnection var1 = this.ensureConnection();
      return var1.getMetrics();
   }

   public void flush() throws IOException {
      OperatedClientConnection var1 = this.ensureConnection();
      var1.flush();
   }

   public boolean isResponseAvailable(int var1) throws IOException {
      OperatedClientConnection var2 = this.ensureConnection();
      return var2.isResponseAvailable(var1);
   }

   public void receiveResponseEntity(HttpResponse var1) throws HttpException, IOException {
      OperatedClientConnection var2 = this.ensureConnection();
      var2.receiveResponseEntity(var1);
   }

   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
      OperatedClientConnection var1 = this.ensureConnection();
      return var1.receiveResponseHeader();
   }

   public void sendRequestEntity(HttpEntityEnclosingRequest var1) throws HttpException, IOException {
      OperatedClientConnection var2 = this.ensureConnection();
      var2.sendRequestEntity(var1);
   }

   public void sendRequestHeader(HttpRequest var1) throws HttpException, IOException {
      OperatedClientConnection var2 = this.ensureConnection();
      var2.sendRequestHeader(var1);
   }

   public InetAddress getLocalAddress() {
      OperatedClientConnection var1 = this.ensureConnection();
      return var1.getLocalAddress();
   }

   public int getLocalPort() {
      OperatedClientConnection var1 = this.ensureConnection();
      return var1.getLocalPort();
   }

   public InetAddress getRemoteAddress() {
      OperatedClientConnection var1 = this.ensureConnection();
      return var1.getRemoteAddress();
   }

   public int getRemotePort() {
      OperatedClientConnection var1 = this.ensureConnection();
      return var1.getRemotePort();
   }

   public boolean isSecure() {
      OperatedClientConnection var1 = this.ensureConnection();
      return var1.isSecure();
   }

   public void bind(Socket var1) throws IOException {
      throw new UnsupportedOperationException();
   }

   public Socket getSocket() {
      OperatedClientConnection var1 = this.ensureConnection();
      return var1.getSocket();
   }

   public SSLSession getSSLSession() {
      OperatedClientConnection var1 = this.ensureConnection();
      SSLSession var2 = null;
      Socket var3 = var1.getSocket();
      if (var3 instanceof SSLSocket) {
         var2 = ((SSLSocket)var3).getSession();
      }

      return var2;
   }

   public Object getAttribute(String var1) {
      OperatedClientConnection var2 = this.ensureConnection();
      return var2 instanceof HttpContext ? ((HttpContext)var2).getAttribute(var1) : null;
   }

   public Object removeAttribute(String var1) {
      OperatedClientConnection var2 = this.ensureConnection();
      return var2 instanceof HttpContext ? ((HttpContext)var2).removeAttribute(var1) : null;
   }

   public void setAttribute(String var1, Object var2) {
      OperatedClientConnection var3 = this.ensureConnection();
      if (var3 instanceof HttpContext) {
         ((HttpContext)var3).setAttribute(var1, var2);
      }

   }

   public HttpRoute getRoute() {
      HttpPoolEntry var1 = this.ensurePoolEntry();
      return var1.getEffectiveRoute();
   }

   public void open(HttpRoute var1, HttpContext var2, HttpParams var3) throws IOException {
      Args.notNull(var1, "Route");
      Args.notNull(var3, "HTTP parameters");
      synchronized(this){}
      if (this.poolEntry == null) {
         throw new ConnectionShutdownException();
      } else {
         RouteTracker var6 = this.poolEntry.getTracker();
         Asserts.notNull(var6, "Route tracker");
         Asserts.check(!var6.isConnected(), "Connection already open");
         OperatedClientConnection var4 = (OperatedClientConnection)this.poolEntry.getConnection();
         HttpHost var5 = var1.getProxyHost();
         this.operator.openConnection(var4, var5 != null ? var5 : var1.getTargetHost(), var1.getLocalAddress(), var2, var3);
         synchronized(this){}
         if (this.poolEntry == null) {
            throw new InterruptedIOException();
         } else {
            RouteTracker var7 = this.poolEntry.getTracker();
            if (var5 == null) {
               var7.connectTarget(var4.isSecure());
            } else {
               var7.connectProxy(var5, var4.isSecure());
            }

         }
      }
   }

   public void tunnelTarget(boolean var1, HttpParams var2) throws IOException {
      Args.notNull(var2, "HTTP parameters");
      synchronized(this){}
      if (this.poolEntry == null) {
         throw new ConnectionShutdownException();
      } else {
         RouteTracker var6 = this.poolEntry.getTracker();
         Asserts.notNull(var6, "Route tracker");
         Asserts.check(var6.isConnected(), "Connection not open");
         Asserts.check(!var6.isTunnelled(), "Connection is already tunnelled");
         HttpHost var3 = var6.getTargetHost();
         OperatedClientConnection var4 = (OperatedClientConnection)this.poolEntry.getConnection();
         var4.update((Socket)null, var3, var1, var2);
         synchronized(this){}
         if (this.poolEntry == null) {
            throw new InterruptedIOException();
         } else {
            var6 = this.poolEntry.getTracker();
            var6.tunnelTarget(var1);
         }
      }
   }

   public void tunnelProxy(HttpHost var1, boolean var2, HttpParams var3) throws IOException {
      Args.notNull(var1, "Next proxy");
      Args.notNull(var3, "HTTP parameters");
      synchronized(this){}
      if (this.poolEntry == null) {
         throw new ConnectionShutdownException();
      } else {
         RouteTracker var6 = this.poolEntry.getTracker();
         Asserts.notNull(var6, "Route tracker");
         Asserts.check(var6.isConnected(), "Connection not open");
         OperatedClientConnection var4 = (OperatedClientConnection)this.poolEntry.getConnection();
         var4.update((Socket)null, var1, var2, var3);
         synchronized(this){}
         if (this.poolEntry == null) {
            throw new InterruptedIOException();
         } else {
            var6 = this.poolEntry.getTracker();
            var6.tunnelProxy(var1, var2);
         }
      }
   }

   public void layerProtocol(HttpContext var1, HttpParams var2) throws IOException {
      Args.notNull(var2, "HTTP parameters");
      synchronized(this){}
      if (this.poolEntry == null) {
         throw new ConnectionShutdownException();
      } else {
         RouteTracker var6 = this.poolEntry.getTracker();
         Asserts.notNull(var6, "Route tracker");
         Asserts.check(var6.isConnected(), "Connection not open");
         Asserts.check(var6.isTunnelled(), "Protocol layering without a tunnel not supported");
         Asserts.check(!var6.isLayered(), "Multiple protocol layering not supported");
         HttpHost var3 = var6.getTargetHost();
         OperatedClientConnection var4 = (OperatedClientConnection)this.poolEntry.getConnection();
         this.operator.updateSecureConnection(var4, var3, var1, var2);
         synchronized(this){}
         if (this.poolEntry == null) {
            throw new InterruptedIOException();
         } else {
            var6 = this.poolEntry.getTracker();
            var6.layerProtocol(var4.isSecure());
         }
      }
   }

   public Object getState() {
      HttpPoolEntry var1 = this.ensurePoolEntry();
      return var1.getState();
   }

   public void setState(Object var1) {
      HttpPoolEntry var2 = this.ensurePoolEntry();
      var2.setState(var1);
   }

   public void markReusable() {
      this.reusable = true;
   }

   public void unmarkReusable() {
      this.reusable = false;
   }

   public boolean isMarkedReusable() {
      return this.reusable;
   }

   public void setIdleDuration(long var1, TimeUnit var3) {
      if (var1 > 0L) {
         this.duration = var3.toMillis(var1);
      } else {
         this.duration = -1L;
      }

   }

   public void releaseConnection() {
      synchronized(this){}
      if (this.poolEntry != null) {
         this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
         this.poolEntry = null;
      }
   }

   public void abortConnection() {
      synchronized(this){}
      if (this.poolEntry != null) {
         this.reusable = false;
         OperatedClientConnection var2 = (OperatedClientConnection)this.poolEntry.getConnection();

         try {
            var2.shutdown();
         } catch (IOException var5) {
         }

         this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
         this.poolEntry = null;
      }
   }
}
