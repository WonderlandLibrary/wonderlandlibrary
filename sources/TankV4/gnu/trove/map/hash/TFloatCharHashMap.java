package gnu.trove.map.hash;

import gnu.trove.TCharCollection;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.iterator.TFloatCharIterator;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.map.TFloatCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TFloatCharHashMap extends TFloatCharHash implements TFloatCharMap, Externalizable {
   static final long serialVersionUID = 1L;
   protected transient char[] _values;

   public TFloatCharHashMap() {
   }

   public TFloatCharHashMap(int var1) {
      super(var1);
   }

   public TFloatCharHashMap(int var1, float var2) {
      super(var1, var2);
   }

   public TFloatCharHashMap(int var1, float var2, float var3, char var4) {
      super(var1, var2, var3, var4);
   }

   public TFloatCharHashMap(float[] var1, char[] var2) {
      super(Math.max(var1.length, var2.length));
      int var3 = Math.min(var1.length, var2.length);

      for(int var4 = 0; var4 < var3; ++var4) {
         this.put(var1[var4], var2[var4]);
      }

   }

   public TFloatCharHashMap(TFloatCharMap var1) {
      super(var1.size());
      if (var1 instanceof TFloatCharHashMap) {
         TFloatCharHashMap var2 = (TFloatCharHashMap)var1;
         this._loadFactor = var2._loadFactor;
         this.no_entry_key = var2.no_entry_key;
         this.no_entry_value = var2.no_entry_value;
         if (this.no_entry_key != 0.0F) {
            Arrays.fill(this._set, this.no_entry_key);
         }

         if (this.no_entry_value != 0) {
            Arrays.fill(this._values, this.no_entry_value);
         }

         this.setUp((int)Math.ceil((double)(10.0F / this._loadFactor)));
      }

      this.putAll(var1);
   }

   protected int setUp(int var1) {
      int var2 = super.setUp(var1);
      this._values = new char[var2];
      return var2;
   }

   protected void rehash(int var1) {
      int var2 = this._set.length;
      float[] var3 = this._set;
      char[] var4 = this._values;
      byte[] var5 = this._states;
      this._set = new float[var1];
      this._values = new char[var1];
      this._states = new byte[var1];
      int var6 = var2;

      while(var6-- > 0) {
         if (var5[var6] == 1) {
            float var7 = var3[var6];
            int var8 = this.insertKey(var7);
            this._values[var8] = var4[var6];
         }
      }

   }

   public char put(float var1, char var2) {
      int var3 = this.insertKey(var1);
      return this.doPut(var1, var2, var3);
   }

   public char putIfAbsent(float var1, char var2) {
      int var3 = this.insertKey(var1);
      return var3 < 0 ? this._values[-var3 - 1] : this.doPut(var1, var2, var3);
   }

   private char doPut(float var1, char var2, int var3) {
      char var4 = this.no_entry_value;
      boolean var5 = true;
      if (var3 < 0) {
         var3 = -var3 - 1;
         var4 = this._values[var3];
         var5 = false;
      }

      this._values[var3] = var2;
      if (var5) {
         this.postInsertHook(this.consumeFreeSlot);
      }

      return var4;
   }

   public void putAll(Map var1) {
      this.ensureCapacity(var1.size());
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put((Float)var3.getKey(), (Character)var3.getValue());
      }

   }

   public void putAll(TFloatCharMap var1) {
      this.ensureCapacity(var1.size());
      TFloatCharIterator var2 = var1.iterator();

      while(var2.hasNext()) {
         var2.advance();
         this.put(var2.key(), var2.value());
      }

   }

   public char get(float var1) {
      int var2 = this.index(var1);
      return var2 < 0 ? this.no_entry_value : this._values[var2];
   }

   public void clear() {
      super.clear();
      Arrays.fill(this._set, 0, this._set.length, this.no_entry_key);
      Arrays.fill(this._values, 0, this._values.length, this.no_entry_value);
      Arrays.fill(this._states, 0, this._states.length, (byte)0);
   }

   public boolean isEmpty() {
      return 0 == this._size;
   }

   public char remove(float var1) {
      char var2 = this.no_entry_value;
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

   public TFloatSet keySet() {
      return new TFloatCharHashMap.TKeyView(this);
   }

   public float[] keys() {
      float[] var1 = new float[this.size()];
      float[] var2 = this._set;
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

   public float[] keys(float[] var1) {
      int var2 = this.size();
      if (var1.length < var2) {
         var1 = new float[var2];
      }

      float[] var3 = this._set;
      byte[] var4 = this._states;
      int var5 = var3.length;
      int var6 = 0;

      while(var5-- > 0) {
         if (var4[var5] == 1) {
            var1[var6++] = var3[var5];
         }
      }

      return var1;
   }

   public TCharCollection valueCollection() {
      return new TFloatCharHashMap.TValueView(this);
   }

   public char[] values() {
      char[] var1 = new char[this.size()];
      char[] var2 = this._values;
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

   public char[] values(char[] var1) {
      int var2 = this.size();
      if (var1.length < var2) {
         var1 = new char[var2];
      }

      char[] var3 = this._values;
      byte[] var4 = this._states;
      int var5 = var3.length;
      int var6 = 0;

      while(var5-- > 0) {
         if (var4[var5] == 1) {
            var1[var6++] = var3[var5];
         }
      }

      return var1;
   }

   public boolean containsValue(char var1) {
      byte[] var2 = this._states;
      char[] var3 = this._values;
      int var4 = var3.length;

      do {
         if (var4-- <= 0) {
            return false;
         }
      } while(var2[var4] != 1 || var1 != var3[var4]);

      return true;
   }

   public boolean containsKey(float var1) {
      return this.contains(var1);
   }

   public TFloatCharIterator iterator() {
      return new TFloatCharHashMap.TFloatCharHashIterator(this, this);
   }

   public boolean forEachKey(TFloatProcedure var1) {
      return this.forEach(var1);
   }

   public boolean forEachValue(TCharProcedure var1) {
      byte[] var2 = this._states;
      char[] var3 = this._values;
      int var4 = var3.length;

      do {
         if (var4-- <= 0) {
            return true;
         }
      } while(var2[var4] != 1 || var1.execute(var3[var4]));

      return false;
   }

   public boolean forEachEntry(TFloatCharProcedure var1) {
      byte[] var2 = this._states;
      float[] var3 = this._set;
      char[] var4 = this._values;
      int var5 = var3.length;

      do {
         if (var5-- <= 0) {
            return true;
         }
      } while(var2[var5] != 1 || var1.execute(var3[var5], var4[var5]));

      return false;
   }

   public void transformValues(TCharFunction var1) {
      byte[] var2 = this._states;
      char[] var3 = this._values;
      int var4 = var3.length;

      while(var4-- > 0) {
         if (var2[var4] == 1) {
            var3[var4] = var1.execute(var3[var4]);
         }
      }

   }

   public boolean retainEntries(TFloatCharProcedure var1) {
      boolean var2 = false;
      byte[] var3 = this._states;
      float[] var4 = this._set;
      char[] var5 = this._values;
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

   public boolean increment(float var1) {
      return this.adjustValue(var1, '\u0001');
   }

   public boolean adjustValue(float var1, char var2) {
      int var3 = this.index(var1);
      if (var3 < 0) {
         return false;
      } else {
         char[] var10000 = this._values;
         var10000[var3] += var2;
         return true;
      }
   }

   public char adjustOrPutValue(float var1, char var2, char var3) {
      int var4 = this.insertKey(var1);
      boolean var5;
      char var6;
      if (var4 < 0) {
         var4 = -var4 - 1;
         char[] var10000 = this._values;
         var6 = var10000[var4] += var2;
         var5 = false;
      } else {
         var6 = this._values[var4] = var3;
         var5 = true;
      }

      byte var8 = this._states[var4];
      if (var5) {
         this.postInsertHook(this.consumeFreeSlot);
      }

      return var6;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TFloatCharMap)) {
         return false;
      } else {
         TFloatCharMap var2 = (TFloatCharMap)var1;
         if (var2.size() != this.size()) {
            return false;
         } else {
            char[] var3 = this._values;
            byte[] var4 = this._states;
            char var5 = this.getNoEntryValue();
            char var6 = var2.getNoEntryValue();
            int var7 = var3.length;

            while(var7-- > 0) {
               if (var4[var7] == 1) {
                  float var8 = this._set[var7];
                  char var9 = var2.get(var8);
                  char var10 = var3[var7];
                  if (var10 != var9 && var10 != var5 && var9 != var6) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int var1 = 0;
      byte[] var2 = this._states;
      int var3 = this._values.length;

      while(var3-- > 0) {
         if (var2[var3] == 1) {
            var1 += HashFunctions.hash(this._set[var3]) ^ HashFunctions.hash(this._values[var3]);
         }
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");
      this.forEachEntry(new TFloatCharProcedure(this, var1) {
         private boolean first;
         final StringBuilder val$buf;
         final TFloatCharHashMap this$0;

         {
            this.this$0 = var1;
            this.val$buf = var2;
            this.first = true;
         }

         public boolean execute(float var1, char var2) {
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

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      super.writeExternal(var1);
      var1.writeInt(this._size);
      int var2 = this._states.length;

      while(var2-- > 0) {
         if (this._states[var2] == 1) {
            var1.writeFloat(this._set[var2]);
            var1.writeChar(this._values[var2]);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      super.readExternal(var1);
      int var2 = var1.readInt();
      this.setUp(var2);

      while(var2-- > 0) {
         float var3 = var1.readFloat();
         char var4 = var1.readChar();
         this.put(var3, var4);
      }

   }

   static float access$000(TFloatCharHashMap var0) {
      return var0.no_entry_key;
   }

   static int access$100(TFloatCharHashMap var0) {
      return var0._size;
   }

   static int access$200(TFloatCharHashMap var0) {
      return var0._size;
   }

   static char access$300(TFloatCharHashMap var0) {
      return var0.no_entry_value;
   }

   static char access$400(TFloatCharHashMap var0) {
      return var0.no_entry_value;
   }

   static int access$500(TFloatCharHashMap var0) {
      return var0._size;
   }

   static int access$600(TFloatCharHashMap var0) {
      return var0._size;
   }

   class TFloatCharHashIterator extends THashPrimitiveIterator implements TFloatCharIterator {
      final TFloatCharHashMap this$0;

      TFloatCharHashIterator(TFloatCharHashMap var1, TFloatCharHashMap var2) {
         super(var2);
         this.this$0 = var1;
      }

      public void advance() {
         this.moveToNextIndex();
      }

      public float key() {
         return this.this$0._set[this._index];
      }

      public char value() {
         return this.this$0._values[this._index];
      }

      public char setValue(char var1) {
         char var2 = this.value();
         this.this$0._values[this._index] = var1;
         return var2;
      }

      public void remove() {
         if (this._expectedSize != this._hash.size()) {
            throw new ConcurrentModificationException();
         } else {
            this._hash.tempDisableAutoCompaction();
            this.this$0.removeAt(this._index);
            this._hash.reenableAutoCompaction(false);
            --this._expectedSize;
         }
      }
   }

   class TFloatCharValueHashIterator extends THashPrimitiveIterator implements TCharIterator {
      final TFloatCharHashMap this$0;

      TFloatCharValueHashIterator(TFloatCharHashMap var1, TPrimitiveHash var2) {
         super(var2);
         this.this$0 = var1;
      }

      public char next() {
         this.moveToNextIndex();
         return this.this$0._values[this._index];
      }

      public void remove() {
         if (this._expectedSize != this._hash.size()) {
            throw new ConcurrentModificationException();
         } else {
            this._hash.tempDisableAutoCompaction();
            this.this$0.removeAt(this._index);
            this._hash.reenableAutoCompaction(false);
            --this._expectedSize;
         }
      }
   }

   class TFloatCharKeyHashIterator extends THashPrimitiveIterator implements TFloatIterator {
      final TFloatCharHashMap this$0;

      TFloatCharKeyHashIterator(TFloatCharHashMap var1, TPrimitiveHash var2) {
         super(var2);
         this.this$0 = var1;
      }

      public float next() {
         this.moveToNextIndex();
         return this.this$0._set[this._index];
      }

      public void remove() {
         if (this._expectedSize != this._hash.size()) {
            throw new ConcurrentModificationException();
         } else {
            this._hash.tempDisableAutoCompaction();
            this.this$0.removeAt(this._index);
            this._hash.reenableAutoCompaction(false);
            --this._expectedSize;
         }
      }
   }

   protected class TValueView implements TCharCollection {
      final TFloatCharHashMap this$0;

      protected TValueView(TFloatCharHashMap var1) {
         this.this$0 = var1;
      }

      public TCharIterator iterator() {
         return this.this$0.new TFloatCharValueHashIterator(this.this$0, this.this$0);
      }

      public char getNoEntryValue() {
         return TFloatCharHashMap.access$400(this.this$0);
      }

      public int size() {
         return TFloatCharHashMap.access$500(this.this$0);
      }

      public boolean isEmpty() {
         return 0 == TFloatCharHashMap.access$600(this.this$0);
      }

      public boolean contains(char var1) {
         return this.this$0.containsValue(var1);
      }

      public char[] toArray() {
         return this.this$0.values();
      }

      public char[] toArray(char[] var1) {
         return this.this$0.values(var1);
      }

      public boolean add(char var1) {
         throw new UnsupportedOperationException();
      }

      public boolean containsAll(Collection var1) {
         Iterator var2 = var1.iterator();

         char var4;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            Object var3 = var2.next();
            if (!(var3 instanceof Character)) {
               return false;
            }

            var4 = (Character)var3;
         } while(this.this$0.containsValue(var4));

         return false;
      }

      public boolean containsAll(TCharCollection var1) {
         TCharIterator var2 = var1.iterator();

         do {
            if (!var2.hasNext()) {
               return true;
            }
         } while(this.this$0.containsValue(var2.next()));

         return false;
      }

      public boolean containsAll(char[] var1) {
         char[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            char var5 = var2[var4];
            if (!this.this$0.containsValue(var5)) {
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
         char[] var3 = this.this$0._values;
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
               if (var5 > 0) {
                  var2 = true;
               }
            }
         }

         return var2;
      }

      public boolean removeAll(TCharCollection var1) {
         if (this == var1) {
            this.clear();
            return true;
         } else {
            boolean var2 = false;
            TCharIterator var3 = var1.iterator();

            while(var3.hasNext()) {
               char var4 = var3.next();
               if (var4 > 0) {
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
            if (var1[var3] > 0) {
               var2 = true;
            }
         }

         return var2;
      }

      public void clear() {
         this.this$0.clear();
      }

      public boolean forEach(TCharProcedure var1) {
         return this.this$0.forEachValue(var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("{");
         this.this$0.forEachValue(new TCharProcedure(this, var1) {
            private boolean first;
            final StringBuilder val$buf;
            final TFloatCharHashMap.TValueView this$1;

            {
               this.this$1 = var1;
               this.val$buf = var2;
               this.first = true;
            }

            public boolean execute(char var1) {
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
   }

   protected class TKeyView implements TFloatSet {
      final TFloatCharHashMap this$0;

      protected TKeyView(TFloatCharHashMap var1) {
         this.this$0 = var1;
      }

      public TFloatIterator iterator() {
         return this.this$0.new TFloatCharKeyHashIterator(this.this$0, this.this$0);
      }

      public float getNoEntryValue() {
         return TFloatCharHashMap.access$000(this.this$0);
      }

      public int size() {
         return TFloatCharHashMap.access$100(this.this$0);
      }

      public boolean isEmpty() {
         return 0 == TFloatCharHashMap.access$200(this.this$0);
      }

      public boolean contains(float var1) {
         return this.this$0.contains(var1);
      }

      public float[] toArray() {
         return this.this$0.keys();
      }

      public float[] toArray(float[] var1) {
         return this.this$0.keys(var1);
      }

      public boolean add(float var1) {
         throw new UnsupportedOperationException();
      }

      public boolean containsAll(Collection var1) {
         Iterator var2 = var1.iterator();

         float var4;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            Object var3 = var2.next();
            if (!(var3 instanceof Float)) {
               return false;
            }

            var4 = (Float)var3;
         } while(this.this$0.containsKey(var4));

         return false;
      }

      public boolean containsAll(TFloatCollection var1) {
         TFloatIterator var2 = var1.iterator();

         do {
            if (!var2.hasNext()) {
               return true;
            }
         } while(this.this$0.containsKey(var2.next()));

         return false;
      }

      public boolean containsAll(float[] var1) {
         float[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            float var5 = var2[var4];
            if (!this.this$0.contains(var5)) {
               return false;
            }
         }

         return true;
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(TFloatCollection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(float[] var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection var1) {
         boolean var2 = false;
         TFloatIterator var3 = this.iterator();

         while(var3.hasNext()) {
            if (!var1.contains(var3.next())) {
               var3.remove();
               var2 = true;
            }
         }

         return var2;
      }

      public boolean retainAll(TFloatCollection var1) {
         if (this == var1) {
            return false;
         } else {
            boolean var2 = false;
            TFloatIterator var3 = this.iterator();

            while(var3.hasNext()) {
               if (!var1.contains(var3.next())) {
                  var3.remove();
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean retainAll(float[] var1) {
         boolean var2 = false;
         Arrays.sort(var1);
         float[] var3 = this.this$0._set;
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
            if (var4 instanceof Float) {
               float var5 = (Float)var4;
               if (this != var5) {
                  var2 = true;
               }
            }
         }

         return var2;
      }

      public boolean removeAll(TFloatCollection var1) {
         if (this == var1) {
            this.clear();
            return true;
         } else {
            boolean var2 = false;
            TFloatIterator var3 = var1.iterator();

            while(var3.hasNext()) {
               float var4 = var3.next();
               if (this != var4) {
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean removeAll(float[] var1) {
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

      public boolean forEach(TFloatProcedure var1) {
         return this.this$0.forEachKey(var1);
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof TFloatSet)) {
            return false;
         } else {
            TFloatSet var2 = (TFloatSet)var1;
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
         this.this$0.forEachKey(new TFloatProcedure(this, var1) {
            private boolean first;
            final StringBuilder val$buf;
            final TFloatCharHashMap.TKeyView this$1;

            {
               this.this$1 = var1;
               this.val$buf = var2;
               this.first = true;
            }

            public boolean execute(float var1) {
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
   }
}
