package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TShortDoubleIterator;
import gnu.trove.map.TShortDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TShortDoubleProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableShortDoubleMap implements TShortDoubleMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TShortDoubleMap m;
   private transient TShortSet keySet = null;
   private transient TDoubleCollection values = null;

   public TUnmodifiableShortDoubleMap(TShortDoubleMap var1) {
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

   public boolean containsValue(double var1) {
      return this.m.containsValue(var1);
   }

   public double get(short var1) {
      return this.m.get(var1);
   }

   public double put(short var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public double remove(short var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TShortDoubleMap var1) {
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

   public TDoubleCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public double[] values() {
      return this.m.values();
   }

   public double[] values(double[] var1) {
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

   public double getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TShortProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TDoubleProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TShortDoubleProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TShortDoubleIterator iterator() {
      return new TShortDoubleIterator(this) {
         TShortDoubleIterator iter;
         final TUnmodifiableShortDoubleMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableShortDoubleMap.access$000(this.this$0).iterator();
         }

         public short key() {
            return this.iter.key();
         }

         public double value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public double setValue(double var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public double putIfAbsent(short var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TDoubleFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TShortDoubleProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(short var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(short var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public double adjustOrPutValue(short var1, double var2, double var4) {
      throw new UnsupportedOperationException();
   }

   static TShortDoubleMap access$000(TUnmodifiableShortDoubleMap var0) {
      return var0.m;
   }
}
