package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpConnection;

@Deprecated
public class IdleConnectionHandler {
  private final Log log = LogFactory.getLog(getClass());
  
  private final Map<HttpConnection, TimeValues> connectionToTimes;
  
  public IdleConnectionHandler() {
    this.connectionToTimes = new HashMap<HttpConnection, TimeValues>();
  }
  
  public void add(HttpConnection connection, long validDuration, TimeUnit unit) {
    long timeAdded = System.currentTimeMillis();
    if (this.log.isDebugEnabled())
      this.log.debug("Adding connection at: " + timeAdded); 
    this.connectionToTimes.put(connection, new TimeValues(timeAdded, validDuration, unit));
  }
  
  public boolean remove(HttpConnection connection) {
    TimeValues times = this.connectionToTimes.remove(connection);
    if (times == null) {
      this.log.warn("Removing a connection that never existed!");
      return true;
    } 
    return (System.currentTimeMillis() <= times.timeExpires);
  }
  
  public void removeAll() {
    this.connectionToTimes.clear();
  }
  
  public void closeIdleConnections(long idleTime) {
    long idleTimeout = System.currentTimeMillis() - idleTime;
    if (this.log.isDebugEnabled())
      this.log.debug("Checking for connections, idle timeout: " + idleTimeout); 
    for (Map.Entry<HttpConnection, TimeValues> entry : this.connectionToTimes.entrySet()) {
      HttpConnection conn = entry.getKey();
      TimeValues times = entry.getValue();
      long connectionTime = times.timeAdded;
      if (connectionTime <= idleTimeout) {
        if (this.log.isDebugEnabled())
          this.log.debug("Closing idle connection, connection time: " + connectionTime); 
        try {
          conn.close();
        } catch (IOException ex) {
          this.log.debug("I/O error closing connection", ex);
        } 
      } 
    } 
  }
  
  public void closeExpiredConnections() {
    long now = System.currentTimeMillis();
    if (this.log.isDebugEnabled())
      this.log.debug("Checking for expired connections, now: " + now); 
    for (Map.Entry<HttpConnection, TimeValues> entry : this.connectionToTimes.entrySet()) {
      HttpConnection conn = entry.getKey();
      TimeValues times = entry.getValue();
      if (times.timeExpires <= now) {
        if (this.log.isDebugEnabled())
          this.log.debug("Closing connection, expired @: " + times.timeExpires); 
        try {
          conn.close();
        } catch (IOException ex) {
          this.log.debug("I/O error closing connection", ex);
        } 
      } 
    } 
  }
  
  private static class TimeValues {
    private final long timeAdded;
    
    private final long timeExpires;
    
    TimeValues(long now, long validDuration, TimeUnit validUnit) {
      this.timeAdded = now;
      if (validDuration > 0L) {
        this.timeExpires = now + validUnit.toMillis(validDuration);
      } else {
        this.timeExpires = Long.MAX_VALUE;
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\IdleConnectionHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */