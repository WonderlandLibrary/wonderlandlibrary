package org.apache.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface HttpEntity {
  boolean isRepeatable();
  
  boolean isChunked();
  
  long getContentLength();
  
  Header getContentType();
  
  Header getContentEncoding();
  
  InputStream getContent() throws IOException, IllegalStateException;
  
  void writeTo(OutputStream paramOutputStream) throws IOException;
  
  boolean isStreaming();
  
  @Deprecated
  void consumeContent() throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\HttpEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */