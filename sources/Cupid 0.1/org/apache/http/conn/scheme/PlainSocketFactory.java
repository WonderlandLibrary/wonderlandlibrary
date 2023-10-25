package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class PlainSocketFactory implements SocketFactory, SchemeSocketFactory {
  private final HostNameResolver nameResolver;
  
  public static PlainSocketFactory getSocketFactory() {
    return new PlainSocketFactory();
  }
  
  @Deprecated
  public PlainSocketFactory(HostNameResolver nameResolver) {
    this.nameResolver = nameResolver;
  }
  
  public PlainSocketFactory() {
    this.nameResolver = null;
  }
  
  public Socket createSocket(HttpParams params) {
    return new Socket();
  }
  
  public Socket createSocket() {
    return new Socket();
  }
  
  public Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException, ConnectTimeoutException {
    Args.notNull(remoteAddress, "Remote address");
    Args.notNull(params, "HTTP parameters");
    Socket sock = socket;
    if (sock == null)
      sock = createSocket(); 
    if (localAddress != null) {
      sock.setReuseAddress(HttpConnectionParams.getSoReuseaddr(params));
      sock.bind(localAddress);
    } 
    int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
    int soTimeout = HttpConnectionParams.getSoTimeout(params);
    try {
      sock.setSoTimeout(soTimeout);
      sock.connect(remoteAddress, connTimeout);
    } catch (SocketTimeoutException ex) {
      throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
    } 
    return sock;
  }
  
  public final boolean isSecure(Socket sock) {
    return false;
  }
  
  @Deprecated
  public Socket connectSocket(Socket socket, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
    InetAddress remoteAddress;
    InetSocketAddress local = null;
    if (localAddress != null || localPort > 0)
      local = new InetSocketAddress(localAddress, (localPort > 0) ? localPort : 0); 
    if (this.nameResolver != null) {
      remoteAddress = this.nameResolver.resolve(host);
    } else {
      remoteAddress = InetAddress.getByName(host);
    } 
    InetSocketAddress remote = new InetSocketAddress(remoteAddress, port);
    return connectSocket(socket, remote, local, params);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\scheme\PlainSocketFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */