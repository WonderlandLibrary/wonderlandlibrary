package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Deprecated
@NotThreadSafe
public class BasicManagedEntity extends HttpEntityWrapper implements ConnectionReleaseTrigger, EofSensorWatcher {
  protected ManagedClientConnection managedConn;
  
  protected final boolean attemptReuse;
  
  public BasicManagedEntity(HttpEntity entity, ManagedClientConnection conn, boolean reuse) {
    super(entity);
    Args.notNull(conn, "Connection");
    this.managedConn = conn;
    this.attemptReuse = reuse;
  }
  
  public boolean isRepeatable() {
    return false;
  }
  
  public InputStream getContent() throws IOException {
    return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
  }
  
  private void ensureConsumed() throws IOException {
    if (this.managedConn == null)
      return; 
    try {
      if (this.attemptReuse) {
        EntityUtils.consume(this.wrappedEntity);
        this.managedConn.markReusable();
      } else {
        this.managedConn.unmarkReusable();
      } 
    } finally {
      releaseManagedConnection();
    } 
  }
  
  @Deprecated
  public void consumeContent() throws IOException {
    ensureConsumed();
  }
  
  public void writeTo(OutputStream outstream) throws IOException {
    super.writeTo(outstream);
    ensureConsumed();
  }
  
  public void releaseConnection() throws IOException {
    ensureConsumed();
  }
  
  public void abortConnection() throws IOException {
    if (this.managedConn != null)
      try {
        this.managedConn.abortConnection();
      } finally {
        this.managedConn = null;
      }  
  }
  
  public boolean eofDetected(InputStream wrapped) throws IOException {
    try {
      if (this.managedConn != null)
        if (this.attemptReuse) {
          wrapped.close();
          this.managedConn.markReusable();
        } else {
          this.managedConn.unmarkReusable();
        }  
    } finally {
      releaseManagedConnection();
    } 
    return false;
  }
  
  public boolean streamClosed(InputStream wrapped) throws IOException {
    try {
      if (this.managedConn != null)
        if (this.attemptReuse) {
          boolean valid = this.managedConn.isOpen();
          try {
            wrapped.close();
            this.managedConn.markReusable();
          } catch (SocketException ex) {
            if (valid)
              throw ex; 
          } 
        } else {
          this.managedConn.unmarkReusable();
        }  
    } finally {
      releaseManagedConnection();
    } 
    return false;
  }
  
  public boolean streamAbort(InputStream wrapped) throws IOException {
    if (this.managedConn != null)
      this.managedConn.abortConnection(); 
    return false;
  }
  
  protected void releaseManagedConnection() throws IOException {
    if (this.managedConn != null)
      try {
        this.managedConn.releaseConnection();
      } finally {
        this.managedConn = null;
      }  
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\BasicManagedEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */