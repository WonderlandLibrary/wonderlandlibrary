package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TLongShortIterator;
import gnu.trove.map.TLongShortMap;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TLongShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableLongShortMap implements TLongShortMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TLongShortMap m;
   private transient TLongSet keySet = null;
   private transient TShortCollection values = null;

   public TUnmodifiableLongShortMap(TLongShortMap var1) {
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

   public boolean containsValue(short var1) {
      return this.m.containsValue(var1);
   }

   public short get(long var1) {
      return this.m.get(var1);
   }

   public short put(long var1, short var3) {
      throw new UnsupportedOperationException();
   }

   public short remove(long var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TLongShortMap var1) {
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

   public long getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public short getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TLongProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TShortProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TLongShortProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TLongShortIterator iterator() {
      return new TLongShortIterator(this) {
         TLongShortIterator iter;
         final TUnmodifiableLongShortMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableLongShortMap.access$000(this.this$0).iterator();
         }

         public long key() {
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

   public short putIfAbsent(long var1, short var3) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TShortFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TLongShortProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(long var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(long var1, short var3) {
      throw new UnsupportedOperationException();
   }

   public short adjustOrPutValue(long var1, short var3, short var4) {
      throw new UnsupportedOperationException();
   }

   static TLongShortMap access$000(TUnmodifiableLongShortMap var0) {
      return var0.m;
   }
}
