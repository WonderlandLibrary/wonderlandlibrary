package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class KerberosSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {
   private final boolean stripPort;

   public KerberosSchemeFactory(boolean var1) {
      this.stripPort = var1;
   }

   public KerberosSchemeFactory() {
      this(false);
   }

   public boolean isStripPort() {
      return this.stripPort;
   }

   public AuthScheme newInstance(HttpParams var1) {
      return new KerberosScheme(this.stripPort);
   }

   public AuthScheme create(HttpContext var1) {
      return new KerberosScheme(this.stripPort);
   }
}
