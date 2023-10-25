package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

@Deprecated
class LayeredSocketFactoryAdaptor extends SocketFactoryAdaptor implements LayeredSocketFactory {
  private final LayeredSchemeSocketFactory factory;
  
  LayeredSocketFactoryAdaptor(LayeredSchemeSocketFactory factory) {
    super(factory);
    this.factory = factory;
  }
  
  public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
    return this.factory.createLayeredSocket(socket, host, port, autoClose);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\scheme\LayeredSocketFactoryAdaptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */