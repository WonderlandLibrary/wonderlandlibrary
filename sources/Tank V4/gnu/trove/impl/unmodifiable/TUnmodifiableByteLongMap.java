package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TByteLongIterator;
import gnu.trove.map.TByteLongMap;
import gnu.trove.procedure.TByteLongProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableByteLongMap implements TByteLongMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TByteLongMap m;
   private transient TByteSet keySet = null;
   private transient TLongCollection values = null;

   public TUnmodifiableByteLongMap(TByteLongMap var1) {
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

   public boolean containsValue(long var1) {
      return this.m.containsValue(var1);
   }

   public long get(byte var1) {
      return this.m.get(var1);
   }

   public long put(byte var1, long var2) {
      throw new UnsupportedOperationException();
   }

   public long remove(byte var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TByteLongMap var1) {
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

   public byte getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public long getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TByteProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TLongProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TByteLongProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TByteLongIterator iterator() {
      return new TByteLongIterator(this) {
         TByteLongIterator iter;
         final TUnmodifiableByteLongMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableByteLongMap.access$000(this.this$0).iterator();
         }

         public byte key() {
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

   public long putIfAbsent(byte var1, long var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TLongFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TByteLongProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(byte var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(byte var1, long var2) {
      throw new UnsupportedOperationException();
   }

   public long adjustOrPutValue(byte var1, long var2, long var4) {
      throw new UnsupportedOperationException();
   }

   static TByteLongMap access$000(TUnmodifiableByteLongMap var0) {
      return var0.m;
   }
}
