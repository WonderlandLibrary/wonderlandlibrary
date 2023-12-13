package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TShortLongIterator;
import gnu.trove.map.TShortLongMap;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TShortLongProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableShortLongMap implements TShortLongMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TShortLongMap m;
   private transient TShortSet keySet = null;
   private transient TLongCollection values = null;

   public TUnmodifiableShortLongMap(TShortLongMap var1) {
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

   public boolean containsValue(long var1) {
      return this.m.containsValue(var1);
   }

   public long get(short var1) {
      return this.m.get(var1);
   }

   public long put(short var1, long var2) {
      throw new UnsupportedOperationException();
   }

   public long remove(short var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TShortLongMap var1) {
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

   public short getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public long getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TShortProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TLongProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TShortLongProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TShortLongIterator iterator() {
      return new TShortLongIterator(this) {
         TShortLongIterator iter;
         final TUnmodifiableShortLongMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableShortLongMap.access$000(this.this$0).iterator();
         }

         public short key() {
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

   public long putIfAbsent(short var1, long var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TLongFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TShortLongProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(short var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(short var1, long var2) {
      throw new UnsupportedOperationException();
   }

   public long adjustOrPutValue(short var1, long var2, long var4) {
      throw new UnsupportedOperationException();
   }

   static TShortLongMap access$000(TUnmodifiableShortLongMap var0) {
      return var0.m;
   }
}
