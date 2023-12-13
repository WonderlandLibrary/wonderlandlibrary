package org.apache.http.impl.client;

import java.util.HashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthScheme;
import org.apache.http.client.AuthCache;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.util.Args;

@NotThreadSafe
public class BasicAuthCache implements AuthCache {
   private final HashMap map;
   private final SchemePortResolver schemePortResolver;

   public BasicAuthCache(SchemePortResolver var1) {
      this.map = new HashMap();
      this.schemePortResolver = (SchemePortResolver)(var1 != null ? var1 : DefaultSchemePortResolver.INSTANCE);
   }

   public BasicAuthCache() {
      this((SchemePortResolver)null);
   }

   protected HttpHost getKey(HttpHost var1) {
      if (var1.getPort() <= 0) {
         int var2;
         try {
            var2 = this.schemePortResolver.resolve(var1);
         } catch (UnsupportedSchemeException var4) {
            return var1;
         }

         return new HttpHost(var1.getHostName(), var2, var1.getSchemeName());
      } else {
         return var1;
      }
   }

   public void put(HttpHost var1, AuthScheme var2) {
      Args.notNull(var1, "HTTP host");
      this.map.put(this.getKey(var1), var2);
   }

   public AuthScheme get(HttpHost var1) {
      Args.notNull(var1, "HTTP host");
      return (AuthScheme)this.map.get(this.getKey(var1));
   }

   public void remove(HttpHost var1) {
      Args.notNull(var1, "HTTP host");
      this.map.remove(this.getKey(var1));
   }

   public void clear() {
      this.map.clear();
   }

   public String toString() {
      return this.map.toString();
   }
}
