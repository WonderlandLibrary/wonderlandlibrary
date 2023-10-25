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

@Deprecated
@NotThreadSafe
class ManagedClientConnectionImpl implements ManagedClientConnection {
  private final ClientConnectionManager manager;
  
  private final ClientConnectionOperator operator;
  
  private volatile HttpPoolEntry poolEntry;
  
  private volatile boolean reusable;
  
  private volatile long duration;
  
  ManagedClientConnectionImpl(ClientConnectionManager manager, ClientConnectionOperator operator, HttpPoolEntry entry) {
    Args.notNull(manager, "Connection manager");
    Args.notNull(operator, "Connection operator");
    Args.notNull(entry, "HTTP pool entry");
    this.manager = manager;
    this.operator = operator;
    this.poolEntry = entry;
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
    HttpPoolEntry local = this.poolEntry;
    this.poolEntry = null;
    return local;
  }
  
  public ClientConnectionManager getManager() {
    return this.manager;
  }
  
  private OperatedClientConnection getConnection() {
    HttpPoolEntry local = this.poolEntry;
    if (local == null)
      return null; 
    return (OperatedClientConnection)local.getConnection();
  }
  
  private OperatedClientConnection ensureConnection() {
    HttpPoolEntry local = this.poolEntry;
    if (local == null)
      throw new ConnectionShutdownException(); 
    return (OperatedClientConnection)local.getConnection();
  }
  
  private HttpPoolEntry ensurePoolEntry() {
    HttpPoolEntry local = this.poolEntry;
    if (local == null)
      throw new ConnectionShutdownException(); 
    return local;
  }
  
  public void close() throws IOException {
    HttpPoolEntry local = this.poolEntry;
    if (local != null) {
      OperatedClientConnection conn = (OperatedClientConnection)local.getConnection();
      local.getTracker().reset();
      conn.close();
    } 
  }
  
  public void shutdown() throws IOException {
    HttpPoolEntry local = this.poolEntry;
    if (local != null) {
      OperatedClientConnection conn = (OperatedClientConnection)local.getConnection();
      local.getTracker().reset();
      conn.shutdown();
    } 
  }
  
  public boolean isOpen() {
    OperatedClientConnection conn = getConnection();
    if (conn != null)
      return conn.isOpen(); 
    return false;
  }
  
  public boolean isStale() {
    OperatedClientConnection conn = getConnection();
    if (conn != null)
      return conn.isStale(); 
    return true;
  }
  
  public void setSocketTimeout(int timeout) {
    OperatedClientConnection conn = ensureConnection();
    conn.setSocketTimeout(timeout);
  }
  
  public int getSocketTimeout() {
    OperatedClientConnection conn = ensureConnection();
    return conn.getSocketTimeout();
  }
  
  public HttpConnectionMetrics getMetrics() {
    OperatedClientConnection conn = ensureConnection();
    return conn.getMetrics();
  }
  
  public void flush() throws IOException {
    OperatedClientConnection conn = ensureConnection();
    conn.flush();
  }
  
  public boolean isResponseAvailable(int timeout) throws IOException {
    OperatedClientConnection conn = ensureConnection();
    return conn.isResponseAvailable(timeout);
  }
  
  public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
    OperatedClientConnection conn = ensureConnection();
    conn.receiveResponseEntity(response);
  }
  
  public HttpResponse receiveResponseHeader() throws HttpException, IOException {
    OperatedClientConnection conn = ensureConnection();
    return conn.receiveResponseHeader();
  }
  
  public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
    OperatedClientConnection conn = ensureConnection();
    conn.sendRequestEntity(request);
  }
  
  public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
    OperatedClientConnection conn = ensureConnection();
    conn.sendRequestHeader(request);
  }
  
  public InetAddress getLocalAddress() {
    OperatedClientConnection conn = ensureConnection();
    return conn.getLocalAddress();
  }
  
  public int getLocalPort() {
    OperatedClientConnection conn = ensureConnection();
    return conn.getLocalPort();
  }
  
  public InetAddress getRemoteAddress() {
    OperatedClientConnection conn = ensureConnection();
    return conn.getRemoteAddress();
  }
  
  public int getRemotePort() {
    OperatedClientConnection conn = ensureConnection();
    return conn.getRemotePort();
  }
  
  public boolean isSecure() {
    OperatedClientConnection conn = ensureConnection();
    return conn.isSecure();
  }
  
  public void bind(Socket socket) throws IOException {
    throw new UnsupportedOperationException();
  }
  
  public Socket getSocket() {
    OperatedClientConnection conn = ensureConnection();
    return conn.getSocket();
  }
  
  public SSLSession getSSLSession() {
    OperatedClientConnection conn = ensureConnection();
    SSLSession result = null;
    Socket sock = conn.getSocket();
    if (sock instanceof SSLSocket)
      result = ((SSLSocket)sock).getSession(); 
    return result;
  }
  
  public Object getAttribute(String id) {
    OperatedClientConnection conn = ensureConnection();
    if (conn instanceof HttpContext)
      return ((HttpContext)conn).getAttribute(id); 
    return null;
  }
  
  public Object removeAttribute(String id) {
    OperatedClientConnection conn = ensureConnection();
    if (conn instanceof HttpContext)
      return ((HttpContext)conn).removeAttribute(id); 
    return null;
  }
  
  public void setAttribute(String id, Object obj) {
    OperatedClientConnection conn = ensureConnection();
    if (conn instanceof HttpContext)
      ((HttpContext)conn).setAttribute(id, obj); 
  }
  
  public HttpRoute getRoute() {
    HttpPoolEntry local = ensurePoolEntry();
    return local.getEffectiveRoute();
  }
  
  public void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
    OperatedClientConnection conn;
    Args.notNull(route, "Route");
    Args.notNull(params, "HTTP parameters");
    synchronized (this) {
      if (this.poolEntry == null)
        throw new ConnectionShutdownException(); 
      RouteTracker tracker = this.poolEntry.getTracker();
      Asserts.notNull(tracker, "Route tracker");
      Asserts.check(!tracker.isConnected(), "Connection already open");
      conn = (OperatedClientConnection)this.poolEntry.getConnection();
    } 
    HttpHost proxy = route.getProxyHost();
    this.operator.openConnection(conn, (proxy != null) ? proxy : route.getTargetHost(), route.getLocalAddress(), context, params);
    synchronized (this) {
      if (this.poolEntry == null)
        throw new InterruptedIOException(); 
      RouteTracker tracker = this.poolEntry.getTracker();
      if (proxy == null) {
        tracker.connectTarget(conn.isSecure());
      } else {
        tracker.connectProxy(proxy, conn.isSecure());
      } 
    } 
  }
  
  public void tunnelTarget(boolean secure, HttpParams params) throws IOException {
    HttpHost target;
    OperatedClientConnection conn;
    Args.notNull(params, "HTTP parameters");
    synchronized (this) {
      if (this.poolEntry == null)
        throw new ConnectionShutdownException(); 
      RouteTracker tracker = this.poolEntry.getTracker();
      Asserts.notNull(tracker, "Route tracker");
      Asserts.check(tracker.isConnected(), "Connection not open");
      Asserts.check(!tracker.isTunnelled(), "Connection is already tunnelled");
      target = tracker.getTargetHost();
      conn = (OperatedClientConnection)this.poolEntry.getConnection();
    } 
    conn.update(null, target, secure, params);
    synchronized (this) {
      if (this.poolEntry == null)
        throw new InterruptedIOException(); 
      RouteTracker tracker = this.poolEntry.getTracker();
      tracker.tunnelTarget(secure);
    } 
  }
  
  public void tunnelProxy(HttpHost next, boolean secure, HttpParams params) throws IOException {
    OperatedClientConnection conn;
    Args.notNull(next, "Next proxy");
    Args.notNull(params, "HTTP parameters");
    synchronized (this) {
      if (this.poolEntry == null)
        throw new ConnectionShutdownException(); 
      RouteTracker tracker = this.poolEntry.getTracker();
      Asserts.notNull(tracker, "Route tracker");
      Asserts.check(tracker.isConnected(), "Connection not open");
      conn = (OperatedClientConnection)this.poolEntry.getConnection();
    } 
    conn.update(null, next, secure, params);
    synchronized (this) {
      if (this.poolEntry == null)
        throw new InterruptedIOException(); 
      RouteTracker tracker = this.poolEntry.getTracker();
      tracker.tunnelProxy(next, secure);
    } 
  }
  
  public void layerProtocol(HttpContext context, HttpParams params) throws IOException {
    HttpHost target;
    OperatedClientConnection conn;
    Args.notNull(params, "HTTP parameters");
    synchronized (this) {
      if (this.poolEntry == null)
        throw new ConnectionShutdownException(); 
      RouteTracker tracker = this.poolEntry.getTracker();
      Asserts.notNull(tracker, "Route tracker");
      Asserts.check(tracker.isConnected(), "Connection not open");
      Asserts.check(tracker.isTunnelled(), "Protocol layering without a tunnel not supported");
      Asserts.check(!tracker.isLayered(), "Multiple protocol layering not supported");
      target = tracker.getTargetHost();
      conn = (OperatedClientConnection)this.poolEntry.getConnection();
    } 
    this.operator.updateSecureConnection(conn, target, context, params);
    synchronized (this) {
      if (this.poolEntry == null)
        throw new InterruptedIOException(); 
      RouteTracker tracker = this.poolEntry.getTracker();
      tracker.layerProtocol(conn.isSecure());
    } 
  }
  
  public Object getState() {
    HttpPoolEntry local = ensurePoolEntry();
    return local.getState();
  }
  
  public void setState(Object state) {
    HttpPoolEntry local = ensurePoolEntry();
    local.setState(state);
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
  
  public void setIdleDuration(long duration, TimeUnit unit) {
    if (duration > 0L) {
      this.duration = unit.toMillis(duration);
    } else {
      this.duration = -1L;
    } 
  }
  
  public void releaseConnection() {
    synchronized (this) {
      if (this.poolEntry == null)
        return; 
      this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
      this.poolEntry = null;
    } 
  }
  
  public void abortConnection() {
    synchronized (this) {
      if (this.poolEntry == null)
        return; 
      this.reusable = false;
      OperatedClientConnection conn = (OperatedClientConnection)this.poolEntry.getConnection();
      try {
        conn.shutdown();
      } catch (IOException ignore) {}
      this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
      this.poolEntry = null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\ManagedClientConnectionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */