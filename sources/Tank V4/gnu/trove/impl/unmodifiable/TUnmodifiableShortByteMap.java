package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TShortByteProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableShortByteMap implements TShortByteMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TShortByteMap m;
   private transient TShortSet keySet = null;
   private transient TByteCollection values = null;

   public TUnmodifiableShortByteMap(TShortByteMap var1) {
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

   public boolean containsValue(byte var1) {
      return this.m.containsValue(var1);
   }

   public byte get(short var1) {
      return this.m.get(var1);
   }

   public byte put(short var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public byte remove(short var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TShortByteMap var1) {
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

   public short getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public byte getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TShortProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TByteProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TShortByteProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TShortByteIterator iterator() {
      return new TShortByteIterator(this) {
         TShortByteIterator iter;
         final TUnmodifiableShortByteMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableShortByteMap.access$000(this.this$0).iterator();
         }

         public short key() {
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

   public byte putIfAbsent(short var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TByteFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TShortByteProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(short var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(short var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public byte adjustOrPutValue(short var1, byte var2, byte var3) {
      throw new UnsupportedOperationException();
   }

   static TShortByteMap access$000(TUnmodifiableShortByteMap var0) {
      return var0.m;
   }
}
