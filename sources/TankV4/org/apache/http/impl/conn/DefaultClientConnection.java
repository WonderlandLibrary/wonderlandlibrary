package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.impl.SocketHttpClientConnection;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class DefaultClientConnection extends SocketHttpClientConnection implements OperatedClientConnection, ManagedHttpClientConnection, HttpContext {
   private final Log log = LogFactory.getLog(this.getClass());
   private final Log headerLog = LogFactory.getLog("org.apache.http.headers");
   private final Log wireLog = LogFactory.getLog("org.apache.http.wire");
   private volatile Socket socket;
   private HttpHost targetHost;
   private boolean connSecure;
   private volatile boolean shutdown;
   private final Map attributes = new HashMap();

   public String getId() {
      return null;
   }

   public final HttpHost getTargetHost() {
      return this.targetHost;
   }

   public final boolean isSecure() {
      return this.connSecure;
   }

   public final Socket getSocket() {
      return this.socket;
   }

   public SSLSession getSSLSession() {
      return this.socket instanceof SSLSocket ? ((SSLSocket)this.socket).getSession() : null;
   }

   public void opening(Socket var1, HttpHost var2) throws IOException {
      this.assertNotOpen();
      this.socket = var1;
      this.targetHost = var2;
      if (this.shutdown) {
         var1.close();
         throw new InterruptedIOException("Connection already shutdown");
      }
   }

   public void openCompleted(boolean var1, HttpParams var2) throws IOException {
      Args.notNull(var2, "Parameters");
      this.assertNotOpen();
      this.connSecure = var1;
      this.bind(this.socket, var2);
   }

   public void shutdown() throws IOException {
      this.shutdown = true;

      try {
         super.shutdown();
         if (this.log.isDebugEnabled()) {
            this.log.debug("Connection " + this + " shut down");
         }

         Socket var1 = this.socket;
         if (var1 != null) {
            var1.close();
         }
      } catch (IOException var2) {
         this.log.debug("I/O error shutting down connection", var2);
      }

   }

   public void close() throws IOException {
      try {
         super.close();
         if (this.log.isDebugEnabled()) {
            this.log.debug("Connection " + this + " closed");
         }
      } catch (IOException var2) {
         this.log.debug("I/O error closing connection", var2);
      }

   }

   protected SessionInputBuffer createSessionInputBuffer(Socket var1, int var2, HttpParams var3) throws IOException {
      Object var4 = super.createSessionInputBuffer(var1, var2 > 0 ? var2 : 8192, var3);
      if (this.wireLog.isDebugEnabled()) {
         var4 = new LoggingSessionInputBuffer((SessionInputBuffer)var4, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(var3));
      }

      return (SessionInputBuffer)var4;
   }

   protected SessionOutputBuffer createSessionOutputBuffer(Socket var1, int var2, HttpParams var3) throws IOException {
      Object var4 = super.createSessionOutputBuffer(var1, var2 > 0 ? var2 : 8192, var3);
      if (this.wireLog.isDebugEnabled()) {
         var4 = new LoggingSessionOutputBuffer((SessionOutputBuffer)var4, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(var3));
      }

      return (SessionOutputBuffer)var4;
   }

   protected HttpMessageParser createResponseParser(SessionInputBuffer var1, HttpResponseFactory var2, HttpParams var3) {
      return new DefaultHttpResponseParser(var1, (LineParser)null, var2, var3);
   }

   public void bind(Socket var1) throws IOException {
      this.bind(var1, new BasicHttpParams());
   }

   public void update(Socket var1, HttpHost var2, boolean var3, HttpParams var4) throws IOException {
      this.assertOpen();
      Args.notNull(var2, "Target host");
      Args.notNull(var4, "Parameters");
      if (var1 != null) {
         this.socket = var1;
         this.bind(var1, var4);
      }

      this.targetHost = var2;
      this.connSecure = var3;
   }

   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
      HttpResponse var1 = super.receiveResponseHeader();
      if (this.log.isDebugEnabled()) {
         this.log.debug("Receiving response: " + var1.getStatusLine());
      }

      if (this.headerLog.isDebugEnabled()) {
         this.headerLog.debug("<< " + var1.getStatusLine().toString());
         Header[] var2 = var1.getAllHeaders();
         Header[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Header var6 = var3[var5];
            this.headerLog.debug("<< " + var6.toString());
         }
      }

      return var1;
   }

   public void sendRequestHeader(HttpRequest var1) throws HttpException, IOException {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Sending request: " + var1.getRequestLine());
      }

      super.sendRequestHeader(var1);
      if (this.headerLog.isDebugEnabled()) {
         this.headerLog.debug(">> " + var1.getRequestLine().toString());
         Header[] var2 = var1.getAllHeaders();
         Header[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Header var6 = var3[var5];
            this.headerLog.debug(">> " + var6.toString());
         }
      }

   }

   public Object getAttribute(String var1) {
      return this.attributes.get(var1);
   }

   public Object removeAttribute(String var1) {
      return this.attributes.remove(var1);
   }

   public void setAttribute(String var1, Object var2) {
      this.attributes.put(var1, var2);
   }
}
