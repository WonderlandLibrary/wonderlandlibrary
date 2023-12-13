package org.apache.http.conn.ssl;

import org.apache.http.annotation.Immutable;

@Immutable
public class AllowAllHostnameVerifier extends AbstractVerifier {
   public final void verify(String var1, String[] var2, String[] var3) {
   }

   public final String toString() {
      return "ALLOW_ALL";
   }
}
