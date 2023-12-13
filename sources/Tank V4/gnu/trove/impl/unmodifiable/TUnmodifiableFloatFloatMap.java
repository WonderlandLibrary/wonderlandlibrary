package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TFloatFloatIterator;
import gnu.trove.map.TFloatFloatMap;
import gnu.trove.procedure.TFloatFloatProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableFloatFloatMap implements TFloatFloatMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TFloatFloatMap m;
   private transient TFloatSet keySet = null;
   private transient TFloatCollection values = null;

   public TUnmodifiableFloatFloatMap(TFloatFloatMap var1) {
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

   public boolean containsKey(float var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(float var1) {
      return this.m.containsValue(var1);
   }

   public float get(float var1) {
      return this.m.get(var1);
   }

   public float put(float var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public float remove(float var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TFloatFloatMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TFloatSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public float[] keys() {
      return this.m.keys();
   }

   public float[] keys(float[] var1) {
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

   public float getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public float getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TFloatProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TFloatProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TFloatFloatProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TFloatFloatIterator iterator() {
      return new TFloatFloatIterator(this) {
         TFloatFloatIterator iter;
         final TUnmodifiableFloatFloatMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableFloatFloatMap.access$000(this.this$0).iterator();
         }

         public float key() {
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

   public float putIfAbsent(float var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TFloatFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TFloatFloatProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(float var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(float var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public float adjustOrPutValue(float var1, float var2, float var3) {
      throw new UnsupportedOperationException();
   }

   static TFloatFloatMap access$000(TUnmodifiableFloatFloatMap var0) {
      return var0.m;
   }
}
