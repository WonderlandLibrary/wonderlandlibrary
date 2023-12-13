package org.apache.http.conn.scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@ThreadSafe
public final class SchemeRegistry {
   private final ConcurrentHashMap registeredSchemes = new ConcurrentHashMap();

   public final Scheme getScheme(String var1) {
      Scheme var2 = this.get(var1);
      if (var2 == null) {
         throw new IllegalStateException("Scheme '" + var1 + "' not registered.");
      } else {
         return var2;
      }
   }

   public final Scheme getScheme(HttpHost var1) {
      Args.notNull(var1, "Host");
      return this.getScheme(var1.getSchemeName());
   }

   public final Scheme get(String var1) {
      Args.notNull(var1, "Scheme name");
      Scheme var2 = (Scheme)this.registeredSchemes.get(var1);
      return var2;
   }

   public final Scheme register(Scheme var1) {
      Args.notNull(var1, "Scheme");
      Scheme var2 = (Scheme)this.registeredSchemes.put(var1.getName(), var1);
      return var2;
   }

   public final Scheme unregister(String var1) {
      Args.notNull(var1, "Scheme name");
      Scheme var2 = (Scheme)this.registeredSchemes.remove(var1);
      return var2;
   }

   public final List getSchemeNames() {
      return new ArrayList(this.registeredSchemes.keySet());
   }

   public void setItems(Map var1) {
      if (var1 != null) {
         this.registeredSchemes.clear();
         this.registeredSchemes.putAll(var1);
      }
   }
}
