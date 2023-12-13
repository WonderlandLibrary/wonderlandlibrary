package gnu.trove.map.hash;

import gnu.trove.TByteCollection;
import gnu.trove.function.TObjectFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TByteHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.iterator.TByteObjectIterator;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.procedure.TByteObjectProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TByteSet;
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

public class TByteObjectHashMap extends TByteHash implements TByteObjectMap, Externalizable {
   static final long serialVersionUID = 1L;
   private final TByteObjectProcedure PUT_ALL_PROC;
   protected transient Object[] _values;
   protected byte no_entry_key;

   public TByteObjectHashMap() {
      this.PUT_ALL_PROC = new TByteObjectProcedure(this) {
         final TByteObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(byte var1, Object var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
   }

   public TByteObjectHashMap(int var1) {
      super(var1);
      this.PUT_ALL_PROC = new TByteObjectProcedure(this) {
         final TByteObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(byte var1, Object var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
      this.no_entry_key = Constants.DEFAULT_BYTE_NO_ENTRY_VALUE;
   }

   public TByteObjectHashMap(int var1, float var2) {
      super(var1, var2);
      this.PUT_ALL_PROC = new TByteObjectProcedure(this) {
         final TByteObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(byte var1, Object var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
      this.no_entry_key = Constants.DEFAULT_BYTE_NO_ENTRY_VALUE;
   }

   public TByteObjectHashMap(int var1, float var2, byte var3) {
      super(var1, var2);
      this.PUT_ALL_PROC = new TByteObjectProcedure(this) {
         final TByteObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(byte var1, Object var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
      this.no_entry_key = var3;
   }

   public TByteObjectHashMap(TByteObjectMap var1) {
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
      byte[] var3 = this._set;
      Object[] var4 = this._values;
      byte[] var5 = this._states;
      this._set = new byte[var1];
      this._values = (Object[])(new Object[var1]);
      this._states = new byte[var1];
      int var6 = var2;

      while(var6-- > 0) {
         if (var5[var6] == 1) {
            byte var7 = var3[var6];
            int var8 = this.insertKey(var7);
            this._values[var8] = var4[var6];
         }
      }

   }

   public byte getNoEntryKey() {
      return this.no_entry_key;
   }

   public boolean containsKey(byte var1) {
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

   public Object get(byte var1) {
      int var2 = this.index(var1);
      return var2 < 0 ? null : this._values[var2];
   }

   public Object put(byte var1, Object var2) {
      int var3 = this.insertKey(var1);
      return this.doPut(var2, var3);
   }

   public Object putIfAbsent(byte var1, Object var2) {
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

   public Object remove(byte var1) {
      Object var2 = null;
      int var3 = this.index(var1);
      if (var3 >= 0) {
         var2 = this._values[var3];
         this.removeAt(var3);
      }

      return var2;
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
         this.put((Byte)var4.getKey(), var4.getValue());
      }

   }

   public void putAll(TByteObjectMap var1) {
      var1.forEachEntry(this.PUT_ALL_PROC);
   }

   public void clear() {
      super.clear();
      Arrays.fill(this._set, 0, this._set.length, this.no_entry_key);
      Arrays.fill(this._states, 0, this._states.length, (byte)0);
      Arrays.fill(this._values, 0, this._values.length, (Object)null);
   }

   public TByteSet keySet() {
      return new TByteObjectHashMap.KeyView(this);
   }

   public byte[] keys() {
      byte[] var1 = new byte[this.size()];
      byte[] var2 = this._set;
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

   public byte[] keys(byte[] var1) {
      if (var1.length < this._size) {
         var1 = new byte[this._size];
      }

      byte[] var2 = this._set;
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
      return new TByteObjectHashMap.ValueView(this);
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

   public TByteObjectIterator iterator() {
      return new TByteObjectHashMap.TByteObjectHashIterator(this, this);
   }

   public boolean forEachKey(TByteProcedure var1) {
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

   public boolean forEachEntry(TByteObjectProcedure var1) {
      byte[] var2 = this._states;
      byte[] var3 = this._set;
      Object[] var4 = this._values;
      int var5 = var3.length;

      do {
         if (var5-- <= 0) {
            return true;
         }
      } while(var2[var5] != 1 || var1.execute(var3[var5], var4[var5]));

      return false;
   }

   public boolean retainEntries(TByteObjectProcedure var1) {
      boolean var2 = false;
      byte[] var3 = this._states;
      byte[] var4 = this._set;
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
      if (!(var1 instanceof TByteObjectMap)) {
         return false;
      } else {
         TByteObjectMap var2 = (TByteObjectMap)var1;
         if (var2.size() != this.size()) {
            return false;
         } else {
            try {
               TByteObjectIterator var3 = this.iterator();

               byte var4;
               label40:
               do {
                  Object var5;
                  do {
                     if (!var3.hasNext()) {
                        return true;
                     }

                     var3.advance();
                     var4 = var3.key();
                     var5 = var3.value();
                     if (var5 == null) {
                        continue label40;
                     }
                  } while(var5.equals(var2.get(var4)));

                  return false;
               } while(var2.get(var4) == null && var2.containsKey(var4));

               return false;
            } catch (ClassCastException var6) {
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
      var1.writeByte(this.no_entry_key);
      var1.writeInt(this._size);
      int var2 = this._states.length;

      while(var2-- > 0) {
         if (this._states[var2] == 1) {
            var1.writeByte(this._set[var2]);
            var1.writeObject(this._values[var2]);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      super.readExternal(var1);
      this.no_entry_key = var1.readByte();
      int var2 = var1.readInt();
      this.setUp(var2);

      while(var2-- > 0) {
         byte var3 = var1.readByte();
         Object var4 = var1.readObject();
         this.put(var3, var4);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");
      this.forEachEntry(new TByteObjectProcedure(this, var1) {
         private boolean first;
         final StringBuilder val$buf;
         final TByteObjectHashMap this$0;

         {
            this.this$0 = var1;
            this.val$buf = var2;
            this.first = true;
         }

         public boolean execute(byte var1, Object var2) {
            if (this.first) {
               this.first = false;
            } else {
               this.val$buf.append(",");
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

   static int access$000(TByteObjectHashMap var0) {
      return var0._size;
   }

   static int access$100(TByteObjectHashMap var0) {
      return var0._size;
   }

   class TByteObjectHashIterator extends THashPrimitiveIterator implements TByteObjectIterator {
      private final TByteObjectHashMap _map;
      final TByteObjectHashMap this$0;

      public TByteObjectHashIterator(TByteObjectHashMap var1, TByteObjectHashMap var2) {
         super(var2);
         this.this$0 = var1;
         this._map = var2;
      }

      public void advance() {
         this.moveToNextIndex();
      }

      public byte key() {
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
      final TByteObjectHashMap this$0;

      private MapBackedView(TByteObjectHashMap var1) {
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

      MapBackedView(TByteObjectHashMap var1, Object var2) {
         this(var1);
      }
   }

   protected class ValueView extends TByteObjectHashMap.MapBackedView {
      final TByteObjectHashMap this$0;

      protected ValueView(TByteObjectHashMap var1) {
         super(var1, null);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return new TByteObjectHashMap.ValueView.TByteObjectValueHashIterator(this, this.this$0) {
            final TByteObjectHashMap.ValueView this$1;

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

      class TByteObjectValueHashIterator extends THashPrimitiveIterator implements Iterator {
         protected final TByteObjectHashMap _map;
         final TByteObjectHashMap.ValueView this$1;

         public TByteObjectValueHashIterator(TByteObjectHashMap.ValueView var1, TByteObjectHashMap var2) {
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

   class KeyView implements TByteSet {
      final TByteObjectHashMap this$0;

      KeyView(TByteObjectHashMap var1) {
         this.this$0 = var1;
      }

      public byte getNoEntryValue() {
         return this.this$0.no_entry_key;
      }

      public int size() {
         return TByteObjectHashMap.access$000(this.this$0);
      }

      public boolean isEmpty() {
         return TByteObjectHashMap.access$100(this.this$0) == 0;
      }

      public boolean contains(byte var1) {
         return this.this$0.containsKey(var1);
      }

      public TByteIterator iterator() {
         return new TByteObjectHashMap.KeyView.TByteHashIterator(this, this.this$0);
      }

      public byte[] toArray() {
         return this.this$0.keys();
      }

      public byte[] toArray(byte[] var1) {
         return this.this$0.keys(var1);
      }

      public boolean add(byte var1) {
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
         } while(this.this$0.containsKey((Byte)var3));

         return false;
      }

      public boolean containsAll(TByteCollection var1) {
         if (var1 == this) {
            return true;
         } else {
            TByteIterator var2 = var1.iterator();

            do {
               if (!var2.hasNext()) {
                  return true;
               }
            } while(this.this$0.containsKey(var2.next()));

            return false;
         }
      }

      public boolean containsAll(byte[] var1) {
         byte[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte var5 = var2[var4];
            if (!this.this$0.containsKey(var5)) {
               return false;
            }
         }

         return true;
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(TByteCollection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(byte[] var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection var1) {
         boolean var2 = false;
         TByteIterator var3 = this.iterator();

         while(var3.hasNext()) {
            if (!var1.contains(var3.next())) {
               var3.remove();
               var2 = true;
            }
         }

         return var2;
      }

      public boolean retainAll(TByteCollection var1) {
         if (this == var1) {
            return false;
         } else {
            boolean var2 = false;
            TByteIterator var3 = this.iterator();

            while(var3.hasNext()) {
               if (!var1.contains(var3.next())) {
                  var3.remove();
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean retainAll(byte[] var1) {
         boolean var2 = false;
         Arrays.sort(var1);
         byte[] var3 = this.this$0._set;
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
            if (var4 instanceof Byte) {
               byte var5 = (Byte)var4;
               if (this != var5) {
                  var2 = true;
               }
            }
         }

         return var2;
      }

      public boolean removeAll(TByteCollection var1) {
         if (var1 == this) {
            this.clear();
            return true;
         } else {
            boolean var2 = false;
            TByteIterator var3 = var1.iterator();

            while(var3.hasNext()) {
               byte var4 = var3.next();
               if (this != var4) {
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean removeAll(byte[] var1) {
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

      public boolean forEach(TByteProcedure var1) {
         return this.this$0.forEachKey(var1);
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof TByteSet)) {
            return false;
         } else {
            TByteSet var2 = (TByteSet)var1;
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

      class TByteHashIterator extends THashPrimitiveIterator implements TByteIterator {
         private final TByteHash _hash;
         final TByteObjectHashMap.KeyView this$1;

         public TByteHashIterator(TByteObjectHashMap.KeyView var1, TByteHash var2) {
            super(var2);
            this.this$1 = var1;
            this._hash = var2;
         }

         public byte next() {
            this.moveToNextIndex();
            return this._hash._set[this._index];
         }
      }
   }
}
