package org.apache.http;

public interface StatusLine {
  ProtocolVersion getProtocolVersion();
  
  int getStatusCode();
  
  String getReasonPhrase();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\StatusLine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */