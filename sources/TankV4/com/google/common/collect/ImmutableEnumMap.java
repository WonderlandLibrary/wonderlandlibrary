package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
final class ImmutableEnumMap extends ImmutableMap {
   private final transient EnumMap delegate;

   static ImmutableMap asImmutable(EnumMap var0) {
      switch(var0.size()) {
      case 0:
         return ImmutableMap.of();
      case 1:
         Entry var1 = (Entry)Iterables.getOnlyElement(var0.entrySet());
         return ImmutableMap.of(var1.getKey(), var1.getValue());
      default:
         return new ImmutableEnumMap(var0);
      }
   }

   private ImmutableEnumMap(EnumMap var1) {
      this.delegate = var1;
      Preconditions.checkArgument(!var1.isEmpty());
   }

   ImmutableSet createKeySet() {
      return new ImmutableSet(this) {
         final ImmutableEnumMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean contains(Object var1) {
            return ImmutableEnumMap.access$000(this.this$0).containsKey(var1);
         }

         public int size() {
            return this.this$0.size();
         }

         public UnmodifiableIterator iterator() {
            return Iterators.unmodifiableIterator(ImmutableEnumMap.access$000(this.this$0).keySet().iterator());
         }

         boolean isPartialView() {
            return true;
         }

         public Iterator iterator() {
            return this.iterator();
         }
      };
   }

   public int size() {
      return this.delegate.size();
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.delegate.containsKey(var1);
   }

   public Object get(Object var1) {
      return this.delegate.get(var1);
   }

   ImmutableSet createEntrySet() {
      return new ImmutableMapEntrySet(this) {
         final ImmutableEnumMap this$0;

         {
            this.this$0 = var1;
         }

         ImmutableMap map() {
            return this.this$0;
         }

         public UnmodifiableIterator iterator() {
            return new UnmodifiableIterator(this) {
               private final Iterator backingIterator;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
                  this.backingIterator = ImmutableEnumMap.access$000(this.this$1.this$0).entrySet().iterator();
               }

               public boolean hasNext() {
                  return this.backingIterator.hasNext();
               }

               public Entry next() {
                  Entry var1 = (Entry)this.backingIterator.next();
                  return Maps.immutableEntry(var1.getKey(), var1.getValue());
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

   boolean isPartialView() {
      return false;
   }

   Object writeReplace() {
      return new ImmutableEnumMap.EnumSerializedForm(this.delegate);
   }

   static EnumMap access$000(ImmutableEnumMap var0) {
      return var0.delegate;
   }

   ImmutableEnumMap(EnumMap var1, Object var2) {
      this(var1);
   }

   private static class EnumSerializedForm implements Serializable {
      final EnumMap delegate;
      private static final long serialVersionUID = 0L;

      EnumSerializedForm(EnumMap var1) {
         this.delegate = var1;
      }

      Object readResolve() {
         return new ImmutableEnumMap(this.delegate);
      }
   }
}
