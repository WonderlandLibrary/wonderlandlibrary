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

@Deprecated
@NotThreadSafe
public abstract class AbstractClientConnAdapter implements ManagedClientConnection, HttpContext {
  private final ClientConnectionManager connManager;
  
  private volatile OperatedClientConnection wrappedConnection;
  
  private volatile boolean markedReusable;
  
  private volatile boolean released;
  
  private volatile long duration;
  
  protected AbstractClientConnAdapter(ClientConnectionManager mgr, OperatedClientConnection conn) {
    this.connManager = mgr;
    this.wrappedConnection = conn;
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
  
  @Deprecated
  protected final void assertNotAborted() throws InterruptedIOException {
    if (isReleased())
      throw new InterruptedIOException("Connection has been shut down"); 
  }
  
  protected boolean isReleased() {
    return this.released;
  }
  
  protected final void assertValid(OperatedClientConnection wrappedConn) throws ConnectionShutdownException {
    if (isReleased() || wrappedConn == null)
      throw new ConnectionShutdownException(); 
  }
  
  public boolean isOpen() {
    OperatedClientConnection conn = getWrappedConnection();
    if (conn == null)
      return false; 
    return conn.isOpen();
  }
  
  public boolean isStale() {
    if (isReleased())
      return true; 
    OperatedClientConnection conn = getWrappedConnection();
    if (conn == null)
      return true; 
    return conn.isStale();
  }
  
  public void setSocketTimeout(int timeout) {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    conn.setSocketTimeout(timeout);
  }
  
  public int getSocketTimeout() {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    return conn.getSocketTimeout();
  }
  
  public HttpConnectionMetrics getMetrics() {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    return conn.getMetrics();
  }
  
  public void flush() throws IOException {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    conn.flush();
  }
  
  public boolean isResponseAvailable(int timeout) throws IOException {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    return conn.isResponseAvailable(timeout);
  }
  
  public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    unmarkReusable();
    conn.receiveResponseEntity(response);
  }
  
  public HttpResponse receiveResponseHeader() throws HttpException, IOException {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    unmarkReusable();
    return conn.receiveResponseHeader();
  }
  
  public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    unmarkReusable();
    conn.sendRequestEntity(request);
  }
  
  public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    unmarkReusable();
    conn.sendRequestHeader(request);
  }
  
  public InetAddress getLocalAddress() {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    return conn.getLocalAddress();
  }
  
  public int getLocalPort() {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    return conn.getLocalPort();
  }
  
  public InetAddress getRemoteAddress() {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    return conn.getRemoteAddress();
  }
  
  public int getRemotePort() {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    return conn.getRemotePort();
  }
  
  public boolean isSecure() {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    return conn.isSecure();
  }
  
  public void bind(Socket socket) throws IOException {
    throw new UnsupportedOperationException();
  }
  
  public Socket getSocket() {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    if (!isOpen())
      return null; 
    return conn.getSocket();
  }
  
  public SSLSession getSSLSession() {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    if (!isOpen())
      return null; 
    SSLSession result = null;
    Socket sock = conn.getSocket();
    if (sock instanceof SSLSocket)
      result = ((SSLSocket)sock).getSession(); 
    return result;
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
  
  public void setIdleDuration(long duration, TimeUnit unit) {
    if (duration > 0L) {
      this.duration = unit.toMillis(duration);
    } else {
      this.duration = -1L;
    } 
  }
  
  public synchronized void releaseConnection() {
    if (this.released)
      return; 
    this.released = true;
    this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
  }
  
  public synchronized void abortConnection() {
    if (this.released)
      return; 
    this.released = true;
    unmarkReusable();
    try {
      shutdown();
    } catch (IOException ignore) {}
    this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
  }
  
  public Object getAttribute(String id) {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    if (conn instanceof HttpContext)
      return ((HttpContext)conn).getAttribute(id); 
    return null;
  }
  
  public Object removeAttribute(String id) {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    if (conn instanceof HttpContext)
      return ((HttpContext)conn).removeAttribute(id); 
    return null;
  }
  
  public void setAttribute(String id, Object obj) {
    OperatedClientConnection conn = getWrappedConnection();
    assertValid(conn);
    if (conn instanceof HttpContext)
      ((HttpContext)conn).setAttribute(id, obj); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\AbstractClientConnAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */