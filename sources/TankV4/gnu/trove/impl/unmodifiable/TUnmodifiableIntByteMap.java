package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TIntByteIterator;
import gnu.trove.map.TIntByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TIntByteProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableIntByteMap implements TIntByteMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TIntByteMap m;
   private transient TIntSet keySet = null;
   private transient TByteCollection values = null;

   public TUnmodifiableIntByteMap(TIntByteMap var1) {
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

   public boolean containsKey(int var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(byte var1) {
      return this.m.containsValue(var1);
   }

   public byte get(int var1) {
      return this.m.get(var1);
   }

   public byte put(int var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public byte remove(int var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TIntByteMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TIntSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public int[] keys() {
      return this.m.keys();
   }

   public int[] keys(int[] var1) {
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

   public int getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public byte getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TIntProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TByteProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TIntByteProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TIntByteIterator iterator() {
      return new TIntByteIterator(this) {
         TIntByteIterator iter;
         final TUnmodifiableIntByteMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableIntByteMap.access$000(this.this$0).iterator();
         }

         public int key() {
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

   public byte putIfAbsent(int var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TByteFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TIntByteProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(int var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(int var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public byte adjustOrPutValue(int var1, byte var2, byte var3) {
      throw new UnsupportedOperationException();
   }

   static TIntByteMap access$000(TUnmodifiableIntByteMap var0) {
      return var0.m;
   }
}
