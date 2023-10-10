package org.apache.http.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.apache.http.Header;
import org.apache.http.HttpConnection;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpMessage;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ChunkedOutputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.ContentLengthOutputStream;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.IdentityInputStream;
import org.apache.http.impl.io.IdentityOutputStream;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.apache.http.impl.io.SessionOutputBufferImpl;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.NetUtils;

@NotThreadSafe
public class BHttpConnectionBase implements HttpConnection, HttpInetConnection {
   private final SessionInputBufferImpl inbuffer;
   private final SessionOutputBufferImpl outbuffer;
   private final HttpConnectionMetricsImpl connMetrics;
   private final ContentLengthStrategy incomingContentStrategy;
   private final ContentLengthStrategy outgoingContentStrategy;
   private volatile boolean open;
   private volatile Socket socket;

   protected BHttpConnectionBase(int var1, int var2, CharsetDecoder var3, CharsetEncoder var4, MessageConstraints var5, ContentLengthStrategy var6, ContentLengthStrategy var7) {
      Args.positive(var1, "Buffer size");
      HttpTransportMetricsImpl var8 = new HttpTransportMetricsImpl();
      HttpTransportMetricsImpl var9 = new HttpTransportMetricsImpl();
      this.inbuffer = new SessionInputBufferImpl(var8, var1, -1, var5 != null ? var5 : MessageConstraints.DEFAULT, var3);
      this.outbuffer = new SessionOutputBufferImpl(var9, var1, var2, var4);
      this.connMetrics = new HttpConnectionMetricsImpl(var8, var9);
      this.incomingContentStrategy = (ContentLengthStrategy)(var6 != null ? var6 : LaxContentLengthStrategy.INSTANCE);
      this.outgoingContentStrategy = (ContentLengthStrategy)(var7 != null ? var7 : StrictContentLengthStrategy.INSTANCE);
   }

   protected void ensureOpen() throws IOException {
      Asserts.check(this.open, "Connection is not open");
      if (!this.inbuffer.isBound()) {
         this.inbuffer.bind(this.getSocketInputStream(this.socket));
      }

      if (!this.outbuffer.isBound()) {
         this.outbuffer.bind(this.getSocketOutputStream(this.socket));
      }

   }

   protected InputStream getSocketInputStream(Socket var1) throws IOException {
      return var1.getInputStream();
   }

   protected OutputStream getSocketOutputStream(Socket var1) throws IOException {
      return var1.getOutputStream();
   }

   protected void bind(Socket var1) throws IOException {
      Args.notNull(var1, "Socket");
      this.socket = var1;
      this.open = true;
      this.inbuffer.bind((InputStream)null);
      this.outbuffer.bind((OutputStream)null);
   }

   protected SessionInputBuffer getSessionInputBuffer() {
      return this.inbuffer;
   }

   protected SessionOutputBuffer getSessionOutputBuffer() {
      return this.outbuffer;
   }

   protected void doFlush() throws IOException {
      this.outbuffer.flush();
   }

   public boolean isOpen() {
      return this.open;
   }

   protected Socket getSocket() {
      return this.socket;
   }

   protected OutputStream createOutputStream(long var1, SessionOutputBuffer var3) {
      if (var1 == -2L) {
         return new ChunkedOutputStream(2048, var3);
      } else {
         return (OutputStream)(var1 == -1L ? new IdentityOutputStream(var3) : new ContentLengthOutputStream(var3, var1));
      }
   }

   protected OutputStream prepareOutput(HttpMessage var1) throws HttpException {
      long var2 = this.outgoingContentStrategy.determineLength(var1);
      return this.createOutputStream(var2, this.outbuffer);
   }

   protected InputStream createInputStream(long var1, SessionInputBuffer var3) {
      if (var1 == -2L) {
         return new ChunkedInputStream(var3);
      } else {
         return (InputStream)(var1 == -1L ? new IdentityInputStream(var3) : new ContentLengthInputStream(var3, var1));
      }
   }

   protected HttpEntity prepareInput(HttpMessage var1) throws HttpException {
      BasicHttpEntity var2 = new BasicHttpEntity();
      long var3 = this.incomingContentStrategy.determineLength(var1);
      InputStream var5 = this.createInputStream(var3, this.inbuffer);
      if (var3 == -2L) {
         var2.setChunked(true);
         var2.setContentLength(-1L);
         var2.setContent(var5);
      } else if (var3 == -1L) {
         var2.setChunked(false);
         var2.setContentLength(-1L);
         var2.setContent(var5);
      } else {
         var2.setChunked(false);
         var2.setContentLength(var3);
         var2.setContent(var5);
      }

      Header var6 = var1.getFirstHeader("Content-Type");
      if (var6 != null) {
         var2.setContentType(var6);
      }

      Header var7 = var1.getFirstHeader("Content-Encoding");
      if (var7 != null) {
         var2.setContentEncoding(var7);
      }

      return var2;
   }

   public InetAddress getLocalAddress() {
      return this.socket != null ? this.socket.getLocalAddress() : null;
   }

   public int getLocalPort() {
      return this.socket != null ? this.socket.getLocalPort() : -1;
   }

   public InetAddress getRemoteAddress() {
      return this.socket != null ? this.socket.getInetAddress() : null;
   }

   public int getRemotePort() {
      return this.socket != null ? this.socket.getPort() : -1;
   }

   public void setSocketTimeout(int var1) {
      if (this.socket != null) {
         try {
            this.socket.setSoTimeout(var1);
         } catch (SocketException var3) {
         }
      }

   }

   public int getSocketTimeout() {
      if (this.socket != null) {
         try {
            return this.socket.getSoTimeout();
         } catch (SocketException var2) {
            return -1;
         }
      } else {
         return -1;
      }
   }

   public void shutdown() throws IOException {
      this.open = false;
      Socket var1 = this.socket;
      if (var1 != null) {
         var1.close();
      }

   }

   public void close() throws IOException {
      if (this.open) {
         this.open = false;
         Socket var1 = this.socket;
         this.inbuffer.clear();
         this.outbuffer.flush();

         try {
            try {
               var1.shutdownOutput();
            } catch (IOException var5) {
            }

            try {
               var1.shutdownInput();
            } catch (IOException var4) {
            }
         } catch (UnsupportedOperationException var6) {
         }

         var1.close();
      }
   }

   private int fillInputBuffer(int var1) throws IOException {
      int var2 = this.socket.getSoTimeout();
      this.socket.setSoTimeout(var1);
      int var3 = this.inbuffer.fillBuffer();
      this.socket.setSoTimeout(var2);
      return var3;
   }

   protected boolean awaitInput(int var1) throws IOException {
      if (this.inbuffer.hasBufferedData()) {
         return true;
      } else {
         this.fillInputBuffer(var1);
         return this.inbuffer.hasBufferedData();
      }
   }

   public boolean isStale() {
      if (!this.isOpen()) {
         return true;
      } else {
         try {
            int var1 = this.fillInputBuffer(1);
            return var1 < 0;
         } catch (SocketTimeoutException var2) {
            return false;
         } catch (IOException var3) {
            return true;
         }
      }
   }

   protected void incrementRequestCount() {
      this.connMetrics.incrementRequestCount();
   }

   protected void incrementResponseCount() {
      this.connMetrics.incrementResponseCount();
   }

   public HttpConnectionMetrics getMetrics() {
      return this.connMetrics;
   }

   public String toString() {
      if (this.socket != null) {
         StringBuilder var1 = new StringBuilder();
         SocketAddress var2 = this.socket.getRemoteSocketAddress();
         SocketAddress var3 = this.socket.getLocalSocketAddress();
         if (var2 != null && var3 != null) {
            NetUtils.formatAddress(var1, var3);
            var1.append("<->");
            NetUtils.formatAddress(var1, var2);
         }

         return var1.toString();
      } else {
         return "[Not bound]";
      }
   }
}
