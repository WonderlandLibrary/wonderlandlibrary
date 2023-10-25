package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpParams;

@Deprecated
public interface SocketFactory {
  Socket createSocket() throws IOException;
  
  Socket connectSocket(Socket paramSocket, String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2, HttpParams paramHttpParams) throws IOException, UnknownHostException, ConnectTimeoutException;
  
  boolean isSecure(Socket paramSocket) throws IllegalArgumentException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\scheme\SocketFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */