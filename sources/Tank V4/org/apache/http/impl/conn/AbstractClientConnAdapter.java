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
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.protocol.HttpContext;

/** @deprecated */
@Deprecated
@NotThreadSafe
public abstract class AbstractClientConnAdapter implements ManagedClientConnection, HttpContext {
   private final ClientConnectionManager connManager;
   private volatile OperatedClientConnection wrappedConnection;
   private volatile boolean markedReusable;
   private volatile boolean released;
   private volatile long duration;

   protected AbstractClientConnAdapter(ClientConnectionManager var1, OperatedClientConnection var2) {
      this.connManager = var1;
      this.wrappedConnection = var2;
      this.markedReusable = false;
      this.released = false;
      this.duration = Long.MAX_VALUE;
   }

   protected synchronized void detach() {
      this.wrappedConnection = null;
      this.duration = Long.MAX_VALUE;
   }

   protected OperatedClientConnection getWrappedConnection() {
      return this.wrappedConnection;
   }

   protected ClientConnectionManager getManager() {
      return this.connManager;
   }

   /** @deprecated */
   @Deprecated
   protected final void assertNotAborted() throws InterruptedIOException {
      if (this.isReleased()) {
         throw new InterruptedIOException("Connection has been shut down");
      }
   }

   protected boolean isReleased() {
      return this.released;
   }

   protected final void assertValid(OperatedClientConnection var1) throws ConnectionShutdownException {
      if (this.isReleased() || var1 == null) {
         throw new ConnectionShutdownException();
      }
   }

   public boolean isStale() {
      if (this.isReleased()) {
         return true;
      } else {
         OperatedClientConnection var1 = this.getWrappedConnection();
         return var1 == null ? true : var1.isStale();
      }
   }

   public void setSocketTimeout(int var1) {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      var2.setSocketTimeout(var1);
   }

   public int getSocketTimeout() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getSocketTimeout();
   }

   public HttpConnectionMetrics getMetrics() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getMetrics();
   }

   public void flush() throws IOException {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      var1.flush();
   }

   public boolean isResponseAvailable(int var1) throws IOException {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      return var2.isResponseAvailable(var1);
   }

   public void receiveResponseEntity(HttpResponse var1) throws HttpException, IOException {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      this.unmarkReusable();
      var2.receiveResponseEntity(var1);
   }

   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      this.unmarkReusable();
      return var1.receiveResponseHeader();
   }

   public void sendRequestEntity(HttpEntityEnclosingRequest var1) throws HttpException, IOException {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      this.unmarkReusable();
      var2.sendRequestEntity(var1);
   }

   public void sendRequestHeader(HttpRequest var1) throws HttpException, IOException {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      this.unmarkReusable();
      var2.sendRequestHeader(var1);
   }

   public InetAddress getLocalAddress() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getLocalAddress();
   }

   public int getLocalPort() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getLocalPort();
   }

   public InetAddress getRemoteAddress() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getRemoteAddress();
   }

   public int getRemotePort() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.getRemotePort();
   }

   public boolean isSecure() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return var1.isSecure();
   }

   public void bind(Socket var1) throws IOException {
      throw new UnsupportedOperationException();
   }

   public Socket getSocket() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      return this == null ? null : var1.getSocket();
   }

   public SSLSession getSSLSession() {
      OperatedClientConnection var1 = this.getWrappedConnection();
      this.assertValid(var1);
      if (this == null) {
         return null;
      } else {
         SSLSession var2 = null;
         Socket var3 = var1.getSocket();
         if (var3 instanceof SSLSocket) {
            var2 = ((SSLSocket)var3).getSession();
         }

         return var2;
      }
   }

   public void markReusable() {
      this.markedReusable = true;
   }

   public void unmarkReusable() {
      this.markedReusable = false;
   }

   public boolean isMarkedReusable() {
      return this.markedReusable;
   }

   public void setIdleDuration(long var1, TimeUnit var3) {
      if (var1 > 0L) {
         this.duration = var3.toMillis(var1);
      } else {
         this.duration = -1L;
      }

   }

   public synchronized void releaseConnection() {
      if (!this.released) {
         this.released = true;
         this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
      }
   }

   public synchronized void abortConnection() {
      if (!this.released) {
         this.released = true;
         this.unmarkReusable();

         try {
            this.shutdown();
         } catch (IOException var2) {
         }

         this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
      }
   }

   public Object getAttribute(String var1) {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      return var2 instanceof HttpContext ? ((HttpContext)var2).getAttribute(var1) : null;
   }

   public Object removeAttribute(String var1) {
      OperatedClientConnection var2 = this.getWrappedConnection();
      this.assertValid(var2);
      return var2 instanceof HttpContext ? ((HttpContext)var2).removeAttribute(var1) : null;
   }

   public void setAttribute(String var1, Object var2) {
      OperatedClientConnection var3 = this.getWrappedConnection();
      this.assertValid(var3);
      if (var3 instanceof HttpContext) {
         ((HttpContext)var3).setAttribute(var1, var2);
      }

   }
}
