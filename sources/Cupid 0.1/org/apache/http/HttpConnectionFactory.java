package org.apache.http;

import java.io.IOException;
import java.net.Socket;

public interface HttpConnectionFactory<T extends HttpConnection> {
  T createConnection(Socket paramSocket) throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\HttpConnectionFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */