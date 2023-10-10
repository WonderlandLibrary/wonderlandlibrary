package gnu.trove.map.hash;

import gnu.trove.TCharCollection;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCharDoubleHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TCharDoubleIterator;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.map.TCharDoubleMap;
import gnu.trove.procedure.TCharDoubleProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TCharSet;
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

public class TCharDoubleHashMap extends TCharDoubleHash implements TCharDoubleMap, Externalizable {
   static final long serialVersionUID = 1L;
   protected transient double[] _values;

   public TCharDoubleHashMap() {
   }

   public TCharDoubleHashMap(int var1) {
      super(var1);
   }

   public TCharDoubleHashMap(int var1, float var2) {
      super(var1, var2);
   }

   public TCharDoubleHashMap(int var1, float var2, char var3, double var4) {
      super(var1, var2, var3, var4);
   }

   public TCharDoubleHashMap(char[] var1, double[] var2) {
      super(Math.max(var1.length, var2.length));
      int var3 = Math.min(var1.length, var2.length);

      for(int var4 = 0; var4 < var3; ++var4) {
         this.put(var1[var4], var2[var4]);
      }

   }

   public TCharDoubleHashMap(TCharDoubleMap var1) {
      super(var1.size());
      if (var1 instanceof TCharDoubleHashMap) {
         TCharDoubleHashMap var2 = (TCharDoubleHashMap)var1;
         this._loadFactor = var2._loadFactor;
         this.no_entry_key = var2.no_entry_key;
         this.no_entry_value = var2.no_entry_value;
         if (this.no_entry_key != 0) {
            Arrays.fill(this._set, this.no_entry_key);
         }

         if (this.no_entry_value != 0.0D) {
            Arrays.fill(this._values, this.no_entry_value);
         }

         this.setUp((int)Math.ceil((double)(10.0F / this._loadFactor)));
      }

      this.putAll(var1);
   }

   protected int setUp(int var1) {
      int var2 = super.setUp(var1);
      this._values = new double[var2];
      return var2;
   }

   protected void rehash(int var1) {
      int var2 = this._set.length;
      char[] var3 = this._set;
      double[] var4 = this._values;
      byte[] var5 = this._states;
      this._set = new char[var1];
      this._values = new double[var1];
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

   public double put(char var1, double var2) {
      int var4 = this.insertKey(var1);
      return this.doPut(var1, var2, var4);
   }

   public double putIfAbsent(char var1, double var2) {
      int var4 = this.insertKey(var1);
      return var4 < 0 ? this._values[-var4 - 1] : this.doPut(var1, var2, var4);
   }

   private double doPut(char var1, double var2, int var4) {
      double var5 = this.no_entry_value;
      boolean var7 = true;
      if (var4 < 0) {
         var4 = -var4 - 1;
         var5 = this._values[var4];
         var7 = false;
      }

      this._values[var4] = var2;
      if (var7) {
         this.postInsertHook(this.consumeFreeSlot);
      }

      return var5;
   }

   public void putAll(Map var1) {
      this.ensureCapacity(var1.size());
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put((Character)var3.getKey(), (Double)var3.getValue());
      }

   }

   public void putAll(TCharDoubleMap var1) {
      this.ensureCapacity(var1.size());
      TCharDoubleIterator var2 = var1.iterator();

      while(var2.hasNext()) {
         var2.advance();
         this.put(var2.key(), var2.value());
      }

   }

   public double get(char var1) {
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

   public double remove(char var1) {
      double var2 = this.no_entry_value;
      int var4 = this.index(var1);
      if (var4 >= 0) {
         var2 = this._values[var4];
         this.removeAt(var4);
      }

      return var2;
   }

   protected void removeAt(int var1) {
      this._values[var1] = this.no_entry_value;
      super.removeAt(var1);
   }

   public TCharSet keySet() {
      return new TCharDoubleHashMap.TKeyView(this);
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
      int var2 = this.size();
      if (var1.length < var2) {
         var1 = new char[var2];
      }

      char[] var3 = this._set;
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

   public TDoubleCollection valueCollection() {
      return new TCharDoubleHashMap.TValueView(this);
   }

   public double[] values() {
      double[] var1 = new double[this.size()];
      double[] var2 = this._values;
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

   public double[] values(double[] var1) {
      int var2 = this.size();
      if (var1.length < var2) {
         var1 = new double[var2];
      }

      double[] var3 = this._values;
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

   public boolean containsValue(double var1) {
      byte[] var3 = this._states;
      double[] var4 = this._values;
      int var5 = var4.length;

      do {
         if (var5-- <= 0) {
            return false;
         }
      } while(var3[var5] != 1 || var1 != var4[var5]);

      return true;
   }

   public boolean containsKey(char var1) {
      return this.contains(var1);
   }

   public TCharDoubleIterator iterator() {
      return new TCharDoubleHashMap.TCharDoubleHashIterator(this, this);
   }

   public boolean forEachKey(TCharProcedure var1) {
      return this.forEach(var1);
   }

   public boolean forEachValue(TDoubleProcedure var1) {
      byte[] var2 = this._states;
      double[] var3 = this._values;
      int var4 = var3.length;

      do {
         if (var4-- <= 0) {
            return true;
         }
      } while(var2[var4] != 1 || var1.execute(var3[var4]));

      return false;
   }

   public boolean forEachEntry(TCharDoubleProcedure var1) {
      byte[] var2 = this._states;
      char[] var3 = this._set;
      double[] var4 = this._values;
      int var5 = var3.length;

      do {
         if (var5-- <= 0) {
            return true;
         }
      } while(var2[var5] != 1 || var1.execute(var3[var5], var4[var5]));

      return false;
   }

   public void transformValues(TDoubleFunction var1) {
      byte[] var2 = this._states;
      double[] var3 = this._values;
      int var4 = var3.length;

      while(var4-- > 0) {
         if (var2[var4] == 1) {
            var3[var4] = var1.execute(var3[var4]);
         }
      }

   }

   public boolean retainEntries(TCharDoubleProcedure var1) {
      boolean var2 = false;
      byte[] var3 = this._states;
      char[] var4 = this._set;
      double[] var5 = this._values;
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

   public boolean increment(char var1) {
      return this.adjustValue(var1, 1.0D);
   }

   public boolean adjustValue(char var1, double var2) {
      int var4 = this.index(var1);
      if (var4 < 0) {
         return false;
      } else {
         double[] var10000 = this._values;
         var10000[var4] += var2;
         return true;
      }
   }

   public double adjustOrPutValue(char var1, double var2, double var4) {
      int var6 = this.insertKey(var1);
      boolean var7;
      double var8;
      if (var6 < 0) {
         var6 = -var6 - 1;
         double[] var10000 = this._values;
         var8 = var10000[var6] += var2;
         var7 = false;
      } else {
         var8 = this._values[var6] = var4;
         var7 = true;
      }

      byte var11 = this._states[var6];
      if (var7) {
         this.postInsertHook(this.consumeFreeSlot);
      }

      return var8;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TCharDoubleMap)) {
         return false;
      } else {
         TCharDoubleMap var2 = (TCharDoubleMap)var1;
         if (var2.size() != this.size()) {
            return false;
         } else {
            double[] var3 = this._values;
            byte[] var4 = this._states;
            double var5 = this.getNoEntryValue();
            double var7 = var2.getNoEntryValue();
            int var9 = var3.length;

            while(var9-- > 0) {
               if (var4[var9] == 1) {
                  char var10 = this._set[var9];
                  double var11 = var2.get(var10);
                  double var13 = var3[var9];
                  if (var13 != var11 && var13 != var5 && var11 != var7) {
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
      this.forEachEntry(new TCharDoubleProcedure(this, var1) {
         private boolean first;
         final StringBuilder val$buf;
         final TCharDoubleHashMap this$0;

         {
            this.this$0 = var1;
            this.val$buf = var2;
            this.first = true;
         }

         public boolean execute(char var1, double var2) {
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
            var1.writeChar(this._set[var2]);
            var1.writeDouble(this._values[var2]);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      super.readExternal(var1);
      int var2 = var1.readInt();
      this.setUp(var2);

      while(var2-- > 0) {
         char var3 = var1.readChar();
         double var4 = var1.readDouble();
         this.put(var3, var4);
      }

   }

   static char access$000(TCharDoubleHashMap var0) {
      return var0.no_entry_key;
   }

   static int access$100(TCharDoubleHashMap var0) {
      return var0._size;
   }

   static int access$200(TCharDoubleHashMap var0) {
      return var0._size;
   }

   static double access$300(TCharDoubleHashMap var0) {
      return var0.no_entry_value;
   }

   static double access$400(TCharDoubleHashMap var0) {
      return var0.no_entry_value;
   }

   static int access$500(TCharDoubleHashMap var0) {
      return var0._size;
   }

   static int access$600(TCharDoubleHashMap var0) {
      return var0._size;
   }

   class TCharDoubleHashIterator extends THashPrimitiveIterator implements TCharDoubleIterator {
      final TCharDoubleHashMap this$0;

      TCharDoubleHashIterator(TCharDoubleHashMap var1, TCharDoubleHashMap var2) {
         super(var2);
         this.this$0 = var1;
      }

      public void advance() {
         this.moveToNextIndex();
      }

      public char key() {
         return this.this$0._set[this._index];
      }

      public double value() {
         return this.this$0._values[this._index];
      }

      public double setValue(double var1) {
         double var3 = this.value();
         this.this$0._values[this._index] = var1;
         return var3;
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

   class TCharDoubleValueHashIterator extends THashPrimitiveIterator implements TDoubleIterator {
      final TCharDoubleHashMap this$0;

      TCharDoubleValueHashIterator(TCharDoubleHashMap var1, TPrimitiveHash var2) {
         super(var2);
         this.this$0 = var1;
      }

      public double next() {
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

   class TCharDoubleKeyHashIterator extends THashPrimitiveIterator implements TCharIterator {
      final TCharDoubleHashMap this$0;

      TCharDoubleKeyHashIterator(TCharDoubleHashMap var1, TPrimitiveHash var2) {
         super(var2);
         this.this$0 = var1;
      }

      public char next() {
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

   protected class TValueView implements TDoubleCollection {
      final TCharDoubleHashMap this$0;

      protected TValueView(TCharDoubleHashMap var1) {
         this.this$0 = var1;
      }

      public TDoubleIterator iterator() {
         return this.this$0.new TCharDoubleValueHashIterator(this.this$0, this.this$0);
      }

      public double getNoEntryValue() {
         return TCharDoubleHashMap.access$400(this.this$0);
      }

      public int size() {
         return TCharDoubleHashMap.access$500(this.this$0);
      }

      public boolean isEmpty() {
         return 0 == TCharDoubleHashMap.access$600(this.this$0);
      }

      public boolean contains(double var1) {
         return this.this$0.containsValue(var1);
      }

      public double[] toArray() {
         return this.this$0.values();
      }

      public double[] toArray(double[] var1) {
         return this.this$0.values(var1);
      }

      public boolean add(double var1) {
         throw new UnsupportedOperationException();
      }

      public boolean containsAll(Collection var1) {
         Iterator var2 = var1.iterator();

         double var4;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            Object var3 = var2.next();
            if (!(var3 instanceof Double)) {
               return false;
            }

            var4 = (Double)var3;
         } while(this.this$0.containsValue(var4));

         return false;
      }

      public boolean containsAll(TDoubleCollection var1) {
         TDoubleIterator var2 = var1.iterator();

         do {
            if (!var2.hasNext()) {
               return true;
            }
         } while(this.this$0.containsValue(var2.next()));

         return false;
      }

      public boolean containsAll(double[] var1) {
         double[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            double var5 = var2[var4];
            if (!this.this$0.containsValue(var5)) {
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
         double[] var3 = this.this$0._values;
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
               if (var5 > 0) {
                  var2 = true;
               }
            }
         }

         return var2;
      }

      public boolean removeAll(TDoubleCollection var1) {
         if (this == var1) {
            this.clear();
            return true;
         } else {
            boolean var2 = false;
            TDoubleIterator var3 = var1.iterator();

            while(var3.hasNext()) {
               double var4 = var3.next();
               if (var4 > 0) {
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
            if (var1[var3] > 0) {
               var2 = true;
            }
         }

         return var2;
      }

      public void clear() {
         this.this$0.clear();
      }

      public boolean forEach(TDoubleProcedure var1) {
         return this.this$0.forEachValue(var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("{");
         this.this$0.forEachValue(new TDoubleProcedure(this, var1) {
            private boolean first;
            final StringBuilder val$buf;
            final TCharDoubleHashMap.TValueView this$1;

            {
               this.this$1 = var1;
               this.val$buf = var2;
               this.first = true;
            }

            public boolean execute(double var1) {
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

   protected class TKeyView implements TCharSet {
      final TCharDoubleHashMap this$0;

      protected TKeyView(TCharDoubleHashMap var1) {
         this.this$0 = var1;
      }

      public TCharIterator iterator() {
         return this.this$0.new TCharDoubleKeyHashIterator(this.this$0, this.this$0);
      }

      public char getNoEntryValue() {
         return TCharDoubleHashMap.access$000(this.this$0);
      }

      public int size() {
         return TCharDoubleHashMap.access$100(this.this$0);
      }

      public boolean isEmpty() {
         return 0 == TCharDoubleHashMap.access$200(this.this$0);
      }

      public boolean contains(char var1) {
         return this.this$0.contains(var1);
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
         } while(this.this$0.containsKey(var4));

         return false;
      }

      public boolean containsAll(TCharCollection var1) {
         TCharIterator var2 = var1.iterator();

         do {
            if (!var2.hasNext()) {
               return true;
            }
         } while(this.this$0.containsKey(var2.next()));

         return false;
      }

      public boolean containsAll(char[] var1) {
         char[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            char var5 = var2[var4];
            if (!this.this$0.contains(var5)) {
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
               if (var5 != 0) {
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
               if (var4 != 0) {
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
            if (var1[var3] != 0) {
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
         this.this$0.forEachKey(new TCharProcedure(this, var1) {
            private boolean first;
            final StringBuilder val$buf;
            final TCharDoubleHashMap.TKeyView this$1;

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
}
