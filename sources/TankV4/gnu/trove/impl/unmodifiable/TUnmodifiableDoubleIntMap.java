package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TDoubleIntIterator;
import gnu.trove.map.TDoubleIntMap;
import gnu.trove.procedure.TDoubleIntProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableDoubleIntMap implements TDoubleIntMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TDoubleIntMap m;
   private transient TDoubleSet keySet = null;
   private transient TIntCollection values = null;

   public TUnmodifiableDoubleIntMap(TDoubleIntMap var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m = var1;
      }
   }

   public int size() {
      return this.m.size();
   }

   public boolean isEmpty() {
      return this.m.isEmpty();
   }

   public boolean containsKey(double var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(int var1) {
      return this.m.containsValue(var1);
   }

   public int get(double var1) {
      return this.m.get(var1);
   }

   public int put(double var1, int var3) {
      throw new UnsupportedOperationException();
   }

   public int remove(double var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TDoubleIntMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TDoubleSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public double[] keys() {
      return this.m.keys();
   }

   public double[] keys(double[] var1) {
      return this.m.keys(var1);
   }

   public TIntCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public int[] values() {
      return this.m.values();
   }

   public int[] values(int[] var1) {
      return this.m.values(var1);
   }

   public boolean equals(Object var1) {
      return var1 == this || this.m.equals(var1);
   }

   public int hashCode() {
      return this.m.hashCode();
   }

   public String toString() {
      return this.m.toString();
   }

   public double getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public int getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TDoubleProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TIntProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TDoubleIntProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TDoubleIntIterator iterator() {
      return new TDoubleIntIterator(this) {
         TDoubleIntIterator iter;
         final TUnmodifiableDoubleIntMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableDoubleIntMap.access$000(this.this$0).iterator();
         }

         public double key() {
            return this.iter.key();
         }

         public int value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public int setValue(int var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public int putIfAbsent(double var1, int var3) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TIntFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TDoubleIntProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(double var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(double var1, int var3) {
      throw new UnsupportedOperationException();
   }

   public int adjustOrPutValue(double var1, int var3, int var4) {
      throw new UnsupportedOperationException();
   }

   static TDoubleIntMap access$000(TUnmodifiableDoubleIntMap var0) {
      return var0.m;
   }
}
