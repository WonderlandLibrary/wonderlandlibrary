package org.apache.http.conn.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface TrustStrategy {
  boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\ssl\TrustStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */