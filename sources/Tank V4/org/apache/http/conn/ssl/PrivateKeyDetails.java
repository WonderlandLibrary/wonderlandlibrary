package org.apache.http.conn.ssl;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import org.apache.http.util.Args;

public final class PrivateKeyDetails {
   private final String type;
   private final X509Certificate[] certChain;

   public PrivateKeyDetails(String var1, X509Certificate[] var2) {
      this.type = (String)Args.notNull(var1, "Private key type");
      this.certChain = var2;
   }

   public String getType() {
      return this.type;
   }

   public X509Certificate[] getCertChain() {
      return this.certChain;
   }

   public String toString() {
      return this.type + ':' + Arrays.toString(this.certChain);
   }
}
