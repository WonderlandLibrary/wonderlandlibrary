package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.conn.EofSensorWatcher;
import org.apache.http.entity.HttpEntityWrapper;

@NotThreadSafe
class ResponseEntityWrapper extends HttpEntityWrapper implements EofSensorWatcher {
  private final ConnectionHolder connReleaseTrigger;
  
  public ResponseEntityWrapper(HttpEntity entity, ConnectionHolder connReleaseTrigger) {
    super(entity);
    this.connReleaseTrigger = connReleaseTrigger;
  }
  
  private void cleanup() {
    if (this.connReleaseTrigger != null)
      this.connReleaseTrigger.abortConnection(); 
  }
  
  public void releaseConnection() throws IOException {
    if (this.connReleaseTrigger != null)
      try {
        if (this.connReleaseTrigger.isReusable())
          this.connReleaseTrigger.releaseConnection(); 
      } finally {
        cleanup();
      }  
  }
  
  public boolean isRepeatable() {
    return false;
  }
  
  public InputStream getContent() throws IOException {
    return (InputStream)new EofSensorInputStream(this.wrappedEntity.getContent(), this);
  }
  
  @Deprecated
  public void consumeContent() throws IOException {
    releaseConnection();
  }
  
  public void writeTo(OutputStream outstream) throws IOException {
    try {
      this.wrappedEntity.writeTo(outstream);
      releaseConnection();
    } finally {
      cleanup();
    } 
  }
  
  public boolean eofDetected(InputStream wrapped) throws IOException {
    try {
      wrapped.close();
      releaseConnection();
    } finally {
      cleanup();
    } 
    return false;
  }
  
  public boolean streamClosed(InputStream wrapped) throws IOException {
    try {
      boolean open = (this.connReleaseTrigger != null && !this.connReleaseTrigger.isReleased());
      try {
        wrapped.close();
        releaseConnection();
      } catch (SocketException ex) {
        if (open)
          throw ex; 
      } 
    } finally {
      cleanup();
    } 
    return false;
  }
  
  public boolean streamAbort(InputStream wrapped) throws IOException {
    cleanup();
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\execchain\ResponseEntityWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */