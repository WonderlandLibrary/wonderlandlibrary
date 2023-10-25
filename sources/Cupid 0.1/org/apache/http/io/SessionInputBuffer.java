package org.apache.http.io;

import java.io.IOException;
import org.apache.http.util.CharArrayBuffer;

public interface SessionInputBuffer {
  int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  int read(byte[] paramArrayOfbyte) throws IOException;
  
  int read() throws IOException;
  
  int readLine(CharArrayBuffer paramCharArrayBuffer) throws IOException;
  
  String readLine() throws IOException;
  
  @Deprecated
  boolean isDataAvailable(int paramInt) throws IOException;
  
  HttpTransportMetrics getMetrics();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\io\SessionInputBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */