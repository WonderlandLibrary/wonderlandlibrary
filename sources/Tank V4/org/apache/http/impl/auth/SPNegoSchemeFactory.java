package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class SPNegoSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {
   private final boolean stripPort;

   public SPNegoSchemeFactory(boolean var1) {
      this.stripPort = var1;
   }

   public SPNegoSchemeFactory() {
      this(false);
   }

   public boolean isStripPort() {
      return this.stripPort;
   }

   public AuthScheme newInstance(HttpParams var1) {
      return new SPNegoScheme(this.stripPort);
   }

   public AuthScheme create(HttpContext var1) {
      return new SPNegoScheme(this.stripPort);
   }
}
