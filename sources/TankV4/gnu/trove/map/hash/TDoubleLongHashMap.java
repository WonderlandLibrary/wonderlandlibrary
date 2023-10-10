package gnu.trove.map.hash;

import gnu.trove.TDoubleCollection;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TDoubleLongHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TDoubleLongIterator;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.map.TDoubleLongMap;
import gnu.trove.procedure.TDoubleLongProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TDoubleSet;
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

public class TDoubleLongHashMap extends TDoubleLongHash implements TDoubleLongMap, Externalizable {
   static final long serialVersionUID = 1L;
   protected transient long[] _values;

   public TDoubleLongHashMap() {
   }

   public TDoubleLongHashMap(int var1) {
      super(var1);
   }

   public TDoubleLongHashMap(int var1, float var2) {
      super(var1, var2);
   }

   public TDoubleLongHashMap(int var1, float var2, double var3, long var5) {
      super(var1, var2, var3, var5);
   }

   public TDoubleLongHashMap(double[] var1, long[] var2) {
      super(Math.max(var1.length, var2.length));
      int var3 = Math.min(var1.length, var2.length);

      for(int var4 = 0; var4 < var3; ++var4) {
         this.put(var1[var4], var2[var4]);
      }

   }

   public TDoubleLongHashMap(TDoubleLongMap var1) {
      super(var1.size());
      if (var1 instanceof TDoubleLongHashMap) {
         TDoubleLongHashMap var2 = (TDoubleLongHashMap)var1;
         this._loadFactor = var2._loadFactor;
         this.no_entry_key = var2.no_entry_key;
         this.no_entry_value = var2.no_entry_value;
         if (this.no_entry_key != 0.0D) {
            Arrays.fill(this._set, this.no_entry_key);
         }

         if (this.no_entry_value != 0L) {
            Arrays.fill(this._values, this.no_entry_value);
         }

         this.setUp((int)Math.ceil((double)(10.0F / this._loadFactor)));
      }

      this.putAll(var1);
   }

   protected int setUp(int var1) {
      int var2 = super.setUp(var1);
      this._values = new long[var2];
      return var2;
   }

   protected void rehash(int var1) {
      int var2 = this._set.length;
      double[] var3 = this._set;
      long[] var4 = this._values;
      byte[] var5 = this._states;
      this._set = new double[var1];
      this._values = new long[var1];
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

   public long put(double var1, long var3) {
      int var5 = this.insertKey(var1);
      return this.doPut(var1, var3, var5);
   }

   public long putIfAbsent(double var1, long var3) {
      int var5 = this.insertKey(var1);
      return var5 < 0 ? this._values[-var5 - 1] : this.doPut(var1, var3, var5);
   }

   private long doPut(double var1, long var3, int var5) {
      long var6 = this.no_entry_value;
      boolean var8 = true;
      if (var5 < 0) {
         var5 = -var5 - 1;
         var6 = this._values[var5];
         var8 = false;
      }

      this._values[var5] = var3;
      if (var8) {
         this.postInsertHook(this.consumeFreeSlot);
      }

      return var6;
   }

   public void putAll(Map var1) {
      this.ensureCapacity(var1.size());
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put((Double)var3.getKey(), (Long)var3.getValue());
      }

   }

   public void putAll(TDoubleLongMap var1) {
      this.ensureCapacity(var1.size());
      TDoubleLongIterator var2 = var1.iterator();

      while(var2.hasNext()) {
         var2.advance();
         this.put(var2.key(), var2.value());
      }

   }

   public long get(double var1) {
      int var3 = this.index(var1);
      return var3 < 0 ? this.no_entry_value : this._values[var3];
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

   public long remove(double var1) {
      long var3 = this.no_entry_value;
      int var5 = this.index(var1);
      if (var5 >= 0) {
         var3 = this._values[var5];
         this.removeAt(var5);
      }

      return var3;
   }

   protected void removeAt(int var1) {
      this._values[var1] = this.no_entry_value;
      super.removeAt(var1);
   }

   public TDoubleSet keySet() {
      return new TDoubleLongHashMap.TKeyView(this);
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
      int var2 = this.size();
      if (var1.length < var2) {
         var1 = new double[var2];
      }

      double[] var3 = this._set;
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

   public TLongCollection valueCollection() {
      return new TDoubleLongHashMap.TValueView(this);
   }

   public long[] values() {
      long[] var1 = new long[this.size()];
      long[] var2 = this._values;
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

   public long[] values(long[] var1) {
      int var2 = this.size();
      if (var1.length < var2) {
         var1 = new long[var2];
      }

      long[] var3 = this._values;
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

   public boolean containsValue(long var1) {
      byte[] var3 = this._states;
      long[] var4 = this._values;
      int var5 = var4.length;

      do {
         if (var5-- <= 0) {
            return false;
         }
      } while(var3[var5] != 1 || var1 != var4[var5]);

      return true;
   }

   public boolean containsKey(double var1) {
      return this.contains(var1);
   }

   public TDoubleLongIterator iterator() {
      return new TDoubleLongHashMap.TDoubleLongHashIterator(this, this);
   }

   public boolean forEachKey(TDoubleProcedure var1) {
      return this.forEach(var1);
   }

   public boolean forEachValue(TLongProcedure var1) {
      byte[] var2 = this._states;
      long[] var3 = this._values;
      int var4 = var3.length;

      do {
         if (var4-- <= 0) {
            return true;
         }
      } while(var2[var4] != 1 || var1.execute(var3[var4]));

      return false;
   }

   public boolean forEachEntry(TDoubleLongProcedure var1) {
      byte[] var2 = this._states;
      double[] var3 = this._set;
      long[] var4 = this._values;
      int var5 = var3.length;

      do {
         if (var5-- <= 0) {
            return true;
         }
      } while(var2[var5] != 1 || var1.execute(var3[var5], var4[var5]));

      return false;
   }

   public void transformValues(TLongFunction var1) {
      byte[] var2 = this._states;
      long[] var3 = this._values;
      int var4 = var3.length;

      while(var4-- > 0) {
         if (var2[var4] == 1) {
            var3[var4] = var1.execute(var3[var4]);
         }
      }

   }

   public boolean retainEntries(TDoubleLongProcedure var1) {
      boolean var2 = false;
      byte[] var3 = this._states;
      double[] var4 = this._set;
      long[] var5 = this._values;
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

   public boolean increment(double var1) {
      return this.adjustValue(var1, 1L);
   }

   public boolean adjustValue(double var1, long var3) {
      int var5 = this.index(var1);
      if (var5 < 0) {
         return false;
      } else {
         long[] var10000 = this._values;
         var10000[var5] += var3;
         return true;
      }
   }

   public long adjustOrPutValue(double var1, long var3, long var5) {
      int var7 = this.insertKey(var1);
      boolean var8;
      long var9;
      if (var7 < 0) {
         var7 = -var7 - 1;
         long[] var10000 = this._values;
         var9 = var10000[var7] += var3;
         var8 = false;
      } else {
         var9 = this._values[var7] = var5;
         var8 = true;
      }

      byte var12 = this._states[var7];
      if (var8) {
         this.postInsertHook(this.consumeFreeSlot);
      }

      return var9;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TDoubleLongMap)) {
         return false;
      } else {
         TDoubleLongMap var2 = (TDoubleLongMap)var1;
         if (var2.size() != this.size()) {
            return false;
         } else {
            long[] var3 = this._values;
            byte[] var4 = this._states;
            long var5 = this.getNoEntryValue();
            long var7 = var2.getNoEntryValue();
            int var9 = var3.length;

            while(var9-- > 0) {
               if (var4[var9] == 1) {
                  double var10 = this._set[var9];
                  long var12 = var2.get(var10);
                  long var14 = var3[var9];
                  if (var14 != var12 && var14 != var5 && var12 != var7) {
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
      this.forEachEntry(new TDoubleLongProcedure(this, var1) {
         private boolean first;
         final StringBuilder val$buf;
         final TDoubleLongHashMap this$0;

         {
            this.this$0 = var1;
            this.val$buf = var2;
            this.first = true;
         }

         public boolean execute(double var1, long var3) {
            if (this.first) {
               this.first = false;
            } else {
               this.val$buf.append(", ");
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

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      super.writeExternal(var1);
      var1.writeInt(this._size);
      int var2 = this._states.length;

      while(var2-- > 0) {
         if (this._states[var2] == 1) {
            var1.writeDouble(this._set[var2]);
            var1.writeLong(this._values[var2]);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      super.readExternal(var1);
      int var2 = var1.readInt();
      this.setUp(var2);

      while(var2-- > 0) {
         double var3 = var1.readDouble();
         long var5 = var1.readLong();
         this.put(var3, var5);
      }

   }

   static double access$000(TDoubleLongHashMap var0) {
      return var0.no_entry_key;
   }

   static int access$100(TDoubleLongHashMap var0) {
      return var0._size;
   }

   static int access$200(TDoubleLongHashMap var0) {
      return var0._size;
   }

   static long access$300(TDoubleLongHashMap var0) {
      return var0.no_entry_value;
   }

   static long access$400(TDoubleLongHashMap var0) {
      return var0.no_entry_value;
   }

   static int access$500(TDoubleLongHashMap var0) {
      return var0._size;
   }

   static int access$600(TDoubleLongHashMap var0) {
      return var0._size;
   }

   class TDoubleLongHashIterator extends THashPrimitiveIterator implements TDoubleLongIterator {
      final TDoubleLongHashMap this$0;

      TDoubleLongHashIterator(TDoubleLongHashMap var1, TDoubleLongHashMap var2) {
         super(var2);
         this.this$0 = var1;
      }

      public void advance() {
         this.moveToNextIndex();
      }

      public double key() {
         return this.this$0._set[this._index];
      }

      public long value() {
         return this.this$0._values[this._index];
      }

      public long setValue(long var1) {
         long var3 = this.value();
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

   class TDoubleLongValueHashIterator extends THashPrimitiveIterator implements TLongIterator {
      final TDoubleLongHashMap this$0;

      TDoubleLongValueHashIterator(TDoubleLongHashMap var1, TPrimitiveHash var2) {
         super(var2);
         this.this$0 = var1;
      }

      public long next() {
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

   class TDoubleLongKeyHashIterator extends THashPrimitiveIterator implements TDoubleIterator {
      final TDoubleLongHashMap this$0;

      TDoubleLongKeyHashIterator(TDoubleLongHashMap var1, TPrimitiveHash var2) {
         super(var2);
         this.this$0 = var1;
      }

      public double next() {
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

   protected class TValueView implements TLongCollection {
      final TDoubleLongHashMap this$0;

      protected TValueView(TDoubleLongHashMap var1) {
         this.this$0 = var1;
      }

      public TLongIterator iterator() {
         return this.this$0.new TDoubleLongValueHashIterator(this.this$0, this.this$0);
      }

      public long getNoEntryValue() {
         return TDoubleLongHashMap.access$400(this.this$0);
      }

      public int size() {
         return TDoubleLongHashMap.access$500(this.this$0);
      }

      public boolean isEmpty() {
         return 0 == TDoubleLongHashMap.access$600(this.this$0);
      }

      public boolean contains(long var1) {
         return this.this$0.containsValue(var1);
      }

      public long[] toArray() {
         return this.this$0.values();
      }

      public long[] toArray(long[] var1) {
         return this.this$0.values(var1);
      }

      public boolean add(long var1) {
         throw new UnsupportedOperationException();
      }

      public boolean containsAll(Collection var1) {
         Iterator var2 = var1.iterator();

         long var4;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            Object var3 = var2.next();
            if (!(var3 instanceof Long)) {
               return false;
            }

            var4 = (Long)var3;
         } while(this.this$0.containsValue(var4));

         return false;
      }

      public boolean containsAll(TLongCollection var1) {
         TLongIterator var2 = var1.iterator();

         do {
            if (!var2.hasNext()) {
               return true;
            }
         } while(this.this$0.containsValue(var2.next()));

         return false;
      }

      public boolean containsAll(long[] var1) {
         long[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            long var5 = var2[var4];
            if (!this.this$0.containsValue(var5)) {
               return false;
            }
         }

         return true;
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(TLongCollection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(long[] var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection var1) {
         boolean var2 = false;
         TLongIterator var3 = this.iterator();

         while(var3.hasNext()) {
            if (!var1.contains(var3.next())) {
               var3.remove();
               var2 = true;
            }
         }

         return var2;
      }

      public boolean retainAll(TLongCollection var1) {
         if (this == var1) {
            return false;
         } else {
            boolean var2 = false;
            TLongIterator var3 = this.iterator();

            while(var3.hasNext()) {
               if (!var1.contains(var3.next())) {
                  var3.remove();
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean retainAll(long[] var1) {
         boolean var2 = false;
         Arrays.sort(var1);
         long[] var3 = this.this$0._values;
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
            if (var4 instanceof Long) {
               long var5 = (Long)var4;
               if (var5 > 0) {
                  var2 = true;
               }
            }
         }

         return var2;
      }

      public boolean removeAll(TLongCollection var1) {
         if (this == var1) {
            this.clear();
            return true;
         } else {
            boolean var2 = false;
            TLongIterator var3 = var1.iterator();

            while(var3.hasNext()) {
               long var4 = var3.next();
               if (var4 > 0) {
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public boolean removeAll(long[] var1) {
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

      public boolean forEach(TLongProcedure var1) {
         return this.this$0.forEachValue(var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("{");
         this.this$0.forEachValue(new TLongProcedure(this, var1) {
            private boolean first;
            final StringBuilder val$buf;
            final TDoubleLongHashMap.TValueView this$1;

            {
               this.this$1 = var1;
               this.val$buf = var2;
               this.first = true;
            }

            public boolean execute(long var1) {
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

   protected class TKeyView implements TDoubleSet {
      final TDoubleLongHashMap this$0;

      protected TKeyView(TDoubleLongHashMap var1) {
         this.this$0 = var1;
      }

      public TDoubleIterator iterator() {
         return this.this$0.new TDoubleLongKeyHashIterator(this.this$0, this.this$0);
      }

      public double getNoEntryValue() {
         return TDoubleLongHashMap.access$000(this.this$0);
      }

      public int size() {
         return TDoubleLongHashMap.access$100(this.this$0);
      }

      public boolean isEmpty() {
         return 0 == TDoubleLongHashMap.access$200(this.this$0);
      }

      public boolean contains(double var1) {
         return this.this$0.contains(var1);
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
         } while(this.this$0.containsKey(var4));

         return false;
      }

      public boolean containsAll(TDoubleCollection var1) {
         TDoubleIterator var2 = var1.iterator();

         do {
            if (!var2.hasNext()) {
               return true;
            }
         } while(this.this$0.containsKey(var2.next()));

         return false;
      }

      public boolean containsAll(double[] var1) {
         double[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            double var5 = var2[var4];
            if (!this.this$0.contains(var5)) {
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
               if (var5 != false) {
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
               if (var4 != false) {
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
            if (var1[var3] != false) {
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
         this.this$0.forEachKey(new TDoubleProcedure(this, var1) {
            private boolean first;
            final StringBuilder val$buf;
            final TDoubleLongHashMap.TKeyView this$1;

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
}
