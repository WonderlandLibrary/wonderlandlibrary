package org.apache.http.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.config.Lookup;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@ThreadSafe
public final class AuthSchemeRegistry implements Lookup {
   private final ConcurrentHashMap registeredSchemes = new ConcurrentHashMap();

   public void register(String var1, AuthSchemeFactory var2) {
      Args.notNull(var1, "Name");
      Args.notNull(var2, "Authentication scheme factory");
      this.registeredSchemes.put(var1.toLowerCase(Locale.ENGLISH), var2);
   }

   public void unregister(String var1) {
      Args.notNull(var1, "Name");
      this.registeredSchemes.remove(var1.toLowerCase(Locale.ENGLISH));
   }

   public AuthScheme getAuthScheme(String var1, HttpParams var2) throws IllegalStateException {
      Args.notNull(var1, "Name");
      AuthSchemeFactory var3 = (AuthSchemeFactory)this.registeredSchemes.get(var1.toLowerCase(Locale.ENGLISH));
      if (var3 != null) {
         return var3.newInstance(var2);
      } else {
         throw new IllegalStateException("Unsupported authentication scheme: " + var1);
      }
   }

   public List getSchemeNames() {
      return new ArrayList(this.registeredSchemes.keySet());
   }

   public void setItems(Map var1) {
      if (var1 != null) {
         this.registeredSchemes.clear();
         this.registeredSchemes.putAll(var1);
      }
   }

   public AuthSchemeProvider lookup(String var1) {
      return new AuthSchemeProvider(this, var1) {
         final String val$name;
         final AuthSchemeRegistry this$0;

         {
            this.this$0 = var1;
            this.val$name = var2;
         }

         public AuthScheme create(HttpContext var1) {
            HttpRequest var2 = (HttpRequest)var1.getAttribute("http.request");
            return this.this$0.getAuthScheme(this.val$name, var2.getParams());
         }
      };
   }

   public Object lookup(String var1) {
      return this.lookup(var1);
   }
}
