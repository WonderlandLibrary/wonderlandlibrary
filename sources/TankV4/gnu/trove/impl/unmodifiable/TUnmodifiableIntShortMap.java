package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TIntShortIterator;
import gnu.trove.map.TIntShortMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TIntShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableIntShortMap implements TIntShortMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TIntShortMap m;
   private transient TIntSet keySet = null;
   private transient TShortCollection values = null;

   public TUnmodifiableIntShortMap(TIntShortMap var1) {
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

   public boolean containsValue(short var1) {
      return this.m.containsValue(var1);
   }

   public short get(int var1) {
      return this.m.get(var1);
   }

   public short put(int var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short remove(int var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TIntShortMap var1) {
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

   public int getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public short getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TIntProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TShortProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TIntShortProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TIntShortIterator iterator() {
      return new TIntShortIterator(this) {
         TIntShortIterator iter;
         final TUnmodifiableIntShortMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableIntShortMap.access$000(this.this$0).iterator();
         }

         public int key() {
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

   public short putIfAbsent(int var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TShortFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TIntShortProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(int var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(int var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short adjustOrPutValue(int var1, short var2, short var3) {
      throw new UnsupportedOperationException();
   }

   static TIntShortMap access$000(TUnmodifiableIntShortMap var0) {
      return var0.m;
   }
}
