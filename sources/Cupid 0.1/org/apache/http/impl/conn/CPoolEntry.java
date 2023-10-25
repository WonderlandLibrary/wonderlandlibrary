package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.HttpClientConnection;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.PoolEntry;

@ThreadSafe
class CPoolEntry extends PoolEntry<HttpRoute, ManagedHttpClientConnection> {
  private final Log log;
  
  private volatile boolean routeComplete;
  
  public CPoolEntry(Log log, String id, HttpRoute route, ManagedHttpClientConnection conn, long timeToLive, TimeUnit tunit) {
    super(id, route, conn, timeToLive, tunit);
    this.log = log;
  }
  
  public void markRouteComplete() {
    this.routeComplete = true;
  }
  
  public boolean isRouteComplete() {
    return this.routeComplete;
  }
  
  public void closeConnection() throws IOException {
    HttpClientConnection conn = (HttpClientConnection)getConnection();
    conn.close();
  }
  
  public void shutdownConnection() throws IOException {
    HttpClientConnection conn = (HttpClientConnection)getConnection();
    conn.shutdown();
  }
  
  public boolean isExpired(long now) {
    boolean expired = super.isExpired(now);
    if (expired && this.log.isDebugEnabled())
      this.log.debug("Connection " + this + " expired @ " + new Date(getExpiry())); 
    return expired;
  }
  
  public boolean isClosed() {
    HttpClientConnection conn = (HttpClientConnection)getConnection();
    return !conn.isOpen();
  }
  
  public void close() {
    try {
      closeConnection();
    } catch (IOException ex) {
      this.log.debug("I/O error closing connection", ex);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\CPoolEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */