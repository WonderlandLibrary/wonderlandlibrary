package org.apache.http.impl.auth;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
public class NegotiateSchemeFactory implements AuthSchemeFactory {
   private final SpnegoTokenGenerator spengoGenerator;
   private final boolean stripPort;

   public NegotiateSchemeFactory(SpnegoTokenGenerator var1, boolean var2) {
      this.spengoGenerator = var1;
      this.stripPort = var2;
   }

   public NegotiateSchemeFactory(SpnegoTokenGenerator var1) {
      this(var1, false);
   }

   public NegotiateSchemeFactory() {
      this((SpnegoTokenGenerator)null, false);
   }

   public AuthScheme newInstance(HttpParams var1) {
      return new NegotiateScheme(this.spengoGenerator, this.stripPort);
   }

   public boolean isStripPort() {
      return this.stripPort;
   }

   public SpnegoTokenGenerator getSpengoGenerator() {
      return this.spengoGenerator;
   }
}
