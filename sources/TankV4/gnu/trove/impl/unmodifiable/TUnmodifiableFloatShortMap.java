package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TFloatShortIterator;
import gnu.trove.map.TFloatShortMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TFloatShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableFloatShortMap implements TFloatShortMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TFloatShortMap m;
   private transient TFloatSet keySet = null;
   private transient TShortCollection values = null;

   public TUnmodifiableFloatShortMap(TFloatShortMap var1) {
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

   public boolean containsValue(short var1) {
      return this.m.containsValue(var1);
   }

   public short get(float var1) {
      return this.m.get(var1);
   }

   public short put(float var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short remove(float var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TFloatShortMap var1) {
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

   public float getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public short getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TFloatProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TShortProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TFloatShortProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TFloatShortIterator iterator() {
      return new TFloatShortIterator(this) {
         TFloatShortIterator iter;
         final TUnmodifiableFloatShortMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableFloatShortMap.access$000(this.this$0).iterator();
         }

         public float key() {
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

   public short putIfAbsent(float var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TShortFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TFloatShortProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(float var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(float var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short adjustOrPutValue(float var1, short var2, short var3) {
      throw new UnsupportedOperationException();
   }

   static TFloatShortMap access$000(TUnmodifiableFloatShortMap var0) {
      return var0.m;
   }
}
