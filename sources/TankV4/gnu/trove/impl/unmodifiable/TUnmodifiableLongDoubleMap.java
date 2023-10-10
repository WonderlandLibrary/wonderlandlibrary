package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TLongDoubleIterator;
import gnu.trove.map.TLongDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableLongDoubleMap implements TLongDoubleMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TLongDoubleMap m;
   private transient TLongSet keySet = null;
   private transient TDoubleCollection values = null;

   public TUnmodifiableLongDoubleMap(TLongDoubleMap var1) {
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

   public boolean containsKey(long var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(double var1) {
      return this.m.containsValue(var1);
   }

   public double get(long var1) {
      return this.m.get(var1);
   }

   public double put(long var1, double var3) {
      throw new UnsupportedOperationException();
   }

   public double remove(long var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TLongDoubleMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TLongSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public long[] keys() {
      return this.m.keys();
   }

   public long[] keys(long[] var1) {
      return this.m.keys(var1);
   }

   public TDoubleCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public double[] values() {
      return this.m.values();
   }

   public double[] values(double[] var1) {
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

   public long getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public double getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TLongProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TDoubleProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TLongDoubleProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TLongDoubleIterator iterator() {
      return new TLongDoubleIterator(this) {
         TLongDoubleIterator iter;
         final TUnmodifiableLongDoubleMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableLongDoubleMap.access$000(this.this$0).iterator();
         }

         public long key() {
            return this.iter.key();
         }

         public double value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public double setValue(double var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public double putIfAbsent(long var1, double var3) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TDoubleFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TLongDoubleProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(long var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(long var1, double var3) {
      throw new UnsupportedOperationException();
   }

   public double adjustOrPutValue(long var1, double var3, double var5) {
      throw new UnsupportedOperationException();
   }

   static TLongDoubleMap access$000(TUnmodifiableLongDoubleMap var0) {
      return var0.m;
   }
}
