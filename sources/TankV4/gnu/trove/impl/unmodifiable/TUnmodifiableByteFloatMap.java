package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TByteFloatIterator;
import gnu.trove.map.TByteFloatMap;
import gnu.trove.procedure.TByteFloatProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableByteFloatMap implements TByteFloatMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TByteFloatMap m;
   private transient TByteSet keySet = null;
   private transient TFloatCollection values = null;

   public TUnmodifiableByteFloatMap(TByteFloatMap var1) {
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

   public boolean containsKey(byte var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(float var1) {
      return this.m.containsValue(var1);
   }

   public float get(byte var1) {
      return this.m.get(var1);
   }

   public float put(byte var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public float remove(byte var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TByteFloatMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TByteSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public byte[] keys() {
      return this.m.keys();
   }

   public byte[] keys(byte[] var1) {
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

   public byte getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public float getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TByteProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TFloatProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TByteFloatProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TByteFloatIterator iterator() {
      return new TByteFloatIterator(this) {
         TByteFloatIterator iter;
         final TUnmodifiableByteFloatMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableByteFloatMap.access$000(this.this$0).iterator();
         }

         public byte key() {
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

   public float putIfAbsent(byte var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TFloatFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TByteFloatProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(byte var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(byte var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public float adjustOrPutValue(byte var1, float var2, float var3) {
      throw new UnsupportedOperationException();
   }

   static TByteFloatMap access$000(TUnmodifiableByteFloatMap var0) {
      return var0.m;
   }
}
