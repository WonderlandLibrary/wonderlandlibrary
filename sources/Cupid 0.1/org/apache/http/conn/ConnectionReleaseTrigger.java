package org.apache.http.conn;

import java.io.IOException;

public interface ConnectionReleaseTrigger {
  void releaseConnection() throws IOException;
  
  void abortConnection() throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\ConnectionReleaseTrigger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */