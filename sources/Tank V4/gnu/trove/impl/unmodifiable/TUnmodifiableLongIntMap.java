package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TLongIntIterator;
import gnu.trove.map.TLongIntMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TLongIntProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableLongIntMap implements TLongIntMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TLongIntMap m;
   private transient TLongSet keySet = null;
   private transient TIntCollection values = null;

   public TUnmodifiableLongIntMap(TLongIntMap var1) {
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

   public boolean containsValue(int var1) {
      return this.m.containsValue(var1);
   }

   public int get(long var1) {
      return this.m.get(var1);
   }

   public int put(long var1, int var3) {
      throw new UnsupportedOperationException();
   }

   public int remove(long var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TLongIntMap var1) {
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

   public TIntCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public int[] values() {
      return this.m.values();
   }

   public int[] values(int[] var1) {
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

   public int getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TLongProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TIntProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TLongIntProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TLongIntIterator iterator() {
      return new TLongIntIterator(this) {
         TLongIntIterator iter;
         final TUnmodifiableLongIntMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableLongIntMap.access$000(this.this$0).iterator();
         }

         public long key() {
            return this.iter.key();
         }

         public int value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public int setValue(int var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public int putIfAbsent(long var1, int var3) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TIntFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TLongIntProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(long var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(long var1, int var3) {
      throw new UnsupportedOperationException();
   }

   public int adjustOrPutValue(long var1, int var3, int var4) {
      throw new UnsupportedOperationException();
   }

   static TLongIntMap access$000(TUnmodifiableLongIntMap var0) {
      return var0.m;
   }
}
