package io.netty.util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class DefaultAttributeMap implements AttributeMap {
   private static final AtomicReferenceFieldUpdater updater = AtomicReferenceFieldUpdater.newUpdater(DefaultAttributeMap.class, Map.class, "map");
   private volatile Map map;

   public Attribute attr(AttributeKey var1) {
      Object var2 = this.map;
      if (var2 == null) {
         var2 = new IdentityHashMap(2);
         if (!updater.compareAndSet(this, (Object)null, var2)) {
            var2 = this.map;
         }
      }

      synchronized(var2){}
      Object var4 = (Attribute)((Map)var2).get(var1);
      if (var4 == null) {
         var4 = new DefaultAttributeMap.DefaultAttribute((Map)var2, var1);
         ((Map)var2).put(var1, var4);
      }

      return (Attribute)var4;
   }

   private static final class DefaultAttribute extends AtomicReference implements Attribute {
      private static final long serialVersionUID = -2661411462200283011L;
      private final Map map;
      private final AttributeKey key;

      DefaultAttribute(Map var1, AttributeKey var2) {
         this.map = var1;
         this.key = var2;
      }

      public AttributeKey key() {
         return this.key;
      }

      public Object setIfAbsent(Object var1) {
         while(true) {
            if (!this.compareAndSet((Object)null, var1)) {
               Object var2 = this.get();
               if (var2 == null) {
                  continue;
               }

               return var2;
            }

            return null;
         }
      }

      public Object getAndRemove() {
         Object var1 = this.getAndSet((Object)null);
         this.remove0();
         return var1;
      }

      public void remove() {
         this.set((Object)null);
         this.remove0();
      }

      private void remove0() {
         Map var1;
         synchronized(var1 = this.map){}
         this.map.remove(this.key);
      }
   }
}
