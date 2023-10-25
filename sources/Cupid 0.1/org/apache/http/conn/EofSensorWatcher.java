package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;

public interface EofSensorWatcher {
  boolean eofDetected(InputStream paramInputStream) throws IOException;
  
  boolean streamClosed(InputStream paramInputStream) throws IOException;
  
  boolean streamAbort(InputStream paramInputStream) throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\EofSensorWatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */