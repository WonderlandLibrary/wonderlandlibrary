package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

@GwtCompatible
final class WellBehavedMap extends ForwardingMap {
   private final Map delegate;
   private Set entrySet;

   private WellBehavedMap(Map var1) {
      this.delegate = var1;
   }

   static WellBehavedMap wrap(Map var0) {
      return new WellBehavedMap(var0);
   }

   protected Map delegate() {
      return this.delegate;
   }

   public Set entrySet() {
      Set var1 = this.entrySet;
      return var1 != null ? var1 : (this.entrySet = new WellBehavedMap.EntrySet(this));
   }

   protected Object delegate() {
      return this.delegate();
   }

   private final class EntrySet extends Maps.EntrySet {
      final WellBehavedMap this$0;

      private EntrySet(WellBehavedMap var1) {
         this.this$0 = var1;
      }

      Map map() {
         return this.this$0;
      }

      public Iterator iterator() {
         return new TransformedIterator(this, this.this$0.keySet().iterator()) {
            final WellBehavedMap.EntrySet this$1;

            {
               this.this$1 = var1;
            }

            Entry transform(Object var1) {
               return new AbstractMapEntry(this, var1) {
                  final Object val$key;
                  final <undefinedtype> this$2;

                  {
                     this.this$2 = var1;
                     this.val$key = var2;
                  }

                  public Object getKey() {
                     return this.val$key;
                  }

                  public Object getValue() {
                     return this.this$2.this$1.this$0.get(this.val$key);
                  }

                  public Object setValue(Object var1) {
                     return this.this$2.this$1.this$0.put(this.val$key, var1);
                  }
               };
            }

            Object transform(Object var1) {
               return this.transform(var1);
            }
         };
      }

      EntrySet(WellBehavedMap var1, Object var2) {
         this(var1);
      }
   }
}
