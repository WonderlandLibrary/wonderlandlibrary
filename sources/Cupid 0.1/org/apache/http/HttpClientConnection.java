package org.apache.http;

import java.io.IOException;

public interface HttpClientConnection extends HttpConnection {
  boolean isResponseAvailable(int paramInt) throws IOException;
  
  void sendRequestHeader(HttpRequest paramHttpRequest) throws HttpException, IOException;
  
  void sendRequestEntity(HttpEntityEnclosingRequest paramHttpEntityEnclosingRequest) throws HttpException, IOException;
  
  HttpResponse receiveResponseHeader() throws HttpException, IOException;
  
  void receiveResponseEntity(HttpResponse paramHttpResponse) throws HttpException, IOException;
  
  void flush() throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\HttpClientConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */