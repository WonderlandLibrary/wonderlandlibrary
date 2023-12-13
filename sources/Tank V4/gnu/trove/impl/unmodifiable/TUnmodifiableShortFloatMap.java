package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TShortFloatIterator;
import gnu.trove.map.TShortFloatMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TShortFloatProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableShortFloatMap implements TShortFloatMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TShortFloatMap m;
   private transient TShortSet keySet = null;
   private transient TFloatCollection values = null;

   public TUnmodifiableShortFloatMap(TShortFloatMap var1) {
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

   public boolean containsKey(short var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(float var1) {
      return this.m.containsValue(var1);
   }

   public float get(short var1) {
      return this.m.get(var1);
   }

   public float put(short var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public float remove(short var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TShortFloatMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TShortSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public short[] keys() {
      return this.m.keys();
   }

   public short[] keys(short[] var1) {
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

   public short getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public float getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TShortProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TFloatProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TShortFloatProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TShortFloatIterator iterator() {
      return new TShortFloatIterator(this) {
         TShortFloatIterator iter;
         final TUnmodifiableShortFloatMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableShortFloatMap.access$000(this.this$0).iterator();
         }

         public short key() {
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

   public float putIfAbsent(short var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TFloatFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TShortFloatProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(short var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(short var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public float adjustOrPutValue(short var1, float var2, float var3) {
      throw new UnsupportedOperationException();
   }

   static TShortFloatMap access$000(TUnmodifiableShortFloatMap var0) {
      return var0.m;
   }
}
