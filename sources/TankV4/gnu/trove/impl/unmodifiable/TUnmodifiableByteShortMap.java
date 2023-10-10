package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TByteShortIterator;
import gnu.trove.map.TByteShortMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TByteShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableByteShortMap implements TByteShortMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TByteShortMap m;
   private transient TByteSet keySet = null;
   private transient TShortCollection values = null;

   public TUnmodifiableByteShortMap(TByteShortMap var1) {
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

   public boolean containsValue(short var1) {
      return this.m.containsValue(var1);
   }

   public short get(byte var1) {
      return this.m.get(var1);
   }

   public short put(byte var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short remove(byte var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TByteShortMap var1) {
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

   public byte getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public short getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TByteProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TShortProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TByteShortProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TByteShortIterator iterator() {
      return new TByteShortIterator(this) {
         TByteShortIterator iter;
         final TUnmodifiableByteShortMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableByteShortMap.access$000(this.this$0).iterator();
         }

         public byte key() {
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

   public short putIfAbsent(byte var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TShortFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TByteShortProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(byte var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(byte var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short adjustOrPutValue(byte var1, short var2, short var3) {
      throw new UnsupportedOperationException();
   }

   static TByteShortMap access$000(TUnmodifiableByteShortMap var0) {
      return var0.m;
   }
}
