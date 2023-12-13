package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TDoubleFloatIterator;
import gnu.trove.map.TDoubleFloatMap;
import gnu.trove.procedure.TDoubleFloatProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableDoubleFloatMap implements TDoubleFloatMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TDoubleFloatMap m;
   private transient TDoubleSet keySet = null;
   private transient TFloatCollection values = null;

   public TUnmodifiableDoubleFloatMap(TDoubleFloatMap var1) {
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

   public boolean containsValue(float var1) {
      return this.m.containsValue(var1);
   }

   public float get(double var1) {
      return this.m.get(var1);
   }

   public float put(double var1, float var3) {
      throw new UnsupportedOperationException();
   }

   public float remove(double var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TDoubleFloatMap var1) {
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

   public TFloatCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public float[] values() {
      return this.m.values();
   }

   public float[] values(float[] var1) {
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

   public float getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TDoubleProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TFloatProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TDoubleFloatProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TDoubleFloatIterator iterator() {
      return new TDoubleFloatIterator(this) {
         TDoubleFloatIterator iter;
         final TUnmodifiableDoubleFloatMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableDoubleFloatMap.access$000(this.this$0).iterator();
         }

         public double key() {
            return this.iter.key();
         }

         public float value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public float setValue(float var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public float putIfAbsent(double var1, float var3) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TFloatFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TDoubleFloatProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(double var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(double var1, float var3) {
      throw new UnsupportedOperationException();
   }

   public float adjustOrPutValue(double var1, float var3, float var4) {
      throw new UnsupportedOperationException();
   }

   static TDoubleFloatMap access$000(TUnmodifiableDoubleFloatMap var0) {
      return var0.m;
   }
}
