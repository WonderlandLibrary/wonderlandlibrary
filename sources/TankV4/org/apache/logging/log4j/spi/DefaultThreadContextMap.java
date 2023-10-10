package org.apache.logging.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultThreadContextMap implements ThreadContextMap {
   private final boolean useMap;
   private final ThreadLocal localMap = new InheritableThreadLocal(this) {
      final DefaultThreadContextMap this$0;

      {
         this.this$0 = var1;
      }

      protected Map childValue(Map var1) {
         return var1 != null && DefaultThreadContextMap.access$000(this.this$0) ? Collections.unmodifiableMap(new HashMap(var1)) : null;
      }

      protected Object childValue(Object var1) {
         return this.childValue((Map)var1);
      }
   };

   public DefaultThreadContextMap(boolean var1) {
      this.useMap = var1;
   }

   public void put(String var1, String var2) {
      if (this.useMap) {
         Map var3 = (Map)this.localMap.get();
         HashMap var4 = var3 == null ? new HashMap() : new HashMap(var3);
         var4.put(var1, var2);
         this.localMap.set(Collections.unmodifiableMap(var4));
      }
   }

   public String get(String var1) {
      Map var2 = (Map)this.localMap.get();
      return var2 == null ? null : (String)var2.get(var1);
   }

   public void remove(String var1) {
      Map var2 = (Map)this.localMap.get();
      if (var2 != null) {
         HashMap var3 = new HashMap(var2);
         var3.remove(var1);
         this.localMap.set(Collections.unmodifiableMap(var3));
      }

   }

   public void clear() {
      this.localMap.remove();
   }

   public boolean containsKey(String var1) {
      Map var2 = (Map)this.localMap.get();
      return var2 != null && var2.containsKey(var1);
   }

   public Map getCopy() {
      Map var1 = (Map)this.localMap.get();
      return var1 == null ? new HashMap() : new HashMap(var1);
   }

   public Map getImmutableMapOrNull() {
      return (Map)this.localMap.get();
   }

   public boolean isEmpty() {
      Map var1 = (Map)this.localMap.get();
      return var1 == null || var1.size() == 0;
   }

   public String toString() {
      Map var1 = (Map)this.localMap.get();
      return var1 == null ? "{}" : var1.toString();
   }

   static boolean access$000(DefaultThreadContextMap var0) {
      return var0.useMap;
   }
}
