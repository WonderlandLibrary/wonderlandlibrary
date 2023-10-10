package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class RFC2109SpecFactory implements CookieSpecFactory, CookieSpecProvider {
   private final String[] datepatterns;
   private final boolean oneHeader;

   public RFC2109SpecFactory(String[] var1, boolean var2) {
      this.datepatterns = var1;
      this.oneHeader = var2;
   }

   public RFC2109SpecFactory() {
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
         return new RFC2109Spec(var2, var4);
      } else {
         return new RFC2109Spec();
      }
   }

   public CookieSpec create(HttpContext var1) {
      return new RFC2109Spec(this.datepatterns, this.oneHeader);
   }
}
