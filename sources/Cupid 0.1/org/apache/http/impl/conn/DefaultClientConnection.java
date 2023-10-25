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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
public class DefaultClientConnection extends SocketHttpClientConnection implements OperatedClientConnection, ManagedHttpClientConnection, HttpContext {
  private final Log log = LogFactory.getLog(getClass());
  
  private final Log headerLog = LogFactory.getLog("org.apache.http.headers");
  
  private final Log wireLog = LogFactory.getLog("org.apache.http.wire");
  
  private volatile Socket socket;
  
  private HttpHost targetHost;
  
  private boolean connSecure;
  
  private volatile boolean shutdown;
  
  private final Map<String, Object> attributes;
  
  public DefaultClientConnection() {
    this.attributes = new HashMap<String, Object>();
  }
  
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
    if (this.socket instanceof SSLSocket)
      return ((SSLSocket)this.socket).getSession(); 
    return null;
  }
  
  public void opening(Socket sock, HttpHost target) throws IOException {
    assertNotOpen();
    this.socket = sock;
    this.targetHost = target;
    if (this.shutdown) {
      sock.close();
      throw new InterruptedIOException("Connection already shutdown");
    } 
  }
  
  public void openCompleted(boolean secure, HttpParams params) throws IOException {
    Args.notNull(params, "Parameters");
    assertNotOpen();
    this.connSecure = secure;
    bind(this.socket, params);
  }
  
  public void shutdown() throws IOException {
    this.shutdown = true;
    try {
      super.shutdown();
      if (this.log.isDebugEnabled())
        this.log.debug("Connection " + this + " shut down"); 
      Socket sock = this.socket;
      if (sock != null)
        sock.close(); 
    } catch (IOException ex) {
      this.log.debug("I/O error shutting down connection", ex);
    } 
  }
  
  public void close() throws IOException {
    try {
      super.close();
      if (this.log.isDebugEnabled())
        this.log.debug("Connection " + this + " closed"); 
    } catch (IOException ex) {
      this.log.debug("I/O error closing connection", ex);
    } 
  }
  
  protected SessionInputBuffer createSessionInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
    SessionInputBuffer inbuffer = super.createSessionInputBuffer(socket, (buffersize > 0) ? buffersize : 8192, params);
    if (this.wireLog.isDebugEnabled())
      inbuffer = new LoggingSessionInputBuffer(inbuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params)); 
    return inbuffer;
  }
  
  protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
    SessionOutputBuffer outbuffer = super.createSessionOutputBuffer(socket, (buffersize > 0) ? buffersize : 8192, params);
    if (this.wireLog.isDebugEnabled())
      outbuffer = new LoggingSessionOutputBuffer(outbuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params)); 
    return outbuffer;
  }
  
  protected HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
    return (HttpMessageParser<HttpResponse>)new DefaultHttpResponseParser(buffer, null, responseFactory, params);
  }
  
  public void bind(Socket socket) throws IOException {
    bind(socket, (HttpParams)new BasicHttpParams());
  }
  
  public void update(Socket sock, HttpHost target, boolean secure, HttpParams params) throws IOException {
    assertOpen();
    Args.notNull(target, "Target host");
    Args.notNull(params, "Parameters");
    if (sock != null) {
      this.socket = sock;
      bind(sock, params);
    } 
    this.targetHost = target;
    this.connSecure = secure;
  }
  
  public HttpResponse receiveResponseHeader() throws HttpException, IOException {
    HttpResponse response = super.receiveResponseHeader();
    if (this.log.isDebugEnabled())
      this.log.debug("Receiving response: " + response.getStatusLine()); 
    if (this.headerLog.isDebugEnabled()) {
      this.headerLog.debug("<< " + response.getStatusLine().toString());
      Header[] headers = response.getAllHeaders();
      for (Header header : headers)
        this.headerLog.debug("<< " + header.toString()); 
    } 
    return response;
  }
  
  public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
    if (this.log.isDebugEnabled())
      this.log.debug("Sending request: " + request.getRequestLine()); 
    super.sendRequestHeader(request);
    if (this.headerLog.isDebugEnabled()) {
      this.headerLog.debug(">> " + request.getRequestLine().toString());
      Header[] headers = request.getAllHeaders();
      for (Header header : headers)
        this.headerLog.debug(">> " + header.toString()); 
    } 
  }
  
  public Object getAttribute(String id) {
    return this.attributes.get(id);
  }
  
  public Object removeAttribute(String id) {
    return this.attributes.remove(id);
  }
  
  public void setAttribute(String id, Object obj) {
    this.attributes.put(id, obj);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\DefaultClientConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */