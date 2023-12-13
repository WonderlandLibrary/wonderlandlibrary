package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TShortShortIterator;
import gnu.trove.map.TShortShortMap;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.procedure.TShortShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableShortShortMap implements TShortShortMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TShortShortMap m;
   private transient TShortSet keySet = null;
   private transient TShortCollection values = null;

   public TUnmodifiableShortShortMap(TShortShortMap var1) {
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

   public boolean containsValue(short var1) {
      return this.m.containsValue(var1);
   }

   public short get(short var1) {
      return this.m.get(var1);
   }

   public short put(short var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short remove(short var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TShortShortMap var1) {
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

   public short getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public short getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TShortProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TShortProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TShortShortProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TShortShortIterator iterator() {
      return new TShortShortIterator(this) {
         TShortShortIterator iter;
         final TUnmodifiableShortShortMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableShortShortMap.access$000(this.this$0).iterator();
         }

         public short key() {
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

   public short putIfAbsent(short var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TShortFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TShortShortProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(short var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(short var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short adjustOrPutValue(short var1, short var2, short var3) {
      throw new UnsupportedOperationException();
   }

   static TShortShortMap access$000(TUnmodifiableShortShortMap var0) {
      return var0.m;
   }
}
