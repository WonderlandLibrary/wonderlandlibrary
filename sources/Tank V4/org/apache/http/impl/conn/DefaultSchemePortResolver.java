package org.apache.http.impl.conn;

import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.util.Args;

@Immutable
public class DefaultSchemePortResolver implements SchemePortResolver {
   public static final DefaultSchemePortResolver INSTANCE = new DefaultSchemePortResolver();

   public int resolve(HttpHost var1) throws UnsupportedSchemeException {
      Args.notNull(var1, "HTTP host");
      int var2 = var1.getPort();
      if (var2 > 0) {
         return var2;
      } else {
         String var3 = var1.getSchemeName();
         if (var3.equalsIgnoreCase("http")) {
            return 80;
         } else if (var3.equalsIgnoreCase("https")) {
            return 443;
         } else {
            throw new UnsupportedSchemeException(var3 + " protocol is not supported");
         }
      }
   }
}
