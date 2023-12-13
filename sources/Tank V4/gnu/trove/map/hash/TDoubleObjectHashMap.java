package gnu.trove.map.hash;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TObjectFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TDoubleHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TDoubleObjectIterator;
import gnu.trove.map.TDoubleObjectMap;
import gnu.trove.procedure.TDoubleObjectProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class TDoubleObjectHashMap extends TDoubleHash implements TDoubleObjectMap, Externalizable {
   static final long serialVersionUID = 1L;
   private final TDoubleObjectProcedure PUT_ALL_PROC;
   protected transient Object[] _values;
   protected double no_entry_key;

   public TDoubleObjectHashMap() {
      this.PUT_ALL_PROC = new TDoubleObjectProcedure(this) {
         final TDoubleObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(double var1, Object var3) {
            this.this$0.put(var1, var3);
            return true;
         }
      };
   }

   public TDoubleObjectHashMap(int var1) {
      super(var1);
      this.PUT_ALL_PROC = new TDoubleObjectProcedure(this) {
         final TDoubleObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(double var1, Object var3) {
            this.this$0.put(var1, var3);
            return true;
         }
      };
      this.no_entry_key = Constants.DEFAULT_DOUBLE_NO_ENTRY_VALUE;
   }

   public TDoubleObjectHashMap(int var1, float var2) {
      super(var1, var2);
      this.PUT_ALL_PROC = new TDoubleObjectProcedure(this) {
         final TDoubleObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(double var1, Object var3) {
            this.this$0.put(var1, var3);
            return true;
         }
      };
      this.no_entry_key = Constants.DEFAULT_DOUBLE_NO_ENTRY_VALUE;
   }

   public TDoubleObjectHashMap(int var1, float var2, double var3) {
      super(var1, var2);
      this.PUT_ALL_PROC = new TDoubleObjectProcedure(this) {
         final TDoubleObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(double var1, Object var3) {
            this.this$0.put(var1, var3);
            return true;
         }
      };
      this.no_entry_key = var3;
   }

   public TDoubleObjectHashMap(TDoubleObjectMap var1) {
      this(var1.size(), 0.5F, var1.getNoEntryKey());
      this.putAll(var1);
   }

   protected int setUp(int var1) {
      int var2 = super.setUp(var1);
      this._values = (Object[])(new Object[var2]);
      return var2;
   }

   protected void rehash(int var1) {
      int var2 = this._set.length;
      double[] var3 = this._set;
      Object[] var4 = this._values;
      byte[] var5 = this._states;
      this._set = new double[var1];
      this._values = (Object[])(new Object[var1]);
      this._states = new byte[var1];
      int var6 = var2;

      while(var6-- > 0) {
         if (var5[var6] == 1) {
            double var7 = var3[var6];
            int var9 = this.insertKey(var7);
            this._values[var9] = var4[var6];
         }
      }

   }

   public double getNoEntryKey() {
      return this.no_entry_key;
   }

   public boolean containsKey(double var1) {
      return this.contains(var1);
   }

   public boolean containsValue(Object var1) {
      byte[] var2 = this._states;
      Object[] var3 = this._values;
      int var4;
      if (null == var1) {
         var4 = var3.length;

         while(var4-- > 0) {
            if (var2[var4] == 1 && null == var3[var4]) {
               return true;
            }
         }
      } else {
         var4 = var3.length;

         while(var4-- > 0) {
            if (var2[var4] == 1 && (var1 == var3[var4] || var1.equals(var3[var4]))) {
               return true;
            }
         }
      }

      return false;
   }

   public Object get(double var1) {
      int var3 = this.index(var1);
      return var3 < 0 ? null : this._values[var3];
   }

   public Object put(double var1, Object var3) {
      int var4 = this.insertKey(var1);
      return this.doPut(var3, var4);
   }

   public Object putIfAbsent(double var1, Object var3) {
      int var4 = this.insertKey(var1);
      return var4 < 0 ? this._values[-var4 - 1] : this.doPut(var3, var4);
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

   public Object remove(double var1) {
      Object var3 = null;
      int var4 = this.index(var1);
      if (var4 >= 0) {
         var3 = this._values[var4];
         this.removeAt(var4);
      }

      return var3;
   }

   protected void removeAt(int var1) {
      this._values[var1] = null;
      super.removeAt(var1);
   }

   public void putAll(Map var1) {
      Set var2 = var1.entrySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         this.put((Double)var4.getKey(), var4.getValue());
      }

   }

   public void putAll(TDoubleObjectMap var1) {
      var1.forEachEntry(this.PUT_ALL_PROC);
   }

   public void clear() {
      super.clear();
      Arrays.fill(this._set, 0, this._set.length, this.no_entry_key);
      Arrays.fill(this._states, 0, this._states.length, (byte)0);
      Arrays.fill(this._values, 0, this._values.length, (Object)null);
   }

   public TDoubleSet keySet() {
      return new TDoubleObjectHashMap.KeyView(this);
   }

   public double[] keys() {
      double[] var1 = new double[this.size()];
      double[] var2 = this._set;
      byte[] var3 = this._states;
      int var4 = var2.length;
      int var5 = 0;

      while(var4-- > 0) {
         if (var3[var4] == 1) {
            var1[var5++] = var2[var4];
         }
      }

      return var1;
   }

   public double[] keys(double[] var1) {
      if (var1.length < this._size) {
         var1 = new double[this._size];
      }

      double[] var2 = this._set;
      byte[] var3 = this._states;
      int var4 = var2.length;
      int var5 = 0;

      while(var4-- > 0) {
         if (var3[var4] == 1) {
            var1[var5++] = var2[var4];
         }
      }

      return var1;
   }

   public Collection valueCollection() {
      return new TDoubleObjectHashMap.ValueView(this);
   }

   public Object[] values() {
      Object[] var1 = new Object[this.size()];
      Object[] var2 = this._values;
      byte[] var3 = this._states;
      int var4 = var2.length;
      int var5 = 0;

      while(var4-- > 0) {
         if (var3[var4] == 1) {
            var1[var5++] = var2[var4];
         }
      }

      return var1;
   }

   public Object[] values(Object[] var1) {
      if (var1.length < this._size) {
         var1 = (Object[])((Object[])Array.newInstance(var1.getClass().getComponentType(), this._size));
      }

      Object[] var2 = this._values;
      byte[] var3 = this._states;
      int var4 = var2.length;
      int var5 = 0;

      while(var4-- > 0) {
         if (var3[var4] == 1) {
            var1[var5++] = var2[var4];
         }
      }

      return var1;
   }

   public TDoubleObjectIterator iterator() {
      return new TDoubleObjectHashMap.TDoubleObjectHashIterator(this, this);
   }

   public boolean forEachKey(TDoubleProcedure var1) {
      return this.forEach(var1);
   }

   public boolean forEachValue(TObjectProcedure var1) {
      byte[] var2 = this._states;
      Object[] var3 = this._values;
      int var4 = var3.length;

      do {
         if (var4-- <= 0) {
            return true;
         }
      } while(var2[var4] != 1 || var1.execute(var3[var4]));

      return false;
   }

   public boolean forEachEntry(TDoubleObjectProcedure var1) {
      byte[] var2 = this._states;
      double[] var3 = this._set;
      Object[] var4 = this._values;
      int var5 = var3.length;

      do {
         if (var5-- <= 0) {
            return true;
         }
      } while(var2[var5] != 1 || var1.execute(var3[var5], var4[var5]));

      return false;
   }

   public boolean retainEntries(TDoubleObjectProcedure var1) {
      boolean var2 = false;
      byte[] var3 = this._states;
      double[] var4 = this._set;
      Object[] var5 = this._values;
      this.tempDisableAutoCompaction();
      int var6 = var4.length;

      while(var6-- > 0) {
         if (var3[var6] == 1 && !var1.execute(var4[var6], var5[var6])) {
            this.removeAt(var6);
            var2 = true;
         }
      }

      this.reenableAutoCompaction(true);
      return var2;
   }

   public void transformValues(TObjectFunction var1) {
      byte[] var2 = this._states;
      Object[] var3 = this._values;
      int var4 = var3.length;

      while(var4-- > 0) {
         if (var2[var4] == 1) {
            var3[var4] = var1.execute(var3[var4]);
         }
      }

   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TDoubleObjectMap)) {
         return false;
      } else {
         TDoubleObjectMap var2 = (TDoubleObjectMap)var1;
         if (var2.size() != this.size()) {
            return false;
         } else {
            try {
               TDoubleObjectIterator var3 = this.iterator();

               double var4;
               label40:
               do {
                  Object var6;
                  do {
                     if (!var3.hasNext()) {
                        return true;
                     }

                     var3.advance();
                     var4 = var3.key();
                     var6 = var3.value();
                     if (var6 == null) {
                        continue label40;
                     }
                  } while(var6.equals(var2.get(var4)));

                  return false;
               } while(var2.get(var4) == null && var2.containsKey(var4));

               return false;
            } catch (ClassCastException var7) {
               return true;
            }
         }
      }
   }

   public int hashCode() {
      int var1 = 0;
      Object[] var2 = this._values;
      byte[] var3 = this._states;
      int var4 = var2.length;

      while(var4-- > 0) {
         if (var3[var4] == 1) {
            var1 += HashFunctions.hash(this._set[var4]) ^ (var2[var4] == null ? 0 : var2[var4].hashCode());
         }
      }

      return var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      super.writeExternal(var1);
      var1.writeDouble(this.no_entry_key);
      var1.writeInt(this._size);
      int var2 = this._states.length;

      while(var2-- > 0) {
         if (this._states[var2] == 1) {
            var1.writeDouble(this._set[var2]);
            var1.writeObject(this._values[var2]);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      super.readExternal(var1);
      this.no_entry_key = var1.readDouble();
      int var2 = var1.readInt();
      this.setUp(var2);

      while(var2-- > 0) {
         double var3 = var1.readDouble();
         Object var5 = var1.readObject();
         this.put(var3, var5);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");
      this.forEachEntry(new TDoubleObjectProcedure(this, var1) {
         private boolean first;
         final StringBuilder val$buf;
         final TDoubleObjectHashMap this$0;

         {
            this.this$0 = var1;
            this.val$buf = var2;
            this.first = true;
         }

         public boolean execute(double var1, Object var3) {
            if (this.first) {
               this.first = false;
            } else {
               this.val$buf.append(",");
            }

            this.val$buf.append(var1);
            this.val$buf.append("=");
            this.val$buf.append(var3);
            return true;
         }
      });
      var1.append("}");
      return var1.toString();
   }

   static int access$000(TDoubleObjectHashMap var0) {
      return var0._size;
   }

   static int access$100(TDoubleObjectHashMap var0) {
      return var0._size;
   }

   class TDoubleObjectHashIterator extends THashPrimitiveIterator implements TDoubleObjectIterator {
      private final TDoubleObjectHashMap _map;
      final TDoubleObjectHashMap this$0;

      public TDoubleObjectHashIterator(TDoubleObjectHashMap var1, TDoubleObjectHashMap var2) {
         super(var2);
         this.this$0 = var1;
         this._map = var2;
      }

      public void advance() {
         this.moveToNextIndex();
      }

      public double key() {
         return this._map._set[this._index];
      }

      public Object value() {
         return this._map._values[this._index];
      }

      public Object setValue(Object var1) {
         Object var2 = this.value();
         this._map._values[this._index] = var1;
         return var2;
      }
   }

   private abstract class MapBackedView extends AbstractSet implements Set, Iterable {
      final TDoubleObjectHashMap this$0;

      private MapBackedView(TDoubleObjectHashMap var1) {
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

      MapBackedView(TDoubleObjectHashMap var1, Object var2) {
         this(var1);
      }
   }

   protected class ValueView extends TDoubleObjectHashMap.MapBackedView {
      final TDoubleObjectHashMap this$0;

      protected ValueView(TDoubleObjectHashMap var1) {
         super(var1, null);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return new TDoubleObjectHashMap.ValueView.TDoubleObjectValueHashIterator(this, this.this$0) {
            final TDoubleObjectHashMap.ValueView this$1;

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
         byte[] var3 = this.this$0._states;
         int var4 = var2.length;

         do {
            do {
               if (var4-- <= 0) {
                  return false;
               }
            } while(var3[var4] != 1);
         } while(var1 != var2[var4] && (null == var2[var4] || !var2[var4].equals(var1)));

         this.this$0.removeAt(var4);
         return true;
      }

      class TDoubleObjectValueHashIterator extends THashPrimitiveIterator implements Iterator {
         protected final TDoubleObjectHashMap _map;
         final TDoubleObjectHashMap.ValueView this$1;

         public TDoubleObjectValueHashIterator(TDoubleObjectHashMap.ValueView var1, TDoubleObjectHashMap var2) {
            super(var2);
            this.this$1 = var1;
            this._map = var2;
         }

         protected Object objectAtIndex(int var1) {
            byte[] var2 = this.this$1.this$0._states;
            Object var3 = this._map._values[var1];
            return var2[var1] != 1 ? null : var3;
         }

         public Object next() {
            this.moveToNextIndex();
            return this._map._values[this._index];
         }
      }
   }

   class KeyView implements TDoubleSet {
      final TDoubleObjectHashMap this$0;

      KeyView(TDoubleObjectHashMap var1) {
         this.this$0 = var1;
      }

      public double getNoEntryValue() {
         return this.this$0.no_entry_key;
      }

      public int size() {
         return TDoubleObjectHashMap.access$000(this.this$0);
      }

      public boolean isEmpty() {
         return TDoubleObjectHashMap.access$100(this.this$0) == 0;
      }

      public boolean contains(double var1) {
         return this.this$0.containsKey(var1);
      }

      public TDoubleIterator iterator() {
         return new TDoubleObjectHashMap.KeyView.TDoubleHashIterator(this, this.this$0);
      }

      public double[] toArray() {
         return this.this$0.keys();
      }

      public double[] toArray(double[] var1) {
         return this.this$0.keys(var1);
      }

      public boolean add(double var1) {
         throw new UnsupportedOperationException();
      }

      public boolean containsAll(Collection var1) {
         Iterator var2 = var1.iterator();

         Object var3;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            var3 = var2.next();
         } while(this.this$0.containsKey((Double)var3));

         return false;
      }

      public boolean containsAll(TDoubleCollection var1) {
         if (var1 == this) {
            return true;
         } else {
            TDoubleIterator var2 = var1.iterator();

            do {
               if (!var2.hasNext()) {
                  return true;
               }
            } while(this.this$0.containsKey(var2.next()));

            return false;
         }
      }

      public boolean containsAll(double[] var1) {
         double[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            double var5 = var2[var4];
            if (!this.this$0.containsKey(var5)) {
               return false;
            }
         }

         return true;
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(TDoubleCollection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(double[] var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection var1) {
         boolean var2 = false;
         TDoubleIterator var3 = this.iterator();

         while(var3.hasNext()) {
            if (!var1.contains(var3.next())) {
               var3.remove();
               var2 = true;
            }
         }

         return var2;
      }

      public boolean retainAll(TDoubleCollection var1) {
         if (this == var1) {
            return false;
         } else {
            boolean var2 = false;
            TDoubleIterator var3 = this.iterator();

            while(var3.hasNext()) {
               if (!var1.contains(var3.next())) {
                  var3.remove();
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean retainAll(double[] var1) {
         boolean var2 = false;
         Arrays.sort(var1);
         double[] var3 = this.this$0._set;
         byte[] var4 = this.this$0._states;
         int var5 = var3.length;

         while(var5-- > 0) {
            if (var4[var5] == 1 && Arrays.binarySearch(var1, var3[var5]) < 0) {
               this.this$0.removeAt(var5);
               var2 = true;
            }
         }

         return var2;
      }

      public boolean removeAll(Collection var1) {
         boolean var2 = false;
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (var4 instanceof Double) {
               double var5 = (Double)var4;
               if (this != var5) {
                  var2 = true;
               }
            }
         }

         return var2;
      }

      public boolean removeAll(TDoubleCollection var1) {
         if (var1 == this) {
            this.clear();
            return true;
         } else {
            boolean var2 = false;
            TDoubleIterator var3 = var1.iterator();

            while(var3.hasNext()) {
               double var4 = var3.next();
               if (this != var4) {
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean removeAll(double[] var1) {
         boolean var2 = false;
         int var3 = var1.length;

         while(var3-- > 0) {
            if (this != var1[var3]) {
               var2 = true;
            }
         }

         return var2;
      }

      public void clear() {
         this.this$0.clear();
      }

      public boolean forEach(TDoubleProcedure var1) {
         return this.this$0.forEachKey(var1);
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof TDoubleSet)) {
            return false;
         } else {
            TDoubleSet var2 = (TDoubleSet)var1;
            if (var2.size() != this.size()) {
               return false;
            } else {
               int var3 = this.this$0._states.length;

               do {
                  if (var3-- <= 0) {
                     return true;
                  }
               } while(this.this$0._states[var3] != 1 || var2.contains(this.this$0._set[var3]));

               return false;
            }
         }
      }

      public int hashCode() {
         int var1 = 0;
         int var2 = this.this$0._states.length;

         while(var2-- > 0) {
            if (this.this$0._states[var2] == 1) {
               var1 += HashFunctions.hash(this.this$0._set[var2]);
            }
         }

         return var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("{");
         boolean var2 = true;
         int var3 = this.this$0._states.length;

         while(var3-- > 0) {
            if (this.this$0._states[var3] == 1) {
               if (var2) {
                  var2 = false;
               } else {
                  var1.append(",");
               }

               var1.append(this.this$0._set[var3]);
            }
         }

         return var1.toString();
      }

      class TDoubleHashIterator extends THashPrimitiveIterator implements TDoubleIterator {
         private final TDoubleHash _hash;
         final TDoubleObjectHashMap.KeyView this$1;

         public TDoubleHashIterator(TDoubleObjectHashMap.KeyView var1, TDoubleHash var2) {
            super(var2);
            this.this$1 = var1;
            this._hash = var2;
         }

         public double next() {
            this.moveToNextIndex();
            return this._hash._set[this._index];
         }
      }
   }
}
