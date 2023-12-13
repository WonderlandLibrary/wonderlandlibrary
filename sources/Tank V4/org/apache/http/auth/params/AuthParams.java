package org.apache.http.auth.params;

import org.apache.http.annotation.Immutable;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@Immutable
public final class AuthParams {
   private AuthParams() {
   }

   public static String getCredentialCharset(HttpParams var0) {
      Args.notNull(var0, "HTTP parameters");
      String var1 = (String)var0.getParameter("http.auth.credential-charset");
      if (var1 == null) {
         var1 = HTTP.DEF_PROTOCOL_CHARSET.name();
      }

      return var1;
   }

   public static void setCredentialCharset(HttpParams var0, String var1) {
      Args.notNull(var0, "HTTP parameters");
      var0.setParameter("http.auth.credential-charset", var1);
   }
}
