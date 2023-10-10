package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableMap implements Map, Serializable {
   private static final Entry[] EMPTY_ENTRY_ARRAY = new Entry[0];
   private transient ImmutableSet entrySet;
   private transient ImmutableSet keySet;
   private transient ImmutableCollection values;
   private transient ImmutableSetMultimap multimapView;

   public static ImmutableMap of() {
      return ImmutableBiMap.of();
   }

   public static ImmutableMap of(Object var0, Object var1) {
      return ImmutableBiMap.of(var0, var1);
   }

   public static ImmutableMap of(Object var0, Object var1, Object var2, Object var3) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3)});
   }

   public static ImmutableMap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5)});
   }

   public static ImmutableMap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7)});
   }

   public static ImmutableMap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7), entryOf(var8, var9)});
   }

   static ImmutableMapEntry.TerminalEntry entryOf(Object var0, Object var1) {
      CollectPreconditions.checkEntryNotNull(var0, var1);
      return new ImmutableMapEntry.TerminalEntry(var0, var1);
   }

   public static ImmutableMap.Builder builder() {
      return new ImmutableMap.Builder();
   }

   static void checkNoConflict(boolean var0, String var1, Entry var2, Entry var3) {
      if (!var0) {
         throw new IllegalArgumentException("Multiple entries with same " + var1 + ": " + var2 + " and " + var3);
      }
   }

   public static ImmutableMap copyOf(Map var0) {
      if (var0 instanceof ImmutableMap && !(var0 instanceof ImmutableSortedMap)) {
         ImmutableMap var1 = (ImmutableMap)var0;
         if (!var1.isPartialView()) {
            return var1;
         }
      } else if (var0 instanceof EnumMap) {
         return copyOfEnumMapUnsafe(var0);
      }

      Entry[] var3 = (Entry[])var0.entrySet().toArray(EMPTY_ENTRY_ARRAY);
      switch(var3.length) {
      case 0:
         return of();
      case 1:
         Entry var2 = var3[0];
         return of(var2.getKey(), var2.getValue());
      default:
         return new RegularImmutableMap(var3);
      }
   }

   private static ImmutableMap copyOfEnumMapUnsafe(Map var0) {
      return copyOfEnumMap((EnumMap)var0);
   }

   private static ImmutableMap copyOfEnumMap(Map var0) {
      EnumMap var1 = new EnumMap(var0);
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         CollectPreconditions.checkEntryNotNull(var3.getKey(), var3.getValue());
      }

      return ImmutableEnumMap.asImmutable(var1);
   }

   ImmutableMap() {
   }

   /** @deprecated */
   @Deprecated
   public final Object put(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final Object remove(Object var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void clear() {
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.get(var1) != null;
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.values().contains(var1);
   }

   public abstract Object get(@Nullable Object var1);

   public ImmutableSet entrySet() {
      ImmutableSet var1 = this.entrySet;
      return var1 == null ? (this.entrySet = this.createEntrySet()) : var1;
   }

   abstract ImmutableSet createEntrySet();

   public ImmutableSet keySet() {
      ImmutableSet var1 = this.keySet;
      return var1 == null ? (this.keySet = this.createKeySet()) : var1;
   }

   ImmutableSet createKeySet() {
      return new ImmutableMapKeySet(this);
   }

   public ImmutableCollection values() {
      ImmutableCollection var1 = this.values;
      return var1 == null ? (this.values = new ImmutableMapValues(this)) : var1;
   }

   @Beta
   public ImmutableSetMultimap asMultimap() {
      ImmutableSetMultimap var1 = this.multimapView;
      return var1 == null ? (this.multimapView = this.createMultimapView()) : var1;
   }

   private ImmutableSetMultimap createMultimapView() {
      ImmutableMap var1 = this.viewMapValuesAsSingletonSets();
      return new ImmutableSetMultimap(var1, var1.size(), (Comparator)null);
   }

   private ImmutableMap viewMapValuesAsSingletonSets() {
      return new ImmutableMap.MapViewOfValuesAsSingletonSets(this);
   }

   public boolean equals(@Nullable Object var1) {
      return Maps.equalsImpl(this, var1);
   }

   abstract boolean isPartialView();

   public int hashCode() {
      return this.entrySet().hashCode();
   }

   public String toString() {
      return Maps.toStringImpl(this);
   }

   Object writeReplace() {
      return new ImmutableMap.SerializedForm(this);
   }

   public Set entrySet() {
      return this.entrySet();
   }

   public Collection values() {
      return this.values();
   }

   public Set keySet() {
      return this.keySet();
   }

   static class SerializedForm implements Serializable {
      private final Object[] keys;
      private final Object[] values;
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableMap var1) {
         this.keys = new Object[var1.size()];
         this.values = new Object[var1.size()];
         int var2 = 0;

         for(Iterator var3 = var1.entrySet().iterator(); var3.hasNext(); ++var2) {
            Entry var4 = (Entry)var3.next();
            this.keys[var2] = var4.getKey();
            this.values[var2] = var4.getValue();
         }

      }

      Object readResolve() {
         ImmutableMap.Builder var1 = new ImmutableMap.Builder();
         return this.createMap(var1);
      }

      Object createMap(ImmutableMap.Builder var1) {
         for(int var2 = 0; var2 < this.keys.length; ++var2) {
            var1.put(this.keys[var2], this.values[var2]);
         }

         return var1.build();
      }
   }

   private static final class MapViewOfValuesAsSingletonSets extends ImmutableMap {
      private final ImmutableMap delegate;

      MapViewOfValuesAsSingletonSets(ImmutableMap var1) {
         this.delegate = (ImmutableMap)Preconditions.checkNotNull(var1);
      }

      public int size() {
         return this.delegate.size();
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.delegate.containsKey(var1);
      }

      public ImmutableSet get(@Nullable Object var1) {
         Object var2 = this.delegate.get(var1);
         return var2 == null ? null : ImmutableSet.of(var2);
      }

      boolean isPartialView() {
         return false;
      }

      ImmutableSet createEntrySet() {
         return new ImmutableMapEntrySet(this) {
            final ImmutableMap.MapViewOfValuesAsSingletonSets this$0;

            {
               this.this$0 = var1;
            }

            ImmutableMap map() {
               return this.this$0;
            }

            public UnmodifiableIterator iterator() {
               UnmodifiableIterator var1 = ImmutableMap.MapViewOfValuesAsSingletonSets.access$000(this.this$0).entrySet().iterator();
               return new UnmodifiableIterator(this, var1) {
                  final Iterator val$backingIterator;
                  final <undefinedtype> this$1;

                  {
                     this.this$1 = var1;
                     this.val$backingIterator = var2;
                  }

                  public boolean hasNext() {
                     return this.val$backingIterator.hasNext();
                  }

                  public Entry next() {
                     Entry var1 = (Entry)this.val$backingIterator.next();
                     return new AbstractMapEntry(this, var1) {
                        final Entry val$backingEntry;
                        final <undefinedtype> this$2;

                        {
                           this.this$2 = var1;
                           this.val$backingEntry = var2;
                        }

                        public Object getKey() {
                           return this.val$backingEntry.getKey();
                        }

                        public ImmutableSet getValue() {
                           return ImmutableSet.of(this.val$backingEntry.getValue());
                        }

                        public Object getValue() {
                           return this.getValue();
                        }
                     };
                  }

                  public Object next() {
                     return this.next();
                  }
               };
            }

            public Iterator iterator() {
               return this.iterator();
            }
         };
      }

      public Object get(Object var1) {
         return this.get(var1);
      }

      public Set entrySet() {
         return super.entrySet();
      }

      public Collection values() {
         return super.values();
      }

      public Set keySet() {
         return super.keySet();
      }

      static ImmutableMap access$000(ImmutableMap.MapViewOfValuesAsSingletonSets var0) {
         return var0.delegate;
      }
   }

   public static class Builder {
      ImmutableMapEntry.TerminalEntry[] entries;
      int size;

      public Builder() {
         this(4);
      }

      Builder(int var1) {
         this.entries = new ImmutableMapEntry.TerminalEntry[var1];
         this.size = 0;
      }

      private void ensureCapacity(int var1) {
         if (var1 > this.entries.length) {
            this.entries = (ImmutableMapEntry.TerminalEntry[])ObjectArrays.arraysCopyOf(this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, var1));
         }

      }

      public ImmutableMap.Builder put(Object var1, Object var2) {
         this.ensureCapacity(this.size + 1);
         ImmutableMapEntry.TerminalEntry var3 = ImmutableMap.entryOf(var1, var2);
         this.entries[this.size++] = var3;
         return this;
      }

      public ImmutableMap.Builder put(Entry var1) {
         return this.put(var1.getKey(), var1.getValue());
      }

      public ImmutableMap.Builder putAll(Map var1) {
         this.ensureCapacity(this.size + var1.size());
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            this.put(var3);
         }

         return this;
      }

      public ImmutableMap build() {
         switch(this.size) {
         case 0:
            return ImmutableMap.of();
         case 1:
            return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
         default:
            return new RegularImmutableMap(this.size, this.entries);
         }
      }
   }
}
