package org.apache.http.conn.ssl;

import javax.net.ssl.SSLException;
import org.apache.http.annotation.Immutable;

@Immutable
public class BrowserCompatHostnameVerifier extends AbstractVerifier {
   public final void verify(String var1, String[] var2, String[] var3) throws SSLException {
      this.verify(var1, var2, var3, false);
   }

   boolean validCountryWildcard(String var1) {
      return true;
   }

   public final String toString() {
      return "BROWSER_COMPATIBLE";
   }
}
