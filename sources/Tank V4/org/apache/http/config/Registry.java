package org.apache.http.config;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public final class Registry implements Lookup {
   private final Map map;

   Registry(Map var1) {
      this.map = new ConcurrentHashMap(var1);
   }

   public Object lookup(String var1) {
      return var1 == null ? null : this.map.get(var1.toLowerCase(Locale.US));
   }

   public String toString() {
      return this.map.toString();
   }
}
