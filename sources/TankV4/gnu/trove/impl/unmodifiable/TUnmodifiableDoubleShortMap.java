package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TDoubleShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableDoubleShortMap implements TDoubleShortMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TDoubleShortMap m;
   private transient TDoubleSet keySet = null;
   private transient TShortCollection values = null;

   public TUnmodifiableDoubleShortMap(TDoubleShortMap var1) {
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

   public boolean containsValue(short var1) {
      return this.m.containsValue(var1);
   }

   public short get(double var1) {
      return this.m.get(var1);
   }

   public short put(double var1, short var3) {
      throw new UnsupportedOperationException();
   }

   public short remove(double var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TDoubleShortMap var1) {
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

   public TShortCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public short[] values() {
      return this.m.values();
   }

   public short[] values(short[] var1) {
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

   public short getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TDoubleProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TShortProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TDoubleShortProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TDoubleShortIterator iterator() {
      return new TDoubleShortIterator(this) {
         TDoubleShortIterator iter;
         final TUnmodifiableDoubleShortMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableDoubleShortMap.access$000(this.this$0).iterator();
         }

         public double key() {
            return this.iter.key();
         }

         public short value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public short setValue(short var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public short putIfAbsent(double var1, short var3) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TShortFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TDoubleShortProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(double var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(double var1, short var3) {
      throw new UnsupportedOperationException();
   }

   public short adjustOrPutValue(double var1, short var3, short var4) {
      throw new UnsupportedOperationException();
   }

   static TDoubleShortMap access$000(TUnmodifiableDoubleShortMap var0) {
      return var0.m;
   }
}
