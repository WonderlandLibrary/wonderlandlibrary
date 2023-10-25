package org.apache.http;

public interface RequestLine {
  String getMethod();
  
  ProtocolVersion getProtocolVersion();
  
  String getUri();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\RequestLine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */