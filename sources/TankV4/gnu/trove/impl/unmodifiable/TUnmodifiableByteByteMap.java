package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TByteByteIterator;
import gnu.trove.map.TByteByteMap;
import gnu.trove.procedure.TByteByteProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableByteByteMap implements TByteByteMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TByteByteMap m;
   private transient TByteSet keySet = null;
   private transient TByteCollection values = null;

   public TUnmodifiableByteByteMap(TByteByteMap var1) {
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

   public boolean containsValue(byte var1) {
      return this.m.containsValue(var1);
   }

   public byte get(byte var1) {
      return this.m.get(var1);
   }

   public byte put(byte var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public byte remove(byte var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TByteByteMap var1) {
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

   public TByteCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public byte[] values() {
      return this.m.values();
   }

   public byte[] values(byte[] var1) {
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

   public byte getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TByteProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TByteProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TByteByteProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TByteByteIterator iterator() {
      return new TByteByteIterator(this) {
         TByteByteIterator iter;
         final TUnmodifiableByteByteMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableByteByteMap.access$000(this.this$0).iterator();
         }

         public byte key() {
            return this.iter.key();
         }

         public byte value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public byte setValue(byte var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public byte putIfAbsent(byte var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TByteFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TByteByteProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(byte var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(byte var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public byte adjustOrPutValue(byte var1, byte var2, byte var3) {
      throw new UnsupportedOperationException();
   }

   static TByteByteMap access$000(TUnmodifiableByteByteMap var0) {
      return var0.m;
   }
}
