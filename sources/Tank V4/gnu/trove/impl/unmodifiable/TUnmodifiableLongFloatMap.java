package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TLongFloatIterator;
import gnu.trove.map.TLongFloatMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableLongFloatMap implements TLongFloatMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TLongFloatMap m;
   private transient TLongSet keySet = null;
   private transient TFloatCollection values = null;

   public TUnmodifiableLongFloatMap(TLongFloatMap var1) {
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

   public boolean containsValue(float var1) {
      return this.m.containsValue(var1);
   }

   public float get(long var1) {
      return this.m.get(var1);
   }

   public float put(long var1, float var3) {
      throw new UnsupportedOperationException();
   }

   public float remove(long var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TLongFloatMap var1) {
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

   public long getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public float getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TLongProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TFloatProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TLongFloatProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TLongFloatIterator iterator() {
      return new TLongFloatIterator(this) {
         TLongFloatIterator iter;
         final TUnmodifiableLongFloatMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableLongFloatMap.access$000(this.this$0).iterator();
         }

         public long key() {
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

   public float putIfAbsent(long var1, float var3) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TFloatFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TLongFloatProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(long var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(long var1, float var3) {
      throw new UnsupportedOperationException();
   }

   public float adjustOrPutValue(long var1, float var3, float var4) {
      throw new UnsupportedOperationException();
   }

   static TLongFloatMap access$000(TUnmodifiableLongFloatMap var0) {
      return var0.m;
   }
}
