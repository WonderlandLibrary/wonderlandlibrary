package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TDoubleLongIterator;
import gnu.trove.map.TDoubleLongMap;
import gnu.trove.procedure.TDoubleLongProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableDoubleLongMap implements TDoubleLongMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TDoubleLongMap m;
   private transient TDoubleSet keySet = null;
   private transient TLongCollection values = null;

   public TUnmodifiableDoubleLongMap(TDoubleLongMap var1) {
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

   public boolean containsValue(long var1) {
      return this.m.containsValue(var1);
   }

   public long get(double var1) {
      return this.m.get(var1);
   }

   public long put(double var1, long var3) {
      throw new UnsupportedOperationException();
   }

   public long remove(double var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TDoubleLongMap var1) {
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

   public TLongCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public long[] values() {
      return this.m.values();
   }

   public long[] values(long[] var1) {
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

   public long getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TDoubleProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TLongProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TDoubleLongProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TDoubleLongIterator iterator() {
      return new TDoubleLongIterator(this) {
         TDoubleLongIterator iter;
         final TUnmodifiableDoubleLongMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableDoubleLongMap.access$000(this.this$0).iterator();
         }

         public double key() {
            return this.iter.key();
         }

         public long value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public long setValue(long var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public long putIfAbsent(double var1, long var3) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TLongFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TDoubleLongProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(double var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(double var1, long var3) {
      throw new UnsupportedOperationException();
   }

   public long adjustOrPutValue(double var1, long var3, long var5) {
      throw new UnsupportedOperationException();
   }

   static TDoubleLongMap access$000(TUnmodifiableDoubleLongMap var0) {
      return var0.m;
   }
}
