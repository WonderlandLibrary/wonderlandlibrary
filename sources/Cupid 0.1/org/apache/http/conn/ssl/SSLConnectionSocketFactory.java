package org.apache.http.conn.ssl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@ThreadSafe
public class SSLConnectionSocketFactory implements LayeredConnectionSocketFactory {
  public static final String TLS = "TLS";
  
  public static final String SSL = "SSL";
  
  public static final String SSLV2 = "SSLv2";
  
  public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
  
  public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
  
  public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
  
  private final SSLSocketFactory socketfactory;
  
  private final X509HostnameVerifier hostnameVerifier;
  
  private final String[] supportedProtocols;
  
  private final String[] supportedCipherSuites;
  
  public static SSLConnectionSocketFactory getSocketFactory() throws SSLInitializationException {
    return new SSLConnectionSocketFactory(SSLContexts.createDefault(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
  }
  
  private static String[] split(String s) {
    if (TextUtils.isBlank(s))
      return null; 
    return s.split(" *, *");
  }
  
  public static SSLConnectionSocketFactory getSystemSocketFactory() throws SSLInitializationException {
    return new SSLConnectionSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
  }
  
  public SSLConnectionSocketFactory(SSLContext sslContext) {
    this(sslContext, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
  }
  
  public SSLConnectionSocketFactory(SSLContext sslContext, X509HostnameVerifier hostnameVerifier) {
    this(((SSLContext)Args.notNull(sslContext, "SSL context")).getSocketFactory(), (String[])null, (String[])null, hostnameVerifier);
  }
  
  public SSLConnectionSocketFactory(SSLContext sslContext, String[] supportedProtocols, String[] supportedCipherSuites, X509HostnameVerifier hostnameVerifier) {
    this(((SSLContext)Args.notNull(sslContext, "SSL context")).getSocketFactory(), supportedProtocols, supportedCipherSuites, hostnameVerifier);
  }
  
  public SSLConnectionSocketFactory(SSLSocketFactory socketfactory, X509HostnameVerifier hostnameVerifier) {
    this(socketfactory, (String[])null, (String[])null, hostnameVerifier);
  }
  
  public SSLConnectionSocketFactory(SSLSocketFactory socketfactory, String[] supportedProtocols, String[] supportedCipherSuites, X509HostnameVerifier hostnameVerifier) {
    this.socketfactory = (SSLSocketFactory)Args.notNull(socketfactory, "SSL socket factory");
    this.supportedProtocols = supportedProtocols;
    this.supportedCipherSuites = supportedCipherSuites;
    this.hostnameVerifier = (hostnameVerifier != null) ? hostnameVerifier : BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
  }
  
  protected void prepareSocket(SSLSocket socket) throws IOException {}
  
  public Socket createSocket(HttpContext context) throws IOException {
    return SocketFactory.getDefault().createSocket();
  }
  
  public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
    Args.notNull(host, "HTTP host");
    Args.notNull(remoteAddress, "Remote address");
    Socket sock = (socket != null) ? socket : createSocket(context);
    if (localAddress != null)
      sock.bind(localAddress); 
    try {
      sock.connect(remoteAddress, connectTimeout);
    } catch (IOException ex) {
      try {
        sock.close();
      } catch (IOException ignore) {}
      throw ex;
    } 
    if (sock instanceof SSLSocket) {
      SSLSocket sslsock = (SSLSocket)sock;
      sslsock.startHandshake();
      verifyHostname(sslsock, host.getHostName());
      return sock;
    } 
    return createLayeredSocket(sock, host.getHostName(), remoteAddress.getPort(), context);
  }
  
  public Socket createLayeredSocket(Socket socket, String target, int port, HttpContext context) throws IOException {
    SSLSocket sslsock = (SSLSocket)this.socketfactory.createSocket(socket, target, port, true);
    if (this.supportedProtocols != null)
      sslsock.setEnabledProtocols(this.supportedProtocols); 
    if (this.supportedCipherSuites != null)
      sslsock.setEnabledCipherSuites(this.supportedCipherSuites); 
    prepareSocket(sslsock);
    sslsock.startHandshake();
    verifyHostname(sslsock, target);
    return sslsock;
  }
  
  X509HostnameVerifier getHostnameVerifier() {
    return this.hostnameVerifier;
  }
  
  private void verifyHostname(SSLSocket sslsock, String hostname) throws IOException {
    try {
      this.hostnameVerifier.verify(hostname, sslsock);
    } catch (IOException iox) {
      try {
        sslsock.close();
      } catch (Exception x) {}
      throw iox;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\ssl\SSLConnectionSocketFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */