package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TShortIntIterator;
import gnu.trove.map.TShortIntMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TShortIntProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableShortIntMap implements TShortIntMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TShortIntMap m;
   private transient TShortSet keySet = null;
   private transient TIntCollection values = null;

   public TUnmodifiableShortIntMap(TShortIntMap var1) {
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

   public boolean containsValue(int var1) {
      return this.m.containsValue(var1);
   }

   public int get(short var1) {
      return this.m.get(var1);
   }

   public int put(short var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public int remove(short var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TShortIntMap var1) {
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

   public short getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public int getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TShortProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TIntProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TShortIntProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TShortIntIterator iterator() {
      return new TShortIntIterator(this) {
         TShortIntIterator iter;
         final TUnmodifiableShortIntMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableShortIntMap.access$000(this.this$0).iterator();
         }

         public short key() {
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

   public int putIfAbsent(short var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TIntFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TShortIntProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(short var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(short var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public int adjustOrPutValue(short var1, int var2, int var3) {
      throw new UnsupportedOperationException();
   }

   static TShortIntMap access$000(TUnmodifiableShortIntMap var0) {
      return var0.m;
   }
}
