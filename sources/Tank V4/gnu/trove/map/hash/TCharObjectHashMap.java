package gnu.trove.map.hash;

import gnu.trove.TCharCollection;
import gnu.trove.function.TObjectFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.iterator.TCharObjectIterator;
import gnu.trove.map.TCharObjectMap;
import gnu.trove.procedure.TCharObjectProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TCharSet;
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

public class TCharObjectHashMap extends TCharHash implements TCharObjectMap, Externalizable {
   static final long serialVersionUID = 1L;
   private final TCharObjectProcedure PUT_ALL_PROC;
   protected transient Object[] _values;
   protected char no_entry_key;

   public TCharObjectHashMap() {
      this.PUT_ALL_PROC = new TCharObjectProcedure(this) {
         final TCharObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(char var1, Object var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
   }

   public TCharObjectHashMap(int var1) {
      super(var1);
      this.PUT_ALL_PROC = new TCharObjectProcedure(this) {
         final TCharObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(char var1, Object var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
      this.no_entry_key = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
   }

   public TCharObjectHashMap(int var1, float var2) {
      super(var1, var2);
      this.PUT_ALL_PROC = new TCharObjectProcedure(this) {
         final TCharObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(char var1, Object var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
      this.no_entry_key = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
   }

   public TCharObjectHashMap(int var1, float var2, char var3) {
      super(var1, var2);
      this.PUT_ALL_PROC = new TCharObjectProcedure(this) {
         final TCharObjectHashMap this$0;

         {
            this.this$0 = var1;
         }

         public boolean execute(char var1, Object var2) {
            this.this$0.put(var1, var2);
            return true;
         }
      };
      this.no_entry_key = var3;
   }

   public TCharObjectHashMap(TCharObjectMap var1) {
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
      char[] var3 = this._set;
      Object[] var4 = this._values;
      byte[] var5 = this._states;
      this._set = new char[var1];
      this._values = (Object[])(new Object[var1]);
      this._states = new byte[var1];
      int var6 = var2;

      while(var6-- > 0) {
         if (var5[var6] == 1) {
            char var7 = var3[var6];
            int var8 = this.insertKey(var7);
            this._values[var8] = var4[var6];
         }
      }

   }

   public char getNoEntryKey() {
      return this.no_entry_key;
   }

   public boolean containsKey(char var1) {
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

   public Object get(char var1) {
      int var2 = this.index(var1);
      return var2 < 0 ? null : this._values[var2];
   }

   public Object put(char var1, Object var2) {
      int var3 = this.insertKey(var1);
      return this.doPut(var2, var3);
   }

   public Object putIfAbsent(char var1, Object var2) {
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

   public Object remove(char var1) {
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
         this.put((Character)var4.getKey(), var4.getValue());
      }

   }

   public void putAll(TCharObjectMap var1) {
      var1.forEachEntry(this.PUT_ALL_PROC);
   }

   public void clear() {
      super.clear();
      Arrays.fill(this._set, 0, this._set.length, this.no_entry_key);
      Arrays.fill(this._states, 0, this._states.length, (byte)0);
      Arrays.fill(this._values, 0, this._values.length, (Object)null);
   }

   public TCharSet keySet() {
      return new TCharObjectHashMap.KeyView(this);
   }

   public char[] keys() {
      char[] var1 = new char[this.size()];
      char[] var2 = this._set;
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

   public char[] keys(char[] var1) {
      if (var1.length < this._size) {
         var1 = new char[this._size];
      }

      char[] var2 = this._set;
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
      return new TCharObjectHashMap.ValueView(this);
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

   public TCharObjectIterator iterator() {
      return new TCharObjectHashMap.TCharObjectHashIterator(this, this);
   }

   public boolean forEachKey(TCharProcedure var1) {
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

   public boolean forEachEntry(TCharObjectProcedure var1) {
      byte[] var2 = this._states;
      char[] var3 = this._set;
      Object[] var4 = this._values;
      int var5 = var3.length;

      do {
         if (var5-- <= 0) {
            return true;
         }
      } while(var2[var5] != 1 || var1.execute(var3[var5], var4[var5]));

      return false;
   }

   public boolean retainEntries(TCharObjectProcedure var1) {
      boolean var2 = false;
      byte[] var3 = this._states;
      char[] var4 = this._set;
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
      if (!(var1 instanceof TCharObjectMap)) {
         return false;
      } else {
         TCharObjectMap var2 = (TCharObjectMap)var1;
         if (var2.size() != this.size()) {
            return false;
         } else {
            try {
               TCharObjectIterator var3 = this.iterator();

               char var4;
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
      var1.writeChar(this.no_entry_key);
      var1.writeInt(this._size);
      int var2 = this._states.length;

      while(var2-- > 0) {
         if (this._states[var2] == 1) {
            var1.writeChar(this._set[var2]);
            var1.writeObject(this._values[var2]);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      super.readExternal(var1);
      this.no_entry_key = var1.readChar();
      int var2 = var1.readInt();
      this.setUp(var2);

      while(var2-- > 0) {
         char var3 = var1.readChar();
         Object var4 = var1.readObject();
         this.put(var3, var4);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");
      this.forEachEntry(new TCharObjectProcedure(this, var1) {
         private boolean first;
         final StringBuilder val$buf;
         final TCharObjectHashMap this$0;

         {
            this.this$0 = var1;
            this.val$buf = var2;
            this.first = true;
         }

         public boolean execute(char var1, Object var2) {
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

   static int access$000(TCharObjectHashMap var0) {
      return var0._size;
   }

   static int access$100(TCharObjectHashMap var0) {
      return var0._size;
   }

   class TCharObjectHashIterator extends THashPrimitiveIterator implements TCharObjectIterator {
      private final TCharObjectHashMap _map;
      final TCharObjectHashMap this$0;

      public TCharObjectHashIterator(TCharObjectHashMap var1, TCharObjectHashMap var2) {
         super(var2);
         this.this$0 = var1;
         this._map = var2;
      }

      public void advance() {
         this.moveToNextIndex();
      }

      public char key() {
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
      final TCharObjectHashMap this$0;

      private MapBackedView(TCharObjectHashMap var1) {
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

      MapBackedView(TCharObjectHashMap var1, Object var2) {
         this(var1);
      }
   }

   protected class ValueView extends TCharObjectHashMap.MapBackedView {
      final TCharObjectHashMap this$0;

      protected ValueView(TCharObjectHashMap var1) {
         super(var1, null);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return new TCharObjectHashMap.ValueView.TCharObjectValueHashIterator(this, this.this$0) {
            final TCharObjectHashMap.ValueView this$1;

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

      class TCharObjectValueHashIterator extends THashPrimitiveIterator implements Iterator {
         protected final TCharObjectHashMap _map;
         final TCharObjectHashMap.ValueView this$1;

         public TCharObjectValueHashIterator(TCharObjectHashMap.ValueView var1, TCharObjectHashMap var2) {
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

   class KeyView implements TCharSet {
      final TCharObjectHashMap this$0;

      KeyView(TCharObjectHashMap var1) {
         this.this$0 = var1;
      }

      public char getNoEntryValue() {
         return this.this$0.no_entry_key;
      }

      public int size() {
         return TCharObjectHashMap.access$000(this.this$0);
      }

      public boolean isEmpty() {
         return TCharObjectHashMap.access$100(this.this$0) == 0;
      }

      public boolean contains(char var1) {
         return this.this$0.containsKey(var1);
      }

      public TCharIterator iterator() {
         return new TCharObjectHashMap.KeyView.TCharHashIterator(this, this.this$0);
      }

      public char[] toArray() {
         return this.this$0.keys();
      }

      public char[] toArray(char[] var1) {
         return this.this$0.keys(var1);
      }

      public boolean add(char var1) {
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
         } while(this.this$0.containsKey((Character)var3));

         return false;
      }

      public boolean containsAll(TCharCollection var1) {
         if (var1 == this) {
            return true;
         } else {
            TCharIterator var2 = var1.iterator();

            do {
               if (!var2.hasNext()) {
                  return true;
               }
            } while(this.this$0.containsKey(var2.next()));

            return false;
         }
      }

      public boolean containsAll(char[] var1) {
         char[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            char var5 = var2[var4];
            if (!this.this$0.containsKey(var5)) {
               return false;
            }
         }

         return true;
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(TCharCollection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(char[] var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection var1) {
         boolean var2 = false;
         TCharIterator var3 = this.iterator();

         while(var3.hasNext()) {
            if (!var1.contains(var3.next())) {
               var3.remove();
               var2 = true;
            }
         }

         return var2;
      }

      public boolean retainAll(TCharCollection var1) {
         if (this == var1) {
            return false;
         } else {
            boolean var2 = false;
            TCharIterator var3 = this.iterator();

            while(var3.hasNext()) {
               if (!var1.contains(var3.next())) {
                  var3.remove();
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean retainAll(char[] var1) {
         boolean var2 = false;
         Arrays.sort(var1);
         char[] var3 = this.this$0._set;
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
            if (var4 instanceof Character) {
               char var5 = (Character)var4;
               if (this != var5) {
                  var2 = true;
               }
            }
         }

         return var2;
      }

      public boolean removeAll(TCharCollection var1) {
         if (var1 == this) {
            this.clear();
            return true;
         } else {
            boolean var2 = false;
            TCharIterator var3 = var1.iterator();

            while(var3.hasNext()) {
               char var4 = var3.next();
               if (this != var4) {
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean removeAll(char[] var1) {
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

      public boolean forEach(TCharProcedure var1) {
         return this.this$0.forEachKey(var1);
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof TCharSet)) {
            return false;
         } else {
            TCharSet var2 = (TCharSet)var1;
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

      class TCharHashIterator extends THashPrimitiveIterator implements TCharIterator {
         private final TCharHash _hash;
         final TCharObjectHashMap.KeyView this$1;

         public TCharHashIterator(TCharObjectHashMap.KeyView var1, TCharHash var2) {
            super(var2);
            this.this$1 = var1;
            this._hash = var2;
         }

         public char next() {
            this.moveToNextIndex();
            return this._hash._set[this._index];
         }
      }
   }
}
