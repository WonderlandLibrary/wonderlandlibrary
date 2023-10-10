package org.apache.http.cookie;

import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public final class CookieOrigin {
   private final String host;
   private final int port;
   private final String path;
   private final boolean secure;

   public CookieOrigin(String var1, int var2, String var3, boolean var4) {
      Args.notBlank(var1, "Host");
      Args.notNegative(var2, "Port");
      Args.notNull(var3, "Path");
      this.host = var1.toLowerCase(Locale.ENGLISH);
      this.port = var2;
      if (var3.trim().length() != 0) {
         this.path = var3;
      } else {
         this.path = "/";
      }

      this.secure = var4;
   }

   public String getHost() {
      return this.host;
   }

   public String getPath() {
      return this.path;
   }

   public int getPort() {
      return this.port;
   }

   public boolean isSecure() {
      return this.secure;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append('[');
      if (this.secure) {
         var1.append("(secure)");
      }

      var1.append(this.host);
      var1.append(':');
      var1.append(Integer.toString(this.port));
      var1.append(this.path);
      var1.append(']');
      return var1.toString();
   }
}
