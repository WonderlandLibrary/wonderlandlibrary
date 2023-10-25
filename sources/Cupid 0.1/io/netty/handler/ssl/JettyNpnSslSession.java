package io.netty.handler.ssl;

import java.security.Principal;
import java.security.cert.Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.security.cert.X509Certificate;

final class JettyNpnSslSession implements SSLSession {
  private final SSLEngine engine;
  
  private volatile String applicationProtocol;
  
  JettyNpnSslSession(SSLEngine engine) {
    this.engine = engine;
  }
  
  void setApplicationProtocol(String applicationProtocol) {
    if (applicationProtocol != null)
      applicationProtocol = applicationProtocol.replace(':', '_'); 
    this.applicationProtocol = applicationProtocol;
  }
  
  public String getProtocol() {
    String protocol = unwrap().getProtocol();
    String applicationProtocol = this.applicationProtocol;
    if (applicationProtocol == null) {
      if (protocol != null)
        return protocol.replace(':', '_'); 
      return null;
    } 
    StringBuilder buf = new StringBuilder(32);
    if (protocol != null) {
      buf.append(protocol.replace(':', '_'));
      buf.append(':');
    } else {
      buf.append("null:");
    } 
    buf.append(applicationProtocol);
    return buf.toString();
  }
  
  private SSLSession unwrap() {
    return this.engine.getSession();
  }
  
  public byte[] getId() {
    return unwrap().getId();
  }
  
  public SSLSessionContext getSessionContext() {
    return unwrap().getSessionContext();
  }
  
  public long getCreationTime() {
    return unwrap().getCreationTime();
  }
  
  public long getLastAccessedTime() {
    return unwrap().getLastAccessedTime();
  }
  
  public void invalidate() {
    unwrap().invalidate();
  }
  
  public boolean isValid() {
    return unwrap().isValid();
  }
  
  public void putValue(String s, Object o) {
    unwrap().putValue(s, o);
  }
  
  public Object getValue(String s) {
    return unwrap().getValue(s);
  }
  
  public void removeValue(String s) {
    unwrap().removeValue(s);
  }
  
  public String[] getValueNames() {
    return unwrap().getValueNames();
  }
  
  public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
    return unwrap().getPeerCertificates();
  }
  
  public Certificate[] getLocalCertificates() {
    return unwrap().getLocalCertificates();
  }
  
  public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
    return unwrap().getPeerCertificateChain();
  }
  
  public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
    return unwrap().getPeerPrincipal();
  }
  
  public Principal getLocalPrincipal() {
    return unwrap().getLocalPrincipal();
  }
  
  public String getCipherSuite() {
    return unwrap().getCipherSuite();
  }
  
  public String getPeerHost() {
    return unwrap().getPeerHost();
  }
  
  public int getPeerPort() {
    return unwrap().getPeerPort();
  }
  
  public int getPacketBufferSize() {
    return unwrap().getPacketBufferSize();
  }
  
  public int getApplicationBufferSize() {
    return unwrap().getApplicationBufferSize();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\ssl\JettyNpnSslSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */