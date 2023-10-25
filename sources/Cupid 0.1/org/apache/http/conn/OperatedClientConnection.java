package org.apache.http.conn;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.params.HttpParams;

@Deprecated
public interface OperatedClientConnection extends HttpClientConnection, HttpInetConnection {
  HttpHost getTargetHost();
  
  boolean isSecure();
  
  Socket getSocket();
  
  void opening(Socket paramSocket, HttpHost paramHttpHost) throws IOException;
  
  void openCompleted(boolean paramBoolean, HttpParams paramHttpParams) throws IOException;
  
  void update(Socket paramSocket, HttpHost paramHttpHost, boolean paramBoolean, HttpParams paramHttpParams) throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\OperatedClientConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */