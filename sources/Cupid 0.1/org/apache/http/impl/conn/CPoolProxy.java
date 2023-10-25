package org.apache.http.impl.conn;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnection;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.protocol.HttpContext;

@NotThreadSafe
class CPoolProxy implements InvocationHandler {
  private static final Method CLOSE_METHOD;
  
  private static final Method SHUTDOWN_METHOD;
  
  private static final Method IS_OPEN_METHOD;
  
  private static final Method IS_STALE_METHOD;
  
  private volatile CPoolEntry poolEntry;
  
  static {
    try {
      CLOSE_METHOD = HttpConnection.class.getMethod("close", new Class[0]);
      SHUTDOWN_METHOD = HttpConnection.class.getMethod("shutdown", new Class[0]);
      IS_OPEN_METHOD = HttpConnection.class.getMethod("isOpen", new Class[0]);
      IS_STALE_METHOD = HttpConnection.class.getMethod("isStale", new Class[0]);
    } catch (NoSuchMethodException ex) {
      throw new Error(ex);
    } 
  }
  
  CPoolProxy(CPoolEntry entry) {
    this.poolEntry = entry;
  }
  
  CPoolEntry getPoolEntry() {
    return this.poolEntry;
  }
  
  CPoolEntry detach() {
    CPoolEntry local = this.poolEntry;
    this.poolEntry = null;
    return local;
  }
  
  HttpClientConnection getConnection() {
    CPoolEntry local = this.poolEntry;
    if (local == null)
      return null; 
    return (HttpClientConnection)local.getConnection();
  }
  
  public void close() throws IOException {
    CPoolEntry local = this.poolEntry;
    if (local != null)
      local.closeConnection(); 
  }
  
  public void shutdown() throws IOException {
    CPoolEntry local = this.poolEntry;
    if (local != null)
      local.shutdownConnection(); 
  }
  
  public boolean isOpen() {
    CPoolEntry local = this.poolEntry;
    if (local != null)
      return !local.isClosed(); 
    return false;
  }
  
  public boolean isStale() {
    HttpClientConnection conn = getConnection();
    if (conn != null)
      return conn.isStale(); 
    return true;
  }
  
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (method.equals(CLOSE_METHOD)) {
      close();
      return null;
    } 
    if (method.equals(SHUTDOWN_METHOD)) {
      shutdown();
      return null;
    } 
    if (method.equals(IS_OPEN_METHOD))
      return Boolean.valueOf(isOpen()); 
    if (method.equals(IS_STALE_METHOD))
      return Boolean.valueOf(isStale()); 
    HttpClientConnection conn = getConnection();
    if (conn == null)
      throw new ConnectionShutdownException(); 
    try {
      return method.invoke(conn, args);
    } catch (InvocationTargetException ex) {
      Throwable cause = ex.getCause();
      if (cause != null)
        throw cause; 
      throw ex;
    } 
  }
  
  public static HttpClientConnection newProxy(CPoolEntry poolEntry) {
    return (HttpClientConnection)Proxy.newProxyInstance(CPoolProxy.class.getClassLoader(), new Class[] { ManagedHttpClientConnection.class, HttpContext.class }, new CPoolProxy(poolEntry));
  }
  
  private static CPoolProxy getHandler(HttpClientConnection proxy) {
    InvocationHandler handler = Proxy.getInvocationHandler(proxy);
    if (!CPoolProxy.class.isInstance(handler))
      throw new IllegalStateException("Unexpected proxy handler class: " + handler); 
    return CPoolProxy.class.cast(handler);
  }
  
  public static CPoolEntry getPoolEntry(HttpClientConnection proxy) {
    CPoolEntry entry = getHandler(proxy).getPoolEntry();
    if (entry == null)
      throw new ConnectionShutdownException(); 
    return entry;
  }
  
  public static CPoolEntry detach(HttpClientConnection proxy) {
    return getHandler(proxy).detach();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\CPoolProxy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */