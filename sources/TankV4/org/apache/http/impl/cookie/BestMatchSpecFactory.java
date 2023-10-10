package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class BestMatchSpecFactory implements CookieSpecFactory, CookieSpecProvider {
   private final String[] datepatterns;
   private final boolean oneHeader;

   public BestMatchSpecFactory(String[] var1, boolean var2) {
      this.datepatterns = var1;
      this.oneHeader = var2;
   }

   public BestMatchSpecFactory() {
      this((String[])null, false);
   }

   public CookieSpec newInstance(HttpParams var1) {
      if (var1 != null) {
         String[] var2 = null;
         Collection var3 = (Collection)var1.getParameter("http.protocol.cookie-datepatterns");
         if (var3 != null) {
            var2 = new String[var3.size()];
            var2 = (String[])var3.toArray(var2);
         }

         boolean var4 = var1.getBooleanParameter("http.protocol.single-cookie-header", false);
         return new BestMatchSpec(var2, var4);
      } else {
         return new BestMatchSpec();
      }
   }

   public CookieSpec create(HttpContext var1) {
      return new BestMatchSpec(this.datepatterns, this.oneHeader);
   }
}
