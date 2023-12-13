package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
public final class MutableTypeToInstanceMap extends ForwardingMap implements TypeToInstanceMap {
   private final Map backingMap = Maps.newHashMap();

   @Nullable
   public Object getInstance(Class var1) {
      return this.trustedGet(TypeToken.of(var1));
   }

   @Nullable
   public Object putInstance(Class var1, @Nullable Object var2) {
      return this.trustedPut(TypeToken.of(var1), var2);
   }

   @Nullable
   public Object getInstance(TypeToken var1) {
      return this.trustedGet(var1.rejectTypeVariables());
   }

   @Nullable
   public Object putInstance(TypeToken var1, @Nullable Object var2) {
      return this.trustedPut(var1.rejectTypeVariables(), var2);
   }

   public Object put(TypeToken var1, Object var2) {
      throw new UnsupportedOperationException("Please use putInstance() instead.");
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException("Please use putInstance() instead.");
   }

   public Set entrySet() {
      return MutableTypeToInstanceMap.UnmodifiableEntry.transformEntries(super.entrySet());
   }

   protected Map delegate() {
      return this.backingMap;
   }

   @Nullable
   private Object trustedPut(TypeToken var1, @Nullable Object var2) {
      return this.backingMap.put(var1, var2);
   }

   @Nullable
   private Object trustedGet(TypeToken var1) {
      return this.backingMap.get(var1);
   }

   public Object put(Object var1, Object var2) {
      return this.put((TypeToken)var1, var2);
   }

   protected Object delegate() {
      return this.delegate();
   }

   private static final class UnmodifiableEntry extends ForwardingMapEntry {
      private final Entry delegate;

      static Set transformEntries(Set var0) {
         return new ForwardingSet(var0) {
            final Set val$entries;

            {
               this.val$entries = var1;
            }

            protected Set delegate() {
               return this.val$entries;
            }

            public Iterator iterator() {
               return MutableTypeToInstanceMap.UnmodifiableEntry.access$000(super.iterator());
            }

            public Object[] toArray() {
               return this.standardToArray();
            }

            public Object[] toArray(Object[] var1) {
               return this.standardToArray(var1);
            }

            protected Collection delegate() {
               return this.delegate();
            }

            protected Object delegate() {
               return this.delegate();
            }
         };
      }

      private static Iterator transformEntries(Iterator var0) {
         return Iterators.transform(var0, new Function() {
            public Entry apply(Entry var1) {
               return new MutableTypeToInstanceMap.UnmodifiableEntry(var1);
            }

            public Object apply(Object var1) {
               return this.apply((Entry)var1);
            }
         });
      }

      private UnmodifiableEntry(Entry var1) {
         this.delegate = (Entry)Preconditions.checkNotNull(var1);
      }

      protected Entry delegate() {
         return this.delegate;
      }

      public Object setValue(Object var1) {
         throw new UnsupportedOperationException();
      }

      protected Object delegate() {
         return this.delegate();
      }

      static Iterator access$000(Iterator var0) {
         return transformEntries(var0);
      }

      UnmodifiableEntry(Entry var1, Object var2) {
         this(var1);
      }
   }
}
