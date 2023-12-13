package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TFloatIntIterator;
import gnu.trove.map.TFloatIntMap;
import gnu.trove.procedure.TFloatIntProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableFloatIntMap implements TFloatIntMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TFloatIntMap m;
   private transient TFloatSet keySet = null;
   private transient TIntCollection values = null;

   public TUnmodifiableFloatIntMap(TFloatIntMap var1) {
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

   public boolean containsKey(float var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(int var1) {
      return this.m.containsValue(var1);
   }

   public int get(float var1) {
      return this.m.get(var1);
   }

   public int put(float var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public int remove(float var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TFloatIntMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TFloatSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public float[] keys() {
      return this.m.keys();
   }

   public float[] keys(float[] var1) {
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

   public float getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public int getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TFloatProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TIntProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TFloatIntProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TFloatIntIterator iterator() {
      return new TFloatIntIterator(this) {
         TFloatIntIterator iter;
         final TUnmodifiableFloatIntMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableFloatIntMap.access$000(this.this$0).iterator();
         }

         public float key() {
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

   public int putIfAbsent(float var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TIntFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TFloatIntProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(float var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(float var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public int adjustOrPutValue(float var1, int var2, int var3) {
      throw new UnsupportedOperationException();
   }

   static TFloatIntMap access$000(TUnmodifiableFloatIntMap var0) {
      return var0.m;
   }
}
