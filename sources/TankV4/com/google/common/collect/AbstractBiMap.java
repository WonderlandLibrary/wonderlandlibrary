package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
abstract class AbstractBiMap extends ForwardingMap implements BiMap, Serializable {
   private transient Map delegate;
   transient AbstractBiMap inverse;
   private transient Set keySet;
   private transient Set valueSet;
   private transient Set entrySet;
   @GwtIncompatible("Not needed in emulated source.")
   private static final long serialVersionUID = 0L;

   AbstractBiMap(Map var1, Map var2) {
      this.setDelegates(var1, var2);
   }

   private AbstractBiMap(Map var1, AbstractBiMap var2) {
      this.delegate = var1;
      this.inverse = var2;
   }

   protected Map delegate() {
      return this.delegate;
   }

   Object checkKey(@Nullable Object var1) {
      return var1;
   }

   Object checkValue(@Nullable Object var1) {
      return var1;
   }

   void setDelegates(Map var1, Map var2) {
      Preconditions.checkState(this.delegate == null);
      Preconditions.checkState(this.inverse == null);
      Preconditions.checkArgument(var1.isEmpty());
      Preconditions.checkArgument(var2.isEmpty());
      Preconditions.checkArgument(var1 != var2);
      this.delegate = var1;
      this.inverse = new AbstractBiMap.Inverse(var2, this);
   }

   void setInverse(AbstractBiMap var1) {
      this.inverse = var1;
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.inverse.containsKey(var1);
   }

   public Object put(@Nullable Object var1, @Nullable Object var2) {
      return this.putInBothMaps(var1, var2, false);
   }

   public Object forcePut(@Nullable Object var1, @Nullable Object var2) {
      return this.putInBothMaps(var1, var2, true);
   }

   private Object putInBothMaps(@Nullable Object var1, @Nullable Object var2, boolean var3) {
      this.checkKey(var1);
      this.checkValue(var2);
      boolean var4 = this.containsKey(var1);
      if (var4 && Objects.equal(var2, this.get(var1))) {
         return var2;
      } else {
         if (var3) {
            this.inverse().remove(var2);
         } else {
            Preconditions.checkArgument(!this.containsValue(var2), "value already present: %s", var2);
         }

         Object var5 = this.delegate.put(var1, var2);
         this.updateInverseMap(var1, var4, var5, var2);
         return var5;
      }
   }

   private void updateInverseMap(Object var1, boolean var2, Object var3, Object var4) {
      if (var2) {
         this.removeFromInverseMap(var3);
      }

      this.inverse.delegate.put(var4, var1);
   }

   public Object remove(@Nullable Object var1) {
      return this.containsKey(var1) ? this.removeFromBothMaps(var1) : null;
   }

   private Object removeFromBothMaps(Object var1) {
      Object var2 = this.delegate.remove(var1);
      this.removeFromInverseMap(var2);
      return var2;
   }

   private void removeFromInverseMap(Object var1) {
      this.inverse.delegate.remove(var1);
   }

   public void putAll(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put(var3.getKey(), var3.getValue());
      }

   }

   public void clear() {
      this.delegate.clear();
      this.inverse.delegate.clear();
   }

   public BiMap inverse() {
      return this.inverse;
   }

   public Set keySet() {
      Set var1 = this.keySet;
      return var1 == null ? (this.keySet = new AbstractBiMap.KeySet(this)) : var1;
   }

   public Set values() {
      Set var1 = this.valueSet;
      return var1 == null ? (this.valueSet = new AbstractBiMap.ValueSet(this)) : var1;
   }

   public Set entrySet() {
      Set var1 = this.entrySet;
      return var1 == null ? (this.entrySet = new AbstractBiMap.EntrySet(this)) : var1;
   }

   public Collection values() {
      return this.values();
   }

   protected Object delegate() {
      return this.delegate();
   }

   static Map access$200(AbstractBiMap var0) {
      return var0.delegate;
   }

   static Object access$300(AbstractBiMap var0, Object var1) {
      return var0.removeFromBothMaps(var1);
   }

   static void access$600(AbstractBiMap var0, Object var1, boolean var2, Object var3, Object var4) {
      var0.updateInverseMap(var1, var2, var3, var4);
   }

   static void access$700(AbstractBiMap var0, Object var1) {
      var0.removeFromInverseMap(var1);
   }

   AbstractBiMap(Map var1, AbstractBiMap var2, Object var3) {
      this(var1, var2);
   }

   private static class Inverse extends AbstractBiMap {
      @GwtIncompatible("Not needed in emulated source.")
      private static final long serialVersionUID = 0L;

      private Inverse(Map var1, AbstractBiMap var2) {
         super(var1, var2, null);
      }

      Object checkKey(Object var1) {
         return this.inverse.checkValue(var1);
      }

      Object checkValue(Object var1) {
         return this.inverse.checkKey(var1);
      }

      @GwtIncompatible("java.io.ObjectOuputStream")
      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
         var1.writeObject(this.inverse());
      }

      @GwtIncompatible("java.io.ObjectInputStream")
      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         this.setInverse((AbstractBiMap)var1.readObject());
      }

      @GwtIncompatible("Not needed in the emulated source.")
      Object readResolve() {
         return this.inverse().inverse();
      }

      public Collection values() {
         return super.values();
      }

      protected Object delegate() {
         return super.delegate();
      }

      Inverse(Map var1, AbstractBiMap var2, Object var3) {
         this(var1, var2);
      }
   }

   private class EntrySet extends ForwardingSet {
      final Set esDelegate;
      final AbstractBiMap this$0;

      private EntrySet(AbstractBiMap var1) {
         this.this$0 = var1;
         this.esDelegate = AbstractBiMap.access$200(this.this$0).entrySet();
      }

      protected Set delegate() {
         return this.esDelegate;
      }

      public void clear() {
         this.this$0.clear();
      }

      public boolean remove(Object var1) {
         if (!this.esDelegate.contains(var1)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            AbstractBiMap.access$200(this.this$0.inverse).remove(var2.getValue());
            this.esDelegate.remove(var2);
            return true;
         }
      }

      public Iterator iterator() {
         Iterator var1 = this.esDelegate.iterator();
         return new Iterator(this, var1) {
            Entry entry;
            final Iterator val$iterator;
            final AbstractBiMap.EntrySet this$1;

            {
               this.this$1 = var1;
               this.val$iterator = var2;
            }

            public boolean hasNext() {
               return this.val$iterator.hasNext();
            }

            public Entry next() {
               this.entry = (Entry)this.val$iterator.next();
               Entry var1 = this.entry;
               return new ForwardingMapEntry(this, var1) {
                  final Entry val$finalEntry;
                  final <undefinedtype> this$2;

                  {
                     this.this$2 = var1;
                     this.val$finalEntry = var2;
                  }

                  protected Entry delegate() {
                     return this.val$finalEntry;
                  }

                  public Object setValue(Object var1) {
                     Preconditions.checkState(this.this$2.this$1.contains(this), "entry no longer in map");
                     if (Objects.equal(var1, this.getValue())) {
                        return var1;
                     } else {
                        Preconditions.checkArgument(!this.this$2.this$1.this$0.containsValue(var1), "value already present: %s", var1);
                        Object var2 = this.val$finalEntry.setValue(var1);
                        Preconditions.checkState(Objects.equal(var1, this.this$2.this$1.this$0.get(this.getKey())), "entry no longer in map");
                        AbstractBiMap.access$600(this.this$2.this$1.this$0, this.getKey(), true, var2, var1);
                        return var2;
                     }
                  }

                  protected Object delegate() {
                     return this.delegate();
                  }
               };
            }

            public void remove() {
               CollectPreconditions.checkRemove(this.entry != null);
               Object var1 = this.entry.getValue();
               this.val$iterator.remove();
               AbstractBiMap.access$700(this.this$1.this$0, var1);
            }

            public Object next() {
               return this.next();
            }
         };
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public Object[] toArray(Object[] var1) {
         return this.standardToArray(var1);
      }

      public boolean contains(Object var1) {
         return Maps.containsEntryImpl(this.delegate(), var1);
      }

      public boolean containsAll(Collection var1) {
         return this.standardContainsAll(var1);
      }

      public boolean removeAll(Collection var1) {
         return this.standardRemoveAll(var1);
      }

      public boolean retainAll(Collection var1) {
         return this.standardRetainAll(var1);
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }

      EntrySet(AbstractBiMap var1, Object var2) {
         this(var1);
      }
   }

   private class ValueSet extends ForwardingSet {
      final Set valuesDelegate;
      final AbstractBiMap this$0;

      private ValueSet(AbstractBiMap var1) {
         this.this$0 = var1;
         this.valuesDelegate = this.this$0.inverse.keySet();
      }

      protected Set delegate() {
         return this.valuesDelegate;
      }

      public Iterator iterator() {
         return Maps.valueIterator(this.this$0.entrySet().iterator());
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public Object[] toArray(Object[] var1) {
         return this.standardToArray(var1);
      }

      public String toString() {
         return this.standardToString();
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }

      ValueSet(AbstractBiMap var1, Object var2) {
         this(var1);
      }
   }

   private class KeySet extends ForwardingSet {
      final AbstractBiMap this$0;

      private KeySet(AbstractBiMap var1) {
         this.this$0 = var1;
      }

      protected Set delegate() {
         return AbstractBiMap.access$200(this.this$0).keySet();
      }

      public void clear() {
         this.this$0.clear();
      }

      public boolean remove(Object var1) {
         if (!this.contains(var1)) {
            return false;
         } else {
            AbstractBiMap.access$300(this.this$0, var1);
            return true;
         }
      }

      public boolean removeAll(Collection var1) {
         return this.standardRemoveAll(var1);
      }

      public boolean retainAll(Collection var1) {
         return this.standardRetainAll(var1);
      }

      public Iterator iterator() {
         return Maps.keyIterator(this.this$0.entrySet().iterator());
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }

      KeySet(AbstractBiMap var1, Object var2) {
         this(var1);
      }
   }
}
