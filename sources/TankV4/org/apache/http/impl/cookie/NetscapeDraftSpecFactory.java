package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class NetscapeDraftSpecFactory implements CookieSpecFactory, CookieSpecProvider {
   private final String[] datepatterns;

   public NetscapeDraftSpecFactory(String[] var1) {
      this.datepatterns = var1;
   }

   public NetscapeDraftSpecFactory() {
      this((String[])null);
   }

   public CookieSpec newInstance(HttpParams var1) {
      if (var1 != null) {
         String[] var2 = null;
         Collection var3 = (Collection)var1.getParameter("http.protocol.cookie-datepatterns");
         if (var3 != null) {
            var2 = new String[var3.size()];
            var2 = (String[])var3.toArray(var2);
         }

         return new NetscapeDraftSpec(var2);
      } else {
         return new NetscapeDraftSpec();
      }
   }

   public CookieSpec create(HttpContext var1) {
      return new NetscapeDraftSpec(this.datepatterns);
   }
}
