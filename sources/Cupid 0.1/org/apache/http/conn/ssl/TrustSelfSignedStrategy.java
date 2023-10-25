package org.apache.http.conn.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustSelfSignedStrategy implements TrustStrategy {
  public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    return (chain.length == 1);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\ssl\TrustSelfSignedStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */