package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class BrowserCompatSpecFactory implements CookieSpecFactory, CookieSpecProvider {
   private final String[] datepatterns;
   private final BrowserCompatSpecFactory.SecurityLevel securityLevel;

   public BrowserCompatSpecFactory(String[] var1, BrowserCompatSpecFactory.SecurityLevel var2) {
      this.datepatterns = var1;
      this.securityLevel = var2;
   }

   public BrowserCompatSpecFactory(String[] var1) {
      this((String[])null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
   }

   public BrowserCompatSpecFactory() {
      this((String[])null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
   }

   public CookieSpec newInstance(HttpParams var1) {
      if (var1 != null) {
         String[] var2 = null;
         Collection var3 = (Collection)var1.getParameter("http.protocol.cookie-datepatterns");
         if (var3 != null) {
            var2 = new String[var3.size()];
            var2 = (String[])var3.toArray(var2);
         }

         return new BrowserCompatSpec(var2, this.securityLevel);
      } else {
         return new BrowserCompatSpec((String[])null, this.securityLevel);
      }
   }

   public CookieSpec create(HttpContext var1) {
      return new BrowserCompatSpec(this.datepatterns);
   }

   public static enum SecurityLevel {
      SECURITYLEVEL_DEFAULT,
      SECURITYLEVEL_IE_MEDIUM;

      private static final BrowserCompatSpecFactory.SecurityLevel[] $VALUES = new BrowserCompatSpecFactory.SecurityLevel[]{SECURITYLEVEL_DEFAULT, SECURITYLEVEL_IE_MEDIUM};
   }
}
