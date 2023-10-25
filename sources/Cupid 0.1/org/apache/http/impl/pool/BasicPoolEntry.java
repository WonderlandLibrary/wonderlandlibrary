package org.apache.http.impl.pool;

import java.io.IOException;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.pool.PoolEntry;

@ThreadSafe
public class BasicPoolEntry extends PoolEntry<HttpHost, HttpClientConnection> {
  public BasicPoolEntry(String id, HttpHost route, HttpClientConnection conn) {
    super(id, route, conn);
  }
  
  public void close() {
    try {
      ((HttpClientConnection)getConnection()).close();
    } catch (IOException ignore) {}
  }
  
  public boolean isClosed() {
    return !((HttpClientConnection)getConnection()).isOpen();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\pool\BasicPoolEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */