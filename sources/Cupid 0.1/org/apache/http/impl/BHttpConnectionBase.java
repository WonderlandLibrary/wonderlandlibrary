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
import org.apache.http.io.HttpTransportMetrics;
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
  
  protected BHttpConnectionBase(int buffersize, int fragmentSizeHint, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
    Args.positive(buffersize, "Buffer size");
    HttpTransportMetricsImpl inTransportMetrics = new HttpTransportMetricsImpl();
    HttpTransportMetricsImpl outTransportMetrics = new HttpTransportMetricsImpl();
    this.inbuffer = new SessionInputBufferImpl(inTransportMetrics, buffersize, -1, (constraints != null) ? constraints : MessageConstraints.DEFAULT, chardecoder);
    this.outbuffer = new SessionOutputBufferImpl(outTransportMetrics, buffersize, fragmentSizeHint, charencoder);
    this.connMetrics = new HttpConnectionMetricsImpl((HttpTransportMetrics)inTransportMetrics, (HttpTransportMetrics)outTransportMetrics);
    this.incomingContentStrategy = (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)LaxContentLengthStrategy.INSTANCE;
    this.outgoingContentStrategy = (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)StrictContentLengthStrategy.INSTANCE;
  }
  
  protected void ensureOpen() throws IOException {
    Asserts.check(this.open, "Connection is not open");
    if (!this.inbuffer.isBound())
      this.inbuffer.bind(getSocketInputStream(this.socket)); 
    if (!this.outbuffer.isBound())
      this.outbuffer.bind(getSocketOutputStream(this.socket)); 
  }
  
  protected InputStream getSocketInputStream(Socket socket) throws IOException {
    return socket.getInputStream();
  }
  
  protected OutputStream getSocketOutputStream(Socket socket) throws IOException {
    return socket.getOutputStream();
  }
  
  protected void bind(Socket socket) throws IOException {
    Args.notNull(socket, "Socket");
    this.socket = socket;
    this.open = true;
    this.inbuffer.bind(null);
    this.outbuffer.bind(null);
  }
  
  protected SessionInputBuffer getSessionInputBuffer() {
    return (SessionInputBuffer)this.inbuffer;
  }
  
  protected SessionOutputBuffer getSessionOutputBuffer() {
    return (SessionOutputBuffer)this.outbuffer;
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
  
  protected OutputStream createOutputStream(long len, SessionOutputBuffer outbuffer) {
    if (len == -2L)
      return (OutputStream)new ChunkedOutputStream(2048, outbuffer); 
    if (len == -1L)
      return (OutputStream)new IdentityOutputStream(outbuffer); 
    return (OutputStream)new ContentLengthOutputStream(outbuffer, len);
  }
  
  protected OutputStream prepareOutput(HttpMessage message) throws HttpException {
    long len = this.outgoingContentStrategy.determineLength(message);
    return createOutputStream(len, (SessionOutputBuffer)this.outbuffer);
  }
  
  protected InputStream createInputStream(long len, SessionInputBuffer inbuffer) {
    if (len == -2L)
      return (InputStream)new ChunkedInputStream(inbuffer); 
    if (len == -1L)
      return (InputStream)new IdentityInputStream(inbuffer); 
    return (InputStream)new ContentLengthInputStream(inbuffer, len);
  }
  
  protected HttpEntity prepareInput(HttpMessage message) throws HttpException {
    BasicHttpEntity entity = new BasicHttpEntity();
    long len = this.incomingContentStrategy.determineLength(message);
    InputStream instream = createInputStream(len, (SessionInputBuffer)this.inbuffer);
    if (len == -2L) {
      entity.setChunked(true);
      entity.setContentLength(-1L);
      entity.setContent(instream);
    } else if (len == -1L) {
      entity.setChunked(false);
      entity.setContentLength(-1L);
      entity.setContent(instream);
    } else {
      entity.setChunked(false);
      entity.setContentLength(len);
      entity.setContent(instream);
    } 
    Header contentTypeHeader = message.getFirstHeader("Content-Type");
    if (contentTypeHeader != null)
      entity.setContentType(contentTypeHeader); 
    Header contentEncodingHeader = message.getFirstHeader("Content-Encoding");
    if (contentEncodingHeader != null)
      entity.setContentEncoding(contentEncodingHeader); 
    return (HttpEntity)entity;
  }
  
  public InetAddress getLocalAddress() {
    if (this.socket != null)
      return this.socket.getLocalAddress(); 
    return null;
  }
  
  public int getLocalPort() {
    if (this.socket != null)
      return this.socket.getLocalPort(); 
    return -1;
  }
  
  public InetAddress getRemoteAddress() {
    if (this.socket != null)
      return this.socket.getInetAddress(); 
    return null;
  }
  
  public int getRemotePort() {
    if (this.socket != null)
      return this.socket.getPort(); 
    return -1;
  }
  
  public void setSocketTimeout(int timeout) {
    if (this.socket != null)
      try {
        this.socket.setSoTimeout(timeout);
      } catch (SocketException ignore) {} 
  }
  
  public int getSocketTimeout() {
    if (this.socket != null)
      try {
        return this.socket.getSoTimeout();
      } catch (SocketException ignore) {
        return -1;
      }  
    return -1;
  }
  
  public void shutdown() throws IOException {
    this.open = false;
    Socket tmpsocket = this.socket;
    if (tmpsocket != null)
      tmpsocket.close(); 
  }
  
  public void close() throws IOException {
    if (!this.open)
      return; 
    this.open = false;
    Socket sock = this.socket;
    try {
      this.inbuffer.clear();
      this.outbuffer.flush();
      try {
        try {
          sock.shutdownOutput();
        } catch (IOException ignore) {}
        try {
          sock.shutdownInput();
        } catch (IOException ignore) {}
      } catch (UnsupportedOperationException ignore) {}
    } finally {
      sock.close();
    } 
  }
  
  private int fillInputBuffer(int timeout) throws IOException {
    int oldtimeout = this.socket.getSoTimeout();
    try {
      this.socket.setSoTimeout(timeout);
      return this.inbuffer.fillBuffer();
    } finally {
      this.socket.setSoTimeout(oldtimeout);
    } 
  }
  
  protected boolean awaitInput(int timeout) throws IOException {
    if (this.inbuffer.hasBufferedData())
      return true; 
    fillInputBuffer(timeout);
    return this.inbuffer.hasBufferedData();
  }
  
  public boolean isStale() {
    if (!isOpen())
      return true; 
    try {
      int bytesRead = fillInputBuffer(1);
      return (bytesRead < 0);
    } catch (SocketTimeoutException ex) {
      return false;
    } catch (IOException ex) {
      return true;
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
      StringBuilder buffer = new StringBuilder();
      SocketAddress remoteAddress = this.socket.getRemoteSocketAddress();
      SocketAddress localAddress = this.socket.getLocalSocketAddress();
      if (remoteAddress != null && localAddress != null) {
        NetUtils.formatAddress(buffer, localAddress);
        buffer.append("<->");
        NetUtils.formatAddress(buffer, remoteAddress);
      } 
      return buffer.toString();
    } 
    return "[Not bound]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\BHttpConnectionBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */