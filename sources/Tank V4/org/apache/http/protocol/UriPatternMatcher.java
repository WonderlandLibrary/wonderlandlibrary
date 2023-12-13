package org.apache.http.protocol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@ThreadSafe
public class UriPatternMatcher {
   @GuardedBy("this")
   private final Map map = new HashMap();

   public synchronized void register(String var1, Object var2) {
      Args.notNull(var1, "URI request pattern");
      this.map.put(var1, var2);
   }

   public synchronized void unregister(String var1) {
      if (var1 != null) {
         this.map.remove(var1);
      }
   }

   /** @deprecated */
   @Deprecated
   public synchronized void setHandlers(Map var1) {
      Args.notNull(var1, "Map of handlers");
      this.map.clear();
      this.map.putAll(var1);
   }

   /** @deprecated */
   @Deprecated
   public synchronized void setObjects(Map var1) {
      Args.notNull(var1, "Map of handlers");
      this.map.clear();
      this.map.putAll(var1);
   }

   /** @deprecated */
   @Deprecated
   public synchronized Map getObjects() {
      return this.map;
   }

   public synchronized Object lookup(String var1) {
      Args.notNull(var1, "Request path");
      Object var2 = this.map.get(var1);
      if (var2 == null) {
         String var3 = null;
         Iterator var4 = this.map.keySet().iterator();

         while(true) {
            String var5;
            do {
               do {
                  if (!var4.hasNext()) {
                     return var2;
                  }

                  var5 = (String)var4.next();
               } while(var1 == false);
            } while(var3 != null && var3.length() >= var5.length() && (var3.length() != var5.length() || !var5.endsWith("*")));

            var2 = this.map.get(var5);
            var3 = var5;
         }
      } else {
         return var2;
      }
   }

   public String toString() {
      return this.map.toString();
   }
}
