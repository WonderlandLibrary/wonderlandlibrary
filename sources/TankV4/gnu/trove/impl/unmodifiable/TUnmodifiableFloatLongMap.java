package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TFloatLongIterator;
import gnu.trove.map.TFloatLongMap;
import gnu.trove.procedure.TFloatLongProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableFloatLongMap implements TFloatLongMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TFloatLongMap m;
   private transient TFloatSet keySet = null;
   private transient TLongCollection values = null;

   public TUnmodifiableFloatLongMap(TFloatLongMap var1) {
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

   public boolean containsValue(long var1) {
      return this.m.containsValue(var1);
   }

   public long get(float var1) {
      return this.m.get(var1);
   }

   public long put(float var1, long var2) {
      throw new UnsupportedOperationException();
   }

   public long remove(float var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TFloatLongMap var1) {
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

   public float getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public long getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TFloatProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TLongProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TFloatLongProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TFloatLongIterator iterator() {
      return new TFloatLongIterator(this) {
         TFloatLongIterator iter;
         final TUnmodifiableFloatLongMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableFloatLongMap.access$000(this.this$0).iterator();
         }

         public float key() {
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

   public long putIfAbsent(float var1, long var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TLongFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TFloatLongProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(float var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(float var1, long var2) {
      throw new UnsupportedOperationException();
   }

   public long adjustOrPutValue(float var1, long var2, long var4) {
      throw new UnsupportedOperationException();
   }

   static TFloatLongMap access$000(TUnmodifiableFloatLongMap var0) {
      return var0.m;
   }
}
