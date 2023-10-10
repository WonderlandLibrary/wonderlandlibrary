package gnu.trove.map.hash;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TObjectShortIterator;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TObjectShortMap;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.TObjectShortProcedure;
import gnu.trove.procedure.TShortProcedure;
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
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

public class TObjectShortHashMap extends TObjectHash implements TObjectShortMap, Externalizable {
   static final long serialVersionUID = 1L;
   private final TObjectShortProcedure PUT_ALL_PROC;
   protected transient short[] _values;
   protected short no_entry_value;

   public TObjectShortHashMap() {
      this.PUT_ALL_PROC = new TObjectShortProcedure(this) {
         final TObjectShortHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(Object var1, short var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
      this.no_entry_value = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
   }

   public TObjectShortHashMap(int var1) {
      super(var1);
      this.PUT_ALL_PROC = new TObjectShortProcedure(this) {
         final TObjectShortHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(Object var1, short var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
      this.no_entry_value = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
   }

   public TObjectShortHashMap(int var1, float var2) {
      super(var1, var2);
      this.PUT_ALL_PROC = new TObjectShortProcedure(this) {
         final TObjectShortHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(Object var1, short var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
      this.no_entry_value = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
   }

   public TObjectShortHashMap(int var1, float var2, short var3) {
      super(var1, var2);
      this.PUT_ALL_PROC = new TObjectShortProcedure(this) {
         final TObjectShortHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(Object var1, short var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
      this.no_entry_value = var3;
      if (this.no_entry_value != 0) {
         Arrays.fill(this._values, this.no_entry_value);
      }

   }

   public TObjectShortHashMap(TObjectShortMap var1) {
      this(var1.size(), 0.5F, var1.getNoEntryValue());
      if (var1 instanceof TObjectShortHashMap) {
         TObjectShortHashMap var2 = (TObjectShortHashMap)var1;
         this._loadFactor = var2._loadFactor;
         this.no_entry_value = var2.no_entry_value;
         if (this.no_entry_value != 0) {
            Arrays.fill(this._values, this.no_entry_value);
         }

         this.setUp((int)Math.ceil((double)(10.0F / this._loadFactor)));
      }

      this.putAll(var1);
   }

   public int setUp(int var1) {
      int var2 = super.setUp(var1);
      this._values = new short[var2];
      return var2;
   }

   protected void rehash(int var1) {
      int var2 = this._set.length;
      Object[] var3 = (Object[])this._set;
      short[] var4 = this._values;
      this._set = new Object[var1];
      Arrays.fill(this._set, FREE);
      this._values = new short[var1];
      Arrays.fill(this._values, this.no_entry_value);
      int var5 = var2;

      while(var5-- > 0) {
         if (var3[var5] != FREE && var3[var5] != REMOVED) {
            Object var6 = var3[var5];
            int var7 = this.insertKey(var6);
            if (var7 < 0) {
               this.throwObjectContractViolation(this._set[-var7 - 1], var6);
            }

            this._set[var7] = var6;
            this._values[var7] = var4[var5];
         }
      }

   }

   public short getNoEntryValue() {
      return this.no_entry_value;
   }

   public boolean containsKey(Object var1) {
      return this.contains(var1);
   }

   public boolean containsValue(short var1) {
      Object[] var2 = this._set;
      short[] var3 = this._values;
      int var4 = var3.length;

      do {
         if (var4-- <= 0) {
            return false;
         }
      } while(var2[var4] == FREE || var2[var4] == REMOVED || var1 != var3[var4]);

      return true;
   }

   public short get(Object var1) {
      int var2 = this.index(var1);
      return var2 < 0 ? this.no_entry_value : this._values[var2];
   }

   public short put(Object var1, short var2) {
      int var3 = this.insertKey(var1);
      return this.doPut(var2, var3);
   }

   public short putIfAbsent(Object var1, short var2) {
      int var3 = this.insertKey(var1);
      return var3 < 0 ? this._values[-var3 - 1] : this.doPut(var2, var3);
   }

   private short doPut(short var1, int var2) {
      short var3 = this.no_entry_value;
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

   public short remove(Object var1) {
      short var2 = this.no_entry_value;
      int var3 = this.index(var1);
      if (var3 >= 0) {
         var2 = this._values[var3];
         this.removeAt(var3);
      }

      return var2;
   }

   protected void removeAt(int var1) {
      this._values[var1] = this.no_entry_value;
      super.removeAt(var1);
   }

   public void putAll(Map var1) {
      Set var2 = var1.entrySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         this.put(var4.getKey(), (Short)var4.getValue());
      }

   }

   public void putAll(TObjectShortMap var1) {
      var1.forEachEntry(this.PUT_ALL_PROC);
   }

   public void clear() {
      super.clear();
      Arrays.fill(this._set, 0, this._set.length, FREE);
      Arrays.fill(this._values, 0, this._values.length, this.no_entry_value);
   }

   public Set keySet() {
      return new TObjectShortHashMap.KeyView(this);
   }

   public Object[] keys() {
      Object[] var1 = (Object[])(new Object[this.size()]);
      Object[] var2 = this._set;
      int var3 = var2.length;
      int var4 = 0;

      while(var3-- > 0) {
         if (var2[var3] != FREE && var2[var3] != REMOVED) {
            var1[var4++] = var2[var3];
         }
      }

      return var1;
   }

   public Object[] keys(Object[] var1) {
      int var2 = this.size();
      if (var1.length < var2) {
         var1 = (Object[])((Object[])Array.newInstance(var1.getClass().getComponentType(), var2));
      }

      Object[] var3 = this._set;
      int var4 = var3.length;
      int var5 = 0;

      while(var4-- > 0) {
         if (var3[var4] != FREE && var3[var4] != REMOVED) {
            var1[var5++] = var3[var4];
         }
      }

      return var1;
   }

   public TShortCollection valueCollection() {
      return new TObjectShortHashMap.TShortValueCollection(this);
   }

   public short[] values() {
      short[] var1 = new short[this.size()];
      short[] var2 = this._values;
      Object[] var3 = this._set;
      int var4 = var2.length;
      int var5 = 0;

      while(var4-- > 0) {
         if (var3[var4] != FREE && var3[var4] != REMOVED) {
            var1[var5++] = var2[var4];
         }
      }

      return var1;
   }

   public short[] values(short[] var1) {
      int var2 = this.size();
      if (var1.length < var2) {
         var1 = new short[var2];
      }

      short[] var3 = this._values;
      Object[] var4 = this._set;
      int var5 = var3.length;
      int var6 = 0;

      while(var5-- > 0) {
         if (var4[var5] != FREE && var4[var5] != REMOVED) {
            var1[var6++] = var3[var5];
         }
      }

      if (var1.length > var2) {
         var1[var2] = this.no_entry_value;
      }

      return var1;
   }

   public TObjectShortIterator iterator() {
      return new TObjectShortHashMap.TObjectShortHashIterator(this, this);
   }

   public boolean increment(Object var1) {
      return this.adjustValue(var1, (short)1);
   }

   public boolean adjustValue(Object var1, short var2) {
      int var3 = this.index(var1);
      if (var3 < 0) {
         return false;
      } else {
         short[] var10000 = this._values;
         var10000[var3] += var2;
         return true;
      }
   }

   public short adjustOrPutValue(Object var1, short var2, short var3) {
      int var4 = this.insertKey(var1);
      boolean var5;
      short var6;
      if (var4 < 0) {
         var4 = -var4 - 1;
         short[] var10000 = this._values;
         var6 = var10000[var4] += var2;
         var5 = false;
      } else {
         var6 = this._values[var4] = var3;
         var5 = true;
      }

      if (var5) {
         this.postInsertHook(this.consumeFreeSlot);
      }

      return var6;
   }

   public boolean forEachKey(TObjectProcedure var1) {
      return this.forEach(var1);
   }

   public boolean forEachValue(TShortProcedure var1) {
      Object[] var2 = this._set;
      short[] var3 = this._values;
      int var4 = var3.length;

      do {
         if (var4-- <= 0) {
            return true;
         }
      } while(var2[var4] == FREE || var2[var4] == REMOVED || var1.execute(var3[var4]));

      return false;
   }

   public boolean forEachEntry(TObjectShortProcedure var1) {
      Object[] var2 = this._set;
      short[] var3 = this._values;
      int var4 = var2.length;

      do {
         if (var4-- <= 0) {
            return true;
         }
      } while(var2[var4] == FREE || var2[var4] == REMOVED || var1.execute(var2[var4], var3[var4]));

      return false;
   }

   public boolean retainEntries(TObjectShortProcedure var1) {
      boolean var2 = false;
      Object[] var3 = (Object[])this._set;
      short[] var4 = this._values;
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

   public void transformValues(TShortFunction var1) {
      Object[] var2 = this._set;
      short[] var3 = this._values;
      int var4 = var3.length;

      while(var4-- > 0) {
         if (var2[var4] != null && var2[var4] != REMOVED) {
            var3[var4] = var1.execute(var3[var4]);
         }
      }

   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TObjectShortMap)) {
         return false;
      } else {
         TObjectShortMap var2 = (TObjectShortMap)var1;
         if (var2.size() != this.size()) {
            return false;
         } else {
            try {
               TObjectShortIterator var3 = this.iterator();

               Object var4;
               label40:
               do {
                  short var5;
                  do {
                     if (!var3.hasNext()) {
                        return true;
                     }

                     var3.advance();
                     var4 = var3.key();
                     var5 = var3.value();
                     if (var5 == this.no_entry_value) {
                        continue label40;
                     }
                  } while(var5 == var2.get(var4));

                  return false;
               } while(var2.get(var4) == var2.getNoEntryValue() && var2.containsKey(var4));

               return false;
            } catch (ClassCastException var6) {
               return true;
            }
         }
      }
   }

   public int hashCode() {
      int var1 = 0;
      Object[] var2 = this._set;
      short[] var3 = this._values;
      int var4 = var3.length;

      while(var4-- > 0) {
         if (var2[var4] != FREE && var2[var4] != REMOVED) {
            var1 += HashFunctions.hash(var3[var4]) ^ (var2[var4] == null ? 0 : var2[var4].hashCode());
         }
      }

      return var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      super.writeExternal(var1);
      var1.writeShort(this.no_entry_value);
      var1.writeInt(this._size);
      int var2 = this._set.length;

      while(var2-- > 0) {
         if (this._set[var2] != REMOVED && this._set[var2] != FREE) {
            var1.writeObject(this._set[var2]);
            var1.writeShort(this._values[var2]);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      super.readExternal(var1);
      this.no_entry_value = var1.readShort();
      int var2 = var1.readInt();
      this.setUp(var2);

      while(var2-- > 0) {
         Object var3 = var1.readObject();
         short var4 = var1.readShort();
         this.put(var3, var4);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");
      this.forEachEntry(new TObjectShortProcedure(this, var1) {
         private boolean first;
         final StringBuilder val$buf;
         final TObjectShortHashMap this$0;

         {
            this.this$0 = var1;
            this.val$buf = var2;
            this.first = true;
         }

         public boolean execute(Object var1, short var2) {
            if (this.first) {
               this.first = false;
            } else {
               this.val$buf.append(",");
            }

            this.val$buf.append(var1).append("=").append(var2);
            return true;
         }
      });
      var1.append("}");
      return var1.toString();
   }

   static int access$100(TObjectShortHashMap var0) {
      return var0._size;
   }

   static int access$200(TObjectShortHashMap var0) {
      return var0._size;
   }

   class TObjectShortHashIterator extends TObjectHashIterator implements TObjectShortIterator {
      private final TObjectShortHashMap _map;
      final TObjectShortHashMap this$0;

      public TObjectShortHashIterator(TObjectShortHashMap var1, TObjectShortHashMap var2) {
         super(var2);
         this.this$0 = var1;
         this._map = var2;
      }

      public void advance() {
         this.moveToNextIndex();
      }

      public Object key() {
         return this._map._set[this._index];
      }

      public short value() {
         return this._map._values[this._index];
      }

      public short setValue(short var1) {
         short var2 = this.value();
         this._map._values[this._index] = var1;
         return var2;
      }
   }

   class TShortValueCollection implements TShortCollection {
      final TObjectShortHashMap this$0;

      TShortValueCollection(TObjectShortHashMap var1) {
         this.this$0 = var1;
      }

      public TShortIterator iterator() {
         return new TObjectShortHashMap.TShortValueCollection.TObjectShortValueHashIterator(this);
      }

      public short getNoEntryValue() {
         return this.this$0.no_entry_value;
      }

      public int size() {
         return TObjectShortHashMap.access$100(this.this$0);
      }

      public boolean isEmpty() {
         return 0 == TObjectShortHashMap.access$200(this.this$0);
      }

      public boolean contains(short var1) {
         return this.this$0.containsValue(var1);
      }

      public short[] toArray() {
         return this.this$0.values();
      }

      public short[] toArray(short[] var1) {
         return this.this$0.values(var1);
      }

      public boolean add(short var1) {
         throw new UnsupportedOperationException();
      }

      public boolean containsAll(Collection var1) {
         Iterator var2 = var1.iterator();

         short var4;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            Object var3 = var2.next();
            if (!(var3 instanceof Short)) {
               return false;
            }

            var4 = (Short)var3;
         } while(this.this$0.containsValue(var4));

         return false;
      }

      public boolean containsAll(TShortCollection var1) {
         TShortIterator var2 = var1.iterator();

         do {
            if (!var2.hasNext()) {
               return true;
            }
         } while(this.this$0.containsValue(var2.next()));

         return false;
      }

      public boolean containsAll(short[] var1) {
         short[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            short var5 = var2[var4];
            if (!this.this$0.containsValue(var5)) {
               return false;
            }
         }

         return true;
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(TShortCollection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(short[] var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection var1) {
         boolean var2 = false;
         TShortIterator var3 = this.iterator();

         while(var3.hasNext()) {
            if (!var1.contains(var3.next())) {
               var3.remove();
               var2 = true;
            }
         }

         return var2;
      }

      public boolean retainAll(TShortCollection var1) {
         if (this == var1) {
            return false;
         } else {
            boolean var2 = false;
            TShortIterator var3 = this.iterator();

            while(var3.hasNext()) {
               if (!var1.contains(var3.next())) {
                  var3.remove();
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean retainAll(short[] var1) {
         boolean var2 = false;
         Arrays.sort(var1);
         short[] var3 = this.this$0._values;
         Object[] var4 = this.this$0._set;
         int var5 = var4.length;

         while(var5-- > 0) {
            if (var4[var5] != TObjectHash.FREE && var4[var5] != TObjectHash.REMOVED && Arrays.binarySearch(var1, var3[var5]) < 0) {
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
            if (var4 instanceof Short) {
               short var5 = (Short)var4;
               if (var5 > 0) {
                  var2 = true;
               }
            }
         }

         return var2;
      }

      public boolean removeAll(TShortCollection var1) {
         if (this == var1) {
            this.clear();
            return true;
         } else {
            boolean var2 = false;
            TShortIterator var3 = var1.iterator();

            while(var3.hasNext()) {
               short var4 = var3.next();
               if (var4 > 0) {
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean removeAll(short[] var1) {
         boolean var2 = false;
         int var3 = var1.length;

         while(var3-- > 0) {
            if (var1[var3] > 0) {
               var2 = true;
            }
         }

         return var2;
      }

      public void clear() {
         this.this$0.clear();
      }

      public boolean forEach(TShortProcedure var1) {
         return this.this$0.forEachValue(var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("{");
         this.this$0.forEachValue(new TShortProcedure(this, var1) {
            private boolean first;
            final StringBuilder val$buf;
            final TObjectShortHashMap.TShortValueCollection this$1;

            {
               this.this$1 = var1;
               this.val$buf = var2;
               this.first = true;
            }

            public boolean execute(short var1) {
               if (this.first) {
                  this.first = false;
               } else {
                  this.val$buf.append(", ");
               }

               this.val$buf.append(var1);
               return true;
            }
         });
         var1.append("}");
         return var1.toString();
      }

      class TObjectShortValueHashIterator implements TShortIterator {
         protected THash _hash;
         protected int _expectedSize;
         protected int _index;
         final TObjectShortHashMap.TShortValueCollection this$1;

         TObjectShortValueHashIterator(TObjectShortHashMap.TShortValueCollection var1) {
            this.this$1 = var1;
            this._hash = this.this$1.this$0;
            this._expectedSize = this._hash.size();
            this._index = this._hash.capacity();
         }

         public boolean hasNext() {
            return this.nextIndex() >= 0;
         }

         public short next() {
            this.moveToNextIndex();
            return this.this$1.this$0._values[this._index];
         }

         public void remove() {
            if (this._expectedSize != this._hash.size()) {
               throw new ConcurrentModificationException();
            } else {
               this._hash.tempDisableAutoCompaction();
               this.this$1.this$0.removeAt(this._index);
               this._hash.reenableAutoCompaction(false);
               --this._expectedSize;
            }
         }

         protected final void moveToNextIndex() {
            if ((this._index = this.nextIndex()) < 0) {
               throw new NoSuchElementException();
            }
         }

         protected final int nextIndex() {
            if (this._expectedSize != this._hash.size()) {
               throw new ConcurrentModificationException();
            } else {
               Object[] var1 = this.this$1.this$0._set;
               int var2 = this._index;

               while(var2-- > 0 && (var1[var2] == TObjectHash.FREE || var1[var2] == TObjectHash.REMOVED)) {
               }

               return var2;
            }
         }
      }
   }

   private abstract class MapBackedView extends AbstractSet implements Set, Iterable {
      final TObjectShortHashMap this$0;

      private MapBackedView(TObjectShortHashMap var1) {
         this.this$0 = var1;
      }

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

      MapBackedView(TObjectShortHashMap var1, Object var2) {
         this(var1);
      }
   }

   protected class KeyView extends TObjectShortHashMap.MapBackedView {
      final TObjectShortHashMap this$0;

      protected KeyView(TObjectShortHashMap var1) {
         super(var1, null);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return new TObjectHashIterator(this.this$0);
      }

      public boolean removeElement(Object var1) {
         return this.this$0.no_entry_value != this.this$0.remove(var1);
      }

      public boolean containsElement(Object var1) {
         return this.this$0.contains(var1);
      }
   }
}
