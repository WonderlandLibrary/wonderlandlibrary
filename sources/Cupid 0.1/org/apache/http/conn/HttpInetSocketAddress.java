package org.apache.http.conn;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.apache.http.HttpHost;
import org.apache.http.util.Args;

@Deprecated
public class HttpInetSocketAddress extends InetSocketAddress {
  private static final long serialVersionUID = -6650701828361907957L;
  
  private final HttpHost httphost;
  
  public HttpInetSocketAddress(HttpHost httphost, InetAddress addr, int port) {
    super(addr, port);
    Args.notNull(httphost, "HTTP host");
    this.httphost = httphost;
  }
  
  public HttpHost getHttpHost() {
    return this.httphost;
  }
  
  public String toString() {
    return this.httphost.getHostName() + ":" + getPort();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\HttpInetSocketAddress.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */