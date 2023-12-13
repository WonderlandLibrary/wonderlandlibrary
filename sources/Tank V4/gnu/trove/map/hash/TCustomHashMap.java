package gnu.trove.map.hash;

import gnu.trove.function.TObjectFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCustomObjectHash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TMap;
import gnu.trove.procedure.TObjectObjectProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.strategy.HashingStrategy;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TCustomHashMap extends TCustomObjectHash implements TMap, Externalizable {
   static final long serialVersionUID = 1L;
   protected transient Object[] _values;

   public TCustomHashMap() {
   }

   public TCustomHashMap(HashingStrategy var1) {
      super(var1);
   }

   public TCustomHashMap(HashingStrategy var1, int var2) {
      super(var1, var2);
   }

   public TCustomHashMap(HashingStrategy var1, int var2, float var3) {
      super(var1, var2, var3);
   }

   public TCustomHashMap(HashingStrategy var1, Map var2) {
      this(var1, var2.size());
      this.putAll(var2);
   }

   public TCustomHashMap(HashingStrategy var1, TCustomHashMap var2) {
      this(var1, var2.size());
      this.putAll(var2);
   }

   public int setUp(int var1) {
      int var2 = super.setUp(var1);
      this._values = (Object[])(new Object[var2]);
      return var2;
   }

   public Object put(Object var1, Object var2) {
      int var3 = this.insertKey(var1);
      return this.doPut(var2, var3);
   }

   public Object putIfAbsent(Object var1, Object var2) {
      int var3 = this.insertKey(var1);
      return var3 < 0 ? this._values[-var3 - 1] : this.doPut(var2, var3);
   }

   private Object doPut(Object var1, int var2) {
      Object var3 = null;
      boolean var4 = true;
      if (var2 < 0) {
         var2 = -var2 - 1;
         var3 = this._values[var2];
         var4 = false;
      }

      this._values[var2] = var1;
      if (var4) {
         this.postInsertHook(this.consumeFreeSlot);
      }

      return var3;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Map)) {
         return false;
      } else {
         Map var2 = (Map)var1;
         return var2.size() != this.size() ? false : this.forEachEntry(new TCustomHashMap.EqProcedure(var2));
      }
   }

   public int hashCode() {
      TCustomHashMap.HashProcedure var1 = new TCustomHashMap.HashProcedure(this);
      this.forEachEntry(var1);
      return var1.getHashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");
      this.forEachEntry(new TObjectObjectProcedure(this, var1) {
         private boolean first;
         final StringBuilder val$buf;
         final TCustomHashMap this$0;

         {
            this.this$0 = var1;
            this.val$buf = var2;
            this.first = true;
         }

         public boolean execute(Object var1, Object var2) {
            if (this.first) {
               this.first = false;
            } else {
               this.val$buf.append(", ");
            }

            this.val$buf.append(var1);
            this.val$buf.append("=");
            this.val$buf.append(var2);
            return true;
         }
      });
      var1.append("}");
      return var1.toString();
   }

   public boolean forEachKey(TObjectProcedure var1) {
      return this.forEach(var1);
   }

   public boolean forEachValue(TObjectProcedure var1) {
      Object[] var2 = this._values;
      Object[] var3 = this._set;
      int var4 = var2.length;

      do {
         if (var4-- <= 0) {
            return true;
         }
      } while(var3[var4] == FREE || var3[var4] == REMOVED || var1.execute(var2[var4]));

      return false;
   }

   public boolean forEachEntry(TObjectObjectProcedure var1) {
      Object[] var2 = this._set;
      Object[] var3 = this._values;
      int var4 = var2.length;

      do {
         if (var4-- <= 0) {
            return true;
         }
      } while(var2[var4] == FREE || var2[var4] == REMOVED || var1.execute(var2[var4], var3[var4]));

      return false;
   }

   public boolean retainEntries(TObjectObjectProcedure var1) {
      boolean var2 = false;
      Object[] var3 = this._set;
      Object[] var4 = this._values;
      this.tempDisableAutoCompaction();
      int var5 = var3.length;

      while(var5-- > 0) {
         if (var3[var5] != FREE && var3[var5] != REMOVED && !var1.execute(var3[var5], var4[var5])) {
            this.removeAt(var5);
            var2 = true;
         }
      }

      this.reenableAutoCompaction(true);
      return var2;
   }

   public void transformValues(TObjectFunction var1) {
      Object[] var2 = this._values;
      Object[] var3 = this._set;
      int var4 = var2.length;

      while(var4-- > 0) {
         if (var3[var4] != FREE && var3[var4] != REMOVED) {
            var2[var4] = var1.execute(var2[var4]);
         }
      }

   }

   protected void rehash(int var1) {
      int var2 = this._set.length;
      int var3 = this.size();
      Object[] var4 = this._set;
      Object[] var5 = this._values;
      this._set = new Object[var1];
      Arrays.fill(this._set, FREE);
      this._values = (Object[])(new Object[var1]);
      int var6 = var2;

      while(var6-- > 0) {
         Object var7 = var4[var6];
         if (var7 != FREE && var7 != REMOVED) {
            int var8 = this.insertKey(var7);
            if (var8 < 0) {
               this.throwObjectContractViolation(this._set[-var8 - 1], var7, this.size(), var3, var4);
            }

            this._values[var8] = var5[var6];
         }
      }

   }

   public Object get(Object var1) {
      int var2 = this.index(var1);
      return var2 >= 0 && this.strategy.equals(this._set[var2], var1) ? this._values[var2] : null;
   }

   public void clear() {
      if (this.size() != 0) {
         super.clear();
         Arrays.fill(this._set, 0, this._set.length, FREE);
         Arrays.fill(this._values, 0, this._values.length, (Object)null);
      }
   }

   public Object remove(Object var1) {
      Object var2 = null;
      int var3 = this.index(var1);
      if (var3 >= 0) {
         var2 = this._values[var3];
         this.removeAt(var3);
      }

      return var2;
   }

   public void removeAt(int var1) {
      this._values[var1] = null;
      super.removeAt(var1);
   }

   public Collection values() {
      return new TCustomHashMap.ValueView(this);
   }

   public Set keySet() {
      return new TCustomHashMap.KeyView(this);
   }

   public Set entrySet() {
      return new TCustomHashMap.EntryView(this);
   }

   public boolean containsValue(Object var1) {
      Object[] var2 = this._set;
      Object[] var3 = this._values;
      int var4;
      if (null == var1) {
         var4 = var3.length;

         while(var4-- > 0) {
            if (var2[var4] != FREE && var2[var4] != REMOVED && var1 == var3[var4]) {
               return true;
            }
         }

         return false;
      } else {
         var4 = var3.length;

         do {
            do {
               do {
                  if (var4-- <= 0) {
                     return false;
                  }
               } while(var2[var4] == FREE);
            } while(var2[var4] == REMOVED);
         } while(var1 != var3[var4] && !this.strategy.equals(var1, var3[var4]));

         return true;
      }
   }

   public boolean containsKey(Object var1) {
      return this.contains(var1);
   }

   public void putAll(Map var1) {
      this.ensureCapacity(var1.size());
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         java.util.Map.Entry var3 = (java.util.Map.Entry)var2.next();
         this.put(var3.getKey(), var3.getValue());
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(1);
      super.writeExternal(var1);
      var1.writeInt(this._size);
      int var2 = this._set.length;

      while(var2-- > 0) {
         if (this._set[var2] != REMOVED && this._set[var2] != FREE) {
            var1.writeObject(this._set[var2]);
            var1.writeObject(this._values[var2]);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 0) {
         super.readExternal(var1);
      }

      int var3 = var1.readInt();
      this.setUp(var3);

      while(var3-- > 0) {
         Object var4 = var1.readObject();
         Object var5 = var1.readObject();
         this.put(var4, var5);
      }

   }

   static HashingStrategy access$200(TCustomHashMap var0) {
      return var0.strategy;
   }

   static int access$300(TCustomHashMap var0, Object var1) {
      return var0.index(var1);
   }

   static HashingStrategy access$400(TCustomHashMap var0) {
      return var0.strategy;
   }

   static HashingStrategy access$500(TCustomHashMap var0) {
      return var0.strategy;
   }

   static HashingStrategy access$600(TCustomHashMap var0) {
      return var0.strategy;
   }

   final class Entry implements java.util.Map.Entry {
      private Object key;
      private Object val;
      private final int index;
      final TCustomHashMap this$0;

      Entry(TCustomHashMap var1, Object var2, Object var3, int var4) {
         this.this$0 = var1;
         this.key = var2;
         this.val = var3;
         this.index = var4;
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.val;
      }

      public Object setValue(Object var1) {
         if (this.this$0._values[this.index] != this.val) {
            throw new ConcurrentModificationException();
         } else {
            Object var2 = this.val;
            this.this$0._values[this.index] = var1;
            this.val = var1;
            return var2;
         }
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof java.util.Map.Entry)) {
            return false;
         } else {
            boolean var10000;
            label38: {
               label27: {
                  java.util.Map.Entry var3 = (java.util.Map.Entry)var1;
                  if (this.getKey() == null) {
                     if (var3.getKey() != null) {
                        break label27;
                     }
                  } else if (!TCustomHashMap.access$600(this.this$0).equals(this.getKey(), var3.getKey())) {
                     break label27;
                  }

                  if (this.getValue() == null) {
                     if (var3.getValue() == null) {
                        break label38;
                     }
                  } else if (this.getValue().equals(var3.getValue())) {
                     break label38;
                  }
               }

               var10000 = false;
               return var10000;
            }

            var10000 = true;
            return var10000;
         }
      }

      public int hashCode() {
         return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
      }

      public String toString() {
         return this.key + "=" + this.val;
      }
   }

   protected class KeyView extends TCustomHashMap.MapBackedView {
      final TCustomHashMap this$0;

      protected KeyView(TCustomHashMap var1) {
         super(var1, null);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return new TObjectHashIterator(this.this$0);
      }

      public boolean removeElement(Object var1) {
         return null != this.this$0.remove(var1);
      }

      public boolean containsElement(Object var1) {
         return this.this$0.contains(var1);
      }
   }

   private abstract class MapBackedView extends AbstractSet implements Set, Iterable {
      final TCustomHashMap this$0;

      private MapBackedView(TCustomHashMap var1) {
         this.this$0 = var1;
      }

      public abstract Iterator iterator();

      public abstract boolean removeElement(Object var1);

      public abstract boolean containsElement(Object var1);

      public boolean contains(Object var1) {
         return this.containsElement(var1);
      }

      public boolean remove(Object var1) {
         return this.removeElement(var1);
      }

      public void clear() {
         this.this$0.clear();
      }

      public boolean add(Object var1) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return this.this$0.size();
      }

      public Object[] toArray() {
         Object[] var1 = new Object[this.size()];
         Iterator var2 = this.iterator();

         for(int var3 = 0; var2.hasNext(); ++var3) {
            var1[var3] = var2.next();
         }

         return var1;
      }

      public Object[] toArray(Object[] var1) {
         int var2 = this.size();
         if (var1.length < var2) {
            var1 = (Object[])((Object[])Array.newInstance(var1.getClass().getComponentType(), var2));
         }

         Iterator var3 = this.iterator();
         Object[] var4 = var1;

         for(int var5 = 0; var5 < var2; ++var5) {
            var4[var5] = var3.next();
         }

         if (var1.length > var2) {
            var1[var2] = null;
         }

         return var1;
      }

      public boolean isEmpty() {
         return this.this$0.isEmpty();
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection var1) {
         boolean var2 = false;
         Iterator var3 = this.iterator();

         while(var3.hasNext()) {
            if (!var1.contains(var3.next())) {
               var3.remove();
               var2 = true;
            }
         }

         return var2;
      }

      public String toString() {
         Iterator var1 = this.iterator();
         if (!var1.hasNext()) {
            return "{}";
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append('{');

            while(true) {
               Object var3 = var1.next();
               var2.append(var3 == this ? "(this Collection)" : var3);
               if (!var1.hasNext()) {
                  return var2.append('}').toString();
               }

               var2.append(", ");
            }
         }
      }

      MapBackedView(TCustomHashMap var1, Object var2) {
         this(var1);
      }
   }

   protected class EntryView extends TCustomHashMap.MapBackedView {
      final TCustomHashMap this$0;

      protected EntryView(TCustomHashMap var1) {
         super(var1, null);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return new TCustomHashMap.EntryView.EntryIterator(this, this.this$0);
      }

      public boolean removeElement(java.util.Map.Entry var1) {
         Object var4 = this.keyForEntry(var1);
         int var3 = TCustomHashMap.access$300(this.this$0, var4);
         if (var3 >= 0) {
            Object var2 = this.valueForEntry(var1);
            if (var2 == this.this$0._values[var3] || null != var2 && TCustomHashMap.access$400(this.this$0).equals(var2, this.this$0._values[var3])) {
               this.this$0.removeAt(var3);
               return true;
            }
         }

         return false;
      }

      public boolean containsElement(java.util.Map.Entry var1) {
         Object var2 = this.this$0.get(this.keyForEntry(var1));
         Object var3 = var1.getValue();
         return var3 == var2 || null != var2 && TCustomHashMap.access$500(this.this$0).equals(var2, var3);
      }

      protected Object valueForEntry(java.util.Map.Entry var1) {
         return var1.getValue();
      }

      protected Object keyForEntry(java.util.Map.Entry var1) {
         return var1.getKey();
      }

      public boolean containsElement(Object var1) {
         return this.containsElement((java.util.Map.Entry)var1);
      }

      public boolean removeElement(Object var1) {
         return this.removeElement((java.util.Map.Entry)var1);
      }

      private final class EntryIterator extends TObjectHashIterator {
         final TCustomHashMap.EntryView this$1;

         EntryIterator(TCustomHashMap.EntryView var1, TCustomHashMap var2) {
            super(var2);
            this.this$1 = var1;
         }

         public TCustomHashMap.Entry objectAtIndex(int var1) {
            return this.this$1.this$0.new Entry(this.this$1.this$0, this.this$1.this$0._set[var1], this.this$1.this$0._values[var1], var1);
         }

         public Object objectAtIndex(int var1) {
            return this.objectAtIndex(var1);
         }
      }
   }

   protected class ValueView extends TCustomHashMap.MapBackedView {
      final TCustomHashMap this$0;

      protected ValueView(TCustomHashMap var1) {
         super(var1, null);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return new TObjectHashIterator(this, this.this$0) {
            final TCustomHashMap.ValueView this$1;

            {
               this.this$1 = var1;
            }

            protected Object objectAtIndex(int var1) {
               return this.this$1.this$0._values[var1];
            }
         };
      }

      public boolean containsElement(Object var1) {
         return this.this$0.containsValue(var1);
      }

      public boolean removeElement(Object var1) {
         Object[] var2 = this.this$0._values;
         Object[] var3 = this.this$0._set;
         int var4 = var2.length;

         do {
            if (var4-- <= 0) {
               return false;
            }
         } while((var3[var4] == TObjectHash.FREE || var3[var4] == TObjectHash.REMOVED || var1 != var2[var4]) && (null == var2[var4] || !TCustomHashMap.access$200(this.this$0).equals(var2[var4], var1)));

         this.this$0.removeAt(var4);
         return true;
      }
   }

   private static final class EqProcedure implements TObjectObjectProcedure {
      private final Map _otherMap;

      EqProcedure(Map var1) {
         this._otherMap = var1;
      }

      public final boolean execute(Object var1, Object var2) {
         if (var2 == null && !this._otherMap.containsKey(var1)) {
            return false;
         } else {
            Object var3 = this._otherMap.get(var1);
            return var3 == var2 || var3 != null && var3.equals(var2);
         }
      }
   }

   private final class HashProcedure implements TObjectObjectProcedure {
      private int h;
      final TCustomHashMap this$0;

      private HashProcedure(TCustomHashMap var1) {
         this.this$0 = var1;
         this.h = 0;
      }

      public int getHashCode() {
         return this.h;
      }

      public final boolean execute(Object var1, Object var2) {
         this.h += HashFunctions.hash(var1) ^ (var2 == null ? 0 : var2.hashCode());
         return true;
      }

      HashProcedure(TCustomHashMap var1, Object var2) {
         this(var1);
      }
   }
}
