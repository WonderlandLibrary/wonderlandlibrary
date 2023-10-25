package org.apache.http.conn.ssl;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import org.apache.http.util.Args;

public final class PrivateKeyDetails {
  private final String type;
  
  private final X509Certificate[] certChain;
  
  public PrivateKeyDetails(String type, X509Certificate[] certChain) {
    this.type = (String)Args.notNull(type, "Private key type");
    this.certChain = certChain;
  }
  
  public String getType() {
    return this.type;
  }
  
  public X509Certificate[] getCertChain() {
    return this.certChain;
  }
  
  public String toString() {
    return this.type + ':' + Arrays.toString((Object[])this.certChain);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\ssl\PrivateKeyDetails.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */