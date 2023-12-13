package org.apache.http.auth;

import java.util.Locale;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Immutable
public class AuthScope {
   public static final String ANY_HOST = null;
   public static final int ANY_PORT = -1;
   public static final String ANY_REALM = null;
   public static final String ANY_SCHEME = null;
   public static final AuthScope ANY;
   private final String scheme;
   private final String realm;
   private final String host;
   private final int port;

   public AuthScope(String var1, int var2, String var3, String var4) {
      this.host = var1 == null ? ANY_HOST : var1.toLowerCase(Locale.ENGLISH);
      this.port = var2 < 0 ? -1 : var2;
      this.realm = var3 == null ? ANY_REALM : var3;
      this.scheme = var4 == null ? ANY_SCHEME : var4.toUpperCase(Locale.ENGLISH);
   }

   public AuthScope(HttpHost var1, String var2, String var3) {
      this(var1.getHostName(), var1.getPort(), var2, var3);
   }

   public AuthScope(HttpHost var1) {
      this(var1, ANY_REALM, ANY_SCHEME);
   }

   public AuthScope(String var1, int var2, String var3) {
      this(var1, var2, var3, ANY_SCHEME);
   }

   public AuthScope(String var1, int var2) {
      this(var1, var2, ANY_REALM, ANY_SCHEME);
   }

   public AuthScope(AuthScope var1) {
      Args.notNull(var1, "Scope");
      this.host = var1.getHost();
      this.port = var1.getPort();
      this.realm = var1.getRealm();
      this.scheme = var1.getScheme();
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public String getRealm() {
      return this.realm;
   }

   public String getScheme() {
      return this.scheme;
   }

   public int match(AuthScope var1) {
      int var2 = 0;
      if (LangUtils.equals(this.scheme, var1.scheme)) {
         ++var2;
      } else if (this.scheme != ANY_SCHEME && var1.scheme != ANY_SCHEME) {
         return -1;
      }

      if (LangUtils.equals(this.realm, var1.realm)) {
         var2 += 2;
      } else if (this.realm != ANY_REALM && var1.realm != ANY_REALM) {
         return -1;
      }

      if (this.port == var1.port) {
         var2 += 4;
      } else if (this.port != -1 && var1.port != -1) {
         return -1;
      }

      if (LangUtils.equals(this.host, var1.host)) {
         var2 += 8;
      } else if (this.host != ANY_HOST && var1.host != ANY_HOST) {
         return -1;
      }

      return var2;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else if (!(var1 instanceof AuthScope)) {
         return super.equals(var1);
      } else {
         AuthScope var2 = (AuthScope)var1;
         return LangUtils.equals(this.host, var2.host) && this.port == var2.port && LangUtils.equals(this.realm, var2.realm) && LangUtils.equals(this.scheme, var2.scheme);
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      if (this.scheme != null) {
         var1.append(this.scheme.toUpperCase(Locale.ENGLISH));
         var1.append(' ');
      }

      if (this.realm != null) {
         var1.append('\'');
         var1.append(this.realm);
         var1.append('\'');
      } else {
         var1.append("<any realm>");
      }

      if (this.host != null) {
         var1.append('@');
         var1.append(this.host);
         if (this.port >= 0) {
            var1.append(':');
            var1.append(this.port);
         }
      }

      return var1.toString();
   }

   public int hashCode() {
      byte var1 = 17;
      int var2 = LangUtils.hashCode(var1, this.host);
      var2 = LangUtils.hashCode(var2, this.port);
      var2 = LangUtils.hashCode(var2, this.realm);
      var2 = LangUtils.hashCode(var2, this.scheme);
      return var2;
   }

   static {
      ANY = new AuthScope(ANY_HOST, -1, ANY_REALM, ANY_SCHEME);
   }
}
