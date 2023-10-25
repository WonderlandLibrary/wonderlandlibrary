package org.apache.http.conn.ssl;

import javax.net.ssl.SSLException;
import org.apache.http.annotation.Immutable;

@Immutable
public class BrowserCompatHostnameVerifier extends AbstractVerifier {
  public final void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
    verify(host, cns, subjectAlts, false);
  }
  
  boolean validCountryWildcard(String cn) {
    return true;
  }
  
  public final String toString() {
    return "BROWSER_COMPATIBLE";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\ssl\BrowserCompatHostnameVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */