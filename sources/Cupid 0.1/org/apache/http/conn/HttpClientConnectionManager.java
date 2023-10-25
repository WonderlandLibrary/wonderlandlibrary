package org.apache.http.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpContext;

public interface HttpClientConnectionManager {
  ConnectionRequest requestConnection(HttpRoute paramHttpRoute, Object paramObject);
  
  void releaseConnection(HttpClientConnection paramHttpClientConnection, Object paramObject, long paramLong, TimeUnit paramTimeUnit);
  
  void connect(HttpClientConnection paramHttpClientConnection, HttpRoute paramHttpRoute, int paramInt, HttpContext paramHttpContext) throws IOException;
  
  void upgrade(HttpClientConnection paramHttpClientConnection, HttpRoute paramHttpRoute, HttpContext paramHttpContext) throws IOException;
  
  void routeComplete(HttpClientConnection paramHttpClientConnection, HttpRoute paramHttpRoute, HttpContext paramHttpContext) throws IOException;
  
  void closeIdleConnections(long paramLong, TimeUnit paramTimeUnit);
  
  void closeExpiredConnections();
  
  void shutdown();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\HttpClientConnectionManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */